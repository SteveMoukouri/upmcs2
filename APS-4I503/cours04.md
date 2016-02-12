# APS
## Cours 4
### Retour sur la portée lexicale
##### Blocs
Sémantique: si r, m ⊢ cs ~> m' alors r, m ⊢ [cs] ~> **m'/r**

```
[
    VAR x int;
    SET x 0;
    IF (true)
        [ VAR x int;
          SET x 12 ]
        [ SET x 1  ]
    SET x (x + 1)
]
```
À la fin de l'exécution du programme, x vaut 1. Si on retire /r de la règle de sémantique du bloc, on peut avoir le programme suivant où x vaut 4:

```
[
    VAR x int;
    IF (true)
        [ SET x 3 ]
        [   ...   ]
    SET x (x + 1)
]
```

### Fonctions et procédures

* Fonctions: abstractions vis-à-vis d'une expression
* Procédures: abstractions vis-à-vis d'un bloc d'expressions

#### Fonction

* Domaine de départ, codomaine d'arrivée.
* Abstraction d'une expression avec ses variables associées
* En APS: [x:int] (x + 1) : **expression fonctionnelle**.

##### Syntaxe
```
Expr ::= ..
       | '[' TypIds ']' Expr
       | '(' Exprs ')'

Exprs ::=  Expr
        | Expr Exprs

TypIds ::= ident ':' type
         | ident ':' type ';' TypIds
```

##### Exemple

```
CONST xor [b1:bool;b2:bool]
            (and (or b1 b2)
                (not (and b1 b2))); ..
(xor true false)
```

##### Typage

* **Si** G;x1:t1;x2:t2;..;xn:tn ⊢  e:t **alors** G ⊢ [x1:t1;..;xn:tn]e : (t1\*..\*tn)->t
* **Si** G ⊢ e:(t1\*..\*tn)->t et G ⊢ e1:t1 .. G ⊢ en:tn **alors** G ⊢ (e e1 .. en):t

```
Type ::= ..
       | Typeargs '->' Type
       | '(' Type ')'

Typeargs ::= Type
           | Type '*' Typeargs
```
#### Sémantique
Ajouter les définitions des expressions fonctionnelles, des valeurs fonctionnelles (=> fermetures).

(([x1:t1;..;xn:tn]e') e1 .. en)

Fermeture: (notation) **< e, r + [x1=#]..[xn:#] >**

**Sémantique de la fermeture:** r, m ⊢ [x1:t1;..;xn:tn]e ~> < e, r + [x1=#]..[xn:#] >

##### Application - Liaison statique

* **Si** r, m ⊢ e ~> < e', r" + [x1=#]..[xn=#] >  
* **et** r, m ⊢ e1 ~> v1 .. r, m ⊢ en ~> vn
* **et** r'[x1=v1]..[xn=vn], m ⊢ e' ~> v
* **alors** r, m ⊢ (e e1..en) ~> v

```
[ CONST f [x:int] (add x 1)
  CONST g [x:int] (add (f x) 1)
  CONST f [x:int] (add x 3)
  VAR r int
  SET r (g 42)
]
```
Liaison statique: la valeur de g est fabriquée au moment de sa déclaration, g capture la valeur de f à l'instant de la déclaration de g. La valeur de r est donc 44.

### Procédures
Avec d = PROC p [x1:t1;..;xn:tn] blk

#### Syntaxe
```
Dec ::= ..
      | 'PROC' ident '[' TypeIds ']' Prog

Stat ::= ..
       | 'CALL' ident Exprs
```
##### Typage
**Si** G; p:t1\*..\*->void ⊢ cs : void et G ⊢ blk : void **alors** G ⊢ d;cs : void

##### Appel
**Si** G ⊢ p:t1\*..\*tn -> void **et** G ⊢ e1:t1 .. G ⊢ en:tn **alors** G ⊢ CALL p e1..en : void

#### Sémantique
Fermeture "procédurale": **< blk, r + [x1=#]..[xn=#] >**

**Si** r [p = < blk, r + [x1=#]..[xn=#] >], m ⊢ cs ~> (r', m') **alors** r, m ⊢ d; cs ~> (r', m')

##### Application - Liaison statique
* **Si** r(p) = < blk, r + [x1=#]..[xn=#] >
* **et** r, m ⊢ e1 ~> v1 .. r, m ⊢ en ~> vn
* **et** r'[x1=v1..xn=vn], m ⊢ blk ~> m'
* **alors** r, m ⊢ (CALL p e1 .. en) ~> m'
