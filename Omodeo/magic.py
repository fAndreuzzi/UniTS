import math
import sys

def bidimensional(array):
    n = math.sqrt(len(array))
    if not isInteger(n):
        return False

    n = int(n)

    toreturn = [[0] * n for i in range(n)]

    for j in range(n):
        for i in range(n):
            print(j * n + i)
            toreturn[j][i] = array[j * n + i]

    return toreturn

def isInteger(n):
    return n == int(n)

def checkCondition(array):
    if not is2DList(array):
        array = bidimensional(array)

    bitmap = [False] * len(array) * len(array[0])

    minValue = minimum(array)
    maxValue = len(array) * len(array[0])

    for i in range(len(array)):
        for j in range(len(array[i])):
            array[i][j] -= minValue

            if array[i][j] >= maxValue:
                return False
            if bitmap[array[i][j]]:
                return False

            bitmap[array[i][j]] = True

    return True

def minimum(array):
    minValue = sys.maxsize

    for i in range(len(array)):
        for j in range(len(array[i])):
            minValue = min(minValue, array[i][j])

    return minValue

def is2DList(matrix_list):
  if isinstance(matrix_list[0], list):
      return True
  else:
      return False

def isMagic(array):
    if not is2DList(array):
        return False

    if not checkCondition(array):
        return False

    transposed = tranpose(array)

    sum = 0
    for i in range(len(array)):
        sum += array[i][0]

    print("checking for sum: " + str(sum))

    for i in range(len(array)):
        if not checkColumn(array[i], sum, len(array)) or not checkColumn(transposed[i], sum, len(transposed)):
            return False

    return True

def checkColumn(column, sum, index):
    if index == 1:
        return sum == column[0]
    else:
        index -= 1
        return checkColumn(column, sum - column[index], index)

def tranpose(array):
    if not is2DList(array):
        return None

    tranposed = [[0 for _ in range(len(array[0]))] for _ in range(len(array))]

    for i in range(len(tranposed)):
        for j in range(len(tranposed[i])):
            tranposed[i][j] = array[j][i]

    printMatrix(array)
    printMatrix(tranposed)

    return tranposed

def printMatrix(matrix):
    if not is2DList(matrix):
        return None

    print("\n")

    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            sys.stdout.write(str(matrix[i][j]) + " ")
        sys.stdout.write("\n")

    sys.stdout.flush()


# MAIN

arr = [[16,3,2,13],[5,10,11,8],[9,6,7,12],[4,15,14,1]]
#arr = [[1,8,3],[6,4,2],[5,0,7]]
print(isMagic(arr))
