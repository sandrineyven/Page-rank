import com.mongodb.MongoClient;

public class Main {

	public static void main(String[] args) {

		//Connexion MongoDB
		System.out.println("Connexion MongoDB...");
		MongoClient mongoClient = new MongoClient();
//		MongoDatabase database = mongoClient.getDatabase("TP1");
//		MongoCollection<Document> collection = database.getCollection("spells");

		//Creation des pages
		Page pageA = new Page("A");
		Page pageB = new Page("B");
		Page pageC = new Page("C");
		Page pageD = new Page("D");
		
		System.out.println(pageA.getName()+ " " + pageA.getDumpingFactor() + " " +pageA.getPageRank());
		//Creation du graphe
		pageA.addInBoundLinks(pageC);
		pageB.addInBoundLinks(pageA);
		pageC.addInBoundLinks(pageA);
		pageC.addInBoundLinks(pageB);
		pageC.addInBoundLinks(pageC);
		
		pageA.addOutBoundLinks(pageB);
		pageA.addOutBoundLinks(pageC);
		pageB.addOutBoundLinks(pageC);
		pageC.addOutBoundLinks(pageA);
		pageD.addOutBoundLinks(pageC);
		
//		for(int i =0; i<pageC.getInBoundLinks().size();i++){
//			System.out.println(pageC.getInBoundLinks().get(i).getName());
//		}

		//Close DB
		mongoClient.close();
	}

}
