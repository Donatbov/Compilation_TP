package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
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
    private int nbWhile,nbCondGT,nbCondLT;

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

	public void genereCode(FileWriter fw) throws IOException {
		switch (this.valeur){
			case "let":
				if (this.droite != null) {
                    this.droite.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tmov " + this.gauche.valeur + ", eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpush eax"));
                    fw.write(System.lineSeparator());
                }
				break;

			case ";":
				if (this.gauche != null) {
                    this.gauche.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                }
                if (this.droite != null) {
                    this.droite.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                }
                break;

			case "/":
				if (this.gauche != null && this.droite != null) {
                    this.gauche.genereCode(fw);
                    this.droite.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpop ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tdiv ebx, eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tmov eax, ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpush eax"));
                    fw.write(System.lineSeparator());
                }
				break;

			case "*":
                if (this.gauche != null && this.droite != null) {
                    this.gauche.genereCode(fw);
                    this.droite.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpop ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tmul eax, ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpush eax"));
                    fw.write(System.lineSeparator());
                }
				break;

			case "+":
                if (this.gauche != null && this.droite != null) {
                    this.gauche.genereCode(fw);
                    this.droite.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpop ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tadd eax, ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpush eax"));
                    fw.write(System.lineSeparator());
                }
				break;

			case "-":
                if (this.gauche != null && this.droite != null) {
                    this.gauche.genereCode(fw);
                    this.droite.genereCode(fw);
                    fw.write(String.format("\tpop eax"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpop ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tsub eax, ebx"));
                    fw.write(System.lineSeparator());
                    fw.write(String.format("\tpush eax"));
                    fw.write(System.lineSeparator());
                }
				break;

			case "input":
				fw.write("\tin eax");
                fw.write(System.lineSeparator());
				fw.write("\tpush eax");
				break;

			case "while":
				this.nbWhile++;
				fw.write("debut_while" + this.nbWhile);
                fw.write(System.lineSeparator());
				if (this.gauche != null)
					this.gauche.genereCode(fw);
				if (this.droite != null)
					this.droite.genereCode(fw);
				fw.write("\tjmp debut_while_" + nbWhile);
                fw.write(System.lineSeparator());
				fw.write("sortie_while_" + this.nbWhile);
                fw.write(System.lineSeparator());
				break;

			case "<":
				this.nbCondLT++;
				fw.write("\tmov eax, " + this.gauche.valeur);
                fw.write(System.lineSeparator());
				fw.write("\tpush eax");
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, " + this.droite.valeur);
                fw.write(System.lineSeparator());
				fw.write("\tpop ebx");
                fw.write(System.lineSeparator());
				fw.write("\tsub eax, ebx");
                fw.write(System.lineSeparator());
				fw.write("\tjle faux_lt_" + this.nbCondLT);
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, 1");
                fw.write(System.lineSeparator());
				fw.write("\tjmp sortie_lt_" + this.nbCondLT);
                fw.write(System.lineSeparator());
				fw.write("faux_lt_" + this.nbCondLT);
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, 0");
                fw.write(System.lineSeparator());
				fw.write("sortie_lt_" + this.nbCondLT);
                fw.write(System.lineSeparator());
				fw.write("\tjz sortie_while_" + this.nbWhile);
                fw.write(System.lineSeparator());
				if (this.gauche != null)
					this.gauche.genereCode(fw);
				if (this.droite != null)
					this.droite.genereCode(fw);
				break;

			case ">":
				this.nbCondGT++;
				fw.write("\tmov eax, " + this.gauche.valeur);
                fw.write(System.lineSeparator());
				fw.write("\tpush eax");
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, " + this.droite.valeur);
                fw.write(System.lineSeparator());
				fw.write("\tpop ebx");
                fw.write(System.lineSeparator());
				fw.write("\tsub ebx, eax");
                fw.write(System.lineSeparator());
				fw.write("\tjle faux_gt_" + this.nbCondGT);
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, 1");
                fw.write(System.lineSeparator());
				fw.write("\tjmp sortie_gt_" + this.nbCondGT);
                fw.write(System.lineSeparator());
				fw.write("faux_gt_" + this.nbCondGT);
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, 0");
                fw.write(System.lineSeparator());
				fw.write("sortie_gt_" + this.nbCondGT);
                fw.write(System.lineSeparator());
				fw.write("\tjz sortie_while_" + this.nbWhile);
                fw.write(System.lineSeparator());
				if (this.gauche != null)
					this.gauche.genereCode(fw);
				if (this.droite != null)
					this.droite.genereCode(fw);
				break;

			case "%":
				fw.write("\tmov eax, " + this.droite.valeur);
                fw.write(System.lineSeparator());
				fw.write("\tpush eax");
                fw.write(System.lineSeparator());
				fw.write("\tmov eax, " + this.gauche.valeur);
                fw.write(System.lineSeparator());
				fw.write("\tpop ebx");
                fw.write(System.lineSeparator());
				fw.write("\tmov ecx, eax");
                fw.write(System.lineSeparator());
				fw.write("\tdiv ecx, ebx");
                fw.write(System.lineSeparator());
				fw.write("\tmul ecx, ebx");
                fw.write(System.lineSeparator());
				fw.write("\tsub eax, ecx");
                fw.write(System.lineSeparator());
				fw.write("\tpush eax");
                fw.write(System.lineSeparator());
				break;


			default:	// Si on est pas sur un identificateur (et donc une valeur)
				fw.write(String.format("\tmov eax, " + this.valeur)); // on la met dans la pile
				fw.write(System.lineSeparator());
				fw.write(String.format("\tpush eax")); // on la met dans la pile
				fw.write(System.lineSeparator());
				break;
		}
	}

	public String getvalue() {
		return this.valeur;
	}
}
