#include <stdio.h> 
#include <stdlib.h>
#include <string.h>
#include "universal-machine.h"
#include "tools.h"

//#define DEBUG 1
#define DEFAULT_PLATTERS 128

#ifdef DEBUG
#define debug(str) fprintf(stderr, "%s regA = %u; regB = %u; regC = %u\n", str, registers[WORDA], registers[WORDB], registers[WORDC]);
#else
#define debug(str) ;
#endif

#define Opcode(word) (word >> 28)
#define WORDA ((current_word >> 6) & 0x07)
#define WORDB ((current_word >> 3) & 0x07)
#define WORDC (current_word & 0x07)

Um_code * newPlatter(unsigned int size) {
  Um_code * result = malloc(sizeof *result);
  result->code = calloc(size, 4);
  result->int_size = size;
}

void destroyCode(Um_code * code) {
  if (code) {
    free(code->code);
    free(code);
  }
}

Um_code ** realloc_and_copy(Um_code ** platters, 
			    unsigned int oldSize, unsigned int newSize) {
  Um_code ** new_platters = malloc(sizeof(*new_platters) * newSize);
  fprintf(stderr, "Begin copy oldSize %u newSize %u\n", oldSize, newSize);
  for (unsigned int i = 0; i < oldSize; i++) {
    fprintf(stderr, "Iteration %u, platter size %u\n", i, platters[i]->int_size);
    if (platters[i] != NULL) {
      fprintf(stderr, "Inside if\n");
      unsigned int code_size = platters[i]->int_size; 
      Um_code * new_code = newPlatter(code_size);
      fprintf(stderr, "Begin ops\n");
      new_code->code = malloc(code_size * sizeof(unsigned int));
      new_code->int_size = code_size;
      fprintf(stderr, "Begin memcpy\n");
      memcpy(new_code->code, platters[i]->code, code_size * 4);
      fprintf(stderr, "Memcpy done\n");
      new_platters[i] = new_code;
      fprintf(stderr, "End ops\n");
    }
    destroyCode(platters[i]);
    free(platters);
  }
  fprintf(stderr, "End copy\n");
  return new_platters;
}

Um_code * readscroll (FILE * fp) {
  int size;
  unsigned int * code;

  // Taille du code
  fseek(fp, 0, SEEK_END);     
  size = ftell(fp);           
  fseek(fp, 0, SEEK_SET);     

  // Allocation
  code = calloc(size/4, 4);
  
  // Read //
  if (fread(code, 4, size/4, fp) != size/4) {
    fprintf(stderr, "\x1b[31mFile error\x1b[0m\n");
    exit(1);
  }
  for (unsigned int i = 0; i < size / 4; i++) {
    code[i] = 
      (code[i] >> 24) |
      ((code[i] & 0x00FF0000) >> 8) |
      ((code[i] & 0x0000FF00) << 8) |
      (code[i] << 24);
  }
  Um_code * result = malloc(sizeof *result);
  result->code = code;
  result->int_size = size;
  return result;
}

void exec(Um_code * code) {
  Um_code ** platters = malloc(sizeof(*platters) * DEFAULT_PLATTERS);
  unsigned int pc = 0;
  platters[0] = code;
  unsigned int current_word;
  unsigned int registers[8] = { 0 };
  unsigned int next_allocate = 1;
  unsigned int allocate_max = DEFAULT_PLATTERS;
  Free_Queue * spaces = NULL;
  for (;;) {
    current_word = platters[0]->code[pc++];
    switch (Opcode(current_word)) {
    case CMV:
      debug("Conditional move");
      if (registers[WORDC] != 0) {
	registers[WORDA] = registers[WORDB];
      }
      break;
    case AIND:
      debug("Array Index");
      registers[WORDA] = platters[registers[WORDB]]->code[registers[WORDC]];
      break;
    case AAMD:
      debug("Array amendment");
      platters[registers[WORDA]]->code[registers[WORDB]] = registers[WORDC];
      break;
    case ADD:
      debug("Addition");
      registers[WORDA] = registers[WORDB] + registers[WORDC];
      break;
    case MUL:
      debug("Multiplication");
      registers[WORDA] = registers[WORDB] * registers[WORDC];
      break;
    case DIV:
      debug("Division");
      if (registers[WORDC])
	registers[WORDA] = registers[WORDB] / registers[WORDC];
      break;
    case NAND:
      debug("Not-And");
      registers[WORDA] = ~(registers[WORDB] & registers[WORDC]);
      break;
    case HALT:
      debug("Halt");
      for (unsigned int i = 0; i < allocate_max; i++)
	destroyCode(platters[i]);      
      exit(1);
      break;
    case ALLOC:
      debug("Allocation");
      unsigned int size = registers[WORDC];
      unsigned int position = 0;
      Um_code * new_platter = malloc(sizeof *new_platter);
      new_platter->code = calloc(size, 4);
      new_platter->int_size = size;
      if (spaces != NULL) {
	position = popEltValue(&spaces);
      } else {
	if (next_allocate >= allocate_max) {
	  position = size;
	  size = size * 2;
	  platters = realloc_and_copy(platters, size/2, size);
	  next_allocate++;
	  /*
	  if (realloc(platters, size) == NULL) {
	    fprintf(stderr, "\x1b[31mMemory reallocation fail\x1b[0m\n");
	    exit(1);
	  }
	  */
	} else {
	  position = next_allocate++;
	}
      }
      platters[position] = new_platter;
      registers[WORDB] = position;
      break;
    case ABDN:
      debug("Abandonment");
      free(platters[registers[WORDC]]);
      platters[registers[WORDC]] = NULL;
      pushValue(&spaces, registers[WORDC]);
      break;
    case OUT:
      debug("Output");
      unsigned char cout = registers[WORDC];
      fprintf(stdout, "%c", cout);
      break;
    case IN:
      debug("Input");
      unsigned char cin;
      int dummy = scanf("%c", &cin);
      if (cin == EOF)
	registers[WORDC] = 0xFFFFFFFF;
      else
	registers[WORDC] = cin;
      break;
    case LPRO:
      // Seg fault @ LPRO 7 7 0
      debug("Load program");
      if (registers[WORDB] != 0) {
	Um_code * src = platters[registers[WORDB]];
	Um_code * dest = malloc(sizeof(*dest));
	dest->code = calloc(src->int_size, 4);
	memcpy(dest->code, src->code, src->int_size * 4);
	dest->int_size = src->int_size;
	destroyCode(platters[0]);
	platters[0] = dest;
      }
      pc = registers[WORDC];
      break;
    case ORTH:
      debug("Orthography");
      unsigned int imd = current_word & 0x01FFFFFF;
      unsigned int a = (current_word >> 25) & 0x07;
      registers[a] = imd;
      break;
    default:
      debug("Unkown operand");
      break;
    }
  }
}

int main (int argc, char ** argv) {
  FILE * fp;
  if (argc < 2) {
    fprintf(stderr, "\x1b[31mNo scroll specified\x1b[0m\n");
    exit(1);
  }
  else {
    if (!(fp = fopen(argv[1], "rb"))) {
      fprintf(stderr, "\x1b[31mUnable to open scroll\x1b[0m");
      exit(1);
    }
    else {
      Um_code * code = readscroll(fp);
      exec(code);
    }
  }
  return 0;
}
