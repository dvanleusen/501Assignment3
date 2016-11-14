package a3;

/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* an object that contains an array of primitives
*/
public class Obj3 extends MySerializer{
    private int[] newBooks=new int[]{1,2,3};
     
    public Obj3(){}
    public void setArrayValue(){
        newBooks=new int[]{2,3,4};
    }
   
    @Override
    public String getObjectDescrpition(){
        return "An object that contains an array of primitives";
    }
}
