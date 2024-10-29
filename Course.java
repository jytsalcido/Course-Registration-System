package homework1;

import java.util.ArrayList;

public class Course extends CourseRegistrationSystem {
	private String name;
	private String courseId;
	private int seats; //seats available
	private int numEnrolled = 0; 
	private int sectionNum;
	private String instructor; 
	private String location;
	private ArrayList<Student> enrolled = new ArrayList<Student>();

	
	//constructor
	public Course(String name, String courseId, int seats, int numEnrolled, 
			String instructor, int sectionNum, String location) {
		this.name = name;
		this.courseId = courseId;
		this.seats = seats;
		this.sectionNum = sectionNum;
		this.instructor = instructor;
		this.location = location;
		this.numEnrolled = numEnrolled;
	}
	
	//creates and returns a string of the attributes of the class
	public String toString() {
		return getName() + "; " + getCourseId() + "; " + getSeats() + " seats; " + getNumEnrolled() + 
				" students enrolled; Section " + getSectionNum() + "; Lecturer " + getInstructor() + 
				"; At " + getLocation() + "\n\n";
		
	}
	
	//setters and getters
	public void setNumEnrolled(int numEnrolled) {
		this.numEnrolled = numEnrolled;
	}
	
	public void setSeats(int seats) {
		this.seats = seats;
	}
	
	public void setSectionNum(int sectionNum) {
		this.sectionNum = sectionNum;
	}
	
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCourseId() {
		return this.courseId;
	}
	
	public int getNumEnrolled() {
		return this.numEnrolled;
	}
	
	public int getSeats() {
		return this.seats;
	}
	
	public int getSectionNum() {
		return this.sectionNum;
	}
	
	public String getInstructor() {
		return this.instructor;
	}
	
	public String getLocation() { 
		return this.location;
	}
		
	//boolean evaluates whether the number of enrolled students equals the number of seats in course
	public boolean isFull() {
		return (this.getNumEnrolled() == this.getSeats());
	}
	
	//calls and prints the toString() method
	public void viewCourse() {
		System.out.println(this.toString());
	}
	
	//more getters
	public ArrayList<Student> getEnrolled() {
		return this.enrolled;
	}
	
	//validation of data performed by methods that call this function
	public void enroll(Student student) {
		this.enrolled.add(student); //Adds student object to the enrolled ArrayList
		this.numEnrolled++; //Updates the number of enrolled students in the course
	}
	
	//validation of data performed by methods that call this function
	public void withdraw(Student student) {
		this.enrolled.remove(student); //removes student object from this enrolled ArrayList
		this.numEnrolled--; //Decreases the counter of enrolled students
	}
}
