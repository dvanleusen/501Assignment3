package a3;
import java.io.PrintWriter;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 1, 2016
* <p>
* Print out to text file
*/
public class FileOutput {
    PrintWriter writer =null;
    
    public FileOutput(){}
    public void fileWriteToFile(String fileName,String str){
        try{
            if (writer==null)
                writer = new PrintWriter(fileName, "UTF-8");
            
            writer.println(str);
        } 
        catch (Exception e) {
        }
    }
    
    public void fileClose(){
        if (writer!=null)
            writer.close();
    }
}
