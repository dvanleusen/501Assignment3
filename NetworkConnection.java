
package a3;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* Implement a network connection
*/
public class NetworkConnection extends Socket {
    // declare
    static private  String serverAddress="localhost";
    static int listenPort=9999;              
    static private  String localAddress="";
    static NetworkConnection nc=null;
    static boolean blnStartServer=true;
    static boolean blnStartClient=true;  
    
    public static NetworkConnection getNetworkConnection(){
        if (nc==null){
            nc=new NetworkConnection();
            try{
                localAddress=InetAddress.getLocalHost().getHostAddress();
                boolean blnUseSameComputer=true;   //default use the same computer
                try{
                    FileHandler fd=new FileHandler();
                    ArrayList<String>  lst=fd.fileReaderToList("host.txt");
                    for(int i=0;i<lst.size();i++){
                        if (lst.get(i).contains("Server:"))
                            serverAddress=lst.get(i).replace("Server:", "");
                        else if (lst.get(i).contains("UseSameComputer:"))
                            blnUseSameComputer=Boolean.parseBoolean( lst.get(i).replace("UseSameComputer:", ""));
                    }
                }
                catch(Exception e){
                    //can't find host.txt, use default "localhost" value as server address
                    serverAddress=localAddress;
                }
                if (serverAddress.equals("localhost")||blnUseSameComputer) serverAddress=localAddress;
                blnStartServer=localAddress.equals(serverAddress); 
                blnStartClient=!blnStartServer||blnUseSameComputer;   
            }
            catch(Exception e){}       
        }
        return nc;
    }
   
    public boolean isStartServer(){
        return blnStartServer;
    }
    public boolean isStartClient(){
        return blnStartClient;
    }    
    public  void startServer()  {
    (new Thread(){
        @Override
        public void run(){
            ServerSocket listener; 
            Socket clientSocket;
            DataInputStream inStream;
            DataOutputStream outStream;
            MySerializer mySerializer=new MySerializer();
            try {
                 //ServerSocket: listen and accept connections from clients
                listener = new ServerSocket(listenPort); 
                while(true){
                    // receive input from the client
                    clientSocket=listener.accept();
                    System.out.println("========================================\n");
                    System.out.println("Server reviced message from Client IP: "+ clientSocket.getRemoteSocketAddress()+"\n\n");
                     //Server's InputStream receive response from the client
                    inStream = new DataInputStream(clientSocket.getInputStream());
                    String clientInfo=readInputStream(inStream);
                    
                    //todo list
                    try{
                         String strDes=mySerializer.deserialization(clientInfo);
                         System.out.println("Server Deserialize:\n");
                         System.out.println("----------------------------------------\n"+strDes);
                    }
                    catch(Exception e){
                        System.out.println("a3.NetworkConnection.myServer() Error: "+e.getMessage());
                    }
                    System.out.println("========================================\n");
                    //output stream to send information to client
                    outStream = new DataOutputStream(clientSocket.getOutputStream());
                    outStream.writeBytes("13OK!");
                    //display
                }
            }
            catch(IOException e){
                 System.out.println("Server Error: "+e.getMessage());
            }
        }
    }).start();
    }
        
//    public void startClient(final Object obj) {
     public String startClient(final Object obj) {
//    (new Thread(){
//        @Override
//        public void run(){
            // declaration
            String strResponse="";
            Socket mySocket=null;
            DataInputStream inStream=null;
            DataOutputStream outStream=null;
            MySerializer mySerializer=new MySerializer();
                   
            try {
                    //try to open a socket on port 9999
                    mySocket = new Socket(serverAddress, listenPort);
                    //client's InputStream receive response from the server
                    inStream = new DataInputStream(mySocket.getInputStream());
                    //output stream to send information to server
                    outStream = new DataOutputStream(mySocket.getOutputStream());
            }
            catch(UnknownHostException e){
                System.err.println("Couldn't find server: "+serverAddress);
                } 
            catch(IOException e){
                System.err.println("Couldn't get connection to "+serverAddress);
            }
            if (mySocket!=null && inStream!=null && outStream!=null){
                try{
                    //send bytes to server 
    //                outStream.writeBytes("HELLO\n");
                    String strXml=mySerializer.serialization(obj);
                    System.out.println("Client Send:\n========================================\n");
                    System.out.println(strXml);
                    System.out.println("========================================\n");
                    long lenXml=strXml.length();
                    strXml=String.valueOf( String.valueOf(lenXml).length())+String.valueOf(lenXml)+strXml;
                    outStream.writeBytes(strXml);
                    //get server's response            
                    int tryTime=10;                  
                   
                    while (strResponse.equals("") && tryTime>0){
                         Thread.sleep(1000);
                         strResponse=readInputStream(inStream);
                         tryTime--;
                    }
                    
                    //close stream and socket
                    inStream.close();
                    outStream.close();
                    mySocket.close();     
                }
                catch (Exception e){
                    if(e.getCause().getClass().equals(SocketTimeoutException.class)){
                            System.out.println("It is SocketTimeoutException");
                    }
                    else {
                        System.err.println(e);
                    }
                }
            }
            return strResponse;
//        }
//    }).start();
    }
    
    static String readInputStream(DataInputStream inStream){
    //first byte:length of bytes providing length arrive in binary format
        String strReturn="";
        byte[] buffer=new byte[1024]; 
        try{
            int bRead=inStream.read();
            int lenofInfoLen=Integer.parseInt(String.valueOf((char) bRead)) ;
            long bytesToRead=0;
           
            for(int i=0;i<lenofInfoLen;i++){
                bRead=inStream.read();
                bytesToRead=bytesToRead*10 +Integer.parseInt(String.valueOf((char) bRead));
            }
            int bytesRead=0;
            while(strReturn.length() != bytesToRead )
            {
                bytesRead = inStream.read(buffer);
                if (bytesRead==-1)                      //end of stream
                    break;
                else
                    strReturn += new String(buffer, 0, bytesRead);
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
        return strReturn;
    }
}
