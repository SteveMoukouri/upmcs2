## Cours 1
### Introduction
#### Programme
Description statique (fichier) d'un comportement dynamique (exécution).
Statique: exécutable/bytecode/assembleur/code source
Code source: suite de caractères obéissant à des règles de syntaxe.

Syntaxe:

* Lexique: ensemble d'unités lexicales (mots/symboles)
* Grammaire: règles d'agencement des mots

définit les suites de caractères qui sont des programmes

donne un procédé de construction d'un mécanisme de
reconnaissance des programmes (automates, langages formels)

#### Sémantique
* opérationnelle (grand pas/petit pas)
* dénotationnelle (lambda-calcul)
* axiomatique (cf. logique de Hoare en CPS)

Comportement dynamique: résulte du traitement de données statiques
(via CPU/interp bytecode/interp code source)

Règles/mécanismes d'interprétation = fonction d'interprétation

#### Analyse
Dirigée par la syntaxe.
* Analyse syntaxique: éliminer les sources non évaluables
* Analyse de type: éliminer les erreurs prévisibles à l'exécution.
```
Erreur					      Erreur
|          AST			       AST       |
[Source] --> [Analyse syntaxique] ---> [Vérificateur de types] ---> [Evaluateur]
lex/yacc	       	      (Prolog)			 | scheme
[résultat]
```
### Noyau impératif
* Expressions: bool, int, void
* Instructions: affectation, alternative, boucle, séquence, déclaration = Blocs
####Syntaxe
#####Lexique
Mots clés (réservés):
  Key | Bool  | Int
------|-------|-----
VAR   | true  | add
CONST | false | sub
SET   | and   | mul
IF    | or    | div
WHILE |       |

Symboles réservés:
```
[ ] ( ) ;
```
Description du lexique des noms de variables et des entiers: expressions régulières.
En lex:
```
num   : [0-9][0-9]*
ident : [a-z][a-z]*
```
#####Grammaire
Formalisation avec la Backus-Naur Form (BNF)
```
Prog ::= '[' Cmds ']'
Cmds ::= Cmd
       | Cmd ';' Cmds
Cmd  ::= Dec   #(declaration)
       | Stat  #(statement)
Dec  ::= 'CONST' ident Exp
       | 'VAR' ident TypeExp
TypeExp ::= 'bool' | 'int'
Stat ::= 'SET' ident Exp
       | 'IF' Exp Prog Prog
       | 'WHILE' Exp Prog
UnOp ::= '(' 'not' Exp ')'
BinOp ::= '(' 'and' Exp Exp ')'
        | '(' 'or' Exp Exp ')'
        | '(' 'add' Exp Exp ')'
        | '(' 'sub' Exp Exp ')'
        | '(' 'mul' Exp Exp ')'
        | '(' 'div' Exp Exp ')'
Exp  ::= 'true'
       | 'false'
       | num
       | ident
       | UnOp
       | BinOp
```
