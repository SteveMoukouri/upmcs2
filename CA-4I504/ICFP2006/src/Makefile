CC=gcc
CFLAGS=-Wall -std=c11
SRC=umlauncher.c 
EXE=umlauncher

all: umlauncher

%.o: %.c
	${CC} -c $@ $<

umlauncher: umlauncher.o
	${CC} -o $@ $<

clean: 
	rm -f *~ *.o ${EXE}
