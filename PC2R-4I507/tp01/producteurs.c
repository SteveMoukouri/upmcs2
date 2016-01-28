#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <sched.h>

#define BUF_SIZE 16
#define NB_TURN 5

typedef struct _buffer {
  int qty;
  int contents[BUF_SIZE];
} Buffer;

Buffer* creerBuffer() {
  Buffer * buff = (Buffer *) malloc(sizeof(Buffer));
  buff->qty = 0;
  return buff;
}

void EcrireBuffer(Buffer *b, int val) {
  int cont = 0;
  while (!cont) {
    if (b->qty == BUF_SIZE) 
      printf("P");
    else {
      cont = 1;
    }
  }
  b->contents[b->qty] = val;
  b->qty++;
}

int LireBuffer(Buffer *b) {
  int cont = 0;
  int i, val;
  while (!cont) {
    if ()
      printf("V");
    else {
      cont = 1;
    }
  }
  val = b->contents[0];
  for (i = 1; i < BUF_SIZE; i++)
    b->contents[i-1] = b->contents[i];
  b->qty--;
  return val;
}

void * THREAD_Producteur(void * arg) {
  int i;
  for (i = 0; i < NB_TURN; i++) {
    int val = (int) random() % 100;
    printf("Production %d", val);
    EcrireBuffer((Buffer *) arg, val);
    usleep(5);
  }
}

void * THREAD_Consommateur(void * arg) {
  int i;
  for (i = 0; i < NB_TURN; i++) {
    printf("Consommation %d", LireBuffer((Buffer *) arg));
    usleep(5);
  }
}
