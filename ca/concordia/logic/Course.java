package ir.ac.ut.ieproj.department;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;
import ir.ac.ut.ieproj.repository.CourseRepo;

@Entity
@Table(name="courses")
public class Course{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String name;
    private int credit;
    private int level;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="course_prereqs",
                joinColumns={@JoinColumn(name="course_id")},
                inverseJoinColumns={@JoinColumn(name="pre_id")})
    private List<Course> prereqs;
 
    @ManyToMany(mappedBy="prereqs")
    private List<Course> ipres;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="course_coreqs",
                joinColumns={@JoinColumn(name="course_id")},
                inverseJoinColumns={@JoinColumn(name="co_id")})
    private List<Course> coreqs;

    @ManyToMany(mappedBy="coreqs")
    private List<Course> icoreqs;

    @OneToMany(mappedBy="theCourse")
    private List<Offering> allOfferings;

    @ManyToMany(mappedBy="mandatory")
    private List<Program> mandatoryInPrograms;

    @ManyToMany(mappedBy="elective")
    private List<Program> electiveInPrograms;

    @ManyToMany(mappedBy="containments")
    private List<Package> containers;

    public Course(){
    	prereqs = new ArrayList<Course>();
        ipres = new ArrayList<Course>();
        coreqs = new ArrayList<Course>();
    	icoreqs = new ArrayList<Course>();
        allOfferings = new ArrayList<Offering>();
    }

    public Course(int credit, int lvl, String name){
        this.credit = credit;
        this.level = lvl;
        this.name = name;
        prereqs = new ArrayList<Course>();
        ipres = new ArrayList<Course>();
        coreqs = new ArrayList<Course>();
        icoreqs = new ArrayList<Course>();
        allOfferings = new ArrayList<Offering>();
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

    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public List<Course> getPrereqs() {
        return prereqs;
    }
    public void setPrereqs(List<Course> prereqs) {
        this.prereqs = prereqs;
    }

    public List<Course> getIpres() {
        return ipres;
    }
    public void setIpres(List<Course> ipres) {
        this.ipres = ipres;
    }

    public List<Course> getCoreqs() {
        return coreqs;
    }
    public void setCoreqs(List<Course> coreqs) {
        this.coreqs = coreqs;
    }

    public List<Course> getIcoreqs() {
        return icoreqs;
    }
    public void setIcoreqs(List<Course> icoreqs) {
        this.icoreqs = icoreqs;
    }

    public List<Program> getMandatoryInPrograms() {
        return mandatoryInPrograms;
    }
    public void setMandatoryInPrograms(List<Program> mandatoryInPrograms) {
        this.mandatoryInPrograms = mandatoryInPrograms;
    }

    public List<Program> getElectiveInPrograms() {
        return electiveInPrograms;
    }
    public void setElectiveInPrograms(List<Program> electiveInPrograms) {
        this.electiveInPrograms = electiveInPrograms;
    }

    @Override
    public String toString(){
    	String res = "Course id: "+id+", name: "+name+", credit: "+credit+", level: "+level+"\npre: ";
    	for(int i=0; i<prereqs.size(); ++i)
    		res = res + prereqs.get(i) +" ";
    	res += "\nco: ";
    	for(int i=0; i<coreqs.size(); ++i)
    		res = res + coreqs.get(i) +" ";
    	res += "\n";
    	return res;
    }

    private ArrayList<Course> coursePreRequirements(Student student){
        ArrayList<Course> result = new ArrayList<Course>();
        List<Course> passed = student.getAllPassedCourses();
        prereqs = CourseRepo.getRepository().fetchPreReqs(this);
        System.out.println(prereqs.size()+"------------------------");

        for(int i=0; i<prereqs.size(); ++i)
            if(!passed.contains(prereqs.get(i)))
                result.add(prereqs.get(i));
        return result;
    }

    private ArrayList<Course> courseCoRequirements(Student student){
        ArrayList<Course> result = coursePreRequirements(student);
        List<Offering> already = student.getAllTaken();
        coreqs = CourseRepo.getRepository().fetchCoReqs(this);
        for(int i=0; i<coreqs.size(); ++i){
            boolean exists = false;
            for(int j=0; j<already.size(); ++j)
                if(already.get(j).getTheCourse().equals(coreqs.get(i))){
                    exists = true;
                    break;
                }
            if(!exists)
                result.add(coreqs.get(i));
        }

        return result;
    }

    public String checkPrereqAndCoreq(Student student) {
        StringBuilder result = new StringBuilder("");
        ArrayList<Course> pres = this.coursePreRequirements(student);
        ArrayList<Course> cors = this.courseCoRequirements(student);

        for(int i=0; i<pres.size(); ++i){
            if(i == 0)
                result.append("Before Taking this course, It's required to pass: ");
            result.append(pres.get(i).getName());
            if(i != pres.size() - 1)
                result.append(", ");
            else
                result.append(".\n");
        }

        for(int i=0; i<cors.size(); ++i){
            if(i == 0)
                result.append("Before Taking this course, It's required to pass or take: ");
            result.append(cors.get(i).getName());
            if(i != cors.size() - 1)
                result.append(", ");
            else
                result.append(".\n");
        }

        return result.toString();
    }
    
    
}