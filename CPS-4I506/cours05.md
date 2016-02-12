# CPS
## Cours 5
### Model-based testing (MBT)
* Test basé sur les spécifications
* cf. le poly des spécifications de la cuve
#### Références
* *Practical Model-Based Testing*, Hutting et Legeard (2004)
* *SWEBOK 3 IEEE* (SoftWare Engineering Body Of Knowledge)

Le test logiciel consiste en la **vérification dynamique** du comportement attendu d'un programme sur un **nombre fini** de cas de test sélectionnés ("intelligement").


```

            Spécification \             /  Test fonctionnel (boîte noire)
                           \           /
Conception (par contrats)   \         /  Test des contrats
                             \       /        
            Implémentation    \_____/  Test unitaire (boîte blanche)
```

#### Terminologie
* SUT: System Under Test
* Défaillance (failure): comportement inattendu ou non-désiré du SUT
* Erreur (fault): cause de la défaillance
* Tester: essayer de produire des défaillances pour détecter des erreurs
* Débugger: correction d'une erreur à partir d'un rapport de défaillance

#### Test MBT
* Test fonctionnel élaboré à partir des spécifications
* Test boîte noire - le testeur ne regarde pas le code
* Test hors-ligne (classique) - par opposition à du test en ligne (cf. implémentation des contrats)

##### Le plus important (dans le test en général)
* Notion de **couverture de tests** => seul critère de **qualité logicielle** (en développement classique)
* En MBT: le critère de couverture provient des spécifications.

### Méthode MBT pour CPS
1. Critère de couverture
    * Couverture des préconditions
    * Couverture des transitions (ou séquences de 2 transitions)
    * Couverture des états "intéressants"
    * Couverture liée aux données
    * Couverture des scénarios utilisateur (use case)
    * etc...
2. Plan de test: ensemble des **objectifs de test** répondant à un critère de couverture
    * **métrique:** %couverture (def)= objectifs atteints / (objectifs atteints + objectifs non atteints + objectifs inatteignables)
3. Objectifs de test - description en français (ex. franchir la transition "pump") => couverture des transitions
    * déterminer au moins un *cas de test*
    * tout objectif non couvert par un cas de test est *non-atteint* ou *inatteignable*
4. Cas de test - *exemple:* transition "pump"
    * **Cas de test:** Cuve::pumpTrans
    * (objectif: transition "pump": couverture trainsitions)
        * *Conditions initiales:* C0 (def)= fill(init(10), 8)
        * *Opérations:* C1 (def)= pump(C0, 4)
        * *Oracle:*
            * Postconditions:
                1. capacity(C1) = capacity(C0) (capacity est constant)
                2. quantity(C1) = quantity(C0) - 4
            * Invariants:
                3. empty(C1) = (quantity(C1) = 0)
                4. 0 <= quantity(C1) <= capacity(C1)
    * **Rapport de tests:**
        * Échec de 1. => "la capacité a été modifiée"
        * Échec de 2. => "la quantité est incorrecte"
        * etc...

#### Implémentation
Par exemple en Junit:
```java
// Cas de test
public void testPumpTrans () {
    Cuve c = new Cuve();    //
    c.init(10);             // Conditions initiales
    c.fill(8);              //
    // Capture
    double qty_before = c.getQuantity();
    // Opération
    c.pump(4);
    // Oracle
    if (!(c.getQuantity() == qty_before - 4)) {
        // Rapport
        throw new PostConditionError("la quantité est incorrecte");
    }
}
```
#### Critères de couverture
* Couverture des transitions
    * Un objectif de test par transition (transition: constructeur - init - ou opérateur - fill, pump)
        * Variante: un objectif par séquence de 2 transitions (constructeurs exclus): pump;pump, fill;fill, pump;fill, fill;pump
    * Opération unique
    * Oracle: test des postconditions et invariants (observations)
* Couverture des préconditions
    * Pour chaque précondition d'une spécification,
    * un objectif de test -> la précondition "passe"
        * Oracle: test des postconditions et invariants (observations)
    * un objectif de test -> la précondition ne passe pas
        * Oracle: attente d'une exception/d'un code d'erreur etc..
* Couverture des états "intéressants"
    * Un objectif par état "intéressant" (point de vue "métier")
        * Oracle: description de l'état qu'on veut atteindre
    * Exemple: cuve pleine
        * Oracle: quantity(C) = capacity(C)
        * Opérations: suite d'opérations pour atteindre cet état
* Couverture des scénarios
    * Un objectif par scénario utilisateur (use case)
        * Opérations: les transitions du scénario
        * Oracle: en fonction du scénario
    * Exemple: transvasement de cuves
        * Oracle: la bonne quantité de liquide a été transvasée.
