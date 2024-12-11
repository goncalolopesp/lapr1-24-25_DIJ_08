import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        boolean ativo = true;

        while (ativo) {
            System.out.println("Menu:");
            System.out.println("1. Decompor Matriz");
            System.out.println("2. Reconstruir Imagens");
            System.out.println("3. Obter Imagem Semelhante");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Indica o caminho do ficheiro CSV: ");
                    String ficheiroCSV = scanner.nextLine();

                    System.out.print("Digita a quantidade de valores/vetores prórpios a usar: ");
                    int k = scanner.nextInt();

                    if (validarCaminhoFicheiro(ficheiroCSV) && validarMatriz(ficheiroCSV)) {
                        k = ajustarK(ficheiroCSV, k);
                        decomporMatriz(ficheiroCSV, k);
                    }
                    break;

                case 2:
                    System.out.println("Ainda não funciona");
                    break;

                case 3:
                    System.out.println("Ainda não funciona");
                    break;

                case 4:
                    ativo = false;
                    break;

                default:
                    System.out.println("Não existe essa opção");
            }
        }

        scanner.close();
    }

    private static boolean validarCaminhoFicheiro(String ficheiroCSV) {
        File file = new File(ficheiroCSV);
        if (!file.exists()) {
            System.out.println("O caminho indicado não existe");
            return false;
        }
        return true;
    }

    private static boolean validarMatriz(String ficheiroCSV) throws FileNotFoundException {
        double[][] matriz = preencherMatriz(ficheiroCSV);
        if (matriz.length != matriz[0].length) {
            System.out.println("A matriz indicada não é quadrada");
            return false;
        }

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (matriz[i][j] != matriz[j][i]) {
                    System.out.println("A matriz indicada não é simétrica");
                    return false;
                }
            }
        }
        if (matriz.length >= 256){
            System.out.println("O programa não possui capacidade para analisar a matriz fornecida");
            return false;
        }

        return true;
    }

    private static int ajustarK(String ficheiroCSV, int k) throws FileNotFoundException {
        double[][] matriz = preencherMatriz(ficheiroCSV);
        if (k == -1 || k > matriz.length) {
            System.out.println("AVISO: quantidade de valores/vetores próprios ajustada para a quantidade máxima");
            k = matriz.length;
        }
        return k;
    }

    private static void decomporMatriz(String ficheiroCSV, int k) throws FileNotFoundException {
        double[][] matriz = preencherMatriz(ficheiroCSV);

        EigenDecomposition decomposicao = new EigenDecomposition(toRealMatrix(matriz));

        double[] valoresProprios = decomposicao.getRealEigenvalues();

        double[][] vetoresProprios = new double[matriz.length][];
        for (int i = 0; i < matriz.length; i++) {
            vetoresProprios[i] = decomposicao.getEigenvector(i).toArray();
        }

        double[] valoresPropriosOrdenados = ordenarArray(valoresProprios, vetoresProprios);
        vetoresProprios = transporMatriz(vetoresProprios);
        vetoresProprios = normalizarColunas(vetoresProprios);
        double[][] vetoresPropriosOrdenados = ordenarVetoresProprios(vetoresProprios, valoresProprios, valoresPropriosOrdenados);
        vetoresProprios = vetoresPropriosOrdenados;

        double[][] Pk = fatiarMatriz(vetoresProprios, 0, vetoresProprios.length, 0, k);
        double[][] Dk = criarMatrizDiagonal(fatiarArray(valoresPropriosOrdenados, 0, k));
        
        System.out.println("Matriz reconstruída");
        double[][] Ak = multiplicarMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk));
        mostrarMatriz(Ak);

        double erro = calcularEAM(matriz, Ak);
        System.out.println("Erro Absoluto Médio");
        System.out.printf("%.2f %n", erro);
    }

    private static double calcularEAM(double[][] matriz, double[][] Ak) {
        double erro = (double) 1 / (matriz.length * matriz[0].length);
        double soma = 0;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                soma += (float) Math.abs(matriz[i][j] - Ak[i][j]);
            }
        }

        erro *= soma;
        return erro;
    }

    private static double[][] normalizarColunas(double[][] matriz) {
        for (int i = 0; i < matriz[0].length; i++) {
            double norma = 0;
            for (int j = 0; j < matriz.length; j++) {
                norma += Math.pow(matriz[j][i], 2);
            }
            norma = Math.sqrt(norma);
            if (norma != 0) {
                for (int j = 0; j < matriz.length; j++) {
                    matriz[j][i] /= norma;
                }
            }
        }
        return matriz;
    }

    private static double[][] fatiarMatriz(double[][] matriz, int linhaInicio, int linhaFim, int colunaInicio, int colunaFim) {
        double[][] resultado = new double[linhaFim - linhaInicio][colunaFim - colunaInicio];
        for (int i = linhaInicio; i < linhaFim; i++) {
            for (int j = colunaInicio; j < colunaFim; j++) {
                resultado[i - linhaInicio][j - colunaInicio] = matriz[i][j];
            }
        }
        return resultado;
    }

    private static double[] fatiarArray(double[] array, int inicio, int fim) {
        double[] resultado = new double[fim - inicio];
        for (int i = inicio; i < fim; i++) {
            resultado[i - inicio] = array[i];
        }
        return resultado;
    }

    private static double[][] criarMatrizDiagonal(double[] valores) {
        double[][] matrizDiagonal = new double[valores.length][valores.length];
        for (int i = 0; i < valores.length; i++) {
            matrizDiagonal[i][i] = valores[i];
        }
        return matrizDiagonal;
    }

    private static double[][] transporMatriz(double[][] matriz) {
        double[][] transposta = new double[matriz[0].length][matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                transposta[j][i] = matriz[i][j];
            }
        }
        return transposta;
    }

    private static double[][] multiplicarMatrizes(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            throw new IllegalArgumentException("Dimensões das matrizes não são compatíveis para multiplicação.");
        }

        double[][] resultado = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < b.length; k++) {
                    resultado[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return resultado;
    }

    private static double[] ordenarArray(double[] array, double[][] matriz) {
        double[] ordenado = array.clone();
        for (int i = 0; i < ordenado.length; i++) {
            for (int j = i + 1; j < ordenado.length; j++) {
                if (ordenado[j] > ordenado[i]) {
                    double[] matrizAux = matriz[i];
                    matriz[i] = matriz[j];
                    matriz[j] = matrizAux;
                    double temp = ordenado[i];
                    ordenado[i] = ordenado[j];
                    ordenado[j] = temp;
                }
            }
        }
        return ordenado;
    }

    private static double[][] ordenarVetoresProprios(double[][] vetoresProprios, double[] valoresProprios, double[] valoresPropriosOrdenados) {
        double[][] vetoresPropriosOrdenados = new double[vetoresProprios.length][vetoresProprios[0].length];

        for (int i = 0; i < valoresPropriosOrdenados.length; i++) {
            boolean flag = true;
            for (int j = 0; j < valoresProprios.length && flag; j++) {
                if (valoresPropriosOrdenados[i] == valoresProprios[j]) {
                    for (int k = 0; k < vetoresProprios.length; k++) {
                        vetoresPropriosOrdenados[k][i] = vetoresProprios[k][j];
                    }
                    flag = false;
                }
            }
        }

        return vetoresPropriosOrdenados;
    }

    public static double[][] preencherMatriz(String ficheiroCSV) throws FileNotFoundException {
        File csv = new File(ficheiroCSV);
        Scanner sc = new Scanner(csv);

        int numLinhas = 0;
        int numColunas = 0;

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            numLinhas++;
            String[] valores = linha.split(",");
            numColunas = valores.length;
        }

        sc.close();

        double[][] matriz = new double[numLinhas][numColunas];
        sc = new Scanner(csv);
        int numLinha = 0;

        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            String[] valores = linha.split(",");
            for (int numColuna = 0; numColuna < valores.length; numColuna++) {
                matriz[numLinha][numColuna] = Double.parseDouble(valores[numColuna]);
            }
            numLinha++;
        }
        sc.close();
        return matriz;
    }

    public static void mostrarMatriz(double[][] matriz) {
        for (double[] linha : matriz) {
            for (double valor : linha) {
                System.out.printf("%.2f ", valor);
            }
            System.out.println();
        }
    }

    public static RealMatrix toRealMatrix(double[][] matriz) {
        return MatrixUtils.createRealMatrix(matriz);
    }
}
