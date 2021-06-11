package studentregister;

import org.bson.Document;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

class Admin {
	//add,delete,edit,save,load,view
	HashMap<Integer,Student> studentRegister = new HashMap<Integer,Student>();
	Scanner input = new Scanner(System.in);
	Gson gson = new Gson();
	DBmanager dbManager = new DBmanager();
	
	public void add(String Name,Integer ID,String[] Subjects) {
		Student tempStudent = new Student(Name,ID,Subjects);
		studentRegister.put(ID, tempStudent);
	}
	public void edit(Integer ID) {
		Student tempStudent  = studentRegister.get(ID);
		if(tempStudent==null) {
			System.out.println("ID not present");
		}
		else
		{
			System.out.println(tempStudent.toString());
			System.out.println("What do you want to edit?\n 1. Name\n 2.Subjects");
			int choice = Integer.parseInt(input.next());
			switch(choice) {
				case 1:
					System.out.println("Enter new Name:\n");
					String newName = input.next();
					tempStudent.setStudentName(newName);
					studentRegister.put(ID, tempStudent);
					break;
				case 2:
					String[] subjectsTemp = tempStudent.getStudentSubjects();
					for(int i=0;i<subjectsTemp.length;i++)
					{
						System.out.println(i+". "+subjectsTemp[i]);
					}
					System.out.println("Enter which Subject you want to change?\n");
					int change = Integer.parseInt(input.next());
					System.out.println("Enter new Subject:\n");
					String subjectString = input.next();
					subjectsTemp[change] = subjectString;
					tempStudent.setStudentSubjects(subjectsTemp);
					studentRegister.put(ID, tempStudent);
					break;
				default:
					System.out.println("Invalid input!!!");
					break;
			}
		}
	}
	public void delete(Integer ID) {
		Student tempStudent = studentRegister.remove(ID);
		if(tempStudent==null) {
			System.out.println("ID not present,Student not found");
		}
		else
		{
			System.out.println(tempStudent.toString()+"deleted");
		}
	}
	public void save() throws IOException {
		System.out.println("What type of save?\n 1. File \n 2.Database");
		int choice = Integer.parseInt(input.next());
		switch(choice) {
			case 1:
				System.out.println("Enter Qualified filename");
				String fileName = input.next();
				File file = new File(fileName);
				FileOutputStream fileOutputStream = null;
				try {
					fileOutputStream = new FileOutputStream(file);
					
				}
				catch(FileNotFoundException e){
					try {
						file.createNewFile();
					}
					catch(Exception e2){
						e2.printStackTrace();
					}
				}
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(studentRegister);
				objectOutputStream.close();
				fileOutputStream.close();
				System.out.println("Saved...");
				break;
			case 2:
				ArrayList<Document> documents = new  ArrayList<Document>();
				for(Student student : studentRegister.values())
				{
					documents.add(Document.parse(gson.toJson(student)));
				}
				dbManager.saveHashMap(documents);
				
				break;
			default:
				System.out.println("Invalid input!!!");
				break;
		}
	}
	public void load() throws IOException, ClassNotFoundException{
		System.out.println("What type of load?\n 1. File \n 2.Database");
		int choice = Integer.parseInt(input.next());
		switch(choice) {
			case 1:
				System.out.println("Enter Qualified filename");
				String fileName = input.next();
				File file = new File(fileName);
				if(file.exists()) {
					FileInputStream fileInputStream = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					@SuppressWarnings("unchecked")
					HashMap<Integer , Student> objectHashMap = (HashMap<Integer, Student>) objectInputStream.readObject();
					studentRegister = objectHashMap;
					objectInputStream.close();
					fileInputStream.close();
				}else
				{
					System.out.println("File Not Found!!!");
				}
				break;
			case 2:
				ArrayList<Student> students = dbManager.loadHashMap();
				if(students!=null)
				{
					studentRegister = new HashMap<Integer,Student>();
					for(Student student : students)
					{
						studentRegister.put(student.getStudentId(),student);
					}
				}
				break;
			default:
				System.out.println("Invalid input!!!");
				break;	
		}
	
	}
	
	public void view() {
		if(studentRegister.isEmpty())
		{
			System.out.println("No values Inserted");
		}
		else
		{
			for(Student student : studentRegister.values())
			{
				System.out.println(student.toString());
			}
		}
	}
}
	
	
