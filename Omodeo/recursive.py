import sys

def recursive(array, sum, P, index):
    if index == (len(array) - 1):
        return P == sum or P == (sum + array[index])
    else:
        return recursive(array, sum + array[index], P, index + 1) or recursive(array, sum, P, index + 1)

arr = [2,0,5,7,5,-1,10]
n = 23
print(iterative(arr, n))
