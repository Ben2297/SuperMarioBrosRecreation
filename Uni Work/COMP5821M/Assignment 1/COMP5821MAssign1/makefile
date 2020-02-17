all: main.o 
	g++ -std=c++11 -o subdivision main.o diredge.o Cartesian3.o io.o

main.o:	io.o diredge.o Cartesian3.o 
	g++ -std=c++11 -c -o main.o main.cpp 

diredge.o:
	g++ -std=c++11 -c -o diredge.o diredge.cpp

Cartesian3.o: 
	g++ -std=c++11 -c -o Cartesian3.o Cartesian3.cpp

io.o: 
	g++ -std=c++11 -c -o io.o io.cpp


clean:
	rm *.o subdivision

