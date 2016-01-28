/******************************* automate.canevas ****************************/

#include "fthread.h"
#include "stdio.h"
#include "unistd.h"
// #include "traceinstantsf.h"

ft_event_t  event1, event2;

DEFINE_AUTOMATON (autom)
{
  void **args = ARGS;
   
  ft_event_t   event   = args[0]
    ft_thread_t  thread1 = args[1]
    ft_thread_t  thread2 = args[2]
   
    BEGIN_AUTOMATON
     
    STATE (0)
    {
      printf("Begin\n");
      GOTO(1)
    }
  STATE_AWAIT (1, event1)
    {
      printf("Hello ");
      GOTO(2);
    }
  STATE_AWAIT (2, event2)
    {
      printf("World!\n");
      GOTO(1);
    }
  
  END_AUTOMATON
  }

void generator (void *args)
{
  int i;

  for (i=0; i < 15; ++i) {
    ft_thread_generate(event1);
    ft_thread_cooperate();
    ft_thread_generate(event2);
    ft_thread_cooperate();
  }
}


int main ()
{
  ft_scheduler_t sched = ft_scheduler_create();
  event1 = ft_event_create(sched);
  event2 = ft_event_create(sched);

  ft_thread_create(sched, traceinstants, NULL, (void *)15);
  if (NULL == ft_automaton_create(sched, autom, NULL, NULL)) {
    fprintf(stdout, "Cannot create automaton!!!\n");
  }

  ft_thread_create(sched, generator, NULL, NULL);

  ft_scheduler_start(sched);
  

  ft_exit();
  return 0;
}
