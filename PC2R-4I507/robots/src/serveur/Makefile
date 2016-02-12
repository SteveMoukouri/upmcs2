all: server

server: server.ml
	ocamlc -o server -thread -custom unix.cma threads.cma server.ml

clean:
	rm -f *~ *.mli *.cmo server
