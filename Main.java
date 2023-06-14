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

        // check data - not correct size
        // Matrix check = loadMatrix("data/matrix-2000-6000-5-15", 2048, 6144);
        // System.out.println(check.getRows());
        // System.out.println(check.getCols());
        // check.display();

        // Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        // System.out.println("Matrice de controle H");
        // hbase.display();
        // // hbase.addCol(1, 0);
        // System.out.println("Matrice de controle systematique H'");
        // hbase.sysTransform().display();
        // System.out.println("Matrice generatrice G");
        // hbase.sysTransform().genG().display();

        // byte[][] tabU = { { 1, 0, 1, 0, 1 } };
        // Matrix u = new Matrix(tabU);
        // System.out.println("Le mot u");
        // u.display();
        // System.out.println("L'encodage x de u");
        // Matrix x = u.multiply(hbase.sysTransform().genG());
        // x.display();

        // System.out.println("Le graphe de Tanner de H");
        // TGraph tGraph = new TGraph(hbase, 3, 4);
        // tGraph.display();

        // byte[][] tabe1 = { { 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 } };
        // Matrix e1 = new Matrix(tabe1);
        // Matrix y1 = x.add(e1);
        // System.out.println("Le mot y1");
        // y1.display();
        // System.out.println("Le mot y1 corrigé");
        // tGraph.decode(y1, 100).display();
        // System.out.println("Le mot x original");
        // x.display();

        // byte[][] tabe2 = { { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        // 0 } };
        // Matrix e2 = new Matrix(tabe2);
        // Matrix y2 = x.add(e2);
        // System.out.println("Le mot y2");
        // y2.display();
        // System.out.println("Le mot y2 corrigé");
        // tGraph.decode(y2, 100).display();
        // System.out.println("Le mot x original");
        // x.display();

        // byte[][] tabe3 = { { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
        // 0 } };
        // Matrix e3 = new Matrix(tabe3);
        // Matrix y3 = x.add(e3);
        // System.out.println("Le mot y3");
        // y3.display();
        // System.out.println("Le mot y3 corrigé");
        // tGraph.decode(y3, 100).display();
        // System.out.println("Le mot x original");
        // x.display();

        // byte[][] tabe4 = { { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
        // 0 } };
        // Matrix e4 = new Matrix(tabe4);
        // Matrix y4 = x.add(e4);
        // System.out.println("Le mot y4");
        // y4.display();
        // System.out.println("Le mot y4 corrigé");
        // tGraph.decode(y4, 100).display();
        // System.out.println("Le mot x original");
        // x.display();

        Matrix H = loadMatrix("data/Matrix-2048-6144-5-15", 2048, 6144);
        Matrix G = H.sysTransform().genG();
        byte[][] tabu = new byte[1][4096];
        // L'indice de matrice commence de 0 ou de 1?
        for (int i = 0; i < 4096; i++) {
            if (i % 2 == 0) {
                tabu[0][i] = 1;
            } else {
                tabu[0][i] = 0;
            }
        }
        Matrix u = new Matrix(tabu);
        Matrix x = u.multiply(G);
        TGraph tannerGraph = new TGraph(H, 5, 15);

        int w, cntFalse, cntError, rounds = 200;
        w = 124;
        cntFalse = cntError = 0;
        for (int iter = 0; iter < 10000; iter++) {
            // System.out.printf("Iteration #%d\n", iter + 1);
            Matrix e = x.errGen(w);
            Matrix y = x.add(e);
            y = tannerGraph.decode(y, rounds);
            if (y.getElem(0, 0) == -1) {
                cntError++;
            } else if (!y.isEqualTo(x)) {
                cntFalse++;
            }
        }
        System.out.printf("Pour w = %d,\n", w);
        System.out.printf("%.2f", (float) ((cntError + cntFalse) / 100));
        System.out.println("% de cas critiques dont:");
        System.out.printf("\t- %.2f", (float) (cntError / 100));
        System.out.println("% d'echecs");
        System.out.printf("\t- %.2f", (float) (cntFalse / 100));
        System.out.println("% d'erreurs");

        w = 134;
        cntFalse = cntError = 0;
        for (int iter = 0; iter < 10000; iter++) {
            // System.out.printf("Iteration #%d\n", iter + 1);
            Matrix e = x.errGen(w);
            Matrix y = x.add(e);
            y = tannerGraph.decode(y, rounds);
            if (y.getElem(0, 0) == -1) {
                cntError++;
            } else if (!y.isEqualTo(x)) {
                cntFalse++;
            }
        }
        System.out.printf("Pour w = %d,\n", w);
        System.out.printf("%.2f", (float) ((cntError + cntFalse) / 100));
        System.out.println("% de cas critiques dont:");
        System.out.printf("\t- %.2f", (float) (cntError / 100));
        System.out.println("% d'echecs");
        System.out.printf("\t- %.2f", (float) (cntFalse / 100));
        System.out.println("% d'erreurs");

        w = 144;
        cntFalse = cntError = 0;
        for (int iter = 0; iter < 10000; iter++) {
            // System.out.printf("Iteration #%d\n", iter + 1);
            Matrix e = x.errGen(w);
            Matrix y = x.add(e);
            y = tannerGraph.decode(y, rounds);
            if (y.getElem(0, 0) == -1) {
                cntError++;
            } else if (!y.isEqualTo(x)) {
                cntFalse++;
            }
        }
        System.out.printf("Pour w = %d,\n", w);
        System.out.printf("%.2f", (float) ((cntError + cntFalse) / 100));
        System.out.println("% de cas critiques dont:");
        System.out.printf("\t- %.2f", (float) (cntError / 100));
        System.out.println("% d'echecs");
        System.out.printf("\t- %.2f", (float) (cntFalse / 100));
        System.out.println("% d'erreurs");

        w = 154;
        cntFalse = cntError = 0;
        for (int iter = 0; iter < 10000; iter++) {
            // System.out.printf("Iteration #%d\n", iter + 1);
            Matrix e = x.errGen(w);
            Matrix y = x.add(e);
            y = tannerGraph.decode(y, rounds);
            if (y.getElem(0, 0) == -1) {
                cntError++;
            } else if (!y.isEqualTo(x)) {
                cntFalse++;
            }
        }
        System.out.printf("Pour w = %d,\n", w);
        System.out.printf("%.2f", ((float) (cntError + cntFalse) / 100.0));
        System.out.println("% de cas critiques dont:");
        System.out.printf("\t- %.2f", ((float) cntError / 100.0));
        System.out.println("% d'echecs");
        System.out.printf("\t- %.2f", ((float) cntFalse / 100.0));
        System.out.println("% d'erreurs");
    }
}
