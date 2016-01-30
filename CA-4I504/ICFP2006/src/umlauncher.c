#include <stdio.h>
#include <stdlib.h>
//#include "readscroll.h"
//#include "um.h"

int main (int argc, char ** argv) {
  FILE * fp;
  /*
  if (argc < 2) {
    fprintf(stderr, "Error - no scroll specified\n");
    exit(1);
  }
  else {
    if (!(fp = fopen(argv[1], "rb"))) {
      fprintf(stderr, "Error - unable to open scroll");
      exit(1);
    }
    else {
      UM_code * code = readscroll(fp);
      um_exec(code);
    }
  }
  */
  printf("unsigned int size on x64 - %lu\n", sizeof(unsigned int));
  return 0;
}
