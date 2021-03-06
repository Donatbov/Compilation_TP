package fr.usmb.m1isc.compilation.tp;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;


public class Arbre {
	private String valeur;
	private Arbre gauche;
	private Arbre droite;
    private static int nbWhile = 0, nbCondGT = 0, nbCondLT = 0, nbIf = 0, nbNot = 0;

	public Arbre(String valeur, Arbre gauche, Arbre droite) {
		this.valeur = valeur;
		this.gauche = gauche;
		this.droite = droite;
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
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov " + this.gauche.valeur + ", eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpush eax");
                    fw.write(System.lineSeparator());
                }
				break;

			case ";":
				if (this.gauche != null) {
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                }
                if (this.droite != null) {
                    this.droite.genereCode(fw);
                }
                break;

			case "/":
				if (this.gauche != null && this.droite != null) {
                    this.gauche.genereCode(fw);
                    this.droite.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tdiv ebx, eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tpush eax");
                    fw.write(System.lineSeparator());
                }
				break;

			case "*":
                if (this.gauche != null && this.droite != null) {
                    this.gauche.genereCode(fw);
                    this.droite.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tmul eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tpush eax");
                    fw.write(System.lineSeparator());
                }
				break;

			case "+":
            if (this.gauche != null && this.droite != null) {
                this.gauche.genereCode(fw);
                this.droite.genereCode(fw);
                fw.write("\tpop eax");
                fw.write(System.lineSeparator());
                fw.write("\tpop ebx");
                fw.write(System.lineSeparator());
                fw.write("\tadd eax, ebx");
                fw.write(System.lineSeparator());
                fw.write("\tpush eax");
                fw.write(System.lineSeparator());
            }
            break;

            case "-":
            if (this.gauche != null && this.droite != null) {
                this.droite.genereCode(fw);
                this.gauche.genereCode(fw);
                fw.write("\tpop eax");
                fw.write(System.lineSeparator());
                fw.write("\tpop ebx");
                fw.write(System.lineSeparator());
                fw.write("\tsub eax, ebx");
                fw.write(System.lineSeparator());
                fw.write("\tpush eax");
                fw.write(System.lineSeparator());
            }
            break;

            case "%":
                if (this.gauche != null && this.droite != null) {
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov ecx, eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tdiv ecx, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tmul ebx, ecx");
                    fw.write(System.lineSeparator());
                    fw.write("\tsub eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tpush eax");
                    fw.write(System.lineSeparator());
                }
                break;

            case "input":
                fw.write("\tin eax");
                fw.write(System.lineSeparator());
                fw.write("\tpush eax");
                fw.write(System.lineSeparator());
            break;

            case "output":
                fw.write("\tmov eax, " + this.gauche.valeur);
                fw.write(System.lineSeparator());
                fw.write("\tout eax");
                fw.write(System.lineSeparator());
                break;

            case "while":
                if (this.gauche != null && this.droite != null) {
                    nbWhile++;
                    fw.write("debut_while" + nbWhile + ":");
                    fw.write(System.lineSeparator());
                    this.gauche.genereCode(fw); // génération de la condition
                    fw.write("\tjz sortie_while" + nbWhile);    // si la condition est fausse, on sort du while
                    fw.write(System.lineSeparator());
                    this.droite.genereCode(fw);     // sinon, on génere le code associé au while
                    fw.write("\tjmp debut_while" + nbWhile);    // on réévalue la condition
                    fw.write(System.lineSeparator());
                    fw.write("sortie_while" + nbWhile + ":");
                    fw.write(System.lineSeparator());

                }
                break;

            case "if":
                if (this.gauche != null && this.droite != null) {
                    nbIf++;
                    this.gauche.genereCode(fw); // génération de la condition
                    fw.write("\tjnz debut_then" + nbIf);    // si la condition est vraie, on va dans le then statement
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp debut_else" + nbIf);    // sinon, on va dans le else statement
                    fw.write(System.lineSeparator());

                    fw.write("debut_then" + nbIf + ":");
                    fw.write(System.lineSeparator());
                    this.droite.gauche.genereCode(fw);     // on génere le code associé au then
                    fw.write("\tjmp fin_if" + nbIf);
                    fw.write(System.lineSeparator());

                    fw.write("debut_else" + nbIf + ":");
                    fw.write(System.lineSeparator());
                    this.droite.droite.genereCode(fw);     // on génere le code associé au else
                    fw.write("\tjmp fin_if" + nbIf);
                    fw.write(System.lineSeparator());

                    fw.write("fin_if" + nbIf + ":");
                    fw.write(System.lineSeparator());
                }
            break;

            case "not":
                if (this.gauche != null){
                    nbNot++;
                    this.gauche.genereCode(fw);
                    fw.write("\tjz faux_" + nbNot);
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 0");
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp sortie_not_" + nbNot);
                    fw.write(System.lineSeparator());
                    fw.write("faux_" + nbNot + ":");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 1");
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp sortie_not_" + nbNot);
                    fw.write(System.lineSeparator());
                    fw.write("sortie_not_" + nbNot + ":");
                    fw.write(System.lineSeparator());
                }
                break;

            case "and":
                if (this.gauche != null && this.droite != null) {
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tmul eax, ebx");
                    fw.write(System.lineSeparator());
                }
                break;

            case "or":
                if (this.gauche != null && this.droite != null) {
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tadd eax, ebx");
                    fw.write(System.lineSeparator());
                }
                break;

            case "<":
                if (this.gauche != null && this.droite != null) {
                    nbCondLT++;
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tsub eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tjl vrai_lt_" + nbCondLT);
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 0");
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp sortie_lt_" + nbCondLT);
                    fw.write(System.lineSeparator());
                    fw.write("vrai_lt_" + nbCondLT + ":");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 1");
                    fw.write(System.lineSeparator());
                    fw.write("sortie_lt_" + nbCondLT + ":");
                    fw.write(System.lineSeparator());
                }
            break;

            case "<=":
                if (this.gauche != null && this.droite != null) {
                    nbCondLT++;
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tsub eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tjle vrai_lt_" + nbCondLT);
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 0");
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp sortie_lt_" + nbCondLT);
                    fw.write(System.lineSeparator());
                    fw.write("vrai_lt_" + nbCondLT + ":");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 1");
                    fw.write(System.lineSeparator());
                    fw.write("sortie_lt_" + nbCondLT + ":");
                    fw.write(System.lineSeparator());
                }
                break;

            case ">":
                if (this.gauche != null && this.droite != null) {
                    nbCondGT++;
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tsub eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tjg vrai_gt_" + nbCondGT);
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 0");
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp sortie_gt_" + nbCondGT);
                    fw.write(System.lineSeparator());
                    fw.write("vrai_gt_" + nbCondGT + ":");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 1");
                    fw.write(System.lineSeparator());
                    fw.write("sortie_gt_" + nbCondGT + ":");
                    fw.write(System.lineSeparator());
                }
            break;

            case ">=":
                if (this.gauche != null && this.droite != null) {
                    nbCondGT++;
                    this.droite.genereCode(fw);
                    this.gauche.genereCode(fw);
                    fw.write("\tpop eax");
                    fw.write(System.lineSeparator());
                    fw.write("\tpop ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tsub eax, ebx");
                    fw.write(System.lineSeparator());
                    fw.write("\tjge vrai_gt_" + nbCondGT);
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 0");
                    fw.write(System.lineSeparator());
                    fw.write("\tjmp sortie_gt_" + nbCondGT);
                    fw.write(System.lineSeparator());
                    fw.write("vrai_gt_" + nbCondGT + ":");
                    fw.write(System.lineSeparator());
                    fw.write("\tmov eax, 1");
                    fw.write(System.lineSeparator());
                    fw.write("sortie_gt_" + nbCondGT + ":");
                    fw.write(System.lineSeparator());
                }
                break;


			default:	// Si on est pas sur un identificateur (et donc une valeur)
				fw.write("\tmov eax, " + this.valeur);
				fw.write(System.lineSeparator());
				fw.write("\tpush eax"); // on la met dans la pile
				fw.write(System.lineSeparator());
				break;
		}
	}

	public String getvalue() {
		return this.valeur;
	}
}
