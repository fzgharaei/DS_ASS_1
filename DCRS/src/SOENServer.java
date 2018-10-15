import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SOENServer implements RMIDepartment{
    private static SOENServer singleDept = null;
    private SOENServer() {
    }
    public static SOENServer getDepartment(){
        if(singleDept == null)
            synchronized(COMPServer.class){ //double checked locking
                if(singleDept == null)
                    singleDept = new SOENServer();
            }
        return singleDept;
    }
    int port;
    Registry registry;
    public void init(int port) throws Exception{
        this.port = port;
        registry = LocateRegistry.createRegistry(port);

    }
    public void receive(){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(this.port);
            byte[] buffer = new byte[1000];
            System.out.println("Server SOEN Started............");
            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                char semester = request.getData().toString().toCharArray()[0];
                String answer = this.RespCourseAvailability(semester);
                buffer = answer.getBytes();
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length, request.getAddress(),
                        request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

    public void addCourse(String courseId, char semester, int capacity)throws Exception{

    }
    public void removeCourse()throws Exception{

    }
    public void listCourseAvailability(char semester)throws Exception{
        System.out.println("");
    }
    public String getCourseInfoOtherDepts(int serverPort, char semester){
        return null;
    }

    public String RespCourseAvailability(char sem){
        return null;
    }

    public void enrolCourse(int studentId, int courseId, char semester)throws Exception{

    }
    public String getClassSchedule (int studentId){
        return null;
    }

    public void dropCourse(int studentId, int courseId){

    }

    public void registerUser(String Id)throws Exception {
        if(!Id.contains("SOEN"))
            throw new Exception("Wrong Department");
        if(Id.contains("SOENS")){
            Student student = new Student(Id, "SOEN");
            deptStudents.add(student);
        }
        if(Id.contains("SOENA")){
            Advisor advisor = new Advisor(Id, "SOEN");
        }
    }
    public Advisor advisorLookup(String aId){
        for(Advisor a: deptAdvisors){
            if(a.Id.equals(aId))
                return a;
        }
        return null;
    }
    public Student StudentLookup(String sId){
        for(Student s: deptStudents){
            if(s.Id.equals(sId))
                return s;
        }
        return null;
    }
}