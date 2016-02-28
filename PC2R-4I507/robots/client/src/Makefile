all: client

client: repr.o clt_connect.o
	g++ -o client repr.o clt_connect.o

%.o: %.cpp
	g++ -o $@ $<
