import java.util.ArrayList;

public class Course{
    int max_capacity;
    String department;
    int curr_capacity;

    Course(int capacity, String dept){
        this.max_capacity = capacity;
        this.department = dept;
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