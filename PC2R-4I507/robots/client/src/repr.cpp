#include <cstdio>
#include <types.h>

/******************************************************************************/
/*                    repr: Client-side game representation                   */
/******************************************************************************/

/* CasePlateau class definition */

CasePlat::CasePlat(int cx, int cy, Robot cr) {
  x = cx;
  y = cy;
  rbt = cr;
}

/* Plateau class definition */
Plateau::Plateau() {
  cases = CasePlat* [H_PLAT][W_PLAT];
}

void Plateau::setRobots(int [] robots) {
  for (int i = 0; i < 4; i++)
    cases[robots[i]][robots[i+1]]->setRobot(i);
  cases[robots[7]][robots[8]]->setTarget(robots[9]);
}

void Plateau::setWalls(Walls * walls) {
  while (walls) {
    int x = walls->x;
    int y = walls->y;
    switch (walls->pos) {
    case 'H':
      break;
    case 'B':
      break;
    case 'G':
      break;
    case 'D':
      break;
    default:
      cerr << "Unkown wall position";
    }
  }
}
