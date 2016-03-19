CC=gcc
CFLAGS=-Wall -g

all: aps

parser: aps.y
	bison -d aps.y

lexer: aps.l
	flex aps.l

aps: lexer parser prolog_gen.c ast.c
	$(CC) $(CFLAGS) -o aps prolog_gen.c ast.c lex.yy.c aps.tab.c -lfl

tests: lexer parser prolog_gen.c ast.c tests.c
	$(CC) $(CFLAGS) -o tests tests.c prolog_gen.c ast.c aps.tab.c lex.yy.c -lfl

clean:
	rm -f *tab.* lex.yy.c aps *~ *.o tests
