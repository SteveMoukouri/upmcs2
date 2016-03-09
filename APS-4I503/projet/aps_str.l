%{
  #include <string.h>
  #include <stdlib.h>
  #include "apsv2.tab.h"
%}

ident  [a-z][a-zA-Z0-9_]*
nombre [0-9]+

%%

"True"    {  return TRUE;  }
"False"   {  return FALSE; }

"not"     {  return NOT;   }
"and"     {  return AND;   }
"or"      {  return OR;    }

"eq"      {  return EQ;  }
"lt"      {  return LT;  }

"add"     {  return ADD; }
"sub"     {  return SUB; }
"div"     {  return DIV; }
"mul"     {  return MUL; }

"void"    {  return VOID; }
"bool"    {  return BOOL; }
"int"     {  return INT;  }

{ident}     { yylval.str    = strdup(yytext);  return IDENT; }
{nombre}    { yylval.entier = atoi(yytext);  return NOMBRE;  }

"WHILE"   {  return WHILE; }
"SET"     {  return SET;   }
"IF"      {  return IF;    }

"VAR"     {  return VAR;   }
"CONST"   {  return CONST; }

[ \n\t]   { } /* caractères d'espacement ignorés */

.         {  return yytext[0]; } /* les '[' et ']' renvoyés ici */

%%
