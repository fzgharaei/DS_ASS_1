import java.io.Serializable;

public class Advisor extends User implements Serializable {
    Advisor(String id, String dept){
        this.Id = id;
        this.department = dept;
    }
    public boolean isStudent(){
        return false;
    }
}
