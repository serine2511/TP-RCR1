import org.tweetyproject.logics.fol.syntax.*;
import org.tweetyproject.logics.commons.syntax.*;

public class ConnaissancesAnimaux {
    public static void main(String[] args) throws Exception {

        // 1. Définir les prédicats
        Predicate aDesAiles      = new Predicate("ADesAiles",      1);
        Predicate aDesPlumes     = new Predicate("ADesPlumes",     1);
        Predicate pondDesOeufs   = new Predicate("PondDesOeufs",   1);
        Predicate estOiseau      = new Predicate("EstOiseau",      1);
        Predicate peutVoler      = new Predicate("PeutVoler",      1);
        Predicate estOiseauLourd = new Predicate("EstOiseauLourd", 1);

        // 2. Définir les constantes (animaux)
        Constant aigle    = new Constant("Aigle");
        Constant pingouin = new Constant("Pingouin");
        Constant chat     = new Constant("Chat");
        Constant perroquet = new Constant("Perroquet");

        // 3. Construire la base de connaissances
        FolBeliefSet bc = new FolBeliefSet();

        // Faits sur l'Aigle
        bc.add(new FolAtom(aDesAiles,    aigle));
        bc.add(new FolAtom(aDesPlumes,   aigle));
        bc.add(new FolAtom(pondDesOeufs, aigle));

        // Faits sur le Pingouin
        bc.add(new FolAtom(aDesAiles,      pingouin));
        bc.add(new FolAtom(aDesPlumes,     pingouin));
        bc.add(new FolAtom(pondDesOeufs,   pingouin));
        bc.add(new FolAtom(estOiseauLourd, pingouin)); // le pingouin ne vole pas

        // Faits sur le Chat
        bc.add(new FolAtom(pondDesOeufs, chat));       // le chat n'a pas d'ailes

        // Faits sur le Perroquet
        bc.add(new FolAtom(aDesAiles,    perroquet));
        bc.add(new FolAtom(aDesPlumes,   perroquet));
        bc.add(new FolAtom(pondDesOeufs, perroquet));

        // 4. Appliquer les règles et déduire de nouveaux faits
        // Règle 1 : ADesAiles ET ADesPlumes → EstOiseau
        // Règle 2 : EstOiseau ET NON EstOiseauLourd → PeutVoler
        Constant[] animaux = {aigle, pingouin, chat, perroquet};
        String[]   noms    = {"Aigle", "Pingouin", "Chat", "Perroquet"};

        for (int i = 0; i < animaux.length; i++) {
            Constant a = animaux[i];

            boolean ailes   = bc.contains(new FolAtom(aDesAiles,      a));
            boolean plumes  = bc.contains(new FolAtom(aDesPlumes,     a));
            boolean lourd   = bc.contains(new FolAtom(estOiseauLourd, a));

            // Règle 1
            boolean oiseau = ailes && plumes;
            if (oiseau) bc.add(new FolAtom(estOiseau, a));

            // Règle 2
            boolean vole = oiseau && !lourd;
            if (vole) bc.add(new FolAtom(peutVoler, a));
        }

        // 5. Afficher la base de connaissances
        System.out.println("=== Base de Connaissances (après raisonnement) ===");
        for (var formule : bc) {
            System.out.println("  " + formule);
        }

        // 6. Répondre aux requêtes
        System.out.println("\n=== Résultats des Requêtes ===");
        for (int i = 0; i < animaux.length; i++) {
            Constant a = animaux[i];
            boolean oiseau = bc.contains(new FolAtom(estOiseau, a));
            boolean vole   = bc.contains(new FolAtom(peutVoler, a));

            System.out.println(noms[i] + " :");
            System.out.println("  EstOiseau : " + (oiseau ? "OUI ✓" : "NON ✗"));
            System.out.println("  PeutVoler : " + (vole   ? "OUI ✓" : "NON ✗"));
        }
    }
}