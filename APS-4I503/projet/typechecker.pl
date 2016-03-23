/* Forme des règles de typage: */
/* type(Expr, Env, Result)     */
/* L'environnement est une table d'associations */

/* Noeuds terminaux */
type(N, _, int) :- integer(N).
type(true, _, bool).
type(false, _, bool).
type(V, Env, Type) :- get_assoc(V, Env, Type).

/* Noeuds non terminaux - expressions */

% Déclarations
type(var(V, T), Env, void) :- 

% Statements
type(if(Cond, Bloc1, Bloc2), Env, void) :- type(Cond, Env, bool), 
					   type(Bloc1, Env, void),
					   type(Bloc2, Env, void).
type(while(Cond, Bloc), Env, void) :- type(Cond, Env, bool), type(Bloc, Env, void).
type(set(Var, Expr), Env, void) :- .
type(echo(Var), Env, void) :- typeVar(Var, Env, bool).
type(echo(Var), Env, void) :- typeVar(Var, Env, int).

% Opérations
type(not(A), Env, bool) :- type(A, Env, bool).
type(and(A, B), Env, bool) :- type(A, Env, bool), type(B, Env, bool).
type(or(A, B), Env, bool) :- type(A, Env, bool), type(B, Env, bool).

type(add(A, B), Env, int) :- type(A, Env, int), type(B, Env, int).
type(sub(A, B), Env, int) :- type(A, Env, int), type(B, Env, int).
type(mul(A, B), Env, int) :- type(A, Env, int), type(B, Env, int).
type(div(A, B), Env, int) :- type(A, Env, int), type(B, Env, int).
type(mod(A, B), Env, int) :- type(A, Env, int), type(B, Env, int).

% Règles intermédiaires
type([H|T], Env, void) :- type(H, Env, void), type(T, Env, void).

