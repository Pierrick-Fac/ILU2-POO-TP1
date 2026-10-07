package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private Marche marche;
	private int nbVillageois = 0;

	public Village(String nom, int nbVillageoisMaximum, int nbEtal) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtal);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException{
		StringBuilder chaine = new StringBuilder();
		if (chef == null) {
			throw new VillageSansChefException();
		}
		
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder affichage = new StringBuilder();
		int indiceEtal = marche.trouverEtalLibre();
		affichage.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		if (indiceEtal == -1) {
			affichage.append("Le vendeur "+ vendeur.getNom()+ " n'a pas trouver d'endroit pour vendre ses" + nbProduit + " " + produit + ".\n");
		}
		else {
			marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
			affichage.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + (indiceEtal+1) + ".\n");
		}
		return affichage.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		Etal[] etals = marche.trouverEtals(produit);
		StringBuilder affichage = new StringBuilder();
		
		if (etals.length == 0) {
			affichage.append("Il n'y a pas de vendeur qui propose des fleurs au marché.\n");
		}
		else if (etals.length == 1) {
			affichage.append("Seul le vendeur "+ etals[0].getVendeur().getNom() +" propose des " + produit + " au marché.\n");
		}
		else {
			affichage.append("Les vendeurs qui proposent des "+ produit +" sont : \n");
			for (int i = 0; i<etals.length; i++) {
				affichage.append("- "+ etals[i].getVendeur().getNom() + "\n");
			}
		}
		return affichage.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		return rechercherEtal(vendeur).libererEtal();
	}
	
	public String afficherMarche() {
		StringBuilder affichage = new StringBuilder();
		affichage.append("Le marché du village \""+ nom + "\" possède plusieurs étals :\n");
		affichage.append(marche.afficherMarche());
		return affichage.toString();
	}
	
	
	private static class Marche {
		private Etal[] etals;
		private int nbEtal;
		
		private Marche(int nbEtal) {
			this.nbEtal = nbEtal;
			etals = new Etal[nbEtal];
			for (int i = 0; i < nbEtal; i++) {
				etals[i] = new Etal();
			}
		}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			int etalLibre = -1;
			for (int i=nbEtal;i > 0;i--) {
				if (!etals[i-1].isEtalOccupe()) {
					etalLibre = i-1;
				}
			}
			return etalLibre;
		}
		
		private Etal[] trouverEtals(String produit) {
			int nbEtalProduit = 0;
			for (int i = 0; i < nbEtal; i++) {
				if (etals[i].contientProduit(produit)) {
					nbEtalProduit++;
				}
			}
			Etal[] etalProduit = new Etal[nbEtalProduit];
			for(int i = 0, j = 0; i<nbEtal;i++) {
				if (etals[i].contientProduit(produit)) {
					etalProduit[j] = etals[i];
					j++;
				}
			}
			return etalProduit;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			Etal etal = null;
			for (int i = 0; i < nbEtal; i++) {
				if (etals[i].getVendeur()==gaulois) {
					etal = etals[i];
				}
			}
			return etal;
		}
		
		private String afficherMarche() {
			int nbEtalVide = 0;
			StringBuilder affichage = new StringBuilder();
			for (int i = 0; i < nbEtal; i++) {
				if (!etals[i].isEtalOccupe()) {
					nbEtalVide++;
				}
				else {
					affichage.append(etals[i].afficherEtal());
				}
			}
			if (nbEtalVide > 0) {
				affichage.append("Il reste " + nbEtalVide + " étals non utilisées dans le marché.\n");
			}
			return affichage.toString();
		}
	}
}