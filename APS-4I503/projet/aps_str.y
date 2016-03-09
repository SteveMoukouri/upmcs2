
%error-verbose

%{
  #include <stdlib.h>
  #include <stdio.h>
  #include <string.h>

  extern FILE *yyin;

  int yyerror(const char *message) {
    fprintf(stderr, "%s\n", message);
    return 0;
  }

  char* concat2(char* str1, char* str2) {
      char *result = malloc(strlen(str1) + strlen(str2) + 1);
      strcpy(result, str1);
      strcat(result, str2);
      return result;
  }

  char* concat3(char* str1, char* sep, char* str2) {
    char *result = malloc(strlen(str1) + strlen(sep) + strlen(str2)+1);
    strcpy(result, str1);
    strcat(result, sep);
    strcat(result, str2);
    return result;
  }

  char* concat5(char* str1, char* str2, char* str3, char* str4, char* str5) {
    char *result = malloc(strlen(str1) + strlen(str2) + strlen(str3)
			  + strlen(str4) + strlen(str5)+1);
    strcpy(result, str1);
    strcat(result, str2);
    strcat(result, str3);
    strcat(result, str4);
    strcat(result, str5);
    return result;
  }

  char* concat7(char* str1, char* str2, char* str3, char* str4, char* str5, char* str6, char* str7) {
    char *result = malloc(strlen(str1) + strlen(str2) + strlen(str3)
			  + strlen(str4) + strlen(str5) + strlen(str6)
			  + strlen(str7)+1);
    strcpy(result, str1);
    strcat(result, str2);
    strcat(result, str3);
    strcat(result, str4);
    strcat(result, str5);
    strcat(result, str6);
    strcat(result, str7);
    return result;
  }


  char* application_unary(char* app, char* arg) {
    char *result = malloc(3 + 1 + strlen(app) + 1 + 1 + strlen(arg) + 1 + 1 + 1);
    //                    app (    fonction     ,   [    argument     ]   )   \0
    strcpy(result, "app(");
    strcat(result, app);
    strcat(result, ",[");
    strcat(result, arg);
    strcat(result, "])");
    return result;
  }

  char* application_binary(char* app, char* arg1, char* arg2) {
    char *result = malloc(4 + strlen(app) + 2 + strlen(arg1) + 1 + strlen(arg2) + 3);
    //                    app(  fonction  ,[    argument 1     ,   argument 2   ])\0
    strcpy(result, "app(");
    strcat(result, app);
    strcat(result, ",[");
    strcat(result, arg1);
    strcat(result, ",");
    strcat(result, arg2);
    strcat(result, "])");
    return result;
  }

  char* operator_binary(char* op, char* arg1, char* arg2) {
    char *result = malloc(strlen(op) + 1 + strlen(arg1) + 1 + strlen(arg2) + 2);
    //                    operator     (    argument 1     ,   argument 2   )\0
    strcpy(result, op);
    strcat(result, "(");
    strcat(result, arg1);
    strcat(result, ",");
    strcat(result, arg2);
    strcat(result, ")");
    return result;
  }


  char* itoa(int a) {
    int length = a / 10 + 1;
    char *result = malloc(length * sizeof(char));
    sprintf(result, "%d", a);
    return result;
  }
%}

%union {
  int entier;
  char* str;
}

%token WHILE IF SET VAR CONST
%token TRUE FALSE
%token AND OR NOT
%token EQ LT ADD SUB DIV MUL
%token VOID BOOL INT
%token<entier> NOMBRE
%token<str> IDENT

%type<str> Entry Prog Cmds Dec Stat Expr Type Function InterStat

%start Entry
%%

Entry: '[' Cmds ']'   { char* res = concat3("prog([", strdup($2), "])");
                        printf("Output   -- %s\n", res);
                        $$ = res;
                      }

Prog: '[' Cmds ']'    { $$ = concat3("[", $2, "]");   }

Cmds: Stat InterStat  { if (strcmp($2, "") != 0)
                            $$ = concat3($1, ",", $2);
                        else
                            $$ = strdup($1);
                      }
	| 	Dec ';' Cmds     { $$ = concat3($1, ",", $3) ; }

InterStat:                   { $$ = strdup(""); }
	| ';' 	Cmds         { $$ = strdup($2) ; }

 Dec: VAR IDENT Type                   { $$ = concat5("dec(VAR,", $2, ",", $3, ")"); }
	| 	CONST IDENT Type Expr  { $$ = concat7("dec(CONST,", $2, ",", $3, ",", $4, ")"); }

Stat: SET IDENT Expr               { $$ = operator_binary("set", $2, $3); }
	| 	IF Expr Prog Prog  { $$ = concat7("if(", $2, ",", $3, ",", $4, ")"); }
	| 	WHILE Expr Prog    { $$ = operator_binary("while", $2, $3); }

Expr: TRUE                         { $$ = strdup("true");  }
	| 	FALSE              { $$ = strdup("false"); }
	| 	NOMBRE             { $$ = strdup(itoa($1)); }
	| 	IDENT              { $$ = strdup($1); }
	| '(' 	Function ')'   { $$ = strdup($2); }

Function: NOT Expr                { $$ = application_unary("not", $2);      }
	|  	OR Expr Expr      { $$ = application_binary("or", $2, $3);  }
	|  	AND Expr Expr     { $$ = application_binary("and", $2, $3); }
	|  	EQ Expr Expr      { $$ = application_binary("eq", $2, $3);  }
	|  	LT Expr Expr      { $$ = application_binary("lt", $2, $3);  }
	|  	ADD Expr Expr     { $$ = application_binary("add", $2, $3); }
	|  	SUB Expr Expr     { $$ = application_binary("sub", $2, $3); }
	|  	DIV Expr Expr     { $$ = application_binary("div", $2, $3); }
	|  	MUL Expr Expr     { $$ = application_binary("mul", $2, $3); }

Type: VOID               { $$ = strdup("void"); }
	| 	BOOL     { $$ = strdup("bool"); }
	| 	INT      { $$ = strdup("int"); }


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
