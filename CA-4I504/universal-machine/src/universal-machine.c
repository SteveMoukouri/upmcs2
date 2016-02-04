#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "universal-machine.h"

#define DEBUG 1
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

unsigned int MAX_UINT = (unsigned int) -1;

Um_code * newPlatter(unsigned int size) {
  Um_code * result = malloc(sizeof *result);
  result->code = calloc(size, 4);
  result->int_size = size;
}

void destroyFreeQueue(Free_Queue * fq) {
  Free_Queue * tmp;
  while (fq) {
    tmp = fq->next;
    free(fq);
    fq = tmp;
  }
}

void destroyCode(Um_code * code) {
  if (code) {
    free(code->code);
    free(code);
  }
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
  fread(code, 4, size/4, fp);
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
      registers[WORDA] = (registers[WORDB] + registers[WORDC]) % MAX_UINT;
      break;
    case MUL:
      debug("Multiplication");
      registers[WORDA] = (registers[WORDB] * registers[WORDC]) % MAX_UINT;
      break;
    case DIV:
      debug("Division");
      if (registers[WORDC])
	registers[WORDA] = (registers[WORDB] / registers[WORDC]) % MAX_UINT;
      break;
    case NAND:
      debug("Not-And");
      registers[WORDA] = ~(registers[WORDB] & registers[WORDC]);
      break;
    case HALT:
      debug("Halt");
      /*
      for (unsigned int i = 0; i < allocate_max; i++)
	destroyCode(platters[i]);
      */
      exit(1);
      break;
    case ALLOC:
      debug("Allocation");
      unsigned int size = registers[WORDC];
      unsigned int position = 0;
      Um_code * new = malloc(sizeof *new);
      new->code = calloc(size, 4);
      new->int_size = size;
      for (unsigned int i = 0; i < allocate_max; i++)
	if (platters[i] == NULL) {
	  position = i;
	  break;
	}
      if (position == 0) {
	position = size;
	size = size * 2;
	realloc(platters, size);
      }
      platters[position] = new;
      registers[WORDB] = position;
      break;
    case ABDN:
      debug("Abandonment");
      free(platters[registers[WORDC]]);
      break;
    case OUT:
      debug("Output");
      unsigned char cout = registers[WORDC];
      fprintf(stdout, "%c", cout);
      break;
    case IN:
      debug("Input");
      unsigned char cin;
      scanf("%c", &cin);
      if (cin == EOF)
	registers[WORDC] = 0xFFFFFFFF;
      else
	registers[WORDC] = cin;
      break;
    case LPRO:
      debug("Load program");
      if (registers[WORDB] != 0) {
	Um_code * src = platters[registers[WORDB]];
	Um_code * dest = malloc(sizeof *dest);
	memcpy(dest->code, src->code, src->int_size);
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
    fprintf(stderr, "Error - no scroll specified\n");
    exit(1);
  }
  else {
    if (!(fp = fopen(argv[1], "rb"))) {
      fprintf(stderr, "Error - unable to open scroll");
      exit(1);
    }
    else {
      Um_code * code = readscroll(fp);
      exec(code);
    }
  }
  return 0;
}
