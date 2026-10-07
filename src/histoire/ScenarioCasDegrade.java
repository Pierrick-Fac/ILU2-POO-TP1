package histoire;

import villagegaulois.Etal;
import personnages.Gaulois;

public class ScenarioCasDegrade {
	public static void main(String[] args) {
		Etal etal = new Etal();
		Gaulois bonemine = new Gaulois("Bonemine", 7);
		etal.occuperEtal(bonemine, "fleurs", 10);
		etal.acheterProduit(0,bonemine);
		System.out.println("Fin du test");
		}
}
