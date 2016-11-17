package a3;
/**
* Assignment 3
* @author Daniel Van Leusen
* Student id: 10064708
* E-mail: danvanleusen@yahoo.co.uk
* @version Nov 17, 2016
* <p>
* an object that contains an array of object references
*/
public class Obj4  implements InterfaceObj{
    
    private  ObjBook[] arrayBooks;
     
    public Obj4(){
        
        arrayBooks=new ObjBook[]{new ObjBook("ISBN 978-0-111-1","OH SHE GLOWS EVERY DAY","Angela Liddon",32.00),
            new ObjBook("ISBN 978-0-111-2","THE WHISTLER"," John Grisham",37.00),
            new ObjBook("ISBN 978-0-111-3","COOKING FOR JEFFREY"," Ina Garten",45.00),
            new ObjBook("ISBN 978-0-111-4","APPETITES: A COOKBOOK","Anthony Bourdain",46.50)};
    }
    
    @Override
    public String getObjectDescrpition(){
        return "An object that contains an array of object references";
    }
}
