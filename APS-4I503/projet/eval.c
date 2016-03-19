#include <stdio.h>
#include <stdlib.h>
#include "prolog_gen.h"
#include "types.h"
#include "ast.h"

#define TYPEFILE "tmptype.str"
#define TYPEPATH "typechecker.pl"

int main(int argc, char** argv) {
  if (argc < 2) {
    fprintf(stderr, "No file specified\n");
    exit(EXIT_FAILURE);
  }
  else {
    AST* programAST;
    // Parse it!
    programAST = yyparse();
    // Typecheck it!
    FILE* typefp;
    if (!(typefp = fopen(TYPEFILE, "rw"))) {
      fprintf(stderr, "Unable to obtain temp file for type checking\n");
      exit(EXIT_FAILURE);
    }
    else {
      print_prog(typefp, programAST);
      fflush(typefp);
      popen('/usr/bin/swipl', 'rw');
      
    }
    // Evaluate it!
  }
}
