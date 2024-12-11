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
                        System.out.print("Digite o caminho do arquivo CSV: ");
                        String ficheiroCSV = scanner.nextLine();

                        System.out.print("Digite o valor de k: ");
                        int k = scanner.nextInt();

                        decomporMatriz(ficheiroCSV, k);
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
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }

    private static void decomporMatriz(String ficheiroCSV, int k) throws FileNotFoundException {
        // Obter dimensões e preencher matriz
        double[][] matriz = preencherMatriz(ficheiroCSV);
        mostrarMatriz(matriz);

        // Decomposição de valores e vetores próprios usando Apache Commons
        EigenDecomposition decomposicao = new EigenDecomposition(toRealMatrix(matriz));

        // Obter autovalores
        double[] valoresProprios = decomposicao.getRealEigenvalues();

        // Obter vetores próprios e organizar na mesma ordem dos autovalores
        double[][] vetoresProprios = new double[matriz.length][];
        for (int i = 0; i < matriz.length; i++) {
            vetoresProprios[i] = decomposicao.getEigenvector(i).toArray();
        }

        // Ordenar autovalores em ordem decrescente e reordenar vetores próprios
        double[] valoresPropriosOrdenados = ordenarArray(valoresProprios, vetoresProprios);

        // Transpor para garantir vetores próprios como colunas
        vetoresProprios = transporMatriz(vetoresProprios);

        // Normalizar vetores próprios
        vetoresProprios = normalizarColunas(vetoresProprios);

        // Ordenar vetores próprios
        double[][] vetoresPropriosOrdenados = ordenarVetoresProprios(vetoresProprios, valoresProprios, valoresPropriosOrdenados);

        // Usar vetores próprios reordenados e normalizados
        vetoresProprios = vetoresPropriosOrdenados;

        // Calcular matrizes Pk e Dk
        double[][] Pk = fatiarMatriz(vetoresProprios, 0, vetoresProprios.length, 0, k);
        double[][] Dk = criarMatrizDiagonal(fatiarArray(valoresPropriosOrdenados, 0, k));

        System.out.println("\nPk");
        mostrarMatriz(Pk);
        System.out.println("\nDk");
        mostrarMatriz(Dk);
        System.out.println("\nPk^T");
        mostrarMatriz(transporMatriz(Pk));

        // Calcular Ak = Pk * Dk * Pk^T
        System.out.println("\nAk:");
        double[][] Ak = multiplicarMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk));
        mostrarMatriz(Ak);

        // Calcular erro absoluto médio
        double erro = calcularEAM(matriz, Ak);
        System.out.println("\nErro Absoluto Médio");
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
            if (norma != 0) { // Evita divisão por zero
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
