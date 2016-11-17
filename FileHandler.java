package a3;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* this class writes file and reads file
*/
public class FileHandler  {
    PrintWriter writer =null;
  
    public FileHandler(){}
    
    //output string to file
    public void fileWriteToFile(String strFileName,String str){
        try{
            if (writer==null){
                String strFile=strFileName;
                writer = new PrintWriter(strFile, "UTF-8");
            }
            writer.println(str);
        } 
        catch (Exception e) {
               System.out.println("a3.FileHandler.fileWriteToFile() Error: "+e.getMessage());
        }
    }
    
    //closes file
    public void fileClose(){
        if (writer!=null)
            writer.close();
    }
    
    //reads file and returns an ArrayList
    //each line in file is a line in ArrayList
    public  ArrayList<String> fileReaderToList(String strFileName){
        ArrayList<String> lst=new  ArrayList<String>();      
        String strFile=strFileName;
        
        BufferedReader br =null;
        try{
            br = new BufferedReader(new FileReader(strFile));
            String strline = br.readLine();
            while (strline!=null){   
                lst.add(strline);
                strline = br.readLine();
            }
        }
        catch(Exception e){System.out.println("Cannot find file " + strFileName);}
        finally { 
            try{ if (br!=null) br.close();}
            catch(Exception ce){}    
        }
        return lst;
    }

    //reads file and returns a single string
    public  String fileReader(String strFileName){       
    	ArrayList<String> lst = fileReaderToList(strFileName);
  
        StringBuilder strBuilder = new StringBuilder();
       
        for(int i=0; i<lst.size(); i++){
        	 if (strBuilder.length()!=0)  
                 strBuilder.append(System.lineSeparator());
             strBuilder.append(lst.get(i));  
        }
        return strBuilder.toString();
    }
}


