import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class StudentClient extends SysUser{

    public StudentClient() {

    }

    public void startProcess(String id) {

        try {

                this.Identifier = id;
            if (Identifier.contains("COMP")) {
                currUser = new Student(id, "COMP");
                System.err.println("HERE ");
                Registry registry = LocateRegistry.getRegistry(7070);

                System.err.println("*****");
                //deptInUse = (RMIDepartment) registry.lookup("localhost");
                deptInUse = (RMIDepartment) Naming.lookup("rmi://localhost:7070");
                System.err.println("*****"+ deptInUse);
            }else if (Identifier.contains("INSE")){
                currUser = new Student(id, "INSE");

                Registry registry = LocateRegistry.getRegistry(6060);
                deptInUse = (RMIDepartment) registry.lookup("localhost");
            }else if (Identifier.contains("SOEN")){
                System.err.println("*****");
                currUser = new Student(id, "SOEN");

                Registry registry = LocateRegistry.getRegistry(5050);
                System.err.println("*****");
                deptInUse = (RMIDepartment) registry.lookup("localhost");
                System.err.println("*****"+ deptInUse);
            }else throw new Exception("There is no Department in registry with this identifier");
            System.err.println("HERE "+ deptInUse.getClass());
//            deptInUse = (RMIDepartment) registry.lookup("localhost");
            if(deptInUse.StudentLookup(Identifier)!=null){
                System.err.println("HERE "+ deptInUse.getClass());
                currUser = deptInUse.StudentLookup(Identifier);

            }

        }catch(Exception e){
            System.err.println("hereeeeeeeeeeeeeeee");
            //System.err.println(e.getStackTrace().toString());
    }
        System.err.println("heree2");
        System.out.println("Welcome to DCRS, "+ currUser.Id);


        Scanner scanner = new Scanner(System.in);
        boolean online = true;
        do{
            System.out.println("Kindly select from the following menu: ");
            System.out.println("To enroll to a course: enroll + courseId + semester (fall/winter/summer)");
            System.out.println("To drop a course: drop + courseId");
            System.out.println("To get your class schedule: schedule");
            System.out.println("To Log out from the system: exit");

            String command = scanner.next();
            String[] commandParts = command.split(" ");
            try{
                switch(commandParts[0]){
                    case "enroll":
                        if(!commandParts[1].startsWith("COMP")||
                                !commandParts[1].startsWith("INSE")||
                                !commandParts[1].startsWith("SOEN")){
                            throw new Exception("You have entered an invalid course ID");
                        }
                        if(!commandParts[2].equalsIgnoreCase("fall")||
                                !commandParts[2].equalsIgnoreCase("winter")||
                                !commandParts[2].equalsIgnoreCase("summer")){
                            throw new Exception("You have entered an invalid semester");
                        }
                        deptInUse.enrolCourse(Identifier, commandParts[1], commandParts[2].charAt(0));
                        break;
                    case "drop":
                        break;
                    case "schedule":
                        break;
                    case "exit":
                        System.out.println("Thank you for using our system. Good Bye...");
                        online = false;
                        break;

                    default:
                        System.out.println("You have entered an invalid command. Please try again.");
                        break;
                }
            }catch(Exception e){
                System.out.println("There is a problem with your request:");
                System.err.println(e.getMessage());
                System.out.println("Please Try Again.");
            }
        }while(online);
    }
}
