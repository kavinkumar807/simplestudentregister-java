package studentregister;

import java.util.ArrayList;
import java.util.Scanner;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

class DBmanager {
	
	public String collectionName = "StudentList";
	MongoClient mongoClient = MongoClients.create();
	Scanner input = new Scanner(System.in);
	MongoDatabase database = mongoClient.getDatabase("studentRegister");
	MongoCollection<Document> collection = database.getCollection(collectionName);
	Gson gson = new Gson();
	
	public void saveHashMap(ArrayList<Document> document) {
		if(collection.countDocuments()>0)
		{
			System.out.println("Database already contains this collection,want to override ?\n 1.Yes\n 2.No,Create a new Collection\n");
			
			int choice  = Integer.parseInt(input.next());
			switch(choice) {
				case 1:
					collection.drop();
					database.createCollection(collectionName);
					collection = database.getCollection(collectionName);
					collection.insertMany(document);
					break;
				case 2:
					System.out.println("Enter new Collection Name\n");
					String NewCollectionName = input.next();
					database.createCollection(NewCollectionName);
					collection = database.getCollection(NewCollectionName);
					collection.insertMany(document);
					break;
				default:
					System.out.println("Invalid input !!!");
					break;
			}
		}
		else
		{
			collection.insertMany(document);
		}
		input.close();
	}
	public ArrayList<Student> loadHashMap(){
		ArrayList<Student> students = new ArrayList<Student>();
		if(collection.countDocuments()<=0)
		{
			System.out.println("Collection Empty,Cannot load");
		}
		else {
			MongoCursor<Document> cursor = collection.find().iterator();
			try {
				while(cursor.hasNext())
				{
					students.add(gson.fromJson(cursor.next().toJson(),Student.class));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				cursor.close();
			}
		}
		return students;
	}
}
