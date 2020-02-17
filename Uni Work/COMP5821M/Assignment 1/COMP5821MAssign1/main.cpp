#include <vector>
#include <cmath>
#include <iostream>

#include "io.h"
#include "diredge.h"
#include "Cartesian3.h"

using namespace std;
using namespace diredge;


int main(int argc, char **argv)
	{ // main()
	if (argc != 4)
		{ // wrong # of arguments
		std::cout << "Usage: %s infile outfile iterations" << std::endl;
		return 0;
		} // wrong # of arguments
	
    // Set up input parameters
    string inputFilename = argv[1];
    string outputFilename = argv[2];
    int maxIterations = atoi(argv[3]);

	// Read in the triangle soup to a vector for processing
    vector<Cartesian3> soup = io::readTriangleSoup(inputFilename);

	// Create mesh from the triangle soup
    diredge::diredgeMesh mesh = diredge::createMesh(soup);

	// write the triangle soup back out
    io::writeTriangleSoup(argv[2], soup);

    return 0;
	} // main()
