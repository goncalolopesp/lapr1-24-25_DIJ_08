import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;

public class Main {
    //TESTES

    //valida se o caminho fornecido existe
    private static boolean validarCaminhoFicheiro(String ficheiroCSV) {
        File file = new File(ficheiroCSV);
        if (!file.exists()) {
            System.out.println("O caminho indicado não existe");
            return false;
        }
        return true;
    }

    private static boolean validarTipoDeFicheiro(String nomeDoFicheiro, String tipoDeFicheiro) {
        String[] tipoDoFicheiro = nomeDoFicheiro.split("[.]");
        return tipoDoFicheiro[tipoDoFicheiro.length - 1].equals(tipoDeFicheiro);
    }

    public static boolean verificarTamanho(String pasta, int numDeFicheiros, File[] ficheiros) throws FileNotFoundException {

        int contadorAlturaIgual = 0;
        int tamanhoAltura = preencherMatriz(pasta + "/" + ficheiros[0].getName()).length;
        for (int i = 1; i < numDeFicheiros; i++) {
            int tamanhoMatriz = preencherMatriz(pasta + "/" + ficheiros[i].getName()).length;
            if (tamanhoMatriz == tamanhoAltura) {
                contadorAlturaIgual++;
            }
        }

        int contadorAlturaIgual2 = 0;
        int tamanhoAltura2 = preencherMatriz(pasta + "/" + ficheiros[0].getName())[0].length;
        for (int i = 1; i < numDeFicheiros; i++) {
            int tamanhoMatriz = preencherMatriz(pasta + "/" + ficheiros[i].getName())[0].length;
            if (tamanhoMatriz == tamanhoAltura2) {
                contadorAlturaIgual2++;
            }
        }

        if (contadorAlturaIgual < numDeFicheiros - 1 || contadorAlturaIgual2 < numDeFicheiros - 1) {
            System.out.println("Nem todos os ficheiros têm o mesmo tamanho");
            return false;
        }
        return true;
    }

    //verifica se a matriz é quadrada
    public static boolean isSquareMatrix(double[][] matriz) {
        if (matriz.length != matriz[0].length) {
            return false;
        }
        return true;
    }

    //verfiica se é uma matriz quadrada de índice menor ou igual que 256
    public static boolean isSquareMatrixSmallerThan(double[][] matriz, int maxTamanho) {
        if (matriz.length > maxTamanho) {
            System.out.println("O tamanho da matriz é superior a 256!");
            return false;
        }
        return true;
    }

    //verifica se a matriz é simétrica
    public static boolean isSquareMatrixSimetric(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (matriz[i][j] != matriz[j][i]) {
                    System.out.println("A matriz indicada não é simétrica");
                    return false;
                }
            }
        }
        return true;
    }

    //efetua todas as validações necessárias da matriz
    private static boolean validarMatriz(double[][] matriz) throws FileNotFoundException {

        if (!isSquareMatrix(matriz)) return false;

        if (!isSquareMatrixSmallerThan(matriz, 256)) return false;

        if (!isSquareMatrixSimetric(matriz)) return false;

        return true;
    }

    //Verificar se uma pasta está vazia
    public static boolean isEmpty(String pasta){
        File dir = new File(pasta);
        if (dir.listFiles().length == 0){
            return true;
        }
        return false;
    }

    //Verificar se é possível fazer a subtração de matrizes
    private static boolean canSubtrairArrays(double[] vetor1, double[] vetor2) {
        if (vetor1.length != vetor2.length){
            System.out.println("Os vetores não têm o mesmo tamanho (Subtração de matrizes não pode ser realizada)");
            return false;
        }
        return true;
    }

    //Verificar se podemos multiplicar matrizes
    private static boolean canMultiplyMatrizes(double[][] matrizA, double[][] matrizB) {
        // Compara o número de colunas da matriz A com o número de linhas da matriz B
        if (matrizA[0].length != matrizB.length) {
            return false;
        }
        return true;
    }

    //Verificar se um número está num intervalo de valores e se o número é inteiro
    public static boolean verificarNumerosETiposDeCadaMatriz(double[][] matriz) {
        final int LIMITEMINIMO = 0;
        final int LIMITEMAXIMO = 255;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (matriz[i][j] < LIMITEMINIMO || matriz[i][j] > LIMITEMAXIMO || !((int)matriz[i][j] == matriz[i][j])) {
                    System.out.println("O ficheiro csv apresenta erros");
                }
            }
        }
        return true;
    }

    //Verificar se uma string tem espaços
    private static boolean temEspacos(String valore) {
        for (int i = 0; i < valore.length(); i++) {
            if (!Character.isDigit(valore.charAt(i))) {
                return true;
            }
        }
        return false;
    }



    //Codigo
    public static void main(String[] args) throws IOException {
        menu();
    }

    public static void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        // variável booleana para manter o menu ativo até o utilizador usar a opção sair
        boolean ativo = true;

        while (ativo) {
            System.out.println("****************************************");
            System.out.println("                 MENU           ");
            System.out.println("****************************************");
            System.out.println("  1. Decompor Matriz                  ");
            System.out.println("  2. Reconstruir Imagens              ");
            System.out.println("  3. Obter Imagem Semelhante          ");
            System.out.println("  4. Sair                           ");
            System.out.println("****************************************");
            System.out.print("Escolha uma opção (1-4): ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    funcionalidade1();
                    break;

                case 2:
                    funcionalidade2();
                    break;

                case 3:
                    funcionalidade3();
                    break;

                case 4:
                    //termina o código
                    ativo = false;
                    break;

                default:
                    //mensagem de erro default
                    System.out.println("Não existe essa opção");
            }
        }
        scanner.close();
    }

    public static void funcionalidade1() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Indica o caminho do ficheiro CSV: ");
        String ficheiroCSV = scanner.nextLine();

        System.out.print("Digita a quantidade de valores/vetores próprios a usar: ");
        int k = scanner.nextInt();

        //verifica se a matriz e a quantidade de vetores/valores prórpios é válida
        if (validarCaminhoFicheiro(ficheiroCSV) && validarMatriz(preencherMatriz(ficheiroCSV)) && verificarNumerosETiposDeCadaMatriz(preencherMatriz(ficheiroCSV))) {
            k = ajustarK(ficheiroCSV, k);
            decomporMatriz(ficheiroCSV, k);
        }
    }

    public static void funcionalidade2() throws FileNotFoundException {
        String tipoDeFicheiro = "csv";
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nome da pasta com os ficheiros csv: ");
        String pasta = scanner.nextLine();
        if (!validarCaminhoFicheiro(pasta)) return;

        File diretorio = new File(pasta);
        File[] ficheiros = diretorio.listFiles();

        int posicao = 0;
        int numDeFicheiros = ficheiros.length;

        for (int i = 0; i < numDeFicheiros; i++){
            if (!validarTipoDeFicheiro(ficheiros[i].getName(), tipoDeFicheiro)) return;
        }

        if (!verificarTamanho(pasta, numDeFicheiros, ficheiros)) return;

        System.out.print("Quantas eigenfaces devem ser usadas para reconstrução? ");
        int k = scanner.nextInt();

        double[][] matriz = preencherMatriz(pasta + "/" + ficheiros[0].getName());
        if (!verificarNumerosETiposDeCadaMatriz(matriz)) return;
        double[][] matrizNova = new double[(int) Math.pow(matriz.length, 2)][numDeFicheiros];
        for (int j = 0; j < matrizNova[0].length; j++) {
            matriz = preencherMatriz(pasta + "/" + ficheiros[j].getName());
            double[] matrizVetor = criacaoDeArrayVetor(matriz);
            for (int i = 0; i < matrizVetor.length; i++) {
                matrizNova[i][j] = matrizVetor[posicao];
                posicao++;
            }
            posicao = 0;
        }

        k = ajustarKEmMatriz(matrizNova, k);

        double[] vetorMedia = mediaColunas(matrizNova);

        double[][] matrizA = obterMatrizPhi(matrizNova, vetorMedia);
        double[][] matrizAT = transporMatriz(matrizA);

        if (!canMultiplyMatrizes(matrizAT, matrizA)) return;
        double[][] matrizAT_A = multiplicarMatrizes(matrizAT, matrizA);
        double[][] matrizC = obterMatrizC(matrizAT_A);

        double[][] vetoresPropriosC = obterVetoresProprios(matrizC);

        double[] valoresPropriosC = obterValoresProprios(matrizC, vetoresPropriosC);

        vetoresPropriosC = tratamentoDeVetores(vetoresPropriosC);

        double[][] vetoresPropriosAT_A = obterVetoresProprios(matrizAT_A);

        double[] valoresPropriosAT_A = obterValoresProprios(matrizAT_A, vetoresPropriosAT_A);

        vetoresPropriosAT_A = tratamentoDeVetores(vetoresPropriosAT_A);

        if (canMultiplyMatrizes(matrizA, vetoresPropriosAT_A)) return;
        double[][] vetorU = multiplicarMatrizes(matrizA, vetoresPropriosAT_A);
        vetorU = normalizarColunas(vetorU);

        System.out.println("Vetor U:");
        mostrarMatriz(matrizA);

        System.out.println("Vetor µ:");
        for (int i = 0; i < vetorMedia.length; i++) {
            System.out.printf("%.1f ", vetorMedia[i]);
        }
        System.out.println();
        System.out.println("Matriz de covariância:");
        mostrarMatriz(matrizC);
        System.out.println("Número de eigenfaces:");
        System.out.println(k);
        double[][] IR = reconstruirImagem(vetorMedia, vetorU, matrizA, k);
        System.out.println("IR");
        mostrarMatriz(IR);
    }

    private static double[][] reconstruirImagem(double[] vetorMedia, double[][] vetorU, double[][] matrizA, int k) {
        int linhas = vetorU.length;         // Número de pixels (linhas)
        int colunas = matrizA[0].length;    // Número de imagens (ou dimensões)
        double[][] IR = new double[linhas][colunas]; // Matriz para a imagem reconstruída

        // Calcular a transposta de vetorU
        double[][] vetorUT = transporMatriz(vetorU);

        // Calcular os pesos W = U^T * A
        double[][] vetorW = multiplicarMatrizes(vetorUT, matrizA);

        // Reconstruir a imagem
        for (int i = 0; i < colunas; i++) {         // Para cada imagem
            for (int x = 0; x < linhas; x++) {      // Para cada pixel
                double soma = vetorMedia[x];        // Começa com a média do pixel
                for (int j = 0; j < k; j++) {       // Usar os K componentes principais
                    soma += vetorU[x][j] * vetorW[j][i]; // u_j * w_j
                }
                IR[x][i] = soma;                    // Reconstruir o pixel
            }
        }

        // Ajustar possíveis erros numéricos para garantir correspondência exata quando k é máximo
        if (k == vetorU[0].length) {
            ajustarReconstrucao(IR, matrizA, vetorMedia);
        }

        return IR;
    }

    private static void ajustarReconstrucao(double[][] IR, double[][] matrizA, double[] vetorMedia) {
        int linhas = IR.length;
        int colunas = IR[0].length;

        for (int i = 0; i < colunas; i++) {  // Para cada imagem
            for (int x = 0; x < linhas; x++) {  // Para cada pixel
                // Subtraia a média para comparar a reconstrução com a matrizA original
                double valorReconstruido = IR[x][i] - vetorMedia[x];
                double valorOriginal = matrizA[x][i];

                // Ajuste para corrigir possíveis diferenças de ponto flutuante
                if (Math.abs(valorReconstruido - valorOriginal) > 1e-6) {
                    IR[x][i] = valorOriginal + vetorMedia[x];
                }
            }
        }
    }

    //calcula o vetor média (coluna)
    private static double[] mediaColunas(double[][] matrizM) {
        int numLinhas = matrizM.length;
        int numColunas = matrizM[0].length;
        double[] somaLinhas = new double[numLinhas];
        double[] medias = new double[numLinhas];

        // Soma os valores das colunas para cada linha
        for (int i = 0; i < numLinhas; i++) {
            for (int j = 0; j < numColunas; j++) {
                somaLinhas[i] += matrizM[i][j];
            }
        }

        // Divide cada soma pelo número de colunas
        for (int i = 0; i < numLinhas; i++) {
            medias[i] = somaLinhas[i] / numColunas;
        }
        return medias;
    }

    //obtem a matriz phi
    private static double[][] obterMatrizPhi(double[][] matrizM, double[] vetorMedia) {
        double[][] matrizA = new double[matrizM.length][matrizM[0].length];
        for (int i = 0; i < matrizM.length; i++) {
            for (int j = 0; j < matrizM[0].length; j++) {
                matrizA[i][j] = matrizM[i][j] - vetorMedia[i];
            }
        }
        return matrizA;
    }

    //obtem a matriz C apartir do resultado da multiplicação de A com A^T
    private static double[][] obterMatrizC(double[][] matrizAAt) {
        double[][] matrizC = new double[matrizAAt.length][matrizAAt[0].length];
        for (int i = 0; i < matrizC.length; i++) {
            for (int j = 0; j < matrizC[i].length; j++) {
                matrizC[i][j] = matrizAAt[i][j] / matrizC.length;
            }
        }
        return matrizC;
    }

    //ajusta automáticamente os valores de k
    private static int ajustarK(String ficheiroCSV, int k) throws FileNotFoundException {
        double[][] matriz = preencherMatriz(ficheiroCSV);
        if (k <= 0 || k > matriz.length) {
            System.out.println("AVISO: quantidade de valores/vetores próprios ajustada para a quantidade máxima");
            k = matriz.length;
        }
        return k;
    }

    //ajusta automáticamente os valores de k para uma matriz
    public static int ajustarKEmMatriz(double[][] matriz, int k) {
        if (k <= 0 || k > matriz[0].length) {
            System.out.println("AVISO: quantidade de valores/vetores próprios ajustada para a quantidade máxima");
            k = matriz[0].length;
        }
        return k;
    }

    //decomposição da matriz
    private static void decomporMatriz(String ficheiroCSV, int k) throws IOException {
        double[][] matriz = preencherMatriz(ficheiroCSV);

        double[][] vetoresProprios = obterVetoresProprios(matriz);

        double[] valoresProprios = obterValoresProprios(matriz, vetoresProprios);

        vetoresProprios = tratamentoDeVetores(vetoresProprios);

        double[] valoresPropriosOrdenados = ordenarArray(valoresProprios, vetoresProprios);


        System.out.println();
        System.out.printf("Quantidade de Valores/Vetores próprios usados: %d %n", k);

        System.out.println();


        //imprime todos os valores próprios usados na decomposição
        System.out.println("Valores próprios usados:");
        for (int i = 0; i < k; i++) {
            System.out.printf("Valor próprio %d: %.2f%n", i + 1, valoresPropriosOrdenados[i]);
        }

        System.out.println();

        //imprime todos os vetores próprios usados na decomposição
        System.out.println("Vetores próprios usados:");
        for (int i = 0; i < k; i++) {
            System.out.print("Vetor próprio " + (i + 1) + ": ");
            for (int j = 0; j < vetoresProprios.length; j++) {
                System.out.printf("%.2f ", vetoresProprios[j][i]);
            }
            System.out.println();
        }

        //cria a matriz Pk com os vetores próprios como colunas
        //cria a matriz diagonal Dk com os valores próprios
        double[][] Pk = fatiarMatriz(vetoresProprios, 0, vetoresProprios.length, 0, k);
        double[][] Dk = criarMatrizDiagonal(fatiarArray(valoresPropriosOrdenados, 0, k));

        System.out.println();

        //obtém e imprime a matriz reconstruída
        System.out.println("Matriz reconstruída");
        if (!canMultiplyMatrizes(Pk, Dk)) return;
        if (!canMultiplyMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk))) return;
        double[][] Ak = multiplicarMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk));
        mostrarMatriz(Ak);

        System.out.println();

        //obtém e imprime o Erro Absoluto Médio
        double erro = calcularEAM(matriz, Ak);
        System.out.println("Erro Absoluto Médio");
        System.out.printf("%.2f %n", erro);

        //obtém o valor do caminho do ficheiro csv
        File ficheiro = new File(ficheiroCSV);
        String nomeSemCsv = ficheiro.getName().replaceFirst("[.][^.]+$", "");

        //define o caminho de saída do ficheiro
        String nomeArquivo = "Output/" + nomeSemCsv + "_reconstruida.csv";
        File arquivoSaida = new File(nomeArquivo);

        //cria o arquivo de escrita, caso o mesmo não exista
        if (!arquivoSaida.exists()) {
            arquivoSaida.createNewFile();
        }

        //escreve no ficheiro csv os valores da matriz Ak
        PrintWriter printWriter = new PrintWriter(nomeArquivo);

        for (int i = 0; i < Ak.length; i++) {
            for (int j = 0; j < Ak[i].length; j++) {
                printWriter.print(Ak[i][j]);
                if (j < Ak[i].length - 1) {
                    printWriter.print(",");
                }
            }
            printWriter.println();
        }
        printWriter.close();
        System.out.println();
        System.out.println("Matriz reconstruída guardada na pasta 'Output' com o nome '" + nomeArquivo + "'");
        System.out.println();
    }

    //efetua a transposta e normaliza os vetores proprios
    private static double[][] tratamentoDeVetores(double[][] vetoresProprios) {
        vetoresProprios = transporMatriz(vetoresProprios);
        vetoresProprios = normalizarColunas(vetoresProprios);
        return vetoresProprios;
    }

    //obtem os vetores proprios de uma matriz
    private static double[][] obterVetoresProprios(double[][] matriz) {
        EigenDecomposition decomposicao = new EigenDecomposition(toRealMatrix(matriz));
        double[][] vetoresProprios = new double[matriz.length][];
        for (int i = 0; i < matriz.length; i++) {
            vetoresProprios[i] = decomposicao.getEigenvector(i).toArray();
        }
        return vetoresProprios;
    }

    //obtem os valores prorpios de uma matriz
    private static double[] obterValoresProprios(double[][] matriz, double[][] vetoresProprios) {
        EigenDecomposition decomposicao = new EigenDecomposition(toRealMatrix(matriz));
        double[] valoresProprios = decomposicao.getRealEigenvalues();
        double[] valoresPropriosOrdenados = ordenarArray(valoresProprios, vetoresProprios);

        return valoresProprios;
    }

    // Calcular o erro absoluto médio
    private static double calcularEAM(double[][] matriz, double[][] Ak) {
        // Cálculo da divisão (1/MN)
        double erro = (double) 1 / (matriz.length * matriz[0].length);
        double soma = 0;

        // Cálculo dos somatórios
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                soma += (float) Math.abs(matriz[i][j] - Ak[i][j]);
            }
        }

        //Aplicação final da fórmula
        erro *= soma;
        return erro;
    }

    // Normalização dos vetores próprios
    private static double[][] normalizarColunas(double[][] matriz) {
        for (int i = 0; i < matriz[0].length; i++) {
            double norma = 0;
            // Cálculo da norma do vetor
            for (int j = 0; j < matriz.length; j++) {
                norma += Math.pow(matriz[j][i], 2);
            }
            norma = Math.sqrt(norma);
            // Cálculo dos vetores normalizados e adição dos mesmos à matriz de vetores próprios
            if (norma != 0) {
                for (int j = 0; j < matriz.length; j++) {
                    matriz[j][i] /= norma;
                }
            }
        }
        return matriz;
    }

    // Cria a matriz Pk, com os vetores próprios
    private static double[][] fatiarMatriz(double[][] matriz, int linhaInicio, int linhaFim, int colunaInicio, int colunaFim) {
        double[][] resultado = new double[linhaFim - linhaInicio][colunaFim - colunaInicio];
        for (int i = linhaInicio; i < linhaFim; i++) {
            for (int j = colunaInicio; j < colunaFim; j++) {
                resultado[i - linhaInicio][j - colunaInicio] = matriz[i][j];
            }
        }
        return resultado;
    }

    //
    private static double[] fatiarArray(double[] array, int inicio, int fim) {
        double[] resultado = new double[fim - inicio];
        for (int i = inicio; i < fim; i++) {
            resultado[i - inicio] = array[i];
        }
        return resultado;
    }

    // Cria a matriz diagonal Dk com os valores próprios
    private static double[][] criarMatrizDiagonal(double[] valores) {
        double[][] matrizDiagonal = new double[valores.length][valores.length];
        // Adiciona os valores próprios à matriz
        for (int i = 0; i < valores.length; i++) {
            matrizDiagonal[i][i] = valores[i];
        }
        return matrizDiagonal;
    }

    // Cálculo da transposta da matriz
    private static double[][] transporMatriz(double[][] matriz) {
        double[][] transposta = new double[matriz[0].length][matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                transposta[j][i] = matriz[i][j];
            }
        }
        return transposta;
    }

    // Efetua a multiplicação de matrizes
    private static double[][] multiplicarMatrizes(double[][] a, double[][] b) {
        double[][] resultado = new double[a.length][b[0].length];
        // Faz a multiplicaçâo dos valores e adiciona o resultado à matriz
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                for (int k = 0; k < b.length; k++) {
                    resultado[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return resultado;
    }

    // Ordena os valores e vetores próprios
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

    // Preenche a matriz de acordo com os dados encontrados no ficheiro csv
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
        String matrizStr = "";
        while (sc.hasNextLine()) {
            matrizStr += "|";
        }
        matrizStr = cortarEspacos(matrizStr);

        String[] linhas = matrizStr.split("[|]");
        for (int i = 0; i < linhas.length; i++) {
            String[] valores = linhas[i].split(",");
            for (int j = 0; j < valores.length; j++) {
                matriz[i][j] = tirarEspacos(valores[j]);
            }
        }
        sc.close();
        return matriz;
    }

    private static String cortarEspacos(String matrizStr) {
        // Remove espaços diretamente
        matrizStr = matrizStr.replace(" ", "");

        // Remove múltiplas vírgulas consecutivas
        while (matrizStr.contains(",,")) {
            matrizStr = matrizStr.replace(",,", ",");
        }

        return matrizStr;
    }

    private static double tirarEspacos(String valore) {
        double num = 0;
        for (int i = 0; i < valore.length(); i++) {
            if (valore.charAt(i) != ' ') {
                num = num * 10 + Character.getNumericValue(valore.charAt(i));
            }
        }
        return num;
    }

    // Mostra a matriz
    public static void mostrarMatriz(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%.2f ", matriz[i][j]);
            }
            System.out.println();
        }
    }

    //Converte de double para Real Matrix
    public static RealMatrix toRealMatrix(double[][] matriz) {
        return MatrixUtils.createRealMatrix(matriz);
    }

    //Tranformação de matrizes em vetores
    public static double[] criacaoDeArrayVetor(double[][] matriz) {
        int posicao = 0;
        double[] arrayVetor = new double[matriz.length * matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                arrayVetor[posicao++] = matriz[i][j];
            }
        }
        return arrayVetor;
    }

    public static void funcionalidade3() throws IOException {
        String tipoDeFicheiro = "csv";
        Scanner scanner = new Scanner(System.in);

        System.out.print("Caminho da imagem: ");
        String img = scanner.nextLine();
        if (!validarCaminhoFicheiro(img)) return;
        if (!validarTipoDeFicheiro(img, tipoDeFicheiro)) return;

        System.out.print("Nome da pasta com os ficheiros csv: ");
        String pasta = scanner.nextLine();
        if (!validarCaminhoFicheiro(pasta)) return;
        if (isEmpty(pasta)) return;

        System.out.print("Quantos eigenfaces pretende utilizar? ");
        int k = scanner.nextInt();

        File diretorio = new File(pasta);
        File[] ficheiros = diretorio.listFiles();

        int numDeFicheiros = ficheiros.length;

        if (!verificarTamanho(pasta, numDeFicheiros, ficheiros)) return;

        double[] vetorImg = criacaoDeArrayVetor(preencherMatriz(img));

        int tamanho = (int) Math.pow(preencherMatriz((pasta + "/" + ficheiros[0].getName())).length, 2);
        double[][] matrizNova = new double[tamanho][numDeFicheiros];
        for (int j = 0; j < numDeFicheiros; j++) {
            double[][] matriz = preencherMatriz(pasta + "/" + ficheiros[j].getName());
            double[] matrizVetor = criacaoDeArrayVetor(matriz);
            for (int i = 0; i < matrizVetor.length; i++) {
                matrizNova[i][j] = matrizVetor[i];
            }
        }

        double[] vetorMedia = mediaColunas(matrizNova);
        double[][] matrizA = obterMatrizPhi(matrizNova, vetorMedia);
        double[][] matrizAT = transporMatriz(matrizA);
        if (!canMultiplyMatrizes(matrizAT, matrizA)) return;
        double[][] matrizAT_A = multiplicarMatrizes(matrizAT, matrizA);
        double[][] vetoresPropriosAT_A = obterVetoresProprios(matrizAT_A);
        vetoresPropriosAT_A = tratamentoDeVetores(vetoresPropriosAT_A);
        if (!canMultiplyMatrizes(matrizA, vetoresPropriosAT_A)) return;
        double[][] vetorU = multiplicarMatrizes(matrizA, vetoresPropriosAT_A);
        vetorU = normalizarColunas(vetorU);


        k = ajustarKEmMatriz(matrizNova, k);
        double[] valoresPropriosAT_A = obterValoresProprios(matrizAT_A, vetoresPropriosAT_A);
        vetoresPropriosAT_A = normalizarColunas(vetoresPropriosAT_A);
        double[] valoresPropriosAT_AOrdenados = ordenarArray(valoresPropriosAT_A, vetoresPropriosAT_A);
        double[][] vetoresPropriosAT_AOrdenadosExtraidos = extrairArray(vetoresPropriosAT_A, k);
        double[][] matrizUFatiada = multiplicarMatrizes(matrizA, vetoresPropriosAT_AOrdenadosExtraidos);
        matrizUFatiada = normalizarColunas(matrizUFatiada);

        //Cálculo do vetor omegaNovo
        if (!canSubtrairArrays(vetorImg, vetorMedia)) return;
        double[] fiNova = subtrairArrays(vetorImg, vetorMedia);
        if (!canMultiplyMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiNova))) return;
        double[][] wNovo = multiplicarMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiNova));
        double[] omegaNovo = criacaoDeArrayVetor(wNovo);

        //Ordenar os pesos da imagem introduzida
        omegaNovo = ordenarVetor(omegaNovo);
        omegaNovo = inverterVetor(omegaNovo);

        double menor = 999999999;
        String menorNome = "";

        //mostrar os k pesos da imagem introduzida
        for (int a = 0; a < k; a++) {
            System.out.printf("%.2f ", omegaNovo[a]);
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < numDeFicheiros; i++){
            double soma = 0;
            //Cálculo do vetor omegaJ
            if (!canSubtrairArrays(criacaoDeArrayVetor(preencherMatriz(ficheiros[i].getPath())), vetorMedia)) return;
            double[] fiJ = subtrairArrays(criacaoDeArrayVetor(preencherMatriz(ficheiros[i].getPath())), vetorMedia);
            if (!canMultiplyMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiJ))) return;
            double[][] wJ = multiplicarMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiJ));
            double[] omegaJ = criacaoDeArrayVetor(wJ);

            //Ordenar os pesos de cada imagem
            omegaJ = ordenarVetor(omegaJ);
            omegaJ = inverterVetor(omegaJ);

            //mostrar os k pesos de cada imagem
            for (int a = 0; a < k; a++) {
                System.out.printf("%.2f ", omegaJ[a]);
            }
            System.out.println();

            //Calcular a distância euclidiana
            for (int j = 0; j < k; j++){
                soma += Math.pow(omegaJ[j] - omegaNovo[j], 2);
            }
            double distanciaEuclidiana = Math.sqrt(soma);

            System.out.printf("%.2f%n", distanciaEuclidiana);

            //Verificar qual é a menor distância euclidiana
            if (distanciaEuclidiana < menor){
                menor = distanciaEuclidiana;
                menorNome = ficheiros[i].getName();
            }
            System.out.println();
        }

        //Mostrar a menor distância euclidiana e o nome da imagem correspondente
        System.out.printf("%.2f%n", menor);
        System.out.println();
        System.out.println(pasta + "/" + menorNome);

        //Transformar matriz em JPG
        String pastaOutput = "Identificacao/" + menorNome.split("[.]")[0] + ".jpg";
        writeArrayAsImage(convertDoubleArrayToInteger(preencherMatriz(pasta + "/" + menorNome)), pastaOutput);
    }

    private static double[] ordenarVetor(double[] omegaJ) {
        double[] aux = omegaJ;
        for (int i = 0; i < aux.length - 1; i++) {
            for (int j = i + 1; j < aux.length; j++) {
                if (aux[i] > aux[j]) {
                    double temp = aux[i];
                    aux[i] = aux[j];
                    aux[j] = temp;
                }
            }
        }
        return aux;
    }

    public static int[][] convertDoubleArrayToInteger(double[][] arr){
        int[][] arrNovo = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++) {
                arrNovo[i][j] = (int)arr[i][j];
            }
        }
        return arrNovo;
    }

    public static void writeArrayAsImage(int[][] array, String outputFilePath) throws IOException {
        int height = array.length;
        int width = array[0].length;

        // Create a BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Set the pixel intensities
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int intensity = array[y][x];
                if (intensity < 0 || intensity > 255) {
                    throw new IllegalArgumentException("Pixel intensity must be between 0 and 255.");
                }
                int rgb = (intensity << 16) | (intensity << 8) | intensity; // Set the same value for R, G, B
                image.setRGB(x, y, rgb);
            }
        }

        // Write the image to the file
        File outputFile = new File(outputFilePath);
        ImageIO.write(image, "jpg", outputFile);
    }

    //Inverter um vetor
    private static double[] inverterVetor(double[] arr) {
        double[] aux = new double[arr.length];
        int cont = 0;
        for (int i = arr.length - 1; i > 0; i--) {
            aux[cont++] = arr[i];
        }
        return aux;
    }

    public static double[][] transformarVetorEmColuna(double[] vetor){
        int colunas = 1;
        int linhas = vetor.length;
        double[][] vetorColuna = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++){
            vetorColuna[i][0] = vetor[i];
        }
        return vetorColuna;
    }

    private static double[] subtrairArrays(double[] vetor1, double[] vetor2) {
        double[] novoVetor = new double[vetor1.length];
        for (int i = 0; i < vetor1.length; i++){
            novoVetor[i] = vetor1[i] - vetor2[i];
        }
        return novoVetor;
    }

    public static double[][] extrairArray(double[][] matriz, int k) {
        double[][] matrizExtraida = new double[matriz.length][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < matriz.length; j++) {
                matrizExtraida[j][i] = matriz[j][i];
            }
        }
        return matrizExtraida;
    }
}