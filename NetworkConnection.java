package a3;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* implements network connection
*/
public class NetworkConnection extends Socket {
    //declares variables
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
            	//gets local address
                localAddress=InetAddress.getLocalHost().getHostAddress();
                //default uses the same computer for server and client
                boolean blnUseSameComputer=true; 
                try{
                	//reads host.txt to get Server address and boolean UseSameComputer
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
                //if server is localhost, then server address is the local address
                if (serverAddress.equals("localhost")||blnUseSameComputer) serverAddress=localAddress;
                //checks start server and start client
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
    
    //starts server
    //server listens and receives xml stream, then deserializes it to objects
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
                //ServerSocket: listens and accepts connections from clients
                listener = new ServerSocket(listenPort); 
                while(true){
                    //receives input from the client
                    clientSocket=listener.accept();
                    System.out.println("========================================\n");
                    System.out.println("Server received message from Client IP: "+ clientSocket.getRemoteSocketAddress()+"\n\n");
                    //server's InputStream receive response from the client
                    inStream = new DataInputStream(clientSocket.getInputStream());
                    String clientInfo=readInputStream(inStream);
                    
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
                }
            }
            catch(IOException e){
                 System.out.println("Server Error: "+e.getMessage());
            }
        }
    }).start();
    }
     
     //starts client
     //client creates objects, then serializes them to xml stream, which is then sent to server
     public String startClient(final Object obj) {
            String strResponse="";
            Socket mySocket=null;
            DataInputStream inStream=null;
            DataOutputStream outStream=null;
            MySerializer mySerializer=new MySerializer();
                   
            try {
                    //tries to open a socket on port 9999
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
                    //sends bytes to server 
                    String strXml=mySerializer.serialization(obj);
                    System.out.println("Client Send:\n========================================\n");
                    System.out.println(strXml);
                    System.out.println("========================================\n");
                    long lenXml=strXml.length();
                    //for socket communication, first digit is the number of digits of length
                    //following digits represent length of stream
                    strXml=String.valueOf( String.valueOf(lenXml).length())+String.valueOf(lenXml)+strXml;
                    outStream.writeBytes(strXml);
                    //gets server's response    
                    //tries 10 time to get response, then time out
                    int tryTime=10;                  
                   
                    while (strResponse.equals("") && tryTime>0){
                         Thread.sleep(1000);
                         strResponse=readInputStream(inStream);
                         tryTime--;
                    }
                    
                    //closes stream and socket
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
    }
    
    //reads incoming stream
    static String readInputStream(DataInputStream inStream){
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
                //end of stream
                if (bytesRead==-1)
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
