def iterative(array, P):
    for i in buildCombs(array):
        if i == P:
            return True

    return False

def buildCombs(array):
    combs = [0, array[0]]

    for i in range(1,len(array)):
        combs.extend(combs.copy())

        for j in range(int(len(combs) / 2), len(combs)):
            combs[j] += array[i]

    return combs

arr = [2,0,5,7,5,-1,10]
n = 25
print(iterative(arr, n))
