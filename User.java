package homework1;

import java.util.ArrayList;

public abstract class User extends CourseRegistrationSystem {
	//all private for abstraction and encapsulation
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private CourseRegistrationSystem crs; 
	
	//Typical getters and setters
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getUsername () {
		return this.username;
	}
	
	public String getPassword () {
		return this.password;
	}
	
	public CourseRegistrationSystem getCRS() {
		return this.crs;
	}
	//Constructor
	public User(String username, String password, String firstName, String lastName, CourseRegistrationSystem crs) {
		setUsername(username);
		setPassword(password);
		setFirstName(firstName);
		setLastName(lastName);
		this.crs = crs;
	}
	
	abstract void viewAllCourses(); //Print to screen all courses, blueprint for children classes
	
	abstract void action(); //Action menu, blueprint for children classes
	
}