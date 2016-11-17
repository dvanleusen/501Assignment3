/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

import java.util.Scanner;
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
    public String textBasedMenu(){
   
        System.out.println("-- Select an object option--\n"+
            "Select an option: \n" +
            "  1) An object that contains only primitives\n" +
            "  2) An object that contains reference to other objects\n" +
            "  3) An object that contains an array of primitives \n" +
            "  4) An object that contains an array of object references \n" +
            "  5) An object that uses an instance of collection classes \n" +         
            "  6) Exit\n ");
        
        Scanner input = new Scanner(System.in);
        try{
            int intSelection = input.nextInt();
            input.nextLine();
            return doAction(intSelection, false);
        }
        catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return "Exit";
        }
    } 
    
    public String doAction(int intSelection, boolean isTest){
        String strReturn="";
        MySerializer myObject=null;
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
        else{
            System.out.println("Serialize "+myObject.getObjectDescrpition());
            if (!isTest){
	            try{
	                 NetworkConnection.getNetworkConnection().startClient(myObject);
	                 Thread.sleep(3000);
	            }
	            catch(Exception e){
	                System.out.println("a3.ObjectCreator.doAction() Error: "+e.getMessage());
	            }
            }
            return myObject.getObjectDescrpition();
        }     
    }
}
