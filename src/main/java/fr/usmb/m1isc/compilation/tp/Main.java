package fr.usmb.m1isc.compilation.tp;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import java_cup.runtime.Symbol;

public class Main {

    public static void main(String[] args) throws Exception  {
        LexicalAnalyzer yy;
        if (args.length > 0) {
            yy = new LexicalAnalyzer(new FileReader(args[0]));
        }else {
            yy = new LexicalAnalyzer(new InputStreamReader(System.in));
        }
        @SuppressWarnings("deprecation")
        parser p = new parser (yy);
        Symbol s = p.parse( );
        Arbre arb = (Arbre)s.value;

        /* génération de code */
        FileWriter fw = new FileWriter(new File(args[1]));
        try{

            // 1) DATA SEGMENT
            fw.write("DATA SEGMENT");
            fw.write(System.lineSeparator()); //new line
            // on parcourt l'arbre pour detecter les let
            Set<String> listeIdentificateurs = new HashSet<String>();
            listeIdentificateurs = arb.chercheLetsPrefixement(listeIdentificateurs);
            for (String id : listeIdentificateurs){
                fw.write(String.format("\t%s DD",id));
                fw.write(System.lineSeparator()); //new line
            }
            fw.write("DATA ENDS");
            fw.write(System.lineSeparator()); //new line

            // 2) CODE SEGMENT
            fw.write("CODE SEGMENT");
            fw.write(System.lineSeparator()); //new line
            /* CODE */
            arb.genereCode(fw);
            fw.write("\tpop eax");  // pour vider la pile de la derniere valeur push
            fw.write(System.lineSeparator());

            fw.write("CODE ENDS");
            fw.write(System.lineSeparator()); //new line

            fw.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        arb.ParcoursPrefixe();
    }
}