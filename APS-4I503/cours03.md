# APS
## Cours 2
### Sémantique opérationnelle
* *Expressions:* fonctionnel ~> valeurs
* *Instructions:* impératif ~> état mémoire
* *Déclaration:* ~> état mémoire, environnement

#### Environnement
Association entre identificateurs et
* valeurs
* adresses mémoire

Notation:
```
#v - valeurs
@a - adresses
```
Soit r un environnement.
* Accès, avec x : Id
    * r(x)
    * []\(x) = error
* Ajout, avec w une valeur:
    * r[x = w]
    * r[x = w]\(x) = w
    * r[x = w]\(y) = r(y) avec x != y

#### Mémoire
Associations entre adresses et
* valeurs
* autres adresses

Avec m une mémoire:
* Accès:
    * m(a)
    * []\(a) = error
* Extension (allocation):
    * m[a := new] -- m(a) est non défini
* Modification: m[a := w]
    * m[a := w][a := w'] = m[a := w']
    * m[a' := w][a := w'] = m[a := w'][a' := w] avec a != a'
    * [][a := w] = error
* (m/r) (a) = m(a) si il existe x tq r(x) = @a
* error sinon

#### Jugement sémantique
* *Expression:* r, m ⊢ e ~> v
* *Instructions:* r, m ⊢ s ~> m'
* *Déclarations:* r, m ⊢ d ~> r', m'
* *Suite de commandes/Bloc:* r, m ⊢ [cs] ~> m'

##### Constantes
* r, m ⊢ true ~> #t
* r, m ⊢ false ~> #f
* r, m ⊢ n ~> #n (n : num)

##### Opérateurs booléens
* r, m ⊢ (not e) ~> #f si r, m ⊢ e ~> #t
* r, m ⊢ (not e) ~> #t si r, m ⊢ e ~> #f
* r, m ⊢ (or e1 e2) ~> #t si r, m ⊢ e1 ~> #t
* r, m ⊢ (or e1 e2) ~> #t si r, m ⊢ e1 ~> #f et r, m ⊢ e2 ~> #t
* r, m ⊢ (or e1 e2) ~> #f si r, m ⊢ e1 ~> #f et r, m ⊢ e2 ~> #f
* r, m ⊢ (and e1 e2) ~> #f si r, m ⊢ e1 ~> #f
* r, m ⊢ (or e1 e2) ~> #f si r, m ⊢ e1 ~> #t et r, m ⊢ e2 ~> #f
* r, m ⊢ (or e1 e2) ~> #t si r, m ⊢ e1 ~> #t et r, m ⊢ e2 ~> #t

##### Opérateurs arithmétiques
* r, m ⊢ (add e1 e2) ~> v1 + v2 si r, m ⊢ e1 ~> v1 et r, m ⊢ e2 ~> v2
* r, m ⊢ (sub e1 e2) ~> v1 - v2 si r, m ⊢ e1 ~> v1 et r, m ⊢ e2 ~> v2
* r, m ⊢ (mul e1 e2) ~> v1 * v2 si r, m ⊢ e1 ~> v1 et r, m ⊢ e2 ~> v2
* r, m ⊢ (div e1 e2) ~> v1 / v2 si r, m ⊢ e1 ~> v1 et r, m ⊢ e2 ~> v2 et v2 != 0

##### Instructions
* r, m ⊢ (SET x e) ~> m[a := v] si r, m ⊢ e ~> v et r(x) = @a
* r, m ⊢ (IF b e1 e2) ~> m' si r, m ⊢ b ~> #t et r, m ⊢ e1 ~> m'
* r, m ⊢ (IF b e1 e2) ~> m' si r, m ⊢ b ~> #f et r, m ⊢ e2 ~> m'
* r, m ⊢ (WHILE e b) ~> m  si r, m ⊢ e ~> #f
* r, m ⊢ (WHILE e b) ~> m''  si r, m ⊢ e ~> #t et r, m ⊢ b ~> m' et r, m' ⊢ (WHILE e b) ~> m''

##### Suite de commandes
* Si s une instruction: r, m ⊢ s;cs ~> r, m' ⊢  s ~> m'
* Si d ≅ CONST x e: r, m ⊢ d; cs ~> r[x := #v], m si r, m ⊢ e ~> #v
* Si d ≅ VAR x: r, m ⊢ d; cs ~> r[x := @a], m[a = new]

##### Bloc
* r, m ⊢ [cs] ~> r, (m'/r) si r, m ⊢ cs ~> r', m'

#### Typage
Distinguer les identificateurs modifiables des immuables.

Introduction du type **"pointeur vers"**: **@t** "pointeur vers t"
* Déclaration/suite de commandes
    * G ⊢ (VAR c t; cs) : void si G, x:@t ⊢ cs : void
* Expression
    * G, x:@t ⊢ x:t || G ⊢x:t si G(x) = t ou G(@x) = t
* Affectation
    * G ⊢ (SET x e) : void si G ⊢ x:@t et G ⊢ e:t

##### Sémantique
* r, m ⊢ (SET x e) ~> m[r(x) := v] si r, m ⊢ e ~> v
