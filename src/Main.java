import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class Main {

	public static void main(String[] args) {

		//Connexion MongoDB
		System.out.println("Connexion MongoDB...");
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("TP1");
		MongoCollection<Document> collection = database.getCollection("PageRank");

		//Creation des pages
		List<Page> pages = new ArrayList<>();

		Page pageA = new Page("A");
		Page pageB = new Page("B");
		Page pageC = new Page("C");
		Page pageD = new Page("D");

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

		pages.add(pageA);
		pages.add(pageB);
		pages.add(pageC);
		pages.add(pageD);

		//Insertion dans MongoDB
		if(collection.countDocuments() == 0){
			for(int i=0; i<pages.size(); i++){
				Page currentpage = (Page) pages.get(i);

				//Creation du document json pour mongoDB
				Document doc = new Document("_id", i)
						.append("name",currentpage.getName())
						.append("PR",currentpage.getPageRank())
						.append("DF",currentpage.getDumpingFactor())
						.append("nb_outBoundLinks",currentpage.getOutBoundLinks().size());

				List<String> outBoundsLinksNames = new ArrayList<>();
				for(int j=0; j<currentpage.getOutBoundLinks().size();j++){
					outBoundsLinksNames.add(currentpage.getOutBoundLinks().get(j).getName());
				}
				doc.append("outBoundLinks",outBoundsLinksNames);
				//Insertion du document
				collection.insertOne(doc);
			}
		}

		//Affichage des résultats
		for(int page =0; page<pages.size();page++){
			Page currentpage = (Page) pages.get(page);
			System.out.println("Page " + currentpage.getName());

			System.out.println("PR: " + currentpage.getPageRank());
		}

		//TODO: Map Reduce
		//TODO: iterations
		//TODO: Bien afficher les resultats
		
		//Formule:
		//PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
		//		PR(A) is the PageRank of page A,
		//		PR(Ti) is the PageRank of pages Ti which link to page A,
		//		C(Ti) is the number of outbound links on page Ti and
		//		d is a damping factor which can be set between 0 and 1.

		//page.pagerank = (1-dumpingfactor) + dumping factor * ( pageT1.pagerank/numberoutboundlinksT1  ...)

		//Close DB
		mongoClient.close();
	}

}
