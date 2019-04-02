import random
import sys
import math

def rand_list(n, top):
    return [random.randint(1,top) for _ in range(n)]

def rand_matrix(m, n, top):
    return [rand_list(n, top) for _ in range(m)]

# the higher p, the higher the number of 1s
def rand_bitmap(m, n, p):
    return [[True if random.randint(0,p) % p != 0 else False for _ in range(n)] for _ in range(m)]

def revert_bitmap_matrix(bitmap):
    for i in range(len(bitmap)):
        for j in range(len(bitmap[i])):
            bitmap[i][j] = not bitmap[i][j]

def print_matrix(m):
    longest_value = len(str(max_in_matrix(m)))
    longest_column_index = len(str(len(m[0])))
    longest_row_index = len(str(len(m)))

    mx = max(longest_column_index, longest_value)

    sys.stdout.write("  " + " " * longest_row_index)
    for i in range(len(m[0])):
        sys.stdout.write(" " + " " * math.ceil((mx - len(str(i))) / 2) + str(i) + " " * math.floor((mx - len(str(i))) / 2) + " ")
    sys.stdout.write("\n")

    sys.stdout.write("  " + " " * longest_row_index)
    for i in range(len(m[0])):
        sys.stdout.write("-" * (mx + 2))
    sys.stdout.write("\n")

    for i in range(len(m)):
        sys.stdout.write(str(i) + " " * (longest_row_index - len(str(i))) + " |")
        for j in range(len(m[i])):
            sys.stdout.write(" " + " " * (mx - len(str(m[i][j]))) + str(m[i][j]) + " ")
        sys.stdout.write("|\n")

    sys.stdout.write("  " + " " * longest_row_index)
    for i in range(len(m[0])):
        sys.stdout.write("-" * (mx + 2))
    sys.stdout.write("\n")

    sys.stdout.flush()

def print_bitmap_matrix(bitmap):
    print_matrix([[1 if bitmap[i][j] else 0 for j in range(len(bitmap[i]))] for i in range(len(bitmap))])

def max_in_matrix(m):
    return max([max(m[i]) for i in range(len(m))])
