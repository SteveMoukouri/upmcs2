# APS
## Cours 2

###Typage
Définition: assigner un type (de données)
* aux expressions
* aux instructions
* aux programmes

####Contexte de typage
Un ensemble d'assignations de types. *Noté* Γ.
```
Γ ⊢ x : t si (x : t) ∈ Γ ∀ type t
```
Dans le cadre du langage réalisé en APS:

#####Expressions
```
true  : bool
false : bool
n     : int  si n ∈ num
x     : ?    si x ∈ ident
(not e) : bool si e : bool
(and e1 e2) : bool si e1 : bool et e2 : bool
(or e1 e2) : bool si e1 : bool et e2 : bool

si e1 : int et e2 : int
(add e1 e2) : int
(sub e1 e2) : int
(mul e1 e2) : int
(div e1 e2) : int

si e1 : t et e2 : t pour tout type t
(eq e1 e2) : bool
(lt e1 e2) : bool
```

#####Instructions
*Relation* entre:
* Un contexte de typage
* Une expression
* Un type

```
Γ ⊢ (SET x e) : void si Γ ⊢ x : t et Γ ⊢ e : t ∀ type t
Γ ⊢ (IF e b1 b2) : void si Γ ⊢ e : bool et Γ ⊢ b1 : void
                                         et Γ ⊢ b2 : void
Γ ⊢ (WHILE e b) : void si Γ ⊢ e : bool et b : void
```

#####Suite de commandes

*Relation* entre:
* Un contexte de typage
* Un bloc (programme)
* void

Deux cas à considérer:
* [declaration; command sequence]
* [statement; command sequence]
```
Γ ⊢ [] : void
Γ ⊢ (s::cs) : void si s : void et Γ ⊢ cs : void ∀ s ∈ statement
Γ ⊢ ((CONST x e)::cs) : void si Γ;x:t ⊢ cs : void et Γ ⊢ e : t
Γ ⊢ ((VAR x t)::cs) : void si Γ;x:t ⊢ cs : void
```
Notation ci-dessus: Γ;x:t abrège Γ U {x:t}

#####Programmes/blocs
Bloc: suite de déclarations et d'instructions
```
⊢ [ cs ] : void si ⊢ cs : void
```
Notation d'une règle d'inférence:
```
Γ ⊢ e : bool    Γ ⊢ b : void
----------------------------
  Γ ⊢ (WHILE e b) : void
```
```
Γ; x:t          ⊢ cs : void
----------------------------
 Γ ⊢ ((VAR x t)::cs : void
```
```

*                              x : bool ⊢ x : bool
                            ---------------------------
x : bool ⊢ true : bool        x : bool ⊢ (not x) : bool
---------------------------------------------------------
            Γ ⊢ (and true (not x)) : bool
```

###Simplification

On peut enlever des règles de typage en les ajoutant à la description du programme.

Par exemple:
```
true : bool; false : bool ⊢ [ cs ] : void
```
On peut ainsi enlever toutes les opérations dont les paramètres sont identiques à la valeur de retour.
```
Γ ⊢ op : t1 * t2 -> t      Γ ⊢ e1 : t1 Γ    Γ ⊢  e2 : t2
----------------------------------------------------------
                  Γ ⊢  (op e1 e2) : t
```
En généralisant:
```
Γ ⊢ op : t1 * .. * tN -> t      Γ ⊢ e1 : t1 Γ  ..  Γ ⊢  eN : tN
-----------------------------------------------------------------
                  Γ ⊢  (e e1 e2...eN) : t
```
#####Ajout des types dans la grammaire
```
type ::= 'bool'
      |  'int'
      |  type '*' .. '*' type -> type
      |  vartype
```
