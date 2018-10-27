import java.util.ArrayList;

public class Course{
    int max_capacity;
    String department;
    String courseId;
    int curr_capacity;

    Course(int capacity, String courseId){

        this.max_capacity = capacity;
        this.courseId = courseId;

        if(courseId.contains("COMP"))
            this.department = "COMP";
        else if(courseId.contains("INSE"))
            this.department = "INSE";
        else if(courseId.contains("SOEN"))
            this.department = "SOEN";

        this.curr_capacity = 0;
    }

    public boolean isFull(){
        return max_capacity==curr_capacity;
    }

    public void updateCapacity(int cap){
        curr_capacity = cap;
    }

    public String getDepartment(){
        return department;
    }
}