import java.util.*;
import java.io.*;

public class Main {

    public static Matrix loadMatrix(String file, int r, int c) {
        byte[] tmp = new byte[r * c];
        byte[][] data = new byte[r][c];
        try {
            FileInputStream fos = new FileInputStream(file);
            fos.read(tmp);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                data[i][j] = tmp[i * c + j];
        return new Matrix(data);
    }

    public static void main(String[] arg) {

        // byte[][] tab = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
        // Matrix m = new Matrix(tab);
        // m.display();

        // test addition
        // byte[][] tab1 = { { 1, 0, 1 }, { 0, 1, 0 }, { 1, 0, 1 } };
        // Matrix m1 = new Matrix(tab1);
        // byte[][] tab2 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        // Matrix m2 = new Matrix(tab2);
        // m1.add(m2).display();

        // test multiplication
        // byte[][] tab1 = { { 1, 0, 1 }, { 1, 1, 0 } };
        // Matrix m1 = new Matrix(tab1);
        // byte[][] tab2 = { { 0, 1 }, { 1, 1 }, { 1, 0 } };
        // Matrix m2 = new Matrix(tab2);
        // m1.multiply(m2).display();

        // test transpose
        // byte[][] tab1 = { { 1, 0, 1 }, { 1, 1, 0 } };
        // Matrix m1 = new Matrix(tab1);
        // m1.display();
        // m1.transpose().display();

        // check data - OK 2048x6144
        // Matrix check = loadMatrix("data/matrix-2000-6000-5-15", 2048, 6144);
        // System.out.println(check.getRows());
        // System.out.println(check.getCols());

        Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        System.out.println("Matrice de controle H");
        hbase.display();
        // hbase.addCol(1, 0);
        System.out.println("Matrice de controle systematique H'");
        hbase.sysTransform().display();
        System.out.println("Matrice generatrice G");
        hbase.sysTransform().genG().display();

        byte[][] tabU = { { 1, 0, 1, 0, 1 } };
        Matrix u = new Matrix(tabU);
        System.out.println("Le mot u");
        u.display();
        System.out.println("L'encodage x de u");
        u.multiply(hbase.sysTransform().genG()).display();
    }
}
