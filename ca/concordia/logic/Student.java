package ir.ac.ut.ieproj.department;

import ir.ac.ut.iecommon.exceptions.*;
import ir.ac.ut.ieproj.calendar.Calendar;
import ir.ac.ut.ieproj.repository.StudentRepo;
import ir.ac.ut.ieproj.repository.RecordRepo;
import ir.ac.ut.ieproj.repository.SemesterRepo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name="students")
public class Student{

    @Id 
    //assigned
    private int id;
    private String firstName;

    private String lastName;

    @ManyToOne
    @JoinColumn(name ="program_id")
    private Program major;

    @OneToMany(mappedBy="theStudent")
    private List<Record> studyRecs;

    public Student(int id, String firstName, String lastName, List<Record> studyRecs){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studyRecs = studyRecs;
    }

    public Student(){
        studyRecs = new ArrayList<Record>();
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Program getMajor() {
        return major;
    }
    public void setMajor(Program major) {
        this.major = major;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public List<Record> getStudyRecs() {
        return studyRecs;
    }
    public void setStudyRecs(List<Record> studyRecs) {
        this.studyRecs = studyRecs;
        
    }

    private static final int UNITS_FOR_ADVANCED = 24;
    private static final int UNITS_FOR_NORMAL = 20;
    private static final int UNITS_FOR_EVENTUAL = 12;

    //---------------------other helpers

    public float getLastGPA(){   
        int pof = (StudentRepo.getRepository().getLastPOFCredits(this));  //POF=paased or failed
        int sumOfGrades = (StudentRepo.getRepository().getLastPOFGrades(this));
        if(pof == 0)
            return 0;
        return (sumOfGrades/pof);
    }

    //-------------take

    private int getMaxTotalUnits() {
        float lastGPA = getLastGPA();
        System.out.println(lastGPA+"================================");
        if(lastGPA != 0){
            if(lastGPA < 12)
                return UNITS_FOR_EVENTUAL;
            else if(lastGPA < 18)
                return UNITS_FOR_NORMAL;
            else
                return UNITS_FOR_ADVANCED;
        }
        return UNITS_FOR_NORMAL;
    }

    private void checkNumberofCredits(Course course) throws TakeException {
        int tillNow = (StudentRepo.getRepository().getTotalPassedCredits(this)) + course.getCredit();
        int max = this.getMaxTotalUnits();
        System.out.println(tillNow+"=====================");
        if(tillNow > max)
            throw new TakeException("By taking this course, total units up to now exceed "+max+", which is the maximum.");
    }
    
    private void isAccordingToProgram(Course course) throws TakeException {
        if(!major.checkConformity(this, course))
            throw new TakeException("Taking Course "+course.getName()+" is not possible according to student's educational program.");
    }

    private void canTakeCourse(Course course) throws TakeException{
        int status = StudentRepo.getRepository().hasPassedOrTaken(this, course);
        if(status == 1)
            throw new TakeException("Has already passed this course!");
        else if(status == 2)
            throw new TakeException("Has already taken this course in current semester!");

        String result = course.checkPrereqAndCoreq(this); 
        if(!result.equals(""))
            throw new TakeException(result);

        this.checkNumberofCredits(course);
        //this.isAccordingToProgram(course);
    }

    private void takes(Offering offer) throws TakeException {
        if(!RecordRepo.getRepository().addRecord(new Record(this, offer)))
            throw new TakeException("Offering is at full capacity!");
    }

    public void takeOffering(Offering wanted) throws TakeException{
        Semester currentSemester = SemesterRepo.getRepository().getCurrentSemester();
        if (!currentSemester.checkActionTime(Semester.Action.TAKEANDDROP)){
            throw new TakeException("System is not currently ("+Calendar.today()+") within the 'enrollment' or 'add-and-drop' period.");
        }
        
        String result = SemesterRepo.getRepository().hasTimeConflict(this, wanted);
        if(!result.equals("")){
            throw new TakeException("This Offering has confilct with "+result);
        }

        Course course = wanted.getTheCourse();
        this.canTakeCourse(course);

        this.takes(wanted);    
    }

    //--------------drop
    private void drops(Offering offer) throws DropException{
        if(!RecordRepo.getRepository().rmOfferingRecord(this, offer))
            throw new DropException("Not currently registered in this offering!");
    }

    public void dropOffering(Offering wanted) throws DropException {
        Semester currentSemester = SemesterRepo.getRepository().getCurrentSemester();
        if (!currentSemester.checkActionTime(Semester.Action.TAKEANDDROP)){
            throw new DropException("System is not currently ("+Calendar.today()+") within the 'enrollment' or 'add-and-drop' period.");
        }
        this.drops(wanted);
    }

    //--------------withdraw
    private void isThereWithdrawReq() throws WithdrawException{
        if(StudentRepo.getRepository().hasWithdrawReq(this)){
            throw new WithdrawException("There already exists a withdraw request!");
        }
    }

    private void withdraws(Offering offer) throws WithdrawException{
        if(!RecordRepo.getRepository().withdrawRecord(this, offer))
            throw new WithdrawException("Not currently registered in this offering!");
    }

    public void withdrawOffering(Offering wanted) throws WithdrawException{
        Semester currentSemester = SemesterRepo.getRepository().getCurrentSemester();
        if (!currentSemester.checkActionTime(Semester.Action.WITHDRAW))
            throw new WithdrawException("System is not currently ("+Calendar.today()+") within the 'withdraw' period."); 

        this.isThereWithdrawReq();
        this.withdraws(wanted);
    }

    //---------------------checkdegree
    public List<Course> getAllPassedCourses(){
        return StudentRepo.getRepository().getAllPassedCourses(this);
    }

    public boolean passedAllMandatoryCourses(){
        List<Course> mandatories = major.fetchMandatories();
        List<Course> passed = this.getAllPassedCourses();
        
        if(passed.size() < mandatories.size())
            return false;
        
        for (Course mand : mandatories)
            if(!passed.contains(mand))
                return false;
        return true;
    }

    public boolean passedAllElectiveCourses(){
        List<Course> electives = major.fetchElectives();
        List<Course> passed = this.getAllPassedCourses();
        
        if(passed.size() < electives.size())
            return false;
        
        for (Course elec : electives)
            if(!passed.contains(elec))
                return false;
        return true;
    }

    public void checkDegree() throws CheckDegreeReqException{
        if(!passedAllMandatoryCourses())
            throw new CheckDegreeReqException("There is one or more mandatory courses that are not passed.");
        if(!passedAllElectiveCourses())
            throw new CheckDegreeReqException("Student needs to pass some elective courses in order to get degree.");
    }

    //-------------------- UI helpers
    public List<Offering> getAllCanTake(){
        return StudentRepo.getRepository().getAllPossibleOfferings(this);
    }

    public List<Offering> getAllTaken(){
        return StudentRepo.getRepository().getAllInProgressOfferings(this);
    }


}
