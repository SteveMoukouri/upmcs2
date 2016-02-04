#include <stdio.h>
#include <stdlib.h>

int main (int argc, char ** argv) {
  if (argc < 2) {
    exit(1);
  } else {
    FILE * fp = fopen(argv[1], "rb");
    unsigned char c;
    unsigned int i;
    fread(&c, 1, 1, fp);
    fseek(fp, 0, SEEK_SET);
    fprintf(stdout, "%u\n", c);
    fprintf(stdout, "%u\n", i);
  }
  return 0;
}
