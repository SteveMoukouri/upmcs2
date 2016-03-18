all: aps 

parser: aps.y
	bison -d aps.y

lexer: aps.l
	flex aps.l

aps: parser lexer ast.c
	gcc -g -o aps ast.c lex.yy.c aps.tab.c -lfl

clean:
	rm -f *tab.* lex.yy.c aps *~ *.o
