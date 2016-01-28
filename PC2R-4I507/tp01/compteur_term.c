#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>
#include <sched.h>

#define NB_THREADS 5

int SHARED_compteur = 0;

void* THREAD_Routine (void *arg) {
  int temp = SHARED_compteur;
  printf("Compteur: %d", temp);
  sched_yield();
  temp++;
  sched_yield();
  SHARED_compteur++;
  return NULL;
}

int main () {
  int i;
  pthread_t _threads[NB_THREADS];
  for (i = 0; i < NB_THREADS; i++) {
    int ok = pthread_create(&_threads[i], NULL, THREAD_Routine, NULL);
    if (ok!=0) {
      printf("Well shit. Error.\n");
    }
  }
  for (i = 0; i < NB_THREADS; i++) {
    pthread_join(_threads[i], NULL);
  }
  printf("TERMINE\n");
  return 0;
}
