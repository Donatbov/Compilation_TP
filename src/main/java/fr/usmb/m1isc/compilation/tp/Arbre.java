package fr.usmb.m1isc.compilation.tp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

enum Type{
	Entier,
	Id,
	Operateur
}


public class Arbre {
	private String valeur;
	private Arbre gauche;
	private Arbre droite;
	private Type type;

	public Arbre(String valeur, Arbre gauche, Arbre droite, Type type) {
		this.valeur = valeur;
		this.gauche = gauche;
		this.droite = droite;
		this.type = type;
	}

	public Arbre(String valeur, Arbre gauche, Arbre droite) {
		this.valeur = valeur;
		this.gauche = gauche;
		this.droite = droite;
		this.type = Type.Operateur;
	}

	public void ParcoursPrefixe() {
		System.out.println(this.valeur);
		if (this.gauche != null)
			this.gauche.ParcoursPrefixe();
		if (this.droite != null)
			this.droite.ParcoursPrefixe();
	}

	public void test() {
		System.out.println(this.valeur);
		System.out.println(this.gauche.valeur);
		System.out.println(this.droite.valeur);

	}

	public Set<String> chercheLetsPrefixement(Set<String> listeIdentificateurs) {
		if(this.valeur.equals("let")){
			listeIdentificateurs.add(this.gauche.valeur);
		}
		if (this.gauche != null)
			listeIdentificateurs = this.gauche.chercheLetsPrefixement(listeIdentificateurs);
		if (this.droite != null)
			listeIdentificateurs = this.droite.chercheLetsPrefixement(listeIdentificateurs);

		return listeIdentificateurs;
	}

	public String getvalue() {
		return this.valeur;
	}
}
