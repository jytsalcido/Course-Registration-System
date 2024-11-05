package homework1;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CourseRegistrationSystem implements Serializable {
	//private attributes for abstraction and encapsulation
	private ArrayList<Course> courses;
	private ArrayList<Student> students;
	private ArrayList<Admin> admins;
	//public static to allow all sub classes to use this same Scanner instance
	//Solves the issue where I was closing the Scanner(System.in) within methods 
	//then could no longer access it again
	public static Scanner scn = new Scanner(System.in);

	//No-args constructor
	public CourseRegistrationSystem() {
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
		admins = new ArrayList<Admin>();
	}
	
	public void sortByEnrolled() {
		Collections.sort(this.courses, new Comparator<Course>() {
			public int compare(Course c1, Course c2) {
				return Integer.compare(c1.getNumEnrolled(), c2.getNumEnrolled()); }});
	}
	
	//main function running program
	public static void main(String[] Args) throws FileNotFoundException {
		System.out.println("Welcome to the Course Registration System.\n");
		
		CourseRegistrationSystem crs = null;

		//Deserializing following class guide
		try {
			//FileInputStream receiving bytes from file
			FileInputStream fis = new FileInputStream("CourseRegistrationSystem.ser");
			
			//ObjectInputStream reconstructing object from bytes
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			//Casting object as CourseRegistrationSystem
			crs = (CourseRegistrationSystem)ois.readObject();
			
			ois.close();
			fis.close();
		}
		catch (IOException i) {
			System.out.println("No serialized object exists yet. Reading from csv...\n");
		}
		catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
		
		//If there is no existing serialized object
		if (crs == null) {
			crs = new CourseRegistrationSystem(); //creating new instance of CourseRegistrationSystem
			
			crs.addAdmin(new Admin("Admin", "Admin001", " ", " ",crs)); //Adding default admin per instructions
		
			String fileName = "MyUniversityCourses.csv"; //Name of csv from which to populate courses
			String input = new Scanner(new File(fileName)).useDelimiter("\\A").next(); //Reading contents of csv into String
			
			//getting rid of unnecessary content from input
			input = input.replace("Course_Name,","").replace("Course_Id,","")
					.replace("Maximum_Students,","").replace("Current_Students,","")
					.replace("List_Of_Names,","").replace("Course_Instructor,","")
					.replace("Course_Section_Number,","").replace("Course_Location","");
			
			//using StringTokenizer as exemplified in class code and delimiting tokens by carriage return, new line, and commas
			StringTokenizer strTokens = new StringTokenizer(input,"\r\n,");
			
			String name, courseId, empty, instructor, location;
			int seats, numEnrolled, sectionNum;
			
			//Continue while strTokens has more tokens
			//Populating parameters to call Course() constructor
			while (strTokens.hasMoreTokens()) {
				name = strTokens.nextToken();
				courseId = strTokens.nextToken();
				seats = Integer.parseInt(strTokens.nextToken());
				numEnrolled = Integer.parseInt(strTokens.nextToken());
				empty = strTokens.nextToken(); //storing null value from List_of_Names
				instructor = strTokens.nextToken();
				sectionNum = Integer.parseInt(strTokens.nextToken());
				location = strTokens.nextToken();
			
				//Creating new Course instance and storing in crs object's courses ArrayList
				crs.addCourse(new Course(name, courseId, seats, numEnrolled, instructor, 
						sectionNum, location));
			}
		}
		//Calling action menus
		//Overridden methods so menus differ whether crs.signIn() returns an Admin or Student
		crs.signIn().action();
		
		//Serializing following guide from class code
		try {
			//writing data to file w/ FileOutput
			FileOutputStream fos = new FileOutputStream("CourseRegistrationSystem.ser");
			
			//will be writing objects to data w/ oos
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			//writing objects
			oos.writeObject(crs);
			
			oos.close();
			fos.close();
			System.out.println("Successfully serialized.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Action menu that gathers log-in info from user and returns a matched Admin or Student
	public User signIn() {
		boolean proceed = false; //used to break out of this menu
		
		User match = null;
		String selection;
		
		//Will loop infinitely until user enters correct info or selects 3. to evaluate proceed to true
		while (proceed == false) {			
			System.out.println("You must sign in either as an admin or a student.\n\n1. Admin\n2. "
					+ "Student\n3. Exit\n\nSelection: ");
			selection = scn.nextLine();
			
			switch (selection) {
			case "1": //Searching for the log-in info provided in the admins ArrayList of crs object
				System.out.println("Admin:\n");
				String [] userNPass1 = getLogIn(); //Gathering input values
				
				for(Admin admin : this.getAllAdmins()) {
					//Validating that values provided are correct username and password
					if ((admin.getUsername().equals(userNPass1[0])) 
							&& (admin.getPassword().equals(userNPass1[1]))) {
						proceed = true;
						match = admin;
						break; //break out of for loop
					}
				}
				break; //break out of switch but will loop again if proceed not evaluated to true
				
			case "2": //Searching for input value match in the students ArrayList of the crs object
				System.out.println("Student:\n");
				String [] userNPass2 = getLogIn(); //Gathering input values
				
				for(Student student : this.getAllStudents()) {
					//Verifying values match the user and password of an existing Student object
					if ((student.getUsername().equals(userNPass2[0])) 
							&& (student.getPassword().equals(userNPass2[1]))) {
						proceed = true; //will break out of while loop
						match = student;
						break; //breaking for loop
					}
				}
				break; //breaking switch
		
			case "3": //Quitting. No actions were taken. No serialization done.
				System.out.println("Exitting... Done");
				System.exit(0);
			
			default: //Stays in this menu unless acceptable menu options selected
				break;
			}
		}
		return match;
	}
	//Prompts user to enter username and password Strings to return to signIn() action method. 
	public String[] getLogIn() {		
		String[] userNPass = new String[2];
		
		System.out.println("Enter your username: ");
		userNPass[0] = scn.nextLine();
		
		System.out.println("Enter your password: ");
		userNPass[1] = scn.nextLine();
		
		return userNPass;
	}
	
	//Adds Course object to courses ArrayList, public method for private attribute
	public void addCourse(Course course) {
		this.courses.add(course);
	}
	
	//Removes course from courses ArrayList, public method to modify private attribute
	public void removeCourse(Course course) {
		this.courses.remove(course);
	}
	
	//Getters
	public ArrayList<Course> getAllCourses() {
		return this.courses;
	}
	
	public ArrayList<Student> getAllStudents() {
		return this.students;
	}
	
	public ArrayList<Admin> getAllAdmins() {
		return this.admins;
	}
	
	//public void setStudent(Student student)
	
	//public method to add a Student object to private students ArrayList of crs object
	public void addStudent(Student student) {
		this.students.add(student);
	}
	
	//public method to add Admin to crs private Admins ArrayList
	public void addAdmin(Admin admin) {
		this.admins.add(admin);
	}
	
	//method that returns a matching Student object from given name inputs
	public Student findStudent(String firstName, String lastName) {
		//loops over each student in students to check if match
		for (Student student : getAllStudents()) {
			if ((student.getFirstName().equals(firstName)) && (student.getLastName().equals(lastName))) {
				return student;
			}
		}
		return null;
	}
	
	//method returning a matching Course object from given course name and section number inputs
	public Course findCourseByName(String name, int sectionNum) {
		//checks each existing course
		for (Course course : getAllCourses()) {
			if ((course.getName().equals(name)) && (course.getSectionNum() == sectionNum)) {
				return course;
			}
		}
		return null;
	}
	
	//method returning a matching Course object from given course Id and section number
	public Course findCourseById(String courseId, int sectionNum) {
		for (Course course : getAllCourses()) {
			if ((course.getCourseId().equals(courseId)) && (course.getSectionNum() == sectionNum)) {
				return course;
			}
		}
		return null; //returns null if no such course exists. Handled by wrapper functions
	}
}
