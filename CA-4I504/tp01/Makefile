CC=gcc
CFLAGS=-Wall

all: lambda

parser: lambda.y
	bison -d lambda.y

lexer: lambda.l
	flex lambda.l

ast: ast.c

lambda: parser lexer ast
	gcc -olambda lex.yy.c lambda.tab.c ast.c -lfl

clean:
	rm -f lambda.tab.* lex.yy.c lambda *~
