package ir.ac.ut.ieproj.department;

import ir.ac.ut.ieproj.repository.OfferingRepo;
import ir.ac.ut.iecommon.exceptions.OfferingNotFoundException;
import ir.ac.ut.ieproj.calendar.Calendar;

import java.util.Map;
import java.util.Iterator;
import java.util.TreeMap;
import javax.persistence.*;

@Entity
@Table(name="records")
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Record{
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private float grade;
    private int status;

    @ManyToOne
    private Offering theOffering;

    @ManyToOne
    private Student theStudent;

    public Record(){
    }

    public Record(Student student, Offering offer){
        this.theOffering = offer;
        this.grade = 0;
        this.status = 0;
        this.theStudent = student;
    }

    public float getGrade() {
        return grade;
    }
    public void setGrade(float grade) {
        this.grade = grade;
        if(grade >= 10)
            this.status = 1; //passed
        else
            this.status = 2; //failed
    }

    public Offering getTheOffering(){
        return theOffering;
    }
    public void setTheOffering(Offering offering){  
        this.theOffering = offering;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public void withdraw(){
        this.status = 3; //pending withdraw
    }

    public void rejected(){
        this.status = 0; //withdraw rejected
    }


} 