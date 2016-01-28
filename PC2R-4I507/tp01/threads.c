#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <unistd.h>

int q1 () {
  int _fork = fork();
  printf("Fork result: %d\n", _fork);
  return 0;
}

/*** Question 3 ***/

int i;

void* THREAD_Routine (void *arg) {
  sleep(4);
  printf("I am the child thread PID=%d\n", getpid());
  printf("My value is i=%d\n", i);
  return NULL;
}

int q3 () {
  pthread_t _thread;
  int ok = pthread_create(&_thread, NULL, THREAD_Routine, NULL);
  if (ok!=0) {
    printf("Well shit. Error.\n");
  }
  printf("I am the father process PID=%d.\n", getpid());
  printf("Give me a value:\n");
  scanf("%d", &i);
  pthread_join(_thread, NULL);
}

int main () {
  q3();
}

