#include <stdio.h>
#include <stdlib.h>
#include "prolog_gen.h"
#include "aps.tab.h"
#include "ast.h"
#include "aps.h"

#define PL_PATH "tmptypes.pl"

int main(int argc, char* argv[]) {
  FILE *fp;
  if (argc > 1) {
    // Open program
    if (! (fp = fopen(argv[1], "r"))) {
      fprintf(stderr, "Error, can't open %s\n", argv[1]);
      exit(1);
    }
    // AST!
    AST* prog = ast_from_program(fp);
    // Typing!
    FILE* typefp;
    if (! (typefp = fopen(PL_PATH, "w"))) {
        fprintf(stderr, "Error, can't write prolog typing file %s\n", PL_PATH);
	exit(EXIT_FAILURE);
    }
    print_prog(typefp, prog);
    fprintf(stderr, "Finished");
    fclose(typefp);
  } 
  else {
    fprintf(stderr, "Error, no file specified\n");
    exit(1);
  }
  return 0;
}
