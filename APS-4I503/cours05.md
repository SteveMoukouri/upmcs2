# APS
## Cours 5
### L'instruction RETURN
```
stat ::= ..
       | RETURN Expr
```
#### Typage

* Expression:
    * *Si* G ⊢ e:t *alors* G ⊢ (RETURN e):t
* Séquences: soit s != (RETURN e)
    * *Si* G ⊢ void *et* G ⊢ cs:t *alors* G ⊢ (s;cs):t
    * *Si* G ⊢ s:t avec t != void *et* G ⊢ cs:t *alors* G ⊢ (s; cs):t
* *Si* G ⊢ e:t *et* G ⊢ x:t *alors* G ⊢ (SET x e):void
* *Si* G ⊢ e:bool *et* G ⊢ cs:t *alors* G ⊢ (WHILE e cs):t
* *Si* G ⊢ e:bool *et* G ⊢ cs1:t *et* G ⊢ cs2:t *alors* G ⊢ (IF e cs1 cs2):t
*

#### Sémantique

Instruction:
* une non valeur
* une valeur v

On note w (omega) pour v ou rien.

Relation sémantique des instructions:
* Env, Mem, Stat -> (Val, Mem)
* r, m ⊢ s ~> (w, m')

##### RETURN
Si r, m ⊢ e ~> v alors r, m ⊢ (RETURN e) ~> (v, m)

##### Affectation
Si r, m ⊢ (SET x e) ~> (rien, m[r(x):=v])

##### Boucle
* Si r, m ⊢ e ~> #f alors  r, m ⊢ (WHILE e blk) ~> (rien, m)
* Si r, m ⊢ e ~> #t alors
    * Si r, m ⊢ blk ~> (rien, m') et r, m' ⊢ (WHILE e blk) ~> (w, m") alors r, m ⊢ (WHILE e blk) ~> (w, m")
    * Si r, m ⊢ blk ~> (v, m') alors r, m ~> (WHILE e blk) ~> (v, m')

##### Alternative
* Si r, m ⊢ e ~> #t et r, m ⊢ blk1 ~> (w, m') alors r, m ⊢ (IF e blk1 blk2) ~> (w, m')
* Si r, m ⊢ e ~> #f et r, m ⊢ blk2 ~> (w, m') alors r, m ⊢ (IF e blk1 blk2) ~> (w, m')

##### Suites de commandes
Pour toute instruction s et toute continuation cs:
* Si r, m ⊢ s ~> (rien, m') et r, m' ⊢ cs ~> (w, m") alors r, m ⊢ (s;cs) ~> (w, m")
* Si r, m ⊢ s ~> (v, m') alors r, m ⊢ (s;cs) ~> (v, m')

##### Déclarations
* Si d ≅ CONST x e
    * Si r, m ⊢ e ~> v et r[x=v], m ⊢ cs ~> (w, m') alors r, m ⊢ (d;cs) ~> (w, m')
* Si d ≅ VAR x t
    * Si r[x=a], m[x=new] ⊢ cs ~> (w, m') alors r, m ⊢ (d;cs) ~> (w, m')

### Fonctions procédurales
#### Syntaxe
```
Dec ::= ..
      | 'FUN' ident Type '[' TypeIds ']' Prog
      | 'FUN' ident Type '['  ']' Prog
```

#### Typage
* Expression/application:
    * Si G ⊢ f:void->t alors G ⊢ (f):t
* Déclaration/suite de commandes:
    * d ≅ FUN f t [] blk
        * Si G[f:void->t] ⊢ cs:t' et G ⊢ blk:t alors G ⊢ (d;cs):t'
    * d ≅ FUN f t [x1:t1;..xn:tn] blk
        * Si G[f:t1\*..\*tn] ⊢ cs:t' et G[x1:t1;..;xn:tn] ⊢ blk:t alors G ⊢ (d;cs):t'

#### Sémantique
Relation sémantique des expressions: r, m ⊢ e ~> (v, m')

Valeur d'une fonction procédurale: < bd, r+[x1=#] >

##### Application
* Si
    * r, m ⊢ e ~> (< r'+[x1=#..xn=#]>, m')
    * r, m' ⊢ e1 ~> (v1, m1) et .. et r, mn-1 ⊢ en ~> (vn, mn)
    * r'[x1=v1]..[xn=vn], mn ⊢ bd ~> m"
* alors r, m ⊢ (e e1 .. en) ~> (v, m")

### Fonctions récursives
#### Syntaxe
```
Dec ::= ..
      | 'FUN' 'REC' ident Type '[' TypeIds ']' Prog
      | 'FUN' 'REC' ident Type '[' ']' Prog
```
#### Sémantique
Valeur d'une fonction récursive: < bd, r+[f=!][x1=#]..[xn=#] >

##### Application
* Si
    * r, m ⊢ e ~> (< r'+[f=!][x1=#]..[xn=#]>, m')
    * r, m' ⊢ e1 ~> (v1, m1) et .. et r, mn-1 ⊢ en ~> (vn, mn)
    * r'[f=< bd, r+[f=!][x1=#]..[xn=#] >][x1=v1]..[xn=vn], mn ⊢ bd ~> m"
* alors r, m ⊢ (e e1 .. en) ~> (v, m")
