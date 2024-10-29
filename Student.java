package homework1;

import java.util.ArrayList;
import java.util.Scanner;

interface Students {
	void viewAllCourses(); //Prints to screen all courses 
	void viewOpenCourses(); //Prints all courses that are not full
	void enroll(); //Performs input gathering to determine in which course to enroll student and calls enrollIn(course)
	void enrollIn(Course course); //Performs manipulation of objects to enroll student in course
	void withdrawFrom(); //Gathers from student to determine from which course to withdraw and calls withdrawFrom(course)
	void withdrawFrom(Course course); //Manipulates objects to actually withdraw 
	void viewEnrolledIn(); //Displays courses in which student is enrolled
	void action(); //Action menu when running program
}

public class Student extends User implements Students {
	//Stores course objects in which student is enrolled
	private ArrayList<Course> enrolledIn = new ArrayList<Course>();
	
	//Constructor
	public Student(String username, String password, String firstName, String lastName, CourseRegistrationSystem crs) {
		super(username, password, firstName, lastName, crs);
	}
	
	//Action menu
	public void action() {	
		//Wheter to quit action menu
		boolean proceed = false; 
		//User input
		String selection; 
		
		System.out.println("Welcome " + getFirstName() + " " + getLastName() + "!");
		
		//Stay in this action menu unless user quits (6. so proceed evaluates to true)
		do {
			System.out.println("1. View all courses\n2. View all courses that are not FULL\n"
					+ "3. Register on a course\n4. Withdraw from a course\n5. View enrolled courses\n"
					+ "6. Exit\n\nSelect: ");
			
			selection = scn.nextLine();
			
			switch (selection) {
			case "1":
				viewAllCourses();
				break;
			case "2":
				viewOpenCourses();
				break;
			case "3":
				enroll();
				break;
			case "4":
				withdrawFrom();
				break;
			case "5": 
				viewEnrolledIn();
				break;
			case "6":
				proceed = true;
				System.out.println("Signing out and quitting... Done.");
				break;
			default: 
				break;
			}
		} while (proceed == false);
	}
	
	//Displays all courses stored in CourseRegistrationSystem instance
	public void viewAllCourses() {
		for (Course course : getCRS().getAllCourses()) {
			course.viewCourse();
		}
	}
	
	//Views all courses this student is enrolled in
	public void viewEnrolledIn() {
		for (Course course : this.enrolledIn) {
			course.viewCourse();
		}
	}
	
	//Views all courses that are not full
	public void viewOpenCourses() {
		for (Course course : getCRS().getAllCourses()) {
			if (!course.isFull()) {
				course.viewCourse();
			}
		}
	}
	
	//Prompts user to enter the course in which they want to enroll
	public void enroll() {
		String courseName = " ";
		int section;
		String[] name = new String[2];		
		
		System.out.println("Enter the name of the course in which you want to enroll: ");
		courseName = scn.nextLine();
			
		System.out.println("Enter the section number in which you want to enroll: ");
		section = Integer.parseInt(scn.nextLine());
		
		//Verifying course entered by user exists
		Course courseToRegister = getCRS().findCourseByName(courseName, section);
		
		//If course exists and is not already full, proceed w/ registration
		if ((courseToRegister != null) && (courseToRegister.isFull() == false)) {
			System.out.println("Enter your first name: ");
			name[0] = scn.nextLine();
			System.out.println("Enter your last name: ");
			name[1] = scn.nextLine();
		
			//Double checking student information to confirm  registration
			if (getFirstName().equals(name[0]) && getLastName().equals(name[1])) {
				enrollIn(courseToRegister);
				System.out.println("Registered!");
			}
			else {
				System.out.println("Name entered does not match our records.");
			}
		}
		//If course exists but is full, student must not be able to register. Returns to action menu
		else if ((courseToRegister != null) && (courseToRegister.isFull())) {
			System.out.println("Course is full!");
		}
		else {
			System.out.println("The info provided does not match an existing course.");
		}
	}
	
	//Adds this student to enrolled ArrayList of given course and given course to this student's enrolledIn
	public void enrollIn(Course course) {
		course.enroll(this);
		this.enrolledIn.add(course);
	}
	
	//Gathers input from user to determine from which course, if possible, to withdraw
	public void withdrawFrom() {		
		System.out.println("Enter the name of the course from which you'd like to withdraw: ");
		String nameToWithdraw = scn.nextLine();
		
		boolean found = false; //course not yet found
		
		//Verifying student is indeed enrolled in course of interest
		for (Course course : this.enrolledIn) {
			//Verifying intent to withdraw if student is indeed enrolled
			if (course.getName().equals(nameToWithdraw)) {
				found = true; //course was found
				
				System.out.println("Enter your first name to confirm withdrawal: ");
				String firstName = scn.nextLine();
				
				System.out.println("Enter your last name to finalize: ");
				String lastName = scn.nextLine();
				if ((this.getFirstName().equals(firstName)) && (this.getLastName().equals(lastName))) {
					course.withdraw(this); //removes this student object from course's list of enrolled students
					this.enrolledIn.remove(course); //removes course from student's list of enrolled courses
					System.out.println("Successfully withdrawn!");
				}
				else {
					System.out.println("The name you entered does not match our records.");
				}
				break;
			}
		}
		if (!found) {//if course was not found
			System.out.println(nameToWithdraw + " does not match a course you are currently enrolled in.");
		}
	}
	
	//performs object manipulation to withdraw student from given course, used when admin deletes courses
	//to withdraw all students from the course to be deleted
	public void withdrawFrom(Course course) {
		course.withdraw(this);
		this.enrolledIn.remove(course);
	}

}
