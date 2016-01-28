%error-verbose

%{
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "ast.h"

  extern FILE * yyin;

  int total_applications = 0;

  void yyerror (char const *s) {
    fprintf (stderr, "%s\n", s);
  }

%}

%union {
    int entier;
    char *str;
}

%token LAMBDA
%token<entier> NOMBRE
%token<str> IDENT

%type<ast_node*> Expr

%start Program

%%

Program: '(' Expr Expr ')'  { total_applications++; }

Expr: LAMBDA'.'IDENT Expr   { $$ = newLdef(newIdtf(strdup($3)), $4); }
     |   '(' Expr Expr ')'  { $$ = newAppl($2, $3);     }
     |   IDENT              { $$ = newIdtf(strdup($1)); }

%%

int main (int argc, char** argv) {
    FILE * fp;
    if (argc > 1) {
        if (! (fp = fopen(argv[1], "r")) ) {
            fprintf(stderr, "Error, can't open %s\n", argv[1]);
            exit(1);
        }
        yyin = fp;
        yyparse();
        fclose(fp);
    } else {
        fprintf(stderr, "Error, no file specified\n");
        exit(1);
    }
    return 0;
}
