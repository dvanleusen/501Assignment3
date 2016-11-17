package a3;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* this is used in Obj4 as an object reference
*/
public class ObjBook{
    
    private String referenceID;
    private String name;
    private String author;
    private double price;
    public ObjBook(){
    }
    public ObjBook(String strID,String strName,String strAuthor,double dPrice){
          referenceID=strID;
          name=strName;
          author=strAuthor;
          price=dPrice;
    }
    public void setID(String strID){
        referenceID=strID;
    }
    public void setName(String strName){
        name=strName;
    }
    public void setAuthor(String strAuthor){
        author=strAuthor;
    }
    public void setPrice(double dPrice){
        price=dPrice;
    }
    
}