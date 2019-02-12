import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MapReduceIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;

public class Main {

	static double DAMPING_FACTOR = 0.85;
	static int NB_ITERATIONS = 20;

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

		//Creation des liens sortants
		pageA.getOutBoundLinks().add(pageB);
		pageA.getOutBoundLinks().add(pageC);
		pageB.getOutBoundLinks().add(pageC);
		pageC.getOutBoundLinks().add(pageA);
		pageD.getOutBoundLinks().add(pageC);

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
						.append("nb_outBoundLinks",currentpage.getOutBoundLinks().size());

				//Recuperation des noms des pages liees
				List<String> outBoundsLinksNames = new ArrayList<>();
				for(int j=0; j<currentpage.getOutBoundLinks().size();j++){
					outBoundsLinksNames.add(currentpage.getOutBoundLinks().get(j).getName());
				}
				doc.append("outBoundLinks",outBoundsLinksNames);
				//Insertion du document
				collection.insertOne(doc);
			}
		}

		//MAPREDUCE - MongoDB
		//Formule:
		//PR(A) = (1-d) + d (PR(T1)/C(T1) + ... + PR(Tn)/C(Tn))
		//		PR(A) is the PageRank of page A,
		//		PR(Ti) is the PageRank of pages Ti which link to page A,
		//		C(Ti) is the number of outbound links on page Ti and
		//		d is a damping factor which can be set between 0 and 1.

		String map ="function() {"
				//Calculs des PR(Tn)/C(Tn)
				+ "var nb_outLink = this.PR / this.nb_outBoundLinks;"
				+ "for(var outBoundlink in this.outBoundLinks) {"
				+ "emit(this.outBoundLinks[outBoundlink], nb_outLink);"
				+ "}"
				+ "emit(this.name, this.outBoundLinks);"
				//Necessaire pour recuperer la page D
				+ "emit(this.name, 0);"
				+ "}";

		String reduce ="function(name,value) {"
				+ "var sum = 0;"
				+ "for (var val in value) {"
				+ "if(!isNaN(value[val])) {"
				//Sommes des PR(Tn)/C(Tn)
				+ "sum += value[val];"
				+ "}"
				+ "}"
				//PR(A) = (1-d) + d * sum
				+ "newpagerank = (1 -"+ DAMPING_FACTOR +") + "+ DAMPING_FACTOR +" * sum;"
				+ "emit([name, newpagerank], 1);"
				+ "}"; 

		for(int i =0; i<NB_ITERATIONS;i++){

			MapReduceIterable<Document> mapReduceResult = collection.mapReduce(map, reduce);
			System.out.println("------------ Resultats mapReduce: ITERATION "+i+" ------------");

			//Affichage des resultats
			Iterator<Document> iterator = mapReduceResult.iterator();
			while(iterator.hasNext())
			{
				Document documentResult = (Document)iterator.next();
				try{
					List<String> resultTolist = (List<String>) documentResult.get("_id"); 
					String name = resultTolist.get(0);
					double newpagerank = Double.parseDouble(String.valueOf(resultTolist.get(1)));

					// MAJ de la collection
					collection.updateOne(Filters.eq("name", name), 
							new Document("$set", new Document("PR", newpagerank)));
					//Affichage des résultats
					System.out.println(documentResult.toString());
				}catch(ClassCastException e){
					//Si le resultat ne peut pas etre caster en liste de String
				}
			}
		}
		//Close DB
		mongoClient.close();
	}
}
