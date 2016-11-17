package a3;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* a simple object with only primitive for instance variable
*/
public class Obj1 implements InterfaceObj{
    private int customerID;
    private boolean check;
    
    public Obj1(){};
    public Obj1(int intID,boolean blnCheck){
          customerID=intID;
          check=blnCheck;
    }
    
    public void setIntValue(int intID){
        customerID=intID;
    }
    
    public void setBooleanValue(boolean blnCheck){
    	check=blnCheck;
    }
    
    @Override
    public String getObjectDescrpition(){
        return "An object that contains only primitives";
    }
}
