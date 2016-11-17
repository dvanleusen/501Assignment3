package a3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import org.jdom.*;
import java.lang.reflect.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* this is object serializer, which implements object serialization and deserialization
*/
public class MySerializer {
    //declares constants
    final String NAME_SERIALIZED="serialized";
    final String NAME_OBJECT="object";    
    final String NAME_CLASS="class";
    final String NAME_ID="id";
    final String NAME_DECLARINGCLASS="declaringclass";
    final String NAME_FIELD="field";
    final String NAME_FIELDNAME="name";
    final String NAME_FIELDTYPE="type";
    final String NAME_VALUE="value";
    final String NAME_REFERENCE="reference";
    final String NAME_LENGTH="length";
    private final List<String> lstPrimitive;
    private Document doc;
    private int intId=0;
    
    //creates a string array of primitives, which is used later on to check for primitives
    public MySerializer(){
        String[] a=new String[]{"byte","short","int","long","float","double","boolean","char"};
        lstPrimitive=Arrays.asList(a);
    }
    
    public String serialization(Object obj) throws IOException{
        //initialization
        intId=0;

        //adds root element to xml document
        Element element=new Element(NAME_SERIALIZED);
        doc =new Document();
        doc.setRootElement(element);
        //adds class object
        serializationObj(obj);
        return outputXML("output.xml");
    }
       
    //serializes object 
    Element serializationObj(Object obj){
        Element element=null;
        //adds object element  
        //checks if it is an array object
        if ( obj.getClass().isArray()){
            addArrayContent(obj,null);   
        }
        //checks if it is a collection object (list, arrayList, etc.)
        else if  (isCollection(obj.getClass()))
            addCollectionContent(obj, null);
 
        else {
            element=addObjElement(obj, obj.getClass().getName());
            Field[] fields=obj.getClass().getDeclaredFields();

            //adds field elements
            if (obj.getClass().getDeclaredFields().length>0){
                for (int i=0;i<fields.length;i++){
                    fields[i].setAccessible(true);
                    serializationField(element,obj,fields[i]);
                }
            }
        }
        return element;
    }
    //serializes fields 
    void serializationField(Element parent,Object obj,Field f) throws IllegalArgumentException{
        //field basic information 
        Element element=new Element(NAME_FIELD);
        element.setAttribute(NAME_FIELDNAME, f.getName()); 
        element.setAttribute(NAME_FIELDTYPE, f.getType().getName()); 
        Element refElement=null;
        if ( f.getDeclaringClass().getName()!=null &&  !f.getDeclaringClass().getName().equals(""))
            element.setAttribute(NAME_DECLARINGCLASS, f.getDeclaringClass().getName());
        String vObj="";
        try{
            vObj=f.get(obj).toString();
        }
        catch(Exception e){}
       
        //checks if field is an array, adds reference element
        if ( f.getType().isArray()){
            refElement= addArrayContent(obj, f);
         }
        else {
        	//checks if field is collection
        	//if so, adds reference element
            boolean blnCollection=isCollection(f.getType());
            if (blnCollection)
                refElement= addCollectionContent(obj, f);
            else {
            	//if not primitive, adds reference element
                if (!isPrimitiveType(f.getType().getSimpleName())&& chkClassInPackage(f.getType())){
                    try{
                        Object refObj=f.get(obj);
                        if (refObj==null)
                            refElement= addObjElement(null, f.getType().getName());
                        else
                            refElement= serializationObj(refObj);
                    }
                    //if object value is null, only adds object class name
                    catch(Exception e){
                        refElement= addObjElement(null, f.getType().getName());
                   }
                } 
                //if is primitive, adds value
                else{
                    if (!isPrimitiveType(f.getType().getSimpleName()) && !f.getType().getName().contains("java")){
                        element.setAttribute(NAME_FIELDTYPE, "java.lang."+f.getType().getName().substring(0, 1).toUpperCase() + f.getType().getName().substring(1)); 
                    }
                    try{
                        element.addContent(new Element(NAME_VALUE).setText(vObj));  
                    }
                    catch(Exception e){}
                }
             }
        }
        if (refElement!=null){
             element.addContent(new Element(NAME_REFERENCE).setText(refElement.getAttributeValue(NAME_ID)));       
        }
         parent.addContent(element);
    }
    
    //adds an object element under root element 
    Element addObjElement(Object obj,String strClass){
        //adds object element
        String strId=findId(obj);
        Element element=new Element(NAME_OBJECT);
        element.setAttribute(NAME_CLASS, strClass);
        element.setAttribute(NAME_ID, strId);
        doc.getRootElement().addContent(element);          
        return element;
    } 
    
    //serializes an array
    Element addArrayContent(Object obj,Field field) {
        Element element=null;
        Object rObj=obj;
        boolean blnIsNotPrimitive=false;
        Element refElement;
        String strRefClass="";
        int length=0;
        //adds object element, and then checks component types
        try{
            if (field!=null){
                element=addObjElement(obj,field.getType().getName());  
                blnIsNotPrimitive=!isPrimitiveType(  field.getType().getComponentType().getSimpleName()) &&chkClassInPackage( field.getType().getComponentType());
                rObj=field.get(obj);
                strRefClass=field.getType().getComponentType().getName();
            }
            else{
                element=addObjElement(obj,obj.getClass().getName());
                strRefClass=getComponentType(obj.getClass().getName());               
                blnIsNotPrimitive=!isPrimitiveType(strRefClass) && chkClassInPackage( Class.forName(strRefClass));
            }
            length = Array.getLength(rObj);

            //if value is not primitive, gets reference element again
            if (blnIsNotPrimitive){
                    for (int j = 0; j < length; j++){
                       try{ 
                            refElement=serializationObj(Array.get(rObj, j));}
                       catch(Exception e){
                            refElement= addObjElement(null, strRefClass);
                       }
                       Element vElement=new Element(NAME_VALUE);
                       vElement.addContent(new Element(NAME_REFERENCE).setText( refElement.getAttributeValue(NAME_ID)));
                       element.addContent(vElement);
                    }
                }
                else{
                	//component type is primitive, then just gets its value
                    for (int j = 0; j < length; j++){
                        try{
                            element.addContent(new Element(NAME_VALUE).setText( Array.get(rObj, j).toString()));
                        }
                        catch(Exception e){
                            element.addContent(new Element(NAME_VALUE).setText( ""));
                        }
                    }
                }
        }
        catch (Exception e){    
        }
        if (element!=null)     element.setAttribute(NAME_LENGTH, String.valueOf(length));
        return element;
    }
    
    //gets component types
    String getComponentType(String  strArrayType){
        if (strArrayType.equals("[B")) return "byte";
        else if (strArrayType.equals("[S")) return "short"; 
        else if (strArrayType.equals("[I")) return "int";
        else if (strArrayType.equals("[J")) return "long";  
        else if (strArrayType.equals("[F")) return "float";
        else if (strArrayType.equals("[D")) return "double"; 
        else if (strArrayType.equals("[Z")) return "boolean";
        else if (strArrayType.equals("[C")) return "char"; 
        else return strArrayType.substring(2,strArrayType.length()-1);
    }
    
    //serializes a collection
    Element addCollectionContent(Object obj,Field field) {
        Element element=null;
        Object rObj=obj;
        boolean blnIsNotPrimitive=false;
        Element refElement;
        String strRefClass="";
        int length=0;
        Object[] containedValues;
        
        try{
            if (field!=null){
                element=addObjElement(obj,field.getType().getName());  
                rObj=field.get(obj);
            }
            else{
                element=addObjElement(obj,obj.getClass().getName()); 
            }
            
            //takes out collection values, and adds them to an Object array
            containedValues = ((Collection<?>)rObj).toArray();
            length=containedValues.length;
            for (int j = 0; j < length; j++){
                Element vElement=new Element(NAME_VALUE);
                try{ 
                   refElement=null;
                   blnIsNotPrimitive=chkClassInPackage( containedValues[j]);
                   //if it is an array, recursively finds its reference element
                   if (containedValues[j].getClass().isArray())
                       refElement=addArrayContent(containedValues[j],null);
                   //if it is not a primitive, recursively finds reference element
                   else if(blnIsNotPrimitive)
                       refElement=serializationObj(containedValues[j]);
                   //if primitive, sets basic information
                   else  vElement.setText(containedValues[j].toString());
                }
                catch(Exception e){
                    refElement= addObjElement(null, strRefClass);
                }
              
                //adds reference ID if there is a reference element
                if (refElement!=null)
                    vElement.addContent(new Element(NAME_REFERENCE).setText( refElement.getAttributeValue(NAME_ID)));
                
                element.addContent(vElement);
            }
        }
        catch (Exception e){}
     
        if (element!=null)     element.setAttribute(NAME_LENGTH, String.valueOf(length));
        return element;
    }
    //saves XML Document to a file
    public String outputXML(String fileName) throws IOException{
		XMLOutputter xmlOutput = new XMLOutputter();
		//sets XML format
		xmlOutput.setFormat(Format.getPrettyFormat());
      	xmlOutput.output(doc, new FileWriter(fileName));
        return xmlOutput.outputString(doc);
    }

    boolean isPrimitiveType(String sType){
        return lstPrimitive.contains(sType);
    }
    
    //finds object unique identifier number
    String findId(Object obj){    
            intId++;
            return String.valueOf( intId-1);
    }
    //checks class is a collection
    boolean isCollection(Class<?> c) {
        if (c.getName().contains("Collection"))
            return true;
        else if (c.getName().equals("Object"))
            return false;
        else if (c.getSuperclass()!=null)
            return isCollection(c.getSuperclass());
        else return false;
    }

    //gets package name
    String getPackageName(Object obj){
        if (obj==null) 
            return this.getClass().getPackage().getName();
        else {
             Class<?> cl=null;
            try{
                cl=(Class<?>)obj;
            }
            catch(Exception e){}
            if (cl!=null)
                return cl.getPackage().getName();
            else if (obj.getClass().getDeclaringClass()==null)
                return obj.getClass().getPackage().getName();
            else 
                return obj.getClass().getDeclaringClass().getPackage().getName();
        }
    }
    
    boolean chkClassInPackage(Object obj){
        return getPackageName(obj).equals(getPackageName(null));
    }
    
    //if xml stream deserializes, returns a string of object info
    public String deserialization(String strfileOrXml,boolean blnIsFile){
        String rObj=""; 
        try{
              if(blnIsFile){
                  FileHandler fh=new FileHandler();
                  return deserialization(fh.fileReader(strfileOrXml));
              }
              else return deserialization(strfileOrXml);
         }
         catch(Exception e){
             System.out.println("a3.MySerializer.deserialization() error: \n"+e.getMessage());
         }        
         return rObj;
    }
    
    String deserialization(String strfileOrXml){
        String rObj=""; 
        Map<String, Element> objMap = new HashMap<String, Element>();
        //creates DOM Document
        SAXBuilder docBuilder = new SAXBuilder();
        try{
            doc=(Document)docBuilder.build(new StringReader(strfileOrXml));

            //gets objects elements
            List<?> lst =  doc.getRootElement().getChildren(NAME_OBJECT);
            if(lst.size()>0){
                for (int i=0;i<lst.size();i++)
                    objMap.put(((Element) lst.get(i)).getAttributeValue(NAME_ID), (Element) lst.get(i));
                
                for (int i=0;i<lst.size();i++){
                    Element objElement = (Element) lst.get(i);
                    rObj+= deserializeObject(objElement,objMap);
                }
            }
         }
         catch(Exception e){
             System.out.println("a3.MySerializer.deserialization() error: \n"+e.getMessage());
         }        
         return rObj;
    }
    
    //deserializes object
    String deserializeObject(Element e,Map<String, Element> objMap){     
        String rObj="";
       
        try{
            String strName=e.getAttributeValue(NAME_CLASS);
            String strId=e.getAttributeValue(NAME_ID);
            String strObj="========== Object(ID: "+strId+")"+" ========== \n";
             
            Class<?> cls=Class.forName(strName);
           
            //checks type of object
            if (cls.isArray()){
                strObj+= deserializeArray(e, cls, objMap);
            }
            else if(isCollection(cls)){
                strObj+= deserializeCollection(e, cls, objMap);
            }
            else{
                strObj+="Name: "+cls.getSimpleName()+"\n";
                List<?> lst = e.getChildren(NAME_FIELD);
                for (int i=0;i<lst.size();i++){
                    Element ce=(Element)lst.get(i);
                    String cObj=deserializeField(ce,objMap);
                    strObj+=cObj;
                }
            }
            rObj=strObj;
        }
        catch(Exception ex){
            System.out.println("a3.MySerializer.deserializeObject error: \n"+ex.getMessage());
        }
        return rObj;
    }
    
    //deserializes field 
    String deserializeField(Element e,Map<String, Element> objMap){     
        String rObj="";
        try{
            String strName=e.getAttributeValue(NAME_FIELDNAME);
            String strType=e.getAttributeValue(NAME_FIELDTYPE);
            String simpleTypeName=getSimpleTypeName(strType);           
            List<?> vlst = e.getChildren(NAME_VALUE);
            List<?> rlst = e.getChildren(NAME_REFERENCE);
            //primitive type
            if (isPrimitiveType(simpleTypeName) ){
                rObj+="Field: "+simpleTypeName+" "+strName;
                if (vlst.size()>=1)
                    rObj+=" = "+convertValue(simpleTypeName,((Element)vlst.get(0)).getText())+"\n";
            }
            else{
                Class<?> cls=Class.forName(strType);
                if(cls.isArray() )
                        rObj+="Field: "+cls.getComponentType().getSimpleName()+"[] "+strName+"\n";                   
                else if(isCollection(cls))  rObj+="Field: "+cls.getSimpleName() +" "+strName+"\n";                   
                else rObj+="Field: "+simpleTypeName+" "+strName+"\n";
                if (rlst.size()>=1  ){//Array has length 
                        String refId=((Element)rlst.get(0)).getText();
                        rObj+="\tReference: "+refId+"\n";
                }
            }
         }
         catch(Exception ex){
             System.out.println("a3.MySerializer.deserializeField error: \n"+ex.getMessage());
         }
        return rObj;
    }
       
    //deserializes array object
    String deserializeArray(Element e,Class<?> cls,Map<String, Element> objMap){
        String rObj="";
        String strLen=e.getAttributeValue(NAME_LENGTH);
    
        if (strLen!=null &&!strLen.equals("")){
            rObj+=cls.getComponentType().getSimpleName()+"["+strLen+"]\n"; 
            List<?> vlst = e.getChildren(NAME_VALUE);
            rObj+=vlst.size()==1?"Value:\n":"Values:\n";
            for(int i=0;i<vlst.size();i++){
                List<?> rlst =((Element)vlst.get(i)).getChildren(NAME_REFERENCE);
                if (rlst.size()>0){
                    String refId=((Element)rlst.get(0)).getText();
                    rObj+="\tReference: "+refId+"\n";
                }
                else
                    rObj+="\t"+convertValue(cls.getComponentType().getSimpleName(),((Element)vlst.get(i)).getText()) +"\n";
            }
        }
        return rObj;
    } 
    
    //deserializes Collection object
    String deserializeCollection(Element e,Class<?> cls,Map<String, Element> objMap){
        String rObj="";
        String strLen=e.getAttributeValue(NAME_LENGTH);
    
        if (strLen!=null &&!strLen.equals("")){
            rObj+=cls.getSimpleName()+"\n"; 
            rObj+="Length:"+strLen+"\n";
            List<?> vlst = e.getChildren(NAME_VALUE);
            rObj+=vlst.size()==1?"Value:\n":"Values:\n";
            for(int i=0;i<vlst.size();i++){
                List<?> rlst =((Element)vlst.get(i)).getChildren(NAME_REFERENCE);
                if (rlst.isEmpty())//get value directly
                    rObj+="\t"+((Element)vlst.get(i)).getText()+"\n";
                else  rObj+="\tReference ID: "+((Element)rlst.get(0)).getText()+"\n";              
            }
        }
        return rObj;
    } 
    
    //gets type simple name
    String getSimpleTypeName(String strType){
        if (strType.contains("."))
            return strType.substring(strType.lastIndexOf('.') + 1);
        return strType;
    }
   
    //prints corresponding symbols with types
    String convertValue(String simpleTypeName,String sValue){
        switch (simpleTypeName){
            case "String":
                return "\""+sValue+"\"";
            case "char":
                return "'"+sValue+"'";
            default:
                return sValue;        
        }
    }
  
    //finds reference element
    Element findRefElement(String refId){
        List<?> lst =  doc.getRootElement().getChildren(NAME_OBJECT);
        for (int i = 0; i < lst.size(); i++) {
             Element e=(Element)lst.get(i);
             if (e.getAttribute(NAME_ID).toString().equals(refId))
                 return e;
        }
        return null;
    }
}
