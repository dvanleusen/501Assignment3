package a3;

import static java.util.Collections.list;
import java.util.ArrayList;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* an object that uses an instance of collection classes 
*/
public class Obj5 extends MySerializer{
    
    private ArrayList favorBooks;
   
    public Obj5(){
       favorBooks=new ArrayList();
       favorBooks.add(new Obj1());
       favorBooks.add(new Obj3());
       favorBooks.add("Gulliver Travels");
       favorBooks.add("Clarissa");
       favorBooks.add("Tom Jones");
    }
       
    @Override
    public String getObjectDescrpition(){
        return "An object that uses an instance of collection classes";
    }
}
