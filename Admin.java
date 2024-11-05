package homework1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

interface Admins {
	void createCourse();
	void deleteCourse(); 
	void editCourse();
	void viewCourse();
	void registerNewStudent(); 
	void viewAllCourses();
	void writeFullCoursesToFile();
	void viewFullCourses();
	ArrayList<Course> getFullCourses();
	void viewStudentsIn(); 
	void viewCoursesFor();
	void action();
}

public class Admin extends User implements Admins {
	//Constructor
	public Admin(String username, String password, String firstName, String lastName, CourseRegistrationSystem crs) {
		super(username, password, firstName, lastName, crs);
	}
	
	public void sortByEnrolled() {
		getCRS().sortByEnrolled();
	}
	
	//Action menu when program is running
	public void action() {		
		boolean proceed = false; //boolean to break out of action menu
		String selection;
		
		//Stay in and return to action menu unless proceed evaluates to true (3. is selected)
		do {
			System.out.println("1. Course Management\n\n2. Reports\n\n3. Exit\n\n\nSelection: ");
			
			selection = scn.nextLine();
			
			switch (selection) {
			case "1": //Calls courseManagement() sub menu
				courseManagement();
				break;
			case "2": //Calls reports() sub menu
				reports();
				break;
			case "3": //Quits action menu and signs out
				proceed = true;
				System.out.println("Signing out and quitting... Done.");
				break;
			default: //Returns to this same menu if any input besides acceptable
				break;
			}
		} while (proceed == false);
	}
	
	//subAction menu related to course management
	public void courseManagement() {		
		boolean proceed = false;

		//Loop to remain in action menu unless proceed evaluates to true (6. is selected)
		do {
			System.out.println("1. Create a new course\n2. Delete a course\n3. Edit a course\n"
					+ "4. Display information for a given course\n5. Register a student\n6. Exit\n\nSelection: ");
				
			switch (scn.nextLine()) {
			case "1": //Proceeds to call createCourse() and returns to menu;
				createCourse();
				break;
			case "2":
				deleteCourse();
				break;
			case "3":
				editCourse(); //Calls editCourse() sub sub menu then returns to this menu;
				break;
			case "4":
				viewCourse();
				break;
			case "5":
				registerNewStudent();
				break;
			case "6":
				proceed = true;
				break;
			default:
				break;	
			}
		} while (proceed == false);
	}
	
	//subAction menu related to creating or viewing reports
	public void reports() {		
		boolean proceed = false;
		String selection;
		
		//Stay in action menu unless 6. is selected to evaluate proceed to true and break out of loop
		do {
			System.out.println("1. View all courses\n2. View all courses that are full\n3. Write to a file the list of courses that are full"
					+ "\n4. View the names of the students being refistered in a specific course\n5. View the list of courses that a "
					+ "given student is being registered on\n6. Sort courses by number of students registered\n7. Exit\n\nSelection: ");
			
			selection = scn.nextLine();
			
			//Action to perform given user input
			switch (selection) {
			case "1":
				viewAllCourses();
				break;
			case "2":
				viewFullCourses();
				break;
			case "3":
				writeFullCoursesToFile();
				break;
			case "4":
				viewStudentsIn();
				break;
			case "5":
				viewCoursesFor();
				break;
			case "6":
				sortByEnrolled();
			case "7":
				proceed = true;
				break;
			default:
				break;
			}
		} while (proceed == false);
	}

	//Gathers input from user with which to create new Student object
	public void registerNewStudent() {		
		System.out.println("Enter student's first name: ");
		String firstName = scn.nextLine(); //storing user input as firstName
		
		System.out.println("Enter student's last name: ");
		String lastName = scn.nextLine(); //storing input as lastName
		
		System.out.println("Create username for student: ");
		String username = scn.nextLine(); //storing input as username
		
		System.out.println("Finally, create password for student: ");
		String password = scn.nextLine(); //storing input as password
		
		//Stores new Student object in the crs object's students ArrayList
		getCRS().addStudent(new Student(username, password, firstName, lastName,this.getCRS()));
		
		//Confirms creation of student object by extracting student object's first and last name
		//stored in crs students ArrayList
		System.out.println(getCRS().findStudent(firstName, lastName).getFirstName() + " " +
				getCRS().findStudent(firstName, lastName).getLastName() + " was added as a student.");
		System.out.println("Username: " + getCRS().findStudent(firstName, lastName).getUsername() 
				+ " Password: " + getCRS().findStudent(firstName, lastName).getPassword());
	
	}
	
	//Gathers info from user with which to create new course
	public void createCourse() {		
		System.out.println("Enter the name of the course: ");
		String newName = scn.nextLine();
		
		System.out.println("Enter the courseId of the course: ");
		String newCourseId = scn.nextLine();
		
		System.out.println("Enter the number of available seats in course: ");
		int newSeats = Integer.parseInt(scn.nextLine()); //Using scn.nextLine() then casting to int 
		//because scn.nextInt() does not read the return after an int input so next scn.nextLine() messes up
		
		System.out.println("Enter the number of students already enrolled in the course: ");
		int newNumEnrolled = Integer.parseInt(scn.nextLine());
		
		System.out.println("Enter the section number of the course: ");
		int newSectionNum = Integer.parseInt(scn.nextLine());
		
		System.out.println("Enter the instructor of the course: ");
		String newInstructor = scn.nextLine();
		
		System.out.println("Enter the location of the course: ");
		String newLocation = scn.nextLine();
		
		//Creating new course from user input and storing Course object in crs courses ArrayList
		getCRS().addCourse(new Course(newName, newCourseId, newSeats, newNumEnrolled, newInstructor, newSectionNum, newLocation));
		System.out.println("New course was created.");
	}
	
	//Gathers from user which course to delete 
	public void deleteCourse() {
		System.out.println("Enter the Course Id of the course you want to delete: ");
		String idToDelete = scn.nextLine();
		
		System.out.println("Enter the section number that you want to delete: ");
		int secToDelete = Integer.parseInt(scn.nextLine());
		
		//Looping over all courses to check that course of interest exists
		for (Course course : getCRS().getAllCourses()) {
			if ((course.getCourseId().equals(idToDelete)) && (course.getSectionNum() == secToDelete)) {	
				//Withdrawing all students enrolled in the course before deleting it
				for (Student student : getCRS().getAllStudents()) {
					student.withdrawFrom(course);
				}
				//removing course object from its storage
				getCRS().removeCourse(course);
				System.out.println(idToDelete + " section "+ secToDelete + " was successfully deleted. "
						+ "All enrolled students, if any, were withdrawn.");
				break;
			}
		}
	}
	
	//Action menu to edit information for a given course
	public void editCourse() {		
		System.out.println("Enter the Course Id of the course you wish to edit: ");
		String courseId = scn.nextLine();
		
		System.out.println("Enter the section number that you wish to edit: ");
		int sectionNum = Integer.parseInt(scn.nextLine());
		
		//Looking for courses object in courses ArrayList of crs
		Course courseToEdit = getCRS().findCourseById(courseId, sectionNum);
		String selection;
		
		//If course of interest exists, asks user which field to edit
		if (courseToEdit != null) {
			System.out.println("1. Number of students enrolled\n2. Max number of seats available"
						+ "\n3. Section number\n4. Course instructor\n5. Course location\n\nSelection: ");
				
			selection = scn.nextLine();
			
			//Action to perform given user input
			//Displays current field value and asks user to enter new field value
			switch (selection) {
			case "1":
				System.out.println(courseToEdit.getNumEnrolled() + " students currently enrolled."
						+ " Enter updated number of enrollees: ");
				courseToEdit.setNumEnrolled(Integer.parseInt(scn.nextLine()));
				break;
			case "2":
				System.out.println(courseToEdit.getSeats() + " total seats. Enter updated number of seats: ");
				courseToEdit.setSeats(Integer.parseInt(scn.nextLine()));
				break;
			case "3":
				System.out.println("Current section nuber: " + courseToEdit.getSectionNum() + ". Update"
						+ "section number to ");
				courseToEdit.setSectionNum(Integer.parseInt(scn.nextLine()));
				break;
			case "4":
				System.out.println("Current instructor: " + courseToEdit.getInstructor() + ". Update"
						+ "instructor to ");
				courseToEdit.setInstructor(scn.nextLine());
				break;
			case "5":
				System.out.println("Course located at " + courseToEdit.getLocation() + ". Enter "
						+ "updated location: ");
				courseToEdit.setLocation(scn.nextLine());
				break;
				//Case if any foreign input is given
			default:
				System.out.println("You can only edit the aforementioned fields. Leaving "
						+ "the editor now...");
				break;
			}
		}
		else {
			System.out.println("The information provided does not match an existing course");
		}
	}
	
	//Gets courses ArrayList from CourseRegistrationSystem instance and displays all the courses
	public void viewAllCourses() {
		for (Course course : getCRS().getAllCourses()) {
			course.viewCourse();
		}
	}
	
	public ArrayList<Course> getFullCourses() {
		ArrayList<Course> fullCourses = new ArrayList<Course>();
		
		//Adds all courses that are full to an ArrayList to return
		for (Course course : getCRS().getAllCourses()) {
			if (course.isFull()) {
				fullCourses.add(course);
			}
		}
		return fullCourses;
	}
	
	//Prints all courses returned from getFullCourses()
	public void viewFullCourses() {
		for (Course course : getFullCourses()) {
			course.viewCourse();
		}
	}
	
	//Using FileWriter as demonstrated in class guide code
	public void writeFullCoursesToFile() {	
		//Prompting user to choose a valid name for file to be written
		System.out.println("Enter a valid name for the file (no spaces and do not include .txt termination): ");
		String userIn = scn.nextLine();
		
		String nameOfFile = userIn + ".txt"; 
		
		//Following guide code
		try {
			FileWriter fileWriter = new FileWriter(nameOfFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			bufferedWriter.write("Full courses: ");
			bufferedWriter.newLine();
			for (Course course : this.getFullCourses()) {
				bufferedWriter.write(course.toString());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			fileWriter.close();
		}
		catch (IOException e) {
			System.out.println("Error writing file '" + nameOfFile + ".'");
			e.printStackTrace();
			
		}
	}
	
	public void viewCourse() {	
		//Asks user to input a course id of a course.
		System.out.println("Enter the Course Id of the course you wish to view: ");
		String courseId = scn.nextLine();
		
		System.out.println("Enter the section number of the course you wish to view: ");
		int sectionNum = Integer.parseInt(scn.nextLine());
		
		//Finds course given user input
		Course course = getCRS().findCourseById(courseId, sectionNum);
		
		//Validates that a course was matched before calling viewCourse() to display course information
		if (course != null) {
			course.viewCourse();
		}	
		else {
			System.out.println("The info provided does not match an existing course. ");
		}
	}
	
	//Asks user for which course they'd like to view enrolled students
	public void viewStudentsIn() {		
		System.out.println("Enter the Course Id of the course: "); //Asking for course id
		String courseId = scn.nextLine();
		
		//Further asking for section number because duplicate names exist
		System.out.println("Enter the section number to view its roster: ");
		int sectionNum = Integer.parseInt(scn.nextLine());
		
		//Looking for desired course
		Course course = getCRS().findCourseById(courseId, sectionNum);
		
		//Verify a course was found by that name
		if (course != null) {
			System.out.println(course.getName() + " section " + course.getSectionNum() + ":");
			//and iterate for each student in ArrayList of enrolled students to view them
			for (Student student : course.getEnrolled()) {
				System.out.println(student.getFirstName() + " " + student.getLastName());
			}
		}
		else {
			System.out.println("There is no course that matches that Course Id and section number.");
		}
	}
	
	//Asks user to input name of student to view
	public void viewCoursesFor() {		
		System.out.println("Enter the first name of the student: ");
		String studFirstName = scn.nextLine();
		
		System.out.println("Enter the student's last name: ");
		String studLastName = scn.nextLine();
		
		//Finds student if such a student exists
		Student student = getCRS().findStudent(studFirstName, studLastName);
		
		//If a student was matched
		if (student != null) {
			//Verify it is the desired student and view student object's courses in enrolledIn ArrayList
			System.out.println(student.getFirstName() + " " + student.getLastName() + " is enrolled in the following:\n");
			student.viewEnrolledIn();
		}
		else {
			System.out.println("The name provided does not match an existing student.");
		}
	}
	
	public void sortCoursesByEnrolled() {
		ArrayList<Course> copy;
		Course temp;
		
		for (int i = 1;  i < getCRS().getAllCourses().size(); i++) {
			temp = getCRS().getAllCourses().get(i - 1);
			if (getCRS().getAllCourses().get(i).getNumEnrolled() <= temp.getNumEnrolled()) {
				
			}
		}
	}
	
}
