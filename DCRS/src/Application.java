import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

class Application{
//    static
    static SysUser currUser;
    public static void main(String[] args){
        //RUNNING THE SERVERS:
//            SOENServer soenServer = SOENServer.getDepartment();
//            INSEServer inseServer = INSEServer.getDepartment();
//            COMPServer compServer = COMPServer.getDepartment();
//
//            try{
//                soenServer.init(5050);
//                soenServer.registerUser("SOENA1111");
//                soenServer.registerUser("SOENS1111");
//
//                inseServer.init(6060);
//                inseServer.registerUser("INSEA1111");
//                inseServer.registerUser("INSES1111");
//
//                compServer.init(7070);
//                compServer.registerUser("COMPA1111");
//                compServer.registerUser("COMPS1111");
//
//                Runnable soenRecTask = ()->{
//                    soenServer.receive();
//                };
//                Runnable inseRecTask = ()->{
//                    inseServer.receive();
//                };
//                Runnable compRecTask = ()->{
//                    compServer.receive();
//                };
//
//                Thread thread = new Thread(soenRecTask);
//                Thread thread2 = new Thread(inseRecTask);
//                Thread thread3 = new Thread(compRecTask);
//
//                thread.start();
//                thread2.start();
//                thread3.start();
//
//            }catch(Exception e){
//                System.err.println(e.getMessage());
//            }
//            System.err.println("end");

        //// CLIENT SIDE STARTING
        System.out.println("Welcome to Distributed Course Registration System");
        System.out.println("Please Enter Your Unique Id:");

        Scanner scanner = new Scanner(System.in);

        String id = scanner.next();
        if(!id.contains("COMP")&&!id.contains("SOEN")&&!id.contains("INSE")) {
            System.err.println("You Have entered a bad Id");
            return;
        }
        try{
            if(id.contains("COMPS")||id.contains("INSES")||id.contains("SOENS")){ // if student
                currUser = new StudentClient();
            } else if(id.contains("COMPA")||id.contains("INSEA")||id.contains("SOENA")) { // if advisor
                currUser = new AdvisorClient();
            } else{
                throw new Exception("You Have entered a bad Id");
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }


            currUser.startProcess(id);

        System.out.println("Successfully Logged out");

    }
}