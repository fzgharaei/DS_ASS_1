import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public interface RMIDepartment extends Remote {

    public void init(int port) throws RemoteException, AlreadyBoundException;

    void addCourse(String courseId, char semester, int capacity)throws RemoteException;
    void removeCourse(String courseId, char semester)throws RemoteException;
    void listCourseAvailability(char semester)throws RemoteException;
    String getCourseInfoOtherDepts(int serverPort, char semester) throws RemoteException;
    String RespCourseAvailability(char sem)throws RemoteException;
    void enrolCourse(String studentId, String courseId, char semester)throws RemoteException;
    String getClassSchedule (String studentId)throws RemoteException;
    void dropCourse(String studentId, String courseId)throws RemoteException;

    void registerUser(String Id)throws RemoteException;
    Advisor advisorLookup(String aId) throws RemoteException;
    Student StudentLookup(String sId)throws RemoteException;

}
