#ifndef __tools_h_
#define __tools_h_

typedef struct _free_queue {
  struct _free_queue * next;
  unsigned int position;
} Free_Queue;

unsigned int popEltValue(Free_Queue ** fq);
void pushValue(Free_Queue ** fq, unsigned int value);
void destroyFreeQueue(Free_Queue * fq);

#endif
