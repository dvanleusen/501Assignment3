/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

import java.util.Scanner;
import java.lang.reflect.*;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* Create arbitrary objects using text-based menu
*/
public class ObjectCreator {
    public ObjectCreator() {}
    private MySerializer myObject;
    public String textBasedMenu(){
   
        System.out.println("-- Select an object option--\n"+
            "Select an option: \n" +
            "  Press 1 for an object that contains only primitives\n" +
            "  Press 2 for an object that contains reference to other objects\n" +
            "  Press 3 for an object that contains an array of primitives \n" +
            "  Press 4 for an object that contains an array of object references \n" +
            "  Press 5 for an object that uses an instance of collection classes \n" +         
            "  Press 6 to exit\n ");
        
        Scanner input = new Scanner(System.in);
        try{
            int intSelection = input.nextInt();
            input.nextLine();
            return doAction(intSelection);
        }
        catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return "Exit";
        }
    } 
    
    public String doAction(int intSelection){
        String strReturn="";
        myObject=null;
        switch (intSelection) {
            case 1:
                myObject=new Obj1(1000,true);
                break;
            case 2:
                myObject=new Obj2();
                break;
            case 3:
                myObject=new Obj3();
                break;
            case 4:
                myObject=new Obj4();
                break;
            case 5:
                myObject=new Obj5();
                break;
            case 6://"Exit
                strReturn="Exit";
                break;
            default://"Invalid selection";
                strReturn="Invalid selection";
                break;
        }
        
        if (!strReturn.equals(""))
            return strReturn;
        else return myObject.getObjectDescrpition();
    }
}
