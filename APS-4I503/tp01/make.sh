bison -d aps.y
flex aps.l
gcc -oaps lex.yy.c aps.tab.c -lfl
