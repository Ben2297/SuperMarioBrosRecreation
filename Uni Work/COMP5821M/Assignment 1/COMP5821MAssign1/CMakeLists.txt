cmake_minimum_required(VERSION 3.15)
project(COMP5821MAssign1)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(COMP5821MAssign1
        Cartesian3.cpp
        Cartesian3.h
        diredge.cpp
        diredge.h
        io.cpp
        io.h
        main.cpp
        makefile
        README)
