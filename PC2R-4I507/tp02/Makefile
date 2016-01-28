FTLIB="/usr/local/fairthreads"

%: %.c
	gcc -m32 -Wall -O3 -o $@ -I ${FTLIB}/include -L ${FTLIB}/lib $< -lfthread -lpthread

clean:
	rm -f *~ *.o
