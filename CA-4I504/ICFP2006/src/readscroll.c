#include <stdio.h>
#include <stdlib.h>
#include "readscroll.h"

Um_code * readscroll (FILE * file) {
  unsigned long size;
  unsigned char * code;

  // Taille du code
  fseek(fp, 0, SEEK_END);     
  size = ftell(fp);           
  fseek(fp, 0, SEEK_SET);     

  // Allocation
  code = malloc(sizeof(char) * size);
  
  // Read
  fread(code, 
  return NULL;
}
