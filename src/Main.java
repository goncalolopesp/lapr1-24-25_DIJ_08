import org.la4j.Matrix;
import org.la4j.decomposition.EigenDecompositor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String ficheiroCSV = "exampleInputFunc1.csv";

        int[] dimensoesMatriz = calcularDimensoesCSV(ficheiroCSV); // Obter numero de linhas e colunas

        double[][] matriz = new double[dimensoesMatriz[0]][dimensoesMatriz[1]];
        preencherMatriz(matriz, ficheiroCSV); // Preencher a matriz 2D com os valores do CSV
        mostrarMatriz(matriz); // Imprimir a matriz 2D

        //Guardar os valores e os vetores próprios
        int k = 3;
        EigenDecompositor decompositor = new EigenDecompositor(Matrix.from2DArray(matriz));
        Matrix[] decomposed = decompositor.decompose();

        Matrix eigenVectors = decomposed[0];
        Matrix eigenValues = decomposed[1];

        //Calcular as matrizes Pk e Dk
        for (int i = 0; i < eigenVectors.columns(); i++) {
            double norm = 0;
            for (int j = 0; j < eigenVectors.rows(); j++) {
                norm += Math.pow(eigenVectors.get(j, i), 2);
            }
            norm = Math.sqrt(norm);
            for (int j = 0; j < eigenVectors.rows(); j++) {
                eigenVectors.set(j, i, eigenVectors.get(j, i) / norm);
            }
        }

        Matrix Pk = eigenVectors.slice(0, 0, matriz.length, k);
        Matrix Dk = eigenValues.slice(0, 0, k, k);

        //Fazer a transposta
        System.out.println();
        fazerTransposta(matriz);

        //Ordenar matriz diagonal dos valores prórios
        System.out.println();
        double[] listaValoresProprios = new double[matriz.length];
        double[][] matrizValoresPropriosOrdenada = new double[matriz.length][matriz.length];
        construirMatrizValores(eigenValues, listaValoresProprios);
        ordenarMatrizValores(listaValoresProprios);
        construirMatrizDiagonalOrdenada(listaValoresProprios, matrizValoresPropriosOrdenada);
    }

    private static void mostrarMatriz(double[][] matriz) {
        for (int numLinha = 0; numLinha < matriz.length; numLinha++) {
            for (int numColuna = 0; numColuna < matriz[numLinha].length; numColuna++) {
                System.out.print(matriz[numLinha][numColuna] + " ");
            }
            System.out.println();
        }
    }

    private static void preencherMatriz(double[][] matriz, String ficheiroCSV) throws FileNotFoundException {
        File csv = new File(ficheiroCSV);
        Scanner sc = new Scanner(csv);

        int numLinha = 0;

        // Preencher a matriz com os dados do CSV
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] valores = line.split(",");
            for (int numColuna = 0; numColuna < valores.length; numColuna++) {
                matriz[numLinha][numColuna] = Double.parseDouble(valores[numColuna]); // Preenche a célula da matriz
            }
            numLinha++;
        }

        sc.close();
    }

    private static int[] calcularDimensoesCSV(String ficheiroCSV) throws FileNotFoundException {
        File csv = new File(ficheiroCSV);
        Scanner sc = new Scanner(csv);

        int numLinhas = 0, numColunas = 0;

        // Contar as linhas e determinar o número de colunas
        while (sc.hasNextLine()) {
            String linha = sc.nextLine();
            numLinhas++;
            String[] valores = linha.split(",");
            numColunas = valores.length;
        }

        sc.close();

        // Retorna um array com o número de linhas e o número de colunas
        return new int[]{numLinhas, numColunas};
    }

    //transposta
    public static void fazerTransposta(double[][] matrizOriginal) {
        double[][] matrizTransposta = new double[matrizOriginal[0].length][matrizOriginal.length];
        for (int i = 0; i < matrizTransposta.length; i++) {
            for (int j = 0; j < matrizTransposta[0].length; j++) {
                matrizTransposta[i][j] = matrizOriginal[j][i];
            }
        }
        for (int m = 0; m < matrizTransposta.length; m++) {              //nao sei se sera necessario este print(?)
            for (int n = 0; n < matrizTransposta[0].length; n++) {
                System.out.printf("%.2f ", matrizTransposta[m][n]);
            }
            System.out.println();
        }
    }

    //ordenar matriz diagonal com os valores proprios
    public static void construirMatrizValores(Matrix matrizDosValoresProprios, double[] matrizValores) {
        int posicao = 0;
        for (int i = 0; i < matrizDosValoresProprios.rows(); i++) {
            for (int j = 0; j < matrizDosValoresProprios.columns(); j++) {
                if (i == j) {
                    matrizValores[posicao] = matrizDosValoresProprios.get(i, j);
                    posicao++;
                }
            }
        }
    }

    public static void ordenarMatrizValores(double[] matrizValores) {
        for (int i = 0; i < matrizValores.length - 1; i++) {
            for (int m = i + 1; m < matrizValores.length; m++) {
                if (matrizValores[m] > matrizValores[i]) {
                    double aux = matrizValores[m];
                    matrizValores[m] = matrizValores[i];
                    matrizValores[i] = aux;
                }
            }
        }
    }

    public static void construirMatrizDiagonalOrdenada(double[] listaValores, double[][] matrizDiagonalOrdenada) {
        int posicao = 0;
        for (int i = 0; i < matrizDiagonalOrdenada.length; i++) {
            for (int j = 0; j < matrizDiagonalOrdenada.length; j++) {
                if (i == j) {
                    matrizDiagonalOrdenada[i][j] = listaValores[posicao];
                    posicao++;
                }
            }
        }
        for (int m = 0; m < matrizDiagonalOrdenada.length; m++) {           //nao sei se será necessario este print(?)
            for (int n = 0; n < matrizDiagonalOrdenada.length; n++) {
                System.out.printf("%.2f ", matrizDiagonalOrdenada[m][n]);
            }
            System.out.println();
        }
    }
}