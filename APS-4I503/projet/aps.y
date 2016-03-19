
%error-verbose

%{
  #include <stdlib.h>
  #include <stdio.h>
  #include <string.h>
  #include "prolog_gen.h"
  #include "ast.h"

  extern FILE *yyin;
  
  AST* ast_result;

  int yyerror(const char *message) {
    fprintf(stderr, "%s\n", message);
    return 0;
  }
%}

%union {
  int entier;
  char* str;
  struct _ast* ast;
}

%token WHILE IF SET VAR CONST
%token TRUE FALSE
%token AND OR NOT
%token EQ LT ADD SUB DIV MUL MOD
%token VOID BOOL INT
%token<entier> NOMBRE
%token<str> IDENT

%type<ast> Prog Cmds Dec Stat Expr Type Function InterStat

%start Prog
%%


Prog: '[' Cmds ']'    { $$ = make_prog($2); ast_result = $$; }

Cmds: Stat InterStat             { $$ = make_cmds($1, $2); }
	|      	Dec ';' Cmds     { $$ = make_cmds($1, $3); }

InterStat:                   { $$ = NULL; }
	| ';' 	Cmds         { $$ = $2 ;  }

Dec: VAR IDENT Type                    { $$ = make_var(strdup($2), $3); }
	| 	CONST IDENT Type Expr  { $$ = make_cst(strdup($2), $3, $4); }
		    
Stat: SET IDENT Expr               { $$ = make_set(strdup($2), $3); }
	| 	IF Expr Prog Prog  { $$ = make_cond($2, $3, $4); }
	| 	WHILE Expr Prog    { $$ = make_loop($2, $3); }
			  
 Expr: TRUE                         { $$ = make_bool_expr(T_TRUE);  }
	| 	FALSE              { $$ = make_bool_expr(T_FALSE); }
	| 	NOMBRE             { $$ = make_integer_expr($1); }
	| 	IDENT              { $$ = make_ident_expr(strdup($1)); }
	| '(' 	Function ')'       { $$ = $2; }
   
Function: NOT Expr                { $$ = make_unary_expr(T_NOT, $2);      }
	|  	OR Expr Expr      { $$ = make_binary_expr(T_OR, $2, $3);  }
	|  	AND Expr Expr     { $$ = make_binary_expr(T_AND, $2, $3); }
	|  	EQ Expr Expr      { $$ = make_binary_expr(T_EQ, $2, $3);  }
	|  	LT Expr Expr      { $$ = make_binary_expr(T_LT, $2, $3);  }
	|  	ADD Expr Expr     { $$ = make_binary_expr(T_ADD, $2, $3); }
	|  	SUB Expr Expr     { $$ = make_binary_expr(T_SUB, $2, $3); }
	|  	DIV Expr Expr     { $$ = make_binary_expr(T_DIV, $2, $3); }
	|  	MUL Expr Expr     { $$ = make_binary_expr(T_MUL, $2, $3); }
	|	MOD Expr Expr     { $$ = make_binary_expr(T_MOD, $2, $3); }
   
Type: VOID                { $$ = make_type(T_VOID); }
        | 	BOOL      { $$ = make_type(T_BOOL); }
	| 	INT       { $$ = make_type(T_INT); }
		

%%

AST* ast_from_program(FILE* fp) {
    yyin = fp;
    yyparse();
    fclose(fp);
    return ast_result;
}
