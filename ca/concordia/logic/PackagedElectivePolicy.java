package ir.ac.ut.ieproj.department;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="policies")
@DiscriminatorValue(value="P") //Packaged
public class PackagedElectivePolicy extends ElectivePolicy {
	
	@OneToMany(mappedBy="belongsTo")
	private List<Package> packages;

	public PackagedElectivePolicy(){
		packages = new ArrayList<Package>();
	}

	public List<Package> getPackages(){
		return packages;
	}
	public void setPackages(List<Package> packages){
		this.packages = packages;
	}

	public boolean canTake(int courseId , ArrayList<Course> passedCourses){
		/*for(Iterator<Integer> i = passedCourses.iterator();i.hasNext();){
            Integer passed = i.next();
            if(mandatoryCourses.contains(passed))
            	i.remove();
        }
        if(passedCourses.size()==0)return true;
        if(electiveCourses.contains(courseId)){
        	int sz = packages.size();
        	int count[] = new int [sz];
        	int ones=0 , twos=0;
        	for(int i = 0; i<sz; i++){
        		if(packages.get(i).contains(courseId))count[i]++;
        		for(Integer passed : passedCourses)
        			if (packages.get(i).contains(passed))count[i]++;
        		if(count[i]>2)return false;
        		else if(count[i]==2)twos++;
        		else if(count[i]==1)ones++;
        	}
        	if(twos>1||twos+ones>2)return false;
        }
        else{
        	for(Iterator<Integer> i = passedCourses.iterator();i.hasNext();){
	            Integer passed = i.next();
	            if(electiveCourses.contains(passed))
	            	i.remove();
	        }
	        return passedCourses.size()==0;
        }*/
		return true;
	}

	public boolean containReqiredElectives(ArrayList<Course>passedCourses){
		/*for(Iterator<Integer> i = passedCourses.iterator();i.hasNext();){
            Integer passed = i.next();
            if(mandatoryCourses.contains(passed))
            	i.remove();
        }
        if(passedCourses.size()==0)return false;
    	int sz = packages.size();
    	int count[] = new int [sz];
    	boolean ones=false , twos=false;
    	boolean outbox = false;
		for(Integer passed : passedCourses){
    		if(!electiveCourses.contains(passed)){
    			outbox=true;
    			continue;
    		}
	    	for(int i = 0; i<sz; i++){
    			if (packages.get(i).contains(passed))count[i]++;

	    	}
	    	for(int i = 0; i<sz; i++){
    			if(count[i]>=2)twos=true;
    			else if(count[i]>=1)ones=true;
	    	}

    			
    	}
    	return twos && ones && outbox;*/
    	return true;
	}

    boolean checkCompatibility(Student student, Course course){
        return true;
    }
	




}