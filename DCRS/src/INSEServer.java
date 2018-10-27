import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class INSEServer extends UnicastRemoteObject implements RMIDepartment{

    ArrayList<Student> deptStudents = null;
    ArrayList<Advisor> deptAdvisors = null;
    HashMap<Character, HashMap<String,Course>> deptCourses = new HashMap<Character, HashMap<String, Course> >();;
    Logger deptlogger = null;
    private static INSEServer singleDept = null;
    private INSEServer() throws RemoteException{
        deptStudents = new ArrayList<Student>();
        deptAdvisors = new ArrayList<Advisor>();
        HashMap<String, Course> tempH = new HashMap<String,Course>();
        deptCourses.put('s',tempH);
        deptCourses.put('f',tempH);
        deptCourses.put('w',tempH);

    }
    public static INSEServer getDepartment() throws RemoteException{
        if(singleDept == null)
            synchronized(INSEServer.class){ //double checked locking
                if(singleDept == null)
                    singleDept = new INSEServer();
            }
        return singleDept;
    }
    int port;
    Registry registry;
    public void init(int port) throws RemoteException, AlreadyBoundException {
        this.port = port;
        registry = LocateRegistry.createRegistry(port);
        RMIDepartment stubs = COMPServer.getDepartment();
        registry.bind("localhost", stubs );
    }
    public void receive(){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(this.port);
            byte[] buffer = new byte[1000];
            System.out.println("Server INSE Started............");
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

    public void addCourse(String courseId, char semester, int capacity)throws RemoteException{
        Course newCourse = new Course(capacity, courseId);
        deptCourses.get(semester).put(courseId, newCourse);
    }
    public void removeCourse(String courseId, char semester)throws RemoteException{
        for(Character s:deptCourses.keySet()){
            if(s.charValue()==semester){
                HashMap<String, Course> temp = deptCourses.get(s);
                for(String st: temp.keySet()){
                    if(st.equals(courseId)){
                        deptCourses.get(s).remove(st);
                    }

                }
            }
        }
    }
    public void listCourseAvailability(char semester)throws RemoteException{
        System.out.println("");
    }
    public String RespCourseAvailability(char sem)throws RemoteException{
        return null;
    }

    public String getCourseInfoOtherDepts(int serverPort, char semester) throws RemoteException{
        return null;
    }

    public void enrolCourse(String studentId, String courseId, char semester)throws RemoteException{
        Course selected = null;
        boolean cfound = false;
        for(Character s:deptCourses.keySet()){
            if(s.charValue()==semester){
                HashMap<String, Course> temp = deptCourses.get(s);
                for(String st: temp.keySet()){
                    if(st.equals(courseId)){
                        selected = temp.get(st);
                        cfound = true;
                    }

                }
            }
        }
        if(!cfound) throw new RemoteException("No such Course");
        Student stu = null;
        boolean sfound = false;
        for(Student s: deptStudents){
            if(s.Id.equals(studentId)) {
                stu = s;
                sfound = true;
            }
        }
        if(!sfound) throw new RemoteException("No Such Student");

        if(cfound && sfound && stu!=null && selected!=null) stu.addCourse(semester, selected);
    }
    public String getClassSchedule (String studentId) throws RemoteException{

        Student temp = StudentLookup(studentId);
        if(temp == null)
            System.err.println("There is no such student in the department");
        StringBuffer result = new StringBuffer("");
        for(Character s : temp.courses.keySet()){
            if(s.toString().equals("f")) result.append("fall ");
            else if(s.toString().equals("w")) result.append("winter ");
            else if(s.toString().equals("s")) result.append("summer ");
            for(Course c: temp.courses.get(s)){
                result.append(c.courseId);
                result.append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
    public void dropCourse(String studentId, String courseId) throws RemoteException{
        Student temp = StudentLookup(studentId);
        if(temp == null)
            System.err.println("There is no such student in the department");
        boolean hasTheCourse = false;
        for(Character s : temp.courses.keySet()){
            for(Course c: temp.courses.get(s)){
                if(c.courseId.equals(courseId)){
                    c.updateCapacity(c.curr_capacity-1);
                    hasTheCourse = true;
                }
            }
        }
        if(!hasTheCourse)
            System.err.println("This Student is not registered in the entered course.");
    }



    public void registerUser(String Id) throws RemoteException{
        if(!Id.contains("INSE"))
            throw new RemoteException("Wrong Department");
        if(Id.contains("INSES")){
            Student student = new Student(Id, "INSE");
            deptStudents.add(student);
        }
        if(Id.contains("INSEA")){
            Advisor advisor = new Advisor(Id, "INSE");
            deptAdvisors.add(advisor);
        }
    }
    public Advisor advisorLookup(String aId)throws RemoteException{
        for(Advisor a: deptAdvisors){
            if(a.Id.equals(aId))
                return a;
        }
        return null;
    }
    public Student StudentLookup(String sId)throws RemoteException{
        for(Student s: deptStudents){
            if(s.Id.equals(sId))
                return s;
        }
        return null;
    }
}