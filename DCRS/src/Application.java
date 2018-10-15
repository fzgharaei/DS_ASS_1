import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

class Application{
    static RMIDepartment deptInUse;
    static SysUser currUser;
    public static void main(String[] args){

        System.out.println("Welcome to Distributed Course Registration System");
        System.out.println("Please Enter Your Unique Id:");

        Scanner scanner = new Scanner(System.in);

        String id = scanner.next();
        if(!id.contains("COMP")&&!id.contains("SOEN")&&!id.contains("INSE")) {
            System.err.println("You Have entered a bad Id");
            return;
        }

        if(id.contains("COMP")){
            try{
                Registry registry = LocateRegistry.getRegistry(7070);
                deptInUse = (RMIDepartment) registry.lookup("localhost");
                if(id.contains("COMPA") && ){

                }else if(id.contains("COMPS")){

                }else{
                    throw new Exception("You Have entered a bad Id");
                }
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }

    }
}