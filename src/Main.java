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
        return new int[] {numLinhas, numColunas};
    }
}