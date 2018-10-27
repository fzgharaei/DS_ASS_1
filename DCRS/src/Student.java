import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Student extends User implements Serializable {

    HashMap<Character, ArrayList<Course> >courses;
    final int max_electives = 2;
    final int max_course_per_semster = 3;


    public Student(String id, String dept){
        this.Id = id;
        this.department = dept;
        ArrayList<Course> temp = new ArrayList<Course>();
        courses = new HashMap<Character, ArrayList<Course>>();
        courses.put('S',temp);
        courses.put('F',temp);
        courses.put('W',temp);
    }
    public boolean isStudent(){
        return true;
    }

    void addCourse(Character semester, Course course){
        for(Character sem : courses.keySet()){
            if(sem.equals(semester)){
                courses.get(sem).add(course);
            }
        }
    }
    boolean semesterFull(Character semester){
        int course_count = 0;
        for(Character sem : courses.keySet()){
            if(sem.equals(semester)){
                course_count = courses.get(sem).size();
            }
        }
        return course_count==max_course_per_semster;
    }

    boolean allElectiveTaken(){
        int elec_count = 0;
        for(Character sem : courses.keySet()){
            ArrayList<Course> crs= courses.get(sem);
            for(Course c : crs)
                if(!this.department.equals(c.getDepartment())){
                    elec_count++;
            }
        }
        return (max_electives==elec_count);
    }


}
