package ir.ac.ut.ieproj.department;

import ir.ac.ut.ieproj.calendar.Calendar;
import ir.ac.ut.ieproj.repository.OfferingRepo;
import ir.ac.ut.ieproj.repository.StudentRepo;
import ir.ac.ut.iecommon.exceptions.OfferingNotFoundException;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="terms")
public class Semester{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String name;

    private Date startDate;
    private Date endDate;
    private Date enrollmentStartDate;
    private Date enrollmentEndDate;
    private Date addAndDropStartDate;
    private Date addAndDropEndDate;
    private Date withdrawStartDate;
    private Date withdrawEndDate;
    private Date submitGradeStartDate;
    private Date submitGradeEndDate;

    @OneToMany(mappedBy="presenting")
    private List<Offering> semesterOfferings;

    public Semester(){

    }
    public Semester(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return this.startDate.toString();
    }
    public void setStartDate(String startDate) {
        this.startDate = new Date(startDate);
    }

    public String getEndDate() {
        return endDate.toString();
    }
    public void setEndDate(String endDate) {
        this.endDate = new Date(endDate.toString());
    }

    public String getEnrollmentStartDate() {
        return enrollmentStartDate.toString();
    }
    public void setEnrollmentStartDate(String enrollmentStartDate) {
        this.enrollmentStartDate = new Date(enrollmentStartDate);
    }

    public String getEnrollmentEndDate() {
        return enrollmentEndDate.toString();
    }
    public void setEnrollmentEndDate(String enrollmentEndDate) {
        this.enrollmentEndDate = new Date(enrollmentEndDate);
    }

    public String getAddAndDropStartDate() {
        return addAndDropStartDate.toString();
    }
    public void setAddAndDropStartDate(String addAndDropStartDate) {
        this.addAndDropStartDate = new Date(addAndDropStartDate);
    }

    public String getAddAndDropEndDate() {
        return addAndDropEndDate.toString();
    }
    public void setAddAndDropEndDate(String addAndDropEndDate) {
        this.addAndDropEndDate = new Date(addAndDropEndDate);
    }

    public String getWithdrawStartDate() {
        return withdrawStartDate.toString();
    }
    public void setWithdrawStartDate(String withdrawStartDate) {
        this.withdrawStartDate = new Date(withdrawStartDate);
    }

    public String getWithdrawEndDate() {
        return withdrawEndDate.toString();
    }
    public void setWithdrawEndDate(String withdrawEndDate) {
        this.withdrawEndDate = new Date(withdrawEndDate);
    }

    public String getSubmitGradeStartDate() {
        return submitGradeStartDate.toString();
    }
    public void setSubmitGradeStartDate(String submitGradeStartDate) {
        this.submitGradeStartDate = new Date(submitGradeStartDate);
    }

    public String getSubmitGradeEndDate() {
        return submitGradeEndDate.toString();
    }
    public void setSubmitGradeEndDate(String submitGradeEndDate) {
        this.submitGradeEndDate = new Date(submitGradeEndDate);
    }

    public List<Offering> getSemesterOfferings(){
        return semesterOfferings;
    }
    public void setSemesterOfferings(ArrayList<Offering> offerings){
        this.semesterOfferings = offerings;
    }


    public static enum Action{
        TAKEANDDROP, WITHDRAW , GRADESUBMITION 
    }


    @Override
    public String toString(){
        return "id: "+id+" name: "+name+"startDate: "+startDate.toString();
    }
    
    public boolean checkActionTime(Action ac){
        Date today = Calendar.today();
        if(ac == Action.TAKEANDDROP)
            return Calendar.isDayBetween(today, enrollmentStartDate, enrollmentEndDate)|| Calendar.isDayBetween(today, addAndDropStartDate, addAndDropEndDate);
        if(ac == Action.WITHDRAW)
            return Calendar.isDayBetween(today, withdrawStartDate, withdrawEndDate);
        if(ac == Action.GRADESUBMITION )
            return Calendar.isDayBetween(today, submitGradeStartDate, submitGradeEndDate);
        return false;
    }

    public boolean isWithdrawDone(Date date){
        return date.after(withdrawEndDate);
    }

    public boolean hasOffering(int offerId){
        try{
            Offering offer = OfferingRepo.getRepository().getOfferingById(offerId);
            return true;
        }catch(OfferingNotFoundException o){    
            return false;
        }
    }

}
