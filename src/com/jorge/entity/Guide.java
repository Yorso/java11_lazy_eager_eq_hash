package com.jorge.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

// ONE SIDE entity => INVERSE END

@Entity
@Table(name="guide")
//@BatchSize(size=4)
/**
 * Batch Fetching:
 * 
 * @BatchSize(size=4) => Fetch strategy must be "fetch=FetchType.LAZY" in Guide.java (here). If we want to get a student information and their guide info, it loads their guide information
 * and the next 3 guide informations (of other students) with just a select statement (select guide from Guide guide where guide.id in (?,?,?,?))
 *  
 * One SELECT statement loads 4 Guide objects. In the next select statement will load 4 Guide objects more
 *  
 * It is useful if the number of students becomes too large in DB (i.e.: one guide per student)
 *  
 * Depending of the size of the application, we can optimize the lazy fetching strategy to improbe the performance of the application
 * by the value of the @BatchSize
 *
 * I.e.: one guide per student:
 * 		Without @BatchSize: 1 SELECT to get 1000 different student info
 * 			  	 	        1000 SELECT to get 1000 different guide info
 * 
 * 		With @BatchSize(size=4): 1 SELECT to get 1000 different student info
 * 							     1000/4 SELECT to get 1000 different guide info. 4 associated Guide objects are loaded in a batch of 4 by 1 SELECT
 * 
 * Using Batch Fetching, Hibernate can load several uninitialized proxies, even if just one proxy is accessed
 * 
 * Batch fetching is an optimization of the Lazy Fetching strategy
 * 
 * Batch Feching is not valid in Student.java
 * 
 * @BatchSize is imported from org.hibernate.annotations.BatchSize
 * 
 */
public class Guide {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private Long id;
	
	@Column(name="staff_id", nullable=false)
	private String staffId;
	
	@Column // Hibernate will create this column with the name of this attribute => "name" (CASE SENSITIVE!!!)
	private String name;
	
	@Column(name="salary")
	private Integer salary;
	
	// By default, @OneToMany and @ManyToMany associations are lazy
	@OneToMany(mappedBy="guide", 
			   cascade={CascadeType.PERSIST}, // 'guide' is the name of the private attribute in Student.java class => private Guide guide;
			 							  	  // It tells Hibernate to get the set of students that are using the foreign key => guide_id in DB => private Guide guide;
											  // CascadeType.PERSIST: Everything you change in guide row is save in its linked student rows automatically
											  // 'mappedBy' attribute declares this class as not responsible for the relationship => inversed end
			   fetch=FetchType.EAGER) // This attribute means that it must load student data when fetch guide data from database = lazy fetch OFF
	                                  // Check int numberOfStudents = students.size(); in Main.java
	private Set<Student> students = new HashSet<Student>();
	
	public Guide() {}
	
	public Guide(String staffId, String name, Integer salary) {
		this.staffId = staffId;
		this.name = name;
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	// This makes Guide responsible about the relationship to update data in both entities
	// We can update data from One side (not owner)
	public void addStudent(Student student){
		students.add(student);
		student.setGuide(this);;
	}

	@Override
	public String toString() {
		return "Guide [id=" + id + ", staffId=" + staffId + ", name=" + name + ", salary=" + salary + ", students="
				+ students + "]";
	}
	
}
