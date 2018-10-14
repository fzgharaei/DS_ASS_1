package ir.ac.ut.ieproj.department;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;
import ir.ac.ut.ieproj.repository.ProgramRepo;

@Entity
@Table(name="programs")
public class Program {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;

	private String name;

	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="program_mandatories",
                joinColumns={@JoinColumn(name="program_id")},
                inverseJoinColumns={@JoinColumn(name="man_id")})
	private List<Course> mandatory;

	@ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="program_electives",
               joinColumns={@JoinColumn(name="program_id")},
               inverseJoinColumns={@JoinColumn(name="elec_id")})
	private List<Course> elective;

	@OneToOne(mappedBy="owner", cascade=CascadeType.ALL)
	private ElectivePolicy policy;

	public Program(){
		mandatory = new ArrayList<Course>();
		elective = new ArrayList<Course>();
	}

	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}

	public List<Course> getMandatory(){
		return mandatory;
	}
	public void setMandatory(List<Course> mandatory){
		this.mandatory = mandatory;
	}

	public List<Course> getElective(){
		return elective;
	}
	public void setElective(List<Course> elective){
		this.elective = elective;
	}

	public ElectivePolicy getPolicy(){
		return policy;
	}
	public void setPolicy(ElectivePolicy ep){
		this.policy = ep;
	}

	@Override
	public String toString(){
		return "id: "+id+" name: "+name;
	}

	public List<Course> fetchMandatories(){
		return ProgramRepo.getRepository().fetchMandatories(this);
	}

	public List<Course> fetchElectives(){
		return ProgramRepo.getRepository().fetchElectives(this);
	}

	public boolean checkConformity(Student student, Course course) {
		List<Course> mandatories = fetchMandatories();
		if(mandatories.contains(course))
			return true;
		//check with elective policy
		return policy.checkCompatibility(student, course);
	}


}