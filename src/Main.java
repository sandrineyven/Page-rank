import com.mongodb.MongoClient;

public class Main {

	public static void main(String[] args) {

		//Connexion MongoDB
		System.out.println("Connexion MongoDB...");
		MongoClient mongoClient = new MongoClient();
//		MongoDatabase database = mongoClient.getDatabase("TP1");
//		MongoCollection<Document> collection = database.getCollection("spells");



		//Close DB
		mongoClient.close();
	}

}
