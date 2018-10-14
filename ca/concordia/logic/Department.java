package ir.ac.ut.ieproj.department;

import java.util.ArrayList;
import ir.ac.ut.iecommon.interfaces.DepartmentI;
import ir.ac.ut.iecommon.exceptions.*;
import ir.ac.ut.ieproj.calendar.*;
import ir.ac.ut.ieproj.repository.*;

public class Department implements DepartmentI {

	private String name;
	private static Department onlyDept = null;

	private Department(String name){
		this.setName(name);
	}

	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public static Department getDepartment(){
        if(onlyDept == null)
            synchronized(Department.class){ //double checked locking
            if(onlyDept == null)
                 onlyDept = new Department("ECE"); 
            }
        return onlyDept;
	}

    //----------------------- DepartmentI

	public void take(String stdId, String offerId) throws TakeException
	,StudentNotFoundException, OfferingNotFoundException{
		Student student = getStudentById(stdId);
		Offering offering = getOfferingById(offerId);
		student.takeOffering(offering);
	}


	public void drop(String stdId, String offerId) throws DropException
	,StudentNotFoundException, OfferingNotFoundException{

		Student student = getStudentById(stdId);
		Offering offering = getOfferingById(offerId);
		student.dropOffering(offering);	
	}

	public void withdraw(String stdId, String offerId) throws WithdrawException
	,StudentNotFoundException, OfferingNotFoundException{

		Student student = getStudentById(stdId);
		Offering offering = getOfferingById(offerId);
		student.withdrawOffering(offering);	
	}

	public void acceptWithdraw(String stdId, String offerId, String profId) throws AcceptWithdrawException
	,StudentNotFoundException,OfferingNotFoundException, ProfNotFoundException{	

		Student student = getStudentById(stdId);
		Offering offering = getOfferingById(offerId);
		Professor prof = getProfById(profId);
		prof.acceptWithdrawReq(student, offering);
	}

	public void rejectWithdraw(String stdId, String offerId, String profId) throws RejectWithdrawException
	,StudentNotFoundException, OfferingNotFoundException, ProfNotFoundException{

		Student student = getStudentById(stdId);
		Offering offering = getOfferingById(offerId);
		Professor prof = getProfById(profId);
		prof.rejectWithdrawReq(student, offering);
	}

	public void submitGrade(String stdId, String offerId, String profId , float grade) throws SubmitGradeException
	,StudentNotFoundException,OfferingNotFoundException, ProfNotFoundException{
		
		Student student = getStudentById(stdId);
		Offering offering = getOfferingById(offerId);
		Professor prof = getProfById(profId);
		prof.submitGradeOffering(student, offering, grade);
	}

	public void checkDegreeReq(String stdId)throws CheckDegreeReqException
	,StudentNotFoundException{
		Student student = getStudentById(stdId);
		student.checkDegree();
	}

	//----------------------- helpers

	public Semester getCurrentSemester(){
		return SemesterRepo.getRepository().getCurrentSemester();
	}

	public Student getStudentById(String studentId) throws StudentNotFoundException{
		return StudentRepo.getRepository().getStudentById(Integer.parseInt(studentId));
	}

	public Offering getOfferingById(String offerId) throws OfferingNotFoundException{
		return OfferingRepo.getRepository().getOfferingById(Integer.parseInt(offerId));
	}

	public Professor getProfById(String profId)throws ProfNotFoundException{
		return  ProfessorRepo.getRepository().getProfessorById(Integer.parseInt(profId));
	}
        
}