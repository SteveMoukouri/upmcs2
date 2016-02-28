#define W_PLAT 16
#define H_PLAT 16

/******************************************************************************/
/*                            Représentation du jeu                           */
/******************************************************************************/
typedef enum {R, B, J, V} Color;
typedef enum {H, B, G, D} Direction;

typedef struct _serv_wall {
  int x;
  int y;
  char pos;
} Serv_Wall;

typedef struct _walls {
  Serv_Wall * w;
  struct _walls * next;
} Walls;

class Robot {
 private:
  int x;
  int y;
  Color c;
 public:
  Robot(int x, int y);
  move(Direction d);
}

class CasePlat {
 private:
  Robot rbt;
  Color target;
  int x;
  int y;
  int [] walls;
 public:
  CasePlateau(int x, int y, Color r);
  int isTarget();
  void setTarget();
  int hasRobot();
  void setRobot(Color i);
  Robot getRobot();
  int [] getWalls();
};


class Plateau {
 private:
  CasePlat* [][] cases;
  Robot * [] robots;
 public:
  Plateau();
  void setRobots(int [] robots);
  void setWalls(char * walls);
};
