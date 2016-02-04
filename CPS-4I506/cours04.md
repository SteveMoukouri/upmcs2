# CPS
## Cours 4 - Raffinement
**Sous-typage:** la possibilité de définir un type B comme sous-type d'un type A et permettant la *subsomption*. Si B est un sous-type de A (B <: A) alors une expression de type B peut être utilisée dans un contexte prévu pour une expression de type A *sans erreur de typage*.

##### Exemple
```java
class A {
    public void m() { .. }
}

class B extends A {
    public void m() { .. }
    public void n() { .. }
}
```
Le type B est sous-type de A.

Contexte:

```java
public static void f(A a) { a.m() }
A objA = new A();
f(objA);
// Subsomption
B objB = new B();
f(objB);
```
*Questions:* Quid de la sémantique?
* Un objet de classe A peut-il être substitué à l'exécution par un objet de classe B sans poser de problèmes (polymorphisme)
* Le contrat de A est-il respecté par celui de B? (=> héritage de contrat)
* Les tests prévus pour A peuvent-ils s'appliquer sur B?

*Réponses:* tout va bien si et seulement si le service B **raffine** le service A.

#### Étape 1: raffinement des ensembles
Dans les sensembles, le raffinement est la ?? du sous-ensemble.

L'ensemble B "raffine" A si B est inclus dans A.
* A raffine A (réflexivité)
* si B raffine A et C raffine B, alors C raffine A (transitivité)
* si B raffine A et B != A alors A ne raffine pas B (antisymmétrie)

*Remarque:* B1 * B2 * .. * Bn raffine A1 * A2 * .. * An ssi B1 raffine A1 .. Bn raffine An

#### Étape 2: raffinement dans les fonctions totales
```
let f x =
    if x == 'a' then 1
    elif x == 'b' then 2
    elif x == 'c' then 1
    elif x == 'd' then 2
    else undefined

dom f = {a, b, c, d}
cod f = {1, 2}
let g1 x =
    match x with
        a -> 1
    |   b -> 2
    |   c -> 1
    | _ -> undefined
dom g1 = {a, b, c}

test(phi) : pour tout x appartient à {a, b, c, d}, phi x > 0
```
g1 raffine f si on peut utiliser g1 dans tout contexte utilisant f
* test f est correct
* test g1 est incorrect car g1 'd' n'est pas défini

g raffine f si dom f est inclus dans dom g

```
let g2 x =
    match x with
        a -> 1
    |   b -> 2
    |   c -> 1
    |   d -> 2
    |   e -> 3
    | _ -> undefined
dom g2 = {a, b, c, d, e}
```
* Condition n°1 vérifiée - dom f est inclus dans dom g2

```
test2(phi) = pour tout x appartien à dom phi, phi x <= 2
```
* test2 f passe mais test2 g2 non, g2 renvoie 3.

Donc g2 ne raffine pas f.

Condition n°2: g raffine f implique cod g est inclus dans cod f

#### Etape 3: raffinement dans les spécifications
* opérateur f: A1 * A2 * .. * An -> U
  * pre f(v1, v2 .. vn, U) require pref(v1, v2 .. vn) = true
* observations
  * [invariants]
  * [f]
	* postf(v1, v2 .. vn) appartient à {true, false} (=> interprétation logique)

* dom f = {(v1, v2 .. vn) | pref(v1, v2 .. vn) = true}
* cod f = {u appartien à U | pour tout v1, v2 .. vn, pref(v1, v2 .. vn) = true => postf(v1, v2 .. vn, u) = true}

**g raffine f ssi**
1. **dom f est inclus dans dom g**
2. **cod g est inclus dans cod f**

En termes logiques:
1. pre f => pre g
2. post g => post f

* Service: S
* Operators
  * op [S] ->  [S]
	* pre op(S) req P
* Observations:
  * O

* Service: S'
  * refine: S
* Operators
  * op [S] ->  [S]
	* pre op(S) req P'
* Observations:
  * O'
  
**Conditions pour que le raffinement soit correct**
1. P' => P
2. O => O'

#### Étape 4: héritage dans les contrats
```java
interface S_Service {
	//inv: I
	//pre: Preop
	//post: Postop
	public void op();
}
// + tests de I, Preop et Postop

interface S'_Service {
	//inv: I
	//pre: Preop
	//post: Postop
	public void op();
}
```
##### Tests
**En théorie**(conditions de Lishov)**:** 
* I' => I
* Preop => Preop'
* Postop' => Postop

*Problème*: Pas décidable.

*En pratique:* approche de Eiffel (B Meyer)
* On teste I puis I' (ou I' puis I)
* On teste Postop puir Postop' (ou l'inverse)
* On teste Preop mais *warning* en cas d'erreur, puis on teste Preop'.

##### Étape 5: composants require/provide
* Un composant C': <R', F'> raffine C: <R, F>
* Conditions: 
  * F' raffine F
  * R raffine R'
