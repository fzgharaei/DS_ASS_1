package ir.ac.ut.ieproj.department;

import java.util.ArrayList;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name="policies")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
   name="discriminator",
   discriminatorType=DiscriminatorType.STRING
)
@DiscriminatorValue(value="B")  //Base
public abstract class ElectivePolicy {
	@Id
	@GeneratedValue(generator="gen")
    @GenericGenerator(name="gen", strategy="foreign", parameters=@Parameter(name="property", value="owner"))
	private int id;   //It's actually the program_id

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	protected Program owner;

	public Program getOwner(){
		return owner;
	}
	public void setOwner(Program owner){
		this.owner = owner;
	}

	abstract boolean checkCompatibility(Student student, Course course);
}
