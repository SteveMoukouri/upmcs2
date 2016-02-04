#ifndef __um__h_
#define __um_h_

typedef struct _um_code {
  unsigned int * code;
  unsigned int int_size;
} Um_code;

typedef struct _free_queue {
  struct _free_queue * next;
  unsigned int position;
} Free_Queue;

typedef enum {
  CMV=0, AIND=1, AAMD=2, ADD=3, MUL=4, DIV=5, NAND=6,
  HALT=7, ALLOC=8, ABDN=9, OUT=10, IN=11, LPRO=12, ORTH=13
} Opcode;

#endif
