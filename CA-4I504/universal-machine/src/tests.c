#include <stdio.h>

int main (int argc, char * argv[]) {
  unsigned int i, j, k;
  i = 0xFFFFFFFF;
  j = i + 1;
  k = i * 2;
  printf("2^32-1: %u\n", i);
  printf("2^32 : %u\n", j);
  printf("(2^32-1) * 2 : %u", k);
  return 0;
}
