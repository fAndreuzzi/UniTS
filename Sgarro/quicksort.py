import random

# r è un indice VALIDO
def partition(lz, p, r):
    i = p-1
    for j in range(p,r):
        if lz[j] <= lz[r]:
            i += 1
            lz[i], lz[j] = lz[j], lz[i]
    q = i+1
    lz[q], lz[r] = lz[r], lz[q]
    return q

def quicksort(lz, p, r):
    if p < r:
        q = partition(lz, p, r)
        print('prt:')
        print(lz[p:r])
        print('q: %d' % q)
        quicksort(lz, p, q-1)
        quicksort(lz, q+1, r)

size = 1000
top = 1001
ls = [random.randint(0,top) for _ in range(size)]

print(ls)
quicksort(ls, 0, len(ls)-1)
print(ls)
