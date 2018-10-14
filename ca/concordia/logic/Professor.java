package ir.ac.ut.ieproj.department;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;
import ir.ac.ut.ieproj.repository.RecordRepo;
import ir.ac.ut.ieproj.repository.SemesterRepo;
import ir.ac.ut.ieproj.calendar.Calendar;
import ir.ac.ut.iecommon.exceptions.AcceptWithdrawException;
import ir.ac.ut.iecommon.exceptions.RejectWithdrawException;
import ir.ac.ut.iecommon.exceptions.SubmitGradeException;

@Entity
@Table(name="professors")
public class Professor{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String firstName;
	private String lastName;

	@OneToMany(mappedBy="theProfessor")
	private List<Offering> allOfferings;

	public Professor(){
		allOfferings = new ArrayList<Offering>();
	}

	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}

	public String getFirstName(){
		return firstName;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getLastName(){
		return lastName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public List<Offering> getAllOfferings() {
		return allOfferings;
	}

	public void setAllOfferings(List<Offering> allOfferings) {
		this.allOfferings = allOfferings;
	}

	@Override
	public String toString(){
		return "Name: "+firstName+" "+lastName+", id: "+id;
	}

	//---------------- withdraw
	private void accepts(Student student, Offering wanted) throws AcceptWithdrawException {
		if(!RecordRepo.getRepository().rmOfferingRecord(student, wanted))
			throw new AcceptWithdrawException("Student is not registered in this offering!");
	}

	private void rejects(Student student, Offering wanted) throws RejectWithdrawException{
		if(!RecordRepo.getRepository().updateStatusRecord(student, wanted))
			throw new RejectWithdrawException("Student is not registered in this offering!");
	}

	public void acceptWithdrawReq(Student student, Offering wanted) throws AcceptWithdrawException{
		if(wanted.getTheProfessor().getId() != this.getId())
			throw new AcceptWithdrawException("Not allowed to accept withdraw, mismatch with professor of offering.");
		
 		if(!SemesterRepo.getRepository().hasOffering(wanted))
 			throw new AcceptWithdrawException("This offering is not presented in current semester.");

 		this.accepts(student, wanted);
	}

	public void rejectWithdrawReq(Student student, Offering wanted) throws RejectWithdrawException {
		if(wanted.getTheProfessor().getId() != this.getId())
			throw new RejectWithdrawException("Not allowed to reject withdraw, mismat with professor of offering.");
		
		if(!SemesterRepo.getRepository().hasOffering(wanted))
 			throw new RejectWithdrawException("This offering is not presented in current semester.");

 		this.rejects(student, wanted);
	}

	//---------------- grade
	private void grades(Student student, Offering wanted, float grade) throws SubmitGradeException{
		if(!RecordRepo.getRepository().updateGradeRecord(student, wanted, grade))
			throw new SubmitGradeException("Student is not registered in this offering!");
	}

	public void submitGradeOffering(Student student, Offering wanted, float grade) throws SubmitGradeException{
		if(wanted.getTheProfessor().getId() != this.getId())
			throw new SubmitGradeException("Not allowed to submit grade, mismatch with professor of offering.");

		Semester currentSemester = SemesterRepo.getRepository().getCurrentSemester();
        if (!currentSemester.checkActionTime(Semester.Action.GRADESUBMITION))
            throw new SubmitGradeException("System is not currently ("+Calendar.today()+") within the 'grade-submition' period.");

        this.grades(student, wanted, grade);
	}

	//----------------
	public ArrayList<Offering> getSemesterOfferings(){
		//TODO :shaghaghegh
		return null;
	}
}