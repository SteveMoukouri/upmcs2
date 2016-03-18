CC=gcc
CFLAGS=-Wall -g

all: test 

parser: aps.y
	bison -d aps.y

lexer: aps.l
	flex aps.l

aps: lexer parser ast.c
	$(CC) $(CFLAGS) -o aps ast.c lex.yy.c aps.tab.c -lfl

test: aps
	./aps exemple.aps

clean:
	rm -f *tab.* lex.yy.c aps *~ *.o
