# genera una sottomatrice di lunghezza n-1
def subMatrix(matrix, skipRow, skipColumn):
    copy = [[j for j in i] for i in matrix]
    del copy[skipRow]
    for i in range(len(copy)):
        del copy[i][skipColumn]
    return copy

def determinante(matrix):
    if len(matrix) == 1:
        return matrix[0][0]
    else:
        det = 0
        for i in range(len(matrix)):
            det += (-1)**i * matrix[i][0] * determinante(subMatrix(matrix, i, 0))
        return det

# Main
matrix = [[1,2,3],[6,7,8],[0,2,5]]
print(determinante(matrix))
