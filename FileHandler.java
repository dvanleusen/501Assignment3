
package a3;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* Print out to text file
*/
public class FileHandler  {
    PrintWriter writer =null;
  
    public FileHandler(){
     
    }
    
    //output string to file
    public void fileWriteToFile(String strFileName,String str){
        try{
            if (writer==null){
                String strFile=strFileName;
                if (!isFullPath(strFileName))
                    strFile=getCurrentRuningDir()+strFileName;
                writer = new PrintWriter(strFile, "UTF-8");
            }
            writer.println(str);
        } 
        catch (Exception e) {
               System.out.println("a3.FileHandler.fileWriteToFile() Error: "+e.getMessage());
        }
    }
    
    public void fileClose(){
        if (writer!=null)
            writer.close();
    }
    
    String getCurrentRuningDir(){
        try{
//            ClassLoader loader = FileHandler.class.getClassLoader();
//            String t=loader.getResource("A3/FileHandler.class").toString().replace("%20", " ");
//            return new File(t).getParent();
              //return FileHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ");
              return "";
        }
        catch(Exception e){
            System.out.println("Get Dir Error: "+e.getMessage());  
        }
        return "";
    }
    Boolean isFullPath(String strFileName){
        try{
            Path p=Paths.get(strFileName);
            String strFName=p.getFileName().toString();
            return !strFileName.equals(strFName);
        }
        catch(Exception e){
            
        }
        return true;
    }
    public  String fileReader(String strFileName){
                
        String strFile=strFileName;
        if (!isFullPath(strFileName))
            strFile=getCurrentRuningDir()+strFileName;
        
        String strReturn="";
        BufferedReader br =null;
        try{
            br = new BufferedReader(new FileReader(strFile));
            StringBuilder strBuilder = new StringBuilder();
            String strline = br.readLine();
            while (strline!=null){   
                if (strBuilder.length()!=0)  
                    strBuilder.append(System.lineSeparator());
                strBuilder.append(strline);              
                strline = br.readLine();
            }
            strReturn=strBuilder.toString();
        }
        catch(Exception e){}
        finally { 
           try{ if (br!=null) br.close();}
            catch(Exception ce){}    
        }
        return strReturn;
    }
    public  ArrayList<String> fileReaderToList(String strFileName){
        ArrayList<String> lst=new  ArrayList<String>();      
        String strFile=strFileName;
        if (!isFullPath(strFileName))
            strFile=getCurrentRuningDir()+strFileName;
        
        BufferedReader br =null;
        try{
            br = new BufferedReader(new FileReader(strFile));
            String strline = br.readLine();
            while (strline!=null){   
                lst.add(strline);
                strline = br.readLine();
            }
        }
        catch(Exception e){}
        finally { 
            try{ if (br!=null) br.close();}
            catch(Exception ce){}    
        }
        return lst;
    }
}
