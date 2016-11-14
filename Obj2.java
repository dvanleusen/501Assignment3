package a3;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* This is an object that contains reference to other objects 
*/
public class Obj2 extends MySerializer{
    
    private Obj1 owner=new Obj1(10001, true) ;
    private int intAccount; 
    public Obj2(){
    }
    public Obj2(int account){
        intAccount=account;
    }
    public void setIntAccount(int account){
        intAccount=account;
    }
    
    @Override
    public String getObjectDescrpition(){
        return "An object that contains reference to other objects";
    }
}
