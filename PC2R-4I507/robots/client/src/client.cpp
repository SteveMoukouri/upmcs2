#include <iostream>
#include <sys/types.h>        
#include <sys/socket.h>
#include "client.h" 

#define SERVER_PORT 2016

#define LOCAL

using namespace std;

Socket sock;

void init() {
  /* Socket init */
  sock = socket(PF_INET, SOCK_STREAM, 0);
  if (sock == INVALID_SOCKET) {
    perror("Socket init");
    exit(errno);
  }
  /* Get host info */
}

void listen() {
  
}


int main(int argc, char* argv[]) {
  if (argc < 2) {
    cerr << "Expected hostname and port\n";
    exit(1);
  }
  else {
    cout << argv[1] << "\n";
  }
}
