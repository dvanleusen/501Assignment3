package a3;

import java.util.Scanner;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* this class starts client or server or both
* client side creates objects, then serializes them to XML stream, and then sends it
* server side listens and receives the stream, then deserializes it back to objects
*/
public class Assignment3 {
	public static void main(String[] args) {  
        try{
            //checks if starting server          
            if(NetworkConnection.getNetworkConnection().isStartServer()){
                System.out.println("Starting Server...");
                NetworkConnection.getNetworkConnection().startServer();
            }
            //checks if starting client                       
            if (NetworkConnection.getNetworkConnection().isStartClient()){
                String strReturn="";
                //calls ObjectCreator to create objects until exit is chosen by user
                ObjectCreator objCreate=new ObjectCreator();
                while (!strReturn.equals("Exit")){
                    strReturn=objCreate.textBasedMenu();
                }  
            }
        }
        catch(Exception e){
            System.out.println("a3.A3.main() Error: "+e.getMessage());
        }
    }  
}
