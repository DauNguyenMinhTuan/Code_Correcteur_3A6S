public class TGraph {

    private int n_r, n_c, w_r, w_c;
    private int[][] left, right;

    public TGraph(Matrix H, int wc, int wr) {
        w_c = wc;
        w_r = wr;
        n_r = H.getRows();
        n_c = H.getCols();
        left = new int[n_r][w_r + 1];
        for (int i = 0; i < n_r; i++) {
            int cnt = 0;
            left[i][0] = 0;
            for (int j = 0; j < n_c && cnt < w_r; j++) {
                if (H.getElem(i, j) == 1) {
                    left[i][++cnt] = j;
                }
            }
        }
        right = new int[n_c][w_c + 1];
        for (int i = 0; i < n_c; i++) {
            int cnt = 0;
            right[i][0] = 0;
            for (int j = 0; j < n_r && cnt < w_c; j++) {
                if (H.getElem(j, i) == 1) {
                    right[i][++cnt] = j;
                }
            }
        }
    }

    public void display() {
        System.out.println("left");
        System.out.print("[");
        for (int i = 0; i < n_r; i++) {
            if (i != 0) {
                System.out.print(" ");
            }

            System.out.print("[");

            for (int j = 0; j <= w_r; j++) {
                System.out.printf("%d", left[i][j]);

                if (j != w_r) {
                    System.out.print(" ");
                }
            }

            System.out.print("]");

            if (i == n_r - 1) {
                System.out.print("]");
            }

            System.out.println();
        }
        System.out.println();

        System.out.println("right");
        System.out.print("[");
        for (int i = 0; i < n_c; i++) {
            if (i != 0) {
                System.out.print(" ");
            }

            System.out.print("[");

            for (int j = 0; j <= w_c; j++) {
                System.out.printf("%d", right[i][j]);

                if (j != w_c) {
                    System.out.print(" ");
                }
            }

            System.out.print("]");

            if (i == n_c - 1) {
                System.out.print("]");
            }

            System.out.println();
        }
        System.out.println();
    }

    public Matrix decode(Matrix code, int rounds) {
        // On fixe round = 100
        rounds = 100;
        
        // Initialisation
        // Pour chaque N dans G.V (graphe vérification a.k.a. right)
        for (int i = 0; i < n_c; i++) {
            // N.val = y[i]
            // N.val ici est right[i][0]
            // y[i] est i-ème bit du mot y
            right[i][0] = code.getElem(0, i);
        }

        // Boucle principale
        for (int cnt = 0; cnt < rounds; cnt++) {
            // Calcul des parités
            // Pour chaque N dans G.F (graphe fonctionel a.k.a left)
            for (int i = 0; i < n_r; i++) {
                // N.val <-- 0
                // N.val est left[i][0]
                left[i][0] = 0;
                // Pour chaque K dans N.voisins
                // K est un autre noeud dans G.V
                // N.voisins est right[left[i][j]] avec j > 0
                for (int j = 1; j <= w_r; j++) {
                    // N.val <-- N.val + K.val mod 2
                    // K est right[N.voisins.val]
                    left[i][0] += right[left[i][j]][0];
                    left[i][0] %= 2;
                }
            }

            // Vérification
            // Si pour chaque N dans G.F, N.val = 0
            boolean correct = true;
            for (int i = 0; i < n_r; i++) {
                if (left[i][0] != 0) {
                    correct = false;
                    break;
                }
            }
            if (correct) {
                byte[][] tabx = new byte[1][n_c];
                // Pour chaque N dans G.V
                for (int i = 0; i < n_c; i++) {
                    // x[i] <-- N.val
                    tabx[0][i] = (byte) right[i][0];
                }
                // Renvoyer x
                Matrix x = new Matrix(tabx);
                return x;
            }

            // Calcul du max
            int max = 0;
            int[] count = new int[n_c];
            // Pour chaque N dans G.V
            for (int i = 0; i < n_c; i++) {
                // count[N] <-- 0
                count[i] = 0;
                // Pour chaque K dans N.voisins
                for (int j = 1; j < w_c; j++) {
                    // count[N] <-- count[N] + K.val
                    count[i] += left[right[i][j]][0];
                }
                // Si count[N] > max
                if(count[i] > max){
                    // max <-- count[N]
                    max = count[i];
                }
            }

            // Renversement de bits
            // Pour chaque N dans G.V
            for(int i=0;i< n_c;i++){
                // Si count[N] = max
                if(count[i] == max){
                    // N.val = 1 - N.val
                    // Ici, on change l'état de cet bit
                    right[i][0] = 1 - right[i][0];
                }
            }
        }

        // Si aucune valeur retournée, échec
        byte[][] tabError = new byte[1][code.getCols()];
        for (int i = 0; i < code.getCols(); i++) {
            tabError[0][i] = -1;
        }
        Matrix error = new Matrix(tabError);
        return error;
    }

}