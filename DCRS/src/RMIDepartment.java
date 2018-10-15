import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public interface RMIDepartment extends Remote {

    ArrayList<Student> deptStudents = null;
    ArrayList<Advisor> deptAdvisors = null;
    HashMap<Character,HashMap<String,Course> > deptCourses = null;
    Logger deptlogger = null;

    public void init(int port) throws Exception;

    void addCourse(String courseId, char semester, int capacity)throws Exception;
    void removeCourse()throws Exception;
    void listCourseAvailability(char semester)throws Exception;
    String getCourseInfoOtherDepts(int serverPort, char semester);
    String RespCourseAvailability(char sem);
    void enrolCourse(int studentId, int courseId, char semester)throws Exception;
    String getClassSchedule (int studentId);
    void dropCourse(int studentId, int courseId);

    void registerUser(String Id)throws Exception;
    Advisor advisorLookup(String aId);
    Student StudentLookup(String sId);

}
