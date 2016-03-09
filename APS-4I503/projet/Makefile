all: aps 

parser: aps.y
	bison -d aps.y

lexer: aps.l
	flex aps.l

ast: ast.c ast.h
	gcc -c ast.c

aps: parser lexer ast
	gcc -o aps ast.o lex.yy.c aps.tab.c -lfl

clean:
	rm -f *tab.* lex.yy.c aps *~
