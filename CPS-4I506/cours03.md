# CPS
## Cours 3
### Activabilité et convergence

* **Service:** Cuve
* **Obs:** [..]
* **Cons:** [..]
* **Ops:**
    * op1: [Cuve] x T1 x .. x Tn
        * pre op1
    * op2: [Cuve] x T1 x .. x Tn
        * pre op2

#### Propriétés des opérateurs
##### Activabilité
Dans tous les états, au moins un des opérateurs est applicable (au moins une des préconditions est vraie)
```
pre op1 = true v pre op2 = true v .. v pre opn = true
```
##### Convergence d'un opérateur
On ne peut pas appliquer indéfiniment l'opérateur (sans appliquer d'autres opérateurs).

##### Exemple
pump : [Cuve] x double -> [Cuve]
* **pre:** pump(C, q) require 0 < q <= quantity(C)
    * **converge** pump(C) # **variant**

variant(C) (def)= |quantity(C)|

**Variant:** expression sur un ordre bien fondé (ici, Float+) strictement décroissante par application de l'opération.

Dans l'exemple: Variant(pump(pump(C,q))) < Variant(pump(C))

### Conception par contrat

Livre: Bertrand Meyer, *Conception et programmation objet*

#### Métaphore du service commercial
##### IRL

* **Service:** fourni par un *fournisseur* à des *clients*
* **Contrat:**
    1. Les conditions devant être respectées par le client => **PRÉREQUIS** (précondition)
    2. La description du service fourni => **GARANTIES** (postconditions)

Un contrat peut être:
* **Honoré** => le client exploite les GARANTIES
* **Rompu** par le client (*PRÉREQUIS* ne sont pas assurés) => le *fournisseur* n'a pas d'obligation
* **Rompu** par le fournisseur (*GARANTIES* ne sont pas assurées) => le client peut demander des compensations

##### En informatique
* **Contrat du service:** interface avce des annotations
* **Fournisseur:** classe(s) d'implémentation de l'interface
* **Client:** code dépendant de l'interface
* **PRÉREQUIS:** tests de préconditons et invariants (pour chaque méthode du service)
* **GARANTIES:** tests de postconditions et invariants (pour chaque méthode du service)
* **Rompu:**
    * Par le client => test de précondition invalide (au choix du fournisseur: déclenche une exception, un comportement indéfini...)
    * Par le fournisseur => test de postcondition/invariant invalides (***bug***)

**Comment établir un contrat?** => à partir des spécifications

#### Spécification
**SPEC** | **CONTRAT**
     ---------|------------
Service       | Interface (du même nom)
Observateurs  | Accesseurs + préconditions
Constructeurs | Méthodes d'initialisation
Opérateurs    | Méthodes + préconditions
Observations  | Invariants du contrat
              | Postconditions des méthodes

#####Exemple
* **Service:** Switch
* **Obervateurs:**
    * on [Switch] -> bool
    * off [Switch] -> bool
    * count [Switch] -> int
* **Constructeurs:**
    * init: -> [Switch]
* **Opérateurs:**
    * press: [Switch] -> [Switch]
        * **pre** press(S) require working(S)
* **Observations:**
    * [invariants]
        * off(S) (min)= not(on(S))
        * count(S) >= 0
    * [init]
        * in(init()) = false()
        * count(init()) = 0
    * [press]
        * on(press(S)) = not(on(S))
        * count(press(S)) = count(S) + 1

**Contrat:**
```
public interface SwitchService {

    /* Observateurs */
    public boolean isOn();
    public boolean isOff();
    public int getCount();
    public boolean isWorking();

    /* Invariants */
    // INV: isOff() == !isOn()
    // INV: getCount() >= 0

    /* Constructors */
    // POST: isOn() == false
    // POST: getCount() == 0
    public void init();

    /* Operators */
    // PRE: isWorking() == true
    // POST: isOn() == !isOn()@pre
    public void press();

}
```

A partir du contrat de service (interface):
* Implémentation du service
* Implémentation du contrat (préconditions, postconditions et invariants) ~ test "unitaire" de spécification
* MBT (test fonctionnels)

**Objectif:**
* implémenter le contrat indépendament des implémentations des services
* possibilité d'activer ou non les vérifications
    * => Décorer les instances du service avec le contrat

Cf. feuille pour schéma decorator

```
public class SwitchContract extends SwitchDecorator {
    public SwitchContract(SwitchService s)
        super(s);

    public void checkInvariant() {
        // INV isOff() == !isOn()
        if (!(isOff() == !isOn()))
            throw new InvariantError("isOff()==!isOn()");
        // INV getCount() >= 0
    }

    public void init() {
        // 1. ??
        // 2. Traitement
        // 3. Invariant
        checkInvariant();
        // 4. Postconditions
        if (!(isOn() == false)) {
            throw new PostConditionError();
        }
    }

    public void press() {
        // 1. Pré-invariant
        checkInvariant();
        // 2. Préconditions
        ..
        // 3. Captures
        boolean isOn_atPre = isOn();
        boolean getCount_atPre = getCount();
        // 4. Traitement
        super.press();
        // 5. Postconditions
        // POST: isOn() == !isOn()@Pre
        if (!(isOn() == !isOn_atPre))
            throw new PostConditionError();
        // POST: getCount() == getCount()@Pre + 1
        if (!(getCount() == getCount_atPre + 1))
            throw new PostConditionError();
        // 6. Post invariant
        checkInvariant();
    }
}
```
