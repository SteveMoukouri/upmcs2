#include "types.h"

/******************************************************************************/
/*            clt_connect: client/server communication management             */
/******************************************************************************/

#define SRV_PORT 2016

ClientSocket::ClientSocket() {
  socket = -1;
  addr = NULL;
  port = -1;
}

ClientSocket::connect(char* addr, int port) {
  
}
