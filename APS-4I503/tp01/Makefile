parser: aps.y
	bison -d aps.y

lexer: aps.l
	flex aps.l

aps: parser lexer
	gcc -oaps lex.yy.c aps.tab.c -lfl

clean:
	rm -f aps.tab.* lex.yy.c aps *~
