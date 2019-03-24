primes = []

def isPrime(n):
    if n % 2 == 0:
        return False
 
    for p in primes:
        if p >= n/2:
            return True

        if n % p == 0:
            return False

    return True

for i in range(3,100,2):
    if isPrime(i):
        primes.append(i)
        print(i)
