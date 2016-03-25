%{
#include <string.h>
#include <stdlib.h>
#include "aps.tab.h"
%}

ident           [a-z]+
nombre 		[0-9]+

%%

"True"    {  return TOKEN_TRUE;  }
"False"   {  return TOKEN_FALSE; }

"not"     {  return TOKEN_NOT;   }
"and"     {  return TOKEN_AND;   }
"or"      {  return TOKEN_OR;    }

"eq"      {  return TOKEN_EQ;  }
"lt"      {  return TOKEN_LT;  }

"add"     {  return TOKEN_ADD; }
"sub"     {  return TOKEN_SUB; }
"div"     {  return TOKEN_DIV; }
"mul"     {  return TOKEN_MUL; }
"mod"     {  return TOKEN_MOD; }

"void"    {  return TOKEN_VOID; }
"bool"    {  return TOKEN_BOOL; }
"int"     {  return TOKEN_INT;  }

{ident}     { yylval.str    = strdup(yytext);  return TOKEN_IDENT; }
{nombre}    { yylval.entier = atoi(yytext);   return TOKEN_NOMBRE; }

"WHILE"   {  return TOKEN_WHILE; }
"SET"     {  return TOKEN_SET;   }
"IF"      {  return TOKEN_IF;    }
"ECHO"    {  return TOKEN_ECHO;  }

"VAR"     {  return TOKEN_VAR;   }
"CONST"   {  return TOKEN_CONST; }

[ \n\t]   { } /* caractères d'espacement ignorés */

.         {  return yytext[0]; } /* les '[' et ']' renvoyés ici */

%%
