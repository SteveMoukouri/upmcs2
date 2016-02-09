#include <stdlib.h>
#include "tools.h"

void destroyFreeQueue(Free_Queue * fq) {
  Free_Queue * tmp;
  while (fq) {
    tmp = fq->next;
    free(fq);
    fq = tmp;
  }
}

unsigned int popEltValue(Free_Queue ** fq) {
  Free_Queue * tmp = *fq;
  Free_Queue * newH = tmp->next;
  unsigned int value = tmp->position;
  *fq = newH;
  free(tmp);
  return value;
}

void pushValue(Free_Queue ** fq, unsigned int value) {
  Free_Queue * newH = malloc(sizeof(*newH));
  newH->next = *fq;
  newH->position = value;
  *fq = newH;
}
