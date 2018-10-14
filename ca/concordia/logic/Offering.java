package ir.ac.ut.ieproj.department;

import ir.ac.ut.ieproj.repository.SemesterRepo;
import ir.ac.ut.ieproj.repository.OfferingRepo;
import ir.ac.ut.iecommon.exceptions.TakeException;
import ir.ac.ut.iecommon.exceptions.DropException;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name="offerings")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Offering{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

    @ManyToOne
	private Professor theProfessor;

	private int section;
    private int time;
    private int capacity;
    private Date examDate;
    private int registered;

	@ManyToOne
	private Course theCourse;

    @OneToMany(mappedBy="theOffering")
    private List<Record> allRecords;

    @ManyToOne
    private Semester presenting;

    public Offering(){
        this.allRecords = new ArrayList<Record>();
        this.registered = 0;
    }

    public Offering(int id, Course course, int section, int time, int capacity, String examDate, int termId){
        this.id = id;
        this.theCourse = course;
        this.section = section;
        this.time = time;
        this.capacity = capacity;
        this.examDate = new Date(examDate);
        this.registered = 0;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Professor getTheProfessor() {
        return theProfessor;
    }

    public void setTheProfessor(Professor theProfessor) {
        this.theProfessor = theProfessor;
    }

    public Course getTheCourse() {
        return theCourse;
    }
    public void setTheCourse(Course course) {
        this.theCourse = course;
    }

    public int getSection() {
        return section;
    }
    public void setSection(int section) {
        this.section = section;
    }

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }   

    public Date getExamDate() {
        return examDate;
    }
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }
    
    public int getRegistered() {
		return registered;
	}
	public void setRegistered(int registered) {
		this.registered = registered;
	}
    
    public List<Record> getAllRecords() {
		return allRecords;
	}

	public void setAllRecords(List<Record> allRecords) {
		this.allRecords = allRecords;
	}

	public Semester getPresenting() {
		return presenting;
	}

	public void setPresenting(Semester presenting) {
		this.presenting = presenting;
	}

    //------------------------------------------------

    public void drop(){
        registered--;
    }

    public boolean register(){
        if(registered == capacity)
            return false;
        registered++;
        return true;
    }

    public ArrayList<Student> getWithdrawList(){
        //TODO:
        return null;
    }

    public ArrayList<Student> getStudents(){
        //TODO:
        return null;
    }

}