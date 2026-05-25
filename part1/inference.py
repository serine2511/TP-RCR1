from pysat.solvers import Glucose3



NOMS_VARIABLES = {
    1: "a_tentacules",
    2: "a_manteau",
    3: "est_cephalopode",
    4: "vit_dans_eau",
    5: "a_encre",
    6: "huit_tentacules",
    7: "est_pieuvre"
}

# R1: a_tentacules AND a_manteau → est_cephalopode
# R2: est_cephalopode → vit_dans_eau
# R3: est_cephalopode → a_encre
# R4: huit_tentacules → est_pieuvre
# Faits: animal a tentacules, manteau, 8 tentacules

BASE = [
    [-1, -2, 3],   
    [-3, 4],       
    [-3, 5],       
    [-6, 7],       
    [1],          
    [2],           
    [6],           
]

def infere(base, litteral):
    # Etape 1: Insérer ¬φ dans la base
    negation = [-litteral]
    base_test = base + [negation]
    
    # Etape 2: Appeler le solveur SAT
    with Glucose3() as solveur:
        for clause in base_test:
            solveur.add_clause(clause)
        satisfaisable = solveur.solve()
    
    # Etape 3: Si INSATISFAISABLE → BC infère φ
    return not satisfaisable


print("=" * 50)
print("  Etape 5 - Raisonnement par l'absurde")
print("  Domaine : Cephalopodes")
print("=" * 50)

print("\nBase de connaissances:")
print("  R1: a_tentacules ∧ a_manteau → est_cephalopode")
print("  R2: est_cephalopode → vit_dans_eau")
print("  R3: est_cephalopode → a_encre")
print("  R4: huit_tentacules → est_pieuvre")
print("  Faits: a_tentacules, a_manteau, huit_tentacules")

print("\nRequêtes:")
print("-" * 50)

requetes = [3, 4, 5, 7]

for q in requetes:
    resultat = infere(BASE, q)
    nom = NOMS_VARIABLES[q]
    if resultat:
        print(f"  BC ⊨ {nom} ? → OUI ✓ (prouvé par l'absurde)")
    else:
        print(f"  BC ⊨ {nom} ? → NON ✗")

print("\nExplication:")
print("  BC ∪ {¬est_pieuvre} est INSATISFAISABLE")
print("  donc BC infère bien est_pieuvre ✓")