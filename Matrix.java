import java.util.*;
import java.io.*;

public class Matrix {
    private byte[][] data = null;
    private int rows = 0, cols = 0;

    public Matrix(int r, int c) {
        data = new byte[r][c];
        rows = r;
        cols = c;
    }

    public Matrix(byte[][] tab) {
        rows = tab.length;
        cols = tab[0].length;
        data = new byte[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                data[i][j] = tab[i][j];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public byte getElem(int i, int j) {
        return data[i][j];
    }

    public void setElem(int i, int j, byte b) {
        data[i][j] = b;
    }

    public boolean isEqualTo(Matrix m) {
        if ((rows != m.rows) || (cols != m.cols))
            return false;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (data[i][j] != m.data[i][j])
                    return false;
        return true;
    }

    public void shiftRow(int a, int b) {
        byte tmp = 0;
        for (int i = 0; i < cols; i++) {
            tmp = data[a][i];
            data[a][i] = data[b][i];
            data[b][i] = tmp;
        }
    }

    public void shiftCol(int a, int b) {
        byte tmp = 0;
        for (int i = 0; i < rows; i++) {
            tmp = data[i][a];
            data[i][a] = data[i][b];
            data[i][b] = tmp;
        }
    }

    public void display() {
        System.out.print("[");
        for (int i = 0; i < rows; i++) {
            if (i != 0) {
                System.out.print(" ");
            }

            System.out.print("[");

            for (int j = 0; j < cols; j++) {
                System.out.printf("%d", data[i][j]);

                if (j != cols - 1) {
                    System.out.print(" ");
                }
            }

            System.out.print("]");

            if (i == rows - 1) {
                System.out.print("]");
            }

            System.out.println();
        }
        System.out.println();
    }

    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[j][i] = data[i][j];

        return result;
    }

    public Matrix add(Matrix m) {
        Matrix r = new Matrix(rows, m.cols);

        if ((m.rows != rows) || (m.cols != cols))
            System.out.printf("Erreur d'addition\n");

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                r.data[i][j] = (byte) ((data[i][j] + m.data[i][j]) % 2);
        return r;
    }

    public Matrix multiply(Matrix m) {
        Matrix r = new Matrix(rows, m.cols);

        if (m.rows != cols)
            System.out.printf("Erreur de multiplication\n");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                r.data[i][j] = 0;
                for (int k = 0; k < cols; k++) {
                    r.data[i][j] = (byte) ((r.data[i][j] + data[i][k] * m.data[k][j]) % 2);
                }
            }
        }

        return r;
    }

    public void addRow(int a, int b) {
        if (a >= rows || b >= rows) {
            System.out.printf("Erreur de l'addition de ligne\n");
            return;
        }
        for (int i = 0; i < cols; i++) {
            data[b][i] = (byte) ((data[b][i] + data[a][i]) % 2);
        }
    }

    public void addCol(int a, int b) {
        if (a >= cols || b >= cols) {
            System.out.println("Erreur de l'addition de colonne");
            return;
        }
        for (int i = 0; i < rows; i++) {
            data[i][b] = (byte) ((data[i][b] + data[i][a]) % 2);
        }
    }

    public Matrix sysTransform() {
        Matrix res = new Matrix(data);
        // for each place of 1 in row i of Id in H=[L|Id]
        for (int i = 0; i < res.rows; i++) {
            // if the place [i][cols - rows + i] is not 1
            if (res.data[i][res.cols - res.rows + i] == 0) {
                // find the first row below that has 1 in this column
                for (int j = i + 1; j < res.rows; j++) {
                    if (res.data[j][res.cols - res.rows + i] == 1) {
                        // permutate these rows
                        res.shiftRow(i, j);
                        break;
                    }
                }
            }
            // now the place of Id has the 1
            // we then clear all the 1 in the column below this row
            // for every row below this row
            for (int j = i + 1; j < res.rows; j++) {
                // if the place has 1
                if (res.data[j][res.cols - res.rows + i] == 1) {
                    // add the ith row into this row
                    res.addRow(i, j);
                }
            }
        }
        // now on the right an upper triangle matrix is formed
        // we now clear all the 1 of the upper part of triangle
        // for each 1 on the diagonal
        for (int i = res.rows - 1; i >= 0; i--) {
            // for each row above this row
            for (int j = i - 1; j >= 0; j--) {
                // if the place is 1
                if (res.data[j][res.cols - res.rows + i] == 1) {
                    // add the ith row into this row
                    res.addRow(i, j);
                }
            }
        }
        return res;
    }

    public Matrix genG() {
        // H = [M|Id]
        byte[][] tabM = new byte[rows][cols - rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols - rows; j++) {
                tabM[i][j] = data[i][j];
            }
        }
        // Mt
        Matrix Mt = new Matrix(tabM).transpose();
        // G = [Id|Mt]
        byte[][] tabG = new byte[Mt.rows][Mt.cols + Mt.rows];
        for (int i = 0; i < Mt.rows; i++) {
            for (int j = 0; j < Mt.rows; j++) {
                if (i == j) {
                    tabG[i][j] = 1;
                } else {
                    tabG[i][j] = 0;
                }
            }
            for (int j = 0; j < Mt.cols; j++) {
                tabG[i][Mt.rows + j] = Mt.data[i][j];
            }
        }
        Matrix G = new Matrix(tabG);
        return G;
    }
}
