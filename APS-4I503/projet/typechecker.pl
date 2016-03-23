% Forme des règles de typage:
% type(Expr, Env, NewEnv, Type)
% L'environnement est une liste (Nom, Const, Type)
% où Const est vrai si la variable a été déclarée constante, faux sinon

/* Noeuds terminaux */
type(N, _, int) :- integer(N).
type(true, _, bool).
type(false, _, bool).
type(Var, Env, Type) :- env_get(Var, Env, Type).

/* Noeuds non terminaux - expressions */

% Déclarations de valeurs nommées
typeNamed(var(Name, Type), Env, NextEnv, void) :- 
    env_add(Env, (Name, false, Type), NextEnv).
typeNamed(const(Name, Type, Expr), Env, NextEnv, void) :- 
    type(Expr, Env, Type), env_add(Env, (Name, true, Type), NextEnv).

% Statements
type(if(Cond, Bloc1, Bloc2), Env, void) :- type(Cond, Env, bool), type(Bloc1, Env, void), type(Bloc2, Env, void).
type(while(Cond, Bloc), Env, void) :- type(Cond, Env, bool), type(Bloc, Env, void).
type(set(Var, Expr), Env, void) :- type(Var, Env, VarType), type(Expr, Env, VarType).
type(echo(Var), Env, void) :- typeVar(Var, Env, bool); typeVar(Var, Env, int).

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
type([H|T], Env, void) :- typeNamed(H, Env, NextEnv, void), type(T, NextEnv, void).
type([H|T], Env, void) :- type(H, Env, void), type(T, Env, void).

% Programme
type(prog(Cmds), Env, Type) :- type(Cmds, Env, Type).

% Gestion de l'environnement
env_add(Env, (VarName, IsConst, VarType), [(VarName, IsConst, VarType)|Env]).
env_get([(LVarName, LIsConst, _)|T], (VarName, IsConst), Type) :- % Itération à travers la liste
    (LVarName \= VarName; LIsConst \= IsConst), env_get(T, (VarName, IsConst), Type). 
env_get([(LVarName, LIsConst, LVarType)|_], (LVarName, LIsConst), LVarType).
env_get([(LVarName, LIsConst, LVarType)], (LVarName, LIsConst), LVarType). % Cas terminal
