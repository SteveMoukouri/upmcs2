/******************************************************************************/
/*                        Interaction Client/Serveur                          */
/******************************************************************************/

typedef enum {CONNEXION, SORT, REF_SOLUTION, ENCHERE, RES_SOLUTION} Outgoing_Cmd;

typedef enum 
  {
    CONNECT, DECONNEXION, SESSION, VAINQUEUR, TOUR, TUASTROUVE,
    ILATROUVE, FINREFLEXION, VALIDATION, ECHEC, NOUVELLEENCHERE,
    FINENCHERE, SASOLUTION, BONNE, MAUVAISE, FINRESO, TROPLONG
  } Incoming_Cmd;

typedef enum {S_PH_REFLECT, S_PH_ENCH, S_PH_RES} Server_Phase;

class ClientSocket {
 private:
  int socket;
  char* addr;
  int port;
 public:
  ClientSocket();
  void connect(char* addr, int port);
};
