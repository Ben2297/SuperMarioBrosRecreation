#include "./io.h"
#include <fstream>
#include <iostream>
#include <iomanip>

using namespace std;
using namespace diredge;

std::vector<Cartesian3> io::readTriangleSoup(string filename)
{
    long nVertices = 0;
    std::vector<Cartesian3> raw_vertices;

    std::ifstream fin;
    fin.open(filename);

    if (true == fin.is_open())
    {
        fin >> nVertices;

        raw_vertices.resize(3*nVertices);
        for (long vertex = 0; vertex < 3*nVertices; vertex++)
        {
            fin >> raw_vertices[vertex].x >> raw_vertices[vertex].y >> raw_vertices[vertex].z;
        }

    }

    fin.close();

    // Array is empty if file is not read.
    return raw_vertices;
}

bool io::writeTriangleSoup(string filename, std::vector<Cartesian3> soup)
{
    std::ofstream fout;
    fout.open(filename);

    if (true == fout.is_open())
    {
        fout << soup.size()/3 << std::endl;

        for (auto position : soup)
        {
            fout << 	std::setw(10) << std::setprecision(5) << position.x << " " <<
						std::setw(10) << std::setprecision(5) << position.y << " " <<
						std::setw(10) << std::setprecision(5) << position.z << std::endl;
        }
    }

    fout.close();

    return fout.is_open();
}
