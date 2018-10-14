package ir.ac.ut.ieproj.department;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name="policies")
@DiscriminatorValue(value="S") //Simple
public class SimpleElectivePolicy extends ElectivePolicy {
	
	boolean checkCompatibility(Student student, Course course){
		List<Course> passed = student.getAllPassedCourses();
		List<Course> electives = owner.fetchElectives();
		List<Course> passedElectives = new ArrayList<Course>();
		for(Course elec: electives)
			if(passed.contains(elec))
				passedElectives.add(elec);

		if(passedElectives.size() >= 5)
			return false;
		
		if(course.getLevel() == 0)
			return true;
		//else
		float gpa = student.getLastGPA();
		int gradCount = 0;
		for(Course elec: passedElectives)
			if(elec.getLevel() == 1)
				gradCount ++;

		if(gpa >= 18 && gradCount < 2)
			return true;
		else if(gpa < 18 && gradCount < 1)
			return true;
		else
			return false;
	}

}