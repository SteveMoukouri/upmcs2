%{
  #include <string.h>
  #include <stdlib.h>
  #include "lambda.tab.h"
%}

valeur ([0-9]+)|([a-z][a-zA-Z0-9_]*)

%%

"lambda"       { return LAMBDA; }
{valeur}       { return IDENT; }
[ \n\t]        { }
.              { return yytext[0]; }

%%
