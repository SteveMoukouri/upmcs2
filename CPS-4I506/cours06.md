# CPS
## Cours 6 - Logique de Hoare
### Définition
* *Programme:* if, while, ..
* *Propriétés:*  (liées au comportement)
    * Préconditions
    * Postconditions
    * Invariants

Caractérisation logique des programmes (séquentiels/impératifs)

#### Spécification

Triplet de Hoare:
```
.   { P }         prog         { Q }
préconditions              postconditions
```
Logique "classique" + variables et expressions de programmes.

#### Interprétation

En supposant P vraie **avant** l'exécution du programme, alors Q est vraie
**après** l'exécution pour que le triplet {P} prog {Q} soit considéré vrai.

#### Langage des programmes

* **Programme:** corps d'une méthode (syntaxe Java)
    * Pas d'invocations
    * Types booléens, entiers, tableaux
    * Expressions arithmétiques et "logiques"
* **Instructions:**
    * Affectation
    * Séquence
    * Alternative
    * Boucle while

### Axiome d'affectation

```
____________________________(aff)
{ Q[expr/V] } V = expr { Q }
```
Q[expr/V]: Q dans laquelle expr écrase les occurences de V.

#### Exemple

Trouver P la plus faible précondition telle que { P } x = y + 1 { x = 3 } est vrai.

1.  { x = 3 } x = y + 1 { x = 3 }
2.  { y + 1 = 3 } x = y + 1 { x = 3 }
3.  { y = 3 } x = y + 1 { x = 3 }
Donc P (def)=  { y = 2 }

### Règle du séquencement
```
{ P } C1 { P1 } { P1 } C2 { P2 } .. { Pn } Cn { Q }
___________________________________________________(seq)
           { P } C1; C2; .. ; Cn { Q }
```

#### Exemple

Trouver P la plus faible précondition telle que { P } z = x; z = z + y; u = z; { u = x + y }

1. { z = x + y } u = z { u = x + y } (aff)
2. { z + y = x + y (<=> z = x)} z = z + y { P2 (<=> z = x + y) } (aff)
3. { x = x (<=> true)} z = x { P1 (<=> z = x)} (aff)
4. { true } z = x; z = z + y; u = z; { u = x + y } (seq[3][2][1])

#### Arbre de preuve

```
_______________(aff)   _______________(aff)  ________________(aff)
{true} z=x {P1}        {P1} z=z+y {P2}       {P2} u=z {u=x+y}
_____________________________________________________________(seq)
                    { P } ___; ___; ___ { Q }
```

### Règle du Modus Ponens

Utilisé seulement pour les préconditions dans le cas de CPS.

```
P => P'  {P'} C {Q}
____________________(mp-pre)
     {P} C {Q}
```

#### Exemple

Montrer que {x = 3} y = x + 1 { y > 1 }

Preuve:
1. { x+1 > 1 (<=> x > 0) } y = x + 1 { y > 1 } (aff)
2. x = 3 => x > 0 (CQFD)
3. { x = 3 } y = x + 1 { y > 1 } (mp-pre[2][1])

### Règle de l'alternative

```
{P1} C1 {Q}                    {P2} C2 {Q}
__________________________________________(alt)
{(B => P1)^(!B=>P2)} if (B) C1 else C2 {Q}
```

#### Exemple

Trouver P la plus faible précondition telle que {P} if (x < y) x = y else x = 2 {x = 2}

1. {y = 2 (<=> P1)} x = y {x = 2} (aff)
2. {2 = 2 (<=> P2 <=> true)} x = 2 {x = 2} (aff)
3. {[(x < y) => y = 2]^[(x >= y) => true] (<+> P)} if (x < y) x = y else x = 2 {x = 2}
4. P <=> [(x < y) => y = 2]^true <=> (x < y) => y = 2 <=> (x >= y)v y=2

### Exemples

Prouver {true} a = x+1; if (a-1==0) y = 1 else y = a {y = x+1}

1. {1 = x+1 (<=> x = 0)} y = 1 {y = x+1} (aff)
2. {a = x+1 (<=> x = a-1)} y = a {y = x+1} (aff)
3. {[(a-1=0) => x=0]^[(a-1!=0) => x=a-1]}(P') if (--) -- else -- (alt[1][2])
4. {[(x+1-1 = 0) => x=0]^[(x+1-1 != 0) => x=x+1-1]} a=x+1 {P'} (aff)
    <=> (x=0=>x=0)^(x!=0=>x=x)
    <=> true
5. {true} --; if (--) -- else -- (seq[4][3])
