def iterative(array, P):
    perms = buildPermutations(array)

    for i in perms:
        if sum(i) == P:
            return i

    return None

def buildPermutations(array):
    perm = [[array[0]],[]]

    for i in range(1,len(array)):
        perm.extend(copy(perm))

        for j in range(int(len(perm) / 2), len(perm)):
            if array[i] != 0:
                perm[j].append(array[i])

    return perm

def copy(array):
    return [array[i].copy() for i in range(len(array))]

def sum(array):
    s = 0
    for i in range(0,len(array)):
        s += array[i]

    return s

arr = [2,0,5,7,5,-1,10]
n = 7
print(iterative(arr, n))
