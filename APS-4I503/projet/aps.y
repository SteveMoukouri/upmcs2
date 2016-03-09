
%error-verbose

%{
  #include <stdlib.h>
  #include <stdio.h>
  #include <string.h>
  #include "ast.h"

  extern FILE *yyin;

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
%token EQ LT ADD SUB DIV MUL
%token VOID BOOL INT
%token<entier> NOMBRE
%token<str> IDENT

%type<ast> Entry Prog Cmds Dec Stat Expr Type Function InterStat

%start Entry
%%

Entry: '[' Cmds ']'   {
                        $$ = make_prog($2);
                      }

Prog: '[' Cmds ']'    { $$ = make_prog($2); }

Cmds: Stat InterStat  { $$ = make_cmds($1, $2); }
	|      	Dec ';' Cmds     { $$ = make_cmds($1, $3); }

InterStat:                   {  }
	| ';' 	Cmds         { $$ = $2 ;  }

Dec: VAR IDENT Type                    { $$ = make_dec(T_VAR, $3, strdup($2), NULL); }
	 | 	CONST IDENT Type Expr  { $$ = make_dec(T_CONST, $3, strdup($2), $4); }
		    
Stat: SET IDENT Expr               { $$ = make_stat(T_SET, strdup($2), $3, NULL, NULL); }
	| 	IF Expr Prog Prog  { $$ = make_stat(T_IF, NULL, $2, $3, $4); }
	| 	WHILE Expr Prog    { $$ = make_stat(T_WHILE, NULL, $2, $3, NULL); }
			  
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
   
Type: VOID               { $$ = make_type(T_VOID); }
        | 	BOOL      { $$ = make_type(T_BOOL); }
	| 	INT       { $$ = make_type(T_INT); }
		

%%

int main(int argc, char* argv[]) {
  FILE *fp;
  if (argc > 1) {
    if (! (fp = fopen(argv[1], "r"))) {
      fprintf(stderr, "Error, can't open `%s'\n", argv[1]);
      exit(1);
    }
    yyin = fp;
    yyparse();
    fclose(fp);
    printf("Expected -- prog([set(x,22),while(app(lt, [x, 42]), [set(x, app(add, [x, 1]))])])\n");
} else {
    fprintf(stderr, "Error, no file specified\n");
    exit(1);
  }
  return 0;
}
