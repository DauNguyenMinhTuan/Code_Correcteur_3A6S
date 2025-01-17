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

        // test Matrix
        System.out.println("Test matrice");
        byte[][] tab = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
        Matrix m = new Matrix(tab);
        m.display();

        System.out.println("--------------------------------------------------------------\n");

        // test addition
        System.out.println("Test addition");
        byte[][] tab1 = { { 1, 0, 1 }, { 0, 1, 0 }, { 1, 0, 1 } };
        Matrix m1 = new Matrix(tab1);
        byte[][] tab2 = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        Matrix m2 = new Matrix(tab2);
        System.out.println("Matrice m1");
        m1.display();
        System.out.println("Matrice m2");
        m2.display();
        System.out.println("Résultat de m1 + m2");
        m1.add(m2).display();

        System.out.println("--------------------------------------------------------------\n");

        // test multiplication
        System.out.println("Test multiplication");
        byte[][] tab3 = { { 1, 0, 1 }, { 1, 1, 0 } };
        Matrix m3 = new Matrix(tab3);
        byte[][] tab4 = { { 0, 1 }, { 1, 1 }, { 1, 0 } };
        Matrix m4 = new Matrix(tab4);
        System.out.println("Matrice m3");
        m3.display();
        System.out.println("Matrice m4");
        m4.display();
        System.out.println("Résultat de m3 x m4");
        m3.multiply(m4).display();

        System.out.println("--------------------------------------------------------------\n");

        // test transpose
        byte[][] tab5 = { { 1, 0, 1 }, { 1, 1, 0 } };
        Matrix m5 = new Matrix(tab5);
        System.out.println("Test transpose");
        System.out.println("Matrice m5");
        m5.display();
        System.out.println("Matrice m5 transposé");
        m5.transpose().display();

        // check data - not correct size
        // Matrix check = loadMatrix("data/matrix-2000-6000-5-15", 2048, 6144);
        // System.out.println(check.getRows());
        // System.out.println(check.getCols());
        // check.display();

        System.out.println("--------------------------------------------------------------");
        System.out.println("                           1e Tâche                           ");
        System.out.println("--------------------------------------------------------------\n");

        // 1e tache
        // test sur petit matrice
        Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        System.out.println("Matrice de controle H");
        hbase.display();
        System.out.println("Matrice de controle systematique H'");
        hbase.sysTransform().display();
        System.out.println("Matrice generatrice G");
        hbase.sysTransform().genG().display();

        byte[][] tabU = { { 1, 0, 1, 0, 1 } };
        Matrix U = new Matrix(tabU);
        System.out.println("Le mot u");
        U.display();
        System.out.println("L'encodage x de u");
        Matrix X = U.multiply(hbase.sysTransform().genG());
        X.display();

        System.out.println("--------------------------------------------------------------");
        System.out.println("                           2e Tâche                           ");
        System.out.println("--------------------------------------------------------------\n");

        // 2e tache
        System.out.println("Le graphe de Tanner de H");
        TGraph tGraph = new TGraph(hbase, 3, 4);
        tGraph.display();

        System.out.println("--------------------------------------------------------------\n");

        byte[][] tabe1 = { { 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0 } };
        Matrix e1 = new Matrix(tabe1);
        Matrix y1 = X.add(e1);
        System.out.println("Le mot y1");
        y1.display();
        System.out.println("Transpose de syndrome de y1");
        hbase.multiply(y1.transpose()).transpose().display();
        System.out.println("Le mot y1 corrigé");
        tGraph.decode(y1, 100).display();
        System.out.println("Le mot x original");
        X.display();

        System.out.println("--------------------------------------------------------------\n");

        byte[][] tabe2 = { { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0 } };
        Matrix e2 = new Matrix(tabe2);
        Matrix y2 = X.add(e2);
        System.out.println("Le mot y2");
        y2.display();
        System.out.println("Transpose de syndrome de y2");
        hbase.multiply(y2.transpose()).transpose().display();
        System.out.println("Le mot y2 corrigé");
        tGraph.decode(y2, 100).display();
        System.out.println("Le mot x original");
        X.display();

        System.out.println("--------------------------------------------------------------\n");

        byte[][] tabe3 = { { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
        0 } };
        Matrix e3 = new Matrix(tabe3);
        Matrix y3 = X.add(e3);
        System.out.println("Le mot y3");
        y3.display();
        System.out.println("Transpose de syndrome de y3");
        hbase.multiply(y3.transpose()).transpose().display();
        System.out.println("Le mot y3 corrigé");
        tGraph.decode(y3, 100).display();
        System.out.println("Le mot x original");
        X.display();

        System.out.println("--------------------------------------------------------------\n");

        byte[][] tabe4 = { { 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
        0 } };
        Matrix e4 = new Matrix(tabe4);
        Matrix y4 = X.add(e4);
        System.out.println("Le mot y4");
        y4.display();
        System.out.println("Transpose de syndrome de y4");
        hbase.multiply(y4.transpose()).transpose().display();
        System.out.println("Le mot y4 corrigé");
        tGraph.decode(y4, 100).display();
        System.out.println("Le mot x original");
        X.display();

        System.out.println("--------------------------------------------------------------");
        System.out.println("                           3e Tâche                           ");
        System.out.println("--------------------------------------------------------------\n");

        // 3e tache
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
