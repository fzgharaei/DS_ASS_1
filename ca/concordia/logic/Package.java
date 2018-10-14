package ir.ac.ut.ieproj.department;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

@Entity
@Table(name="packages")
public class Package{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String name;

	@ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="package_courses",
                joinColumns={@JoinColumn(name="package_id")},
                inverseJoinColumns={@JoinColumn(name="course_id")})
	private List<Course> containments;

	@ManyToOne
	private PackagedElectivePolicy belongsTo;

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public List<Course> getContainments(){
		return containments;
	}
	public void setContainments(List<Course> containments){
		this.containments = containments;
	}
} 
