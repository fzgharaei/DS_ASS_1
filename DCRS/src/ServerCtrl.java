import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerCtrl {
    public static void main(String[] args){


        try{
            SOENServer soenServer = SOENServer.getDepartment();
            soenServer.init(5050);
            INSEServer inseServer = INSEServer.getDepartment();
            inseServer.init(6060);
            COMPServer compServer = COMPServer.getDepartment();
            compServer.init(7070);
            System.err.println("Here 1");

           // Registry registrysoen = LocateRegistry.createRegistry(5050);
            System.err.println("HEre 2");

//            SOENServer soenObj = SOENServer.getDepartment();
            System.err.println("HEre3");

          //  registrysoen.bind("localhost", soenObj );
            //Naming.rebind("rmi://localhost:5050", soenObj);
            soenServer.registerUser("SOENA1111");
            soenServer.registerUser("SOENS1111");
            System.err.println("HEre4");

            //Registry registryinse = LocateRegistry.createRegistry(6060);
  //          COMPServer inseObj = COMPServer.getDepartment();
    //        registryinse.bind("localhost", inseObj );
      //      Naming.rebind("rmi://localhost:6060", inseObj);
            inseServer.registerUser("INSEA1111");
            inseServer.registerUser("INSES1111");
            System.err.println("HEre");

      //      Registry registrycomp = LocateRegistry.createRegistry(7070);
        //    COMPServer compObj = COMPServer.getDepartment();
          //  registrycomp.bind("localhost", compObj );
         //   Naming.rebind("rmi://localhost:7070", compObj);
            compServer.registerUser("COMPA1111");
            compServer.registerUser("COMPS1111");
            System.err.println("HEre");

            Runnable soenRecTask = ()->{
                System.err.println("HEre");

                soenServer.receive();
            };
            Runnable inseRecTask = ()->{
                System.err.println("HEre");

                inseServer.receive();
            };
            Runnable compRecTask = ()->{
                System.err.println("HEre");

                compServer.receive();
            };

            Thread thread = new Thread(soenRecTask);
            Thread thread2 = new Thread(inseRecTask);
            Thread thread3 = new Thread(compRecTask);

            thread.start();
            thread2.start();
            thread3.start();

        }
        catch(Exception e){
            System.err.println("catch ex");
            System.err.println(e.getStackTrace().toString());
        }
        System.err.println("end");
    }
}
