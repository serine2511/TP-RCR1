import org.tweetyproject.logics.fol.syntax.*;
import org.tweetyproject.logics.commons.syntax.*;

public class AnimalKnowledge {
    public static void main(String[] args) throws Exception {

        // 1. Define predicates
        Predicate hasWings    = new Predicate("HasWings",    1);
        Predicate hasFeathers = new Predicate("HasFeathers", 1);
        Predicate laysEggs    = new Predicate("LaysEggs",    1);
        Predicate isBird      = new Predicate("IsBird",      1);
        Predicate canFly      = new Predicate("CanFly",      1);
        Predicate isHeavyBird = new Predicate("IsHeavyBird", 1);

        // 2. Define constants (animals)
        Constant eagle   = new Constant("Eagle");
        Constant penguin = new Constant("Penguin");
        Constant cat     = new Constant("Cat");
        Constant parrot  = new Constant("Parrot");

        // 3. Build knowledge base
        FolBeliefSet kb = new FolBeliefSet();

        // Facts about Eagle
        kb.add(new FolAtom(hasWings,    eagle));
        kb.add(new FolAtom(hasFeathers, eagle));
        kb.add(new FolAtom(laysEggs,    eagle));

        // Facts about Penguin
        kb.add(new FolAtom(hasWings,    penguin));
        kb.add(new FolAtom(hasFeathers, penguin));
        kb.add(new FolAtom(laysEggs,    penguin));
        kb.add(new FolAtom(isHeavyBird, penguin)); // penguins can't fly

        // Facts about Cat
        kb.add(new FolAtom(laysEggs, cat));        // cats don't have wings

        // Facts about Parrot
        kb.add(new FolAtom(hasWings,    parrot));
        kb.add(new FolAtom(hasFeathers, parrot));
        kb.add(new FolAtom(laysEggs,    parrot));

        // 4. Apply rules and derive new facts
        // Rule 1: HasWings AND HasFeathers → IsBird
        // Rule 2: IsBird AND NOT IsHeavyBird → CanFly
        Constant[] animals = {eagle, penguin, cat, parrot};
        String[]   names   = {"Eagle", "Penguin", "Cat", "Parrot"};

        for (int i = 0; i < animals.length; i++) {
            Constant a = animals[i];

            boolean wings     = kb.contains(new FolAtom(hasWings,    a));
            boolean feathers  = kb.contains(new FolAtom(hasFeathers, a));
            boolean heavy     = kb.contains(new FolAtom(isHeavyBird, a));

            // Rule 1
            boolean bird = wings && feathers;
            if (bird) kb.add(new FolAtom(isBird, a));

            // Rule 2
            boolean fly = bird && !heavy;
            if (fly) kb.add(new FolAtom(canFly, a));
        }

        // 5. Print knowledge base
        System.out.println("=== Knowledge Base (after reasoning) ===");
        for (var formula : kb) {
            System.out.println("  " + formula);
        }

        // 6. Answer queries
        System.out.println("\n=== Query Results ===");
        for (int i = 0; i < animals.length; i++) {
            Constant a = animals[i];
            boolean bird = kb.contains(new FolAtom(isBird, a));
            boolean fly  = kb.contains(new FolAtom(canFly, a));

            System.out.println(names[i] + ":");
            System.out.println("  IsBird : " + (bird ? "YES ✓" : "NO  ✗"));
            System.out.println("  CanFly : " + (fly  ? "YES ✓" : "NO  ✗"));
        }
    }
}