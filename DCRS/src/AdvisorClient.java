import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AdvisorClient extends SysUser{
    AdvisorClient(){}

    public void startProcess(String id){

        try{

            Registry registry;
            this.Identifier = id;
            if(Identifier.contains("COMP")) {
                currUser = new Advisor(id, "COMP");
                registry = LocateRegistry.getRegistry(7070);
            }else if(Identifier.contains("INSE")) {
                currUser = new Advisor(id, "INSE");
                registry = LocateRegistry.getRegistry(6060);
            }else if(Identifier.contains("SOEN")) {
                currUser = new Advisor(id,"SOEN");
                registry = LocateRegistry.getRegistry(5050);
            }else throw new Exception("There is no Department in registry with this identifier");

            deptInUse = (RMIDepartment) registry.lookup("localhost");
            if(deptInUse.StudentLookup(Identifier)!=null)
                currUser = deptInUse.StudentLookup(Identifier);

        }catch(Exception e){
            System.err.println(e.getMessage());
        }

        System.out.println("Welcome to DCRS, "+ currUser.Id);


        Scanner scanner = new Scanner(System.in);
        boolean online = true;
        do{
            System.out.println("Kindly select from the following menu: ");
            System.out.println("To add a course to system: add + courseId + semester (fall/winter/summer)");
            System.out.println("To remove a course from System: remove + courseId + semester (fall/winter/summer)");
            System.out.println("To get the available courses' list for a semester: list + semester (fall/winter/summer)");
            System.out.println("#########################################################################################");
            System.out.println("If you're wishing to do the roles on behalf of a student please use the following commands:");
            System.out.println("To enroll to a course: enroll + studentId + courseId + semester (fall/winter/summer)");
            System.out.println("To drop a course: drop + studentId + courseId");
            System.out.println("To get your class schedule: schedule + studentId");
            System.out.println("To Log out from the system: exit");

            String command = scanner.nextLine();
            String[] commandParts = command.split(" ");
            System.out.println(commandParts.length);
            try{
                switch(commandParts[0]){
                    case "add":
                        System.out.println("here 112");
                        System.out.println("*****"+commandParts[1]);
                        if(!commandParts[1].contains("COMP")&&
                                !commandParts[1].contains("INSE")&&
                                !commandParts[1].contains("SOEN")){
                            throw new Exception("You have entered an invalid course ID");
                        }
                        System.err.println("here 113");
                        if(!commandParts[2].startsWith("fall")&&
                                !commandParts[2].startsWith("winter")&&
                                !commandParts[2].startsWith("summer")){
                            throw new Exception("You have entered an invalid semester");
                        }
                        System.err.println("here 111");
                        deptInUse.addCourse(commandParts[1],commandParts[2].charAt(0),10);
                        System.err.println("here 222");

                        break;
                    case "remove":
                        if(!commandParts[1].startsWith("COMP")||
                                !commandParts[1].startsWith("INSE")||
                                !commandParts[1].startsWith("SOEN")){
                            throw new Exception("You have entered an invalid course ID");
                        }
                        if(!commandParts[2].startsWith("fall")||
                                !commandParts[2].startsWith("winter")||
                                !commandParts[2].startsWith("summer")){
                            throw new Exception("You have entered an invalid semester");
                        }
                        break;
                    case "list":
                        if(!commandParts[1].startsWith("fall")||
                                !commandParts[1].startsWith("winter")||
                                !commandParts[1].startsWith("summer")){
                            throw new Exception("You have entered an invalid semester");
                        }
                        deptInUse.listCourseAvailability(commandParts[1].charAt(0));
                        break;
                    case "enroll":
                        if(!commandParts[1].startsWith("COMPS")||
                                !commandParts[1].startsWith("INSES")||
                                !commandParts[1].startsWith("SOENS")){
                            throw new Exception("You have entered an invalid student ID");
                        }
                        if(!commandParts[2].startsWith("COMP")||
                                !commandParts[2].startsWith("INSE")||
                                !commandParts[2].startsWith("SOEN")){
                            throw new Exception("You have entered an invalid course ID");
                        }
                        if(!commandParts[3].startsWith("fall")||
                                !commandParts[3].startsWith("winter")||
                                !commandParts[3].startsWith("summer")){
                            throw new Exception("You have entered an invalid semester");
                        }
                        deptInUse.enrolCourse(Identifier, commandParts[1], commandParts[2].charAt(0));
                        break;
                    case "drop":
                        if(!commandParts[1].startsWith("COMPS")||
                                !commandParts[1].startsWith("INSES")||
                                !commandParts[1].startsWith("SOENS")){
                            throw new Exception("You have entered an invalid student ID");
                        }
                        if(!commandParts[2].startsWith("COMP")||
                                !commandParts[2].startsWith("INSE")||
                                !commandParts[2].startsWith("SOEN")){
                            throw new Exception("You have entered an invalid course ID");
                        }
                        break;
                    case "schedule":
                        if(!commandParts[1].startsWith("COMPS")||
                                !commandParts[1].startsWith("INSES")||
                                !commandParts[1].startsWith("SOENS")){
                            throw new Exception("You have entered an invalid student ID");
                        }
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
                System.out.println(e.getMessage());
                System.out.println("Please Try Again.");
            }
        }while(online);
    }
}