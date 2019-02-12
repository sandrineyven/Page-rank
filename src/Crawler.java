import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler {	

	public List<Spell> recupSpell(){

		List<Spell> spellList = new ArrayList<>();

		Document codeSource = null;

		//Parcours de toutes les pages
		for(int page = 1; page <= 1600; page++){

			try {
				codeSource = Jsoup.connect("http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=" + page).get();
			} catch (IOException e1) {
				System.out.println("La connection a echouee");
				e1.printStackTrace();
			}

			Spell spell = new Spell();

			Elements divSpell = codeSource.select("div.SpellDiv");

			//Recuperation du nom du spell
			Elements spell1 = divSpell.select("div.heading");
			spell.setName( spell1.select("p").first().text());

			//Recuperation du level
			String[] listeLevel = divSpell.select("p.SPDet").first().getAllElements()
					.text().split(" ");

			for(int i =0; i< listeLevel.length; i++){
				//Si cest un sorcier, on recupere le level correspondant
				if(listeLevel[i].equals("sorcerer/wizard")){
					spell.setLevel(listeLevel[i+1].replaceAll(",", ""));
					spell.setClasse("wizard");
				}
			}
			//Sinon on prend le premier level
			if(spell.getLevel() == null){
				for(int i =0; i< listeLevel.length; i++){
					if(listeLevel[i].equals("Level")){
						spell.setLevel(listeLevel[i+2].replaceAll(",", ""));
						spell.setClasse(listeLevel[i+1].replaceAll(",", ""));
						break;
					}
				}
			}


			//Composants
			String compoToText = divSpell.select("p.SPDet").get(2).text().replaceAll(",","");
			//On enleve la suite de la chaine si elle contient "("
			if(compoToText.contains("(")){
				int index = compoToText.indexOf("(");
				compoToText = compoToText.substring(0,index-1);
			}		

			String[] compolist = compoToText.split(" ");

			//Recuperation de chaque champs des components
			List<String> recupchamps = new ArrayList<>(3);
			for(int i = 1; i<compolist.length;i++ ){
				recupchamps.add(compolist[i]);
			}
			spell.setComponents(recupchamps);

			//Recuperation de la Spell resistance
			if(divSpell.select("p.SPDet").size()>6){
				String resi = divSpell.select("p.SPDet").get(6).text();
				if(resi.substring(resi.length()-1).equals("s")){
					spell.setResistance(true);
				}else{
					spell.setResistance(false);
				}
			}

			//Ajout du spell recuperer a la liste
			spellList.add(spell);

			//Affichage des résultats
//			System.out.println(spell.getName()
//					+ " " + spell.getClasse()
//					+ " " + spell.getLevel()
//					+ " " + spell.getComponents()
//					+ " " + spell.isResistance()
//					+ " " + page
//					+ " " + spellList.get(page-1).getName());
		}

		return spellList;
	}
}
