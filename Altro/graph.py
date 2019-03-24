import sys
import time

def graph(matrix):
    sys.stdout.write("\t\t\t" + "--" * len(matrix) + "\n")

    for i in reversed(range(len(matrix))):
        time.sleep(0.25)
        sys.stdout.write('\t\t\t|')
        for j in range(len(matrix[0])):
            if matrix[i][j]:
                sys.stdout.write('* ')
            else:
                sys.stdout.write('  ')
        sys.stdout.write('|')
        sys.stdout.write('\n')

    sys.stdout.write("\t\t\t" + "--" * len(matrix) + "\n")

    sys.stdout.flush()

def getMatrix():
    n = 50
    matrix = [[False for _ in range(n)] for _ in range(n)]

    for x in range(n):
        y1 = function1(x)
        y2 = function2(x)

        y1OK = y1 < n and y1 >= 0
        y2OK = y2 < n and y2 >= 0

        print("(x: " + str(x) + ", y1: " + str(y1) + ")")
        print("(x: " + str(x) + ", y2: " + str(y2) + ")")

        if y1OK:
            matrix[y1][x] = True

        if y2OK:
            matrix[y2][x] = True

    return matrix

def function1(x):
    return x ** 2 - 2*x + 10

def function2(x):
    return x - 2

def printMatrix(matrix):
    print("\n")

    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            sys.stdout.write(str(matrix[i][j]) + " ")
        sys.stdout.write("\n")

    sys.stdout.flush()

# MAIN

graph(getMatrix())
