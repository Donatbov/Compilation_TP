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

	public void genereCode() {


		switch (this.valeur){
			case "let":
				if (this.droite != null)
					this.droite.genereCode();
				//System.out.println("pop eax");
				//System.out.println("mov"this.gauche.valeur + ", eax");
				break;

			case ";":
				if (this.gauche != null)
					this.gauche.genereCode();
				if (this.droite != null)
					this.droite.genereCode();
				System.out.println("pop eax");
				System.out.println("pop eax");
				break;

			case "/":
				if (this.gauche != null)
					this.gauche.genereCode();
				if (this.droite != null)
					this.droite.genereCode();
				System.out.println("pop eax");
				System.out.println("pop ebx");
				System.out.println("div eax, ebx");
				System.out.println("push eax");
				break;

			case "*":
				if (this.gauche != null)
					this.gauche.genereCode();
				if (this.droite != null)
					this.droite.genereCode();
				System.out.println("pop eax");
				System.out.println("pop ebx");
				System.out.println("mul eax, ebx");
				System.out.println("push eax");
				break;

			case "+":
				if (this.gauche != null)
					this.gauche.genereCode();
				if (this.droite != null)
					this.droite.genereCode();
				System.out.println("pop eax");
				System.out.println("pop ebx");
				System.out.println("add eax, ebx");
				System.out.println("push eax");
				break;

			case "-":
				if (this.gauche != null)
					this.gauche.genereCode();
				if (this.droite != null)
					this.droite.genereCode();
				System.out.println("pop eax");
				System.out.println("pop ebx");
				System.out.println("add eax, ebx");
				System.out.println("push eax");
				break;


			default:	// Si on est pas sur un identificateur (et donc une valeur)
				System.out.println("mov eax, " + this.valeur );	// on la met dans la pile
				System.out.println("push eax");	// on la met dans la pile
				break;
		}



	}

	public String getvalue() {
		return this.valeur;
	}
}
