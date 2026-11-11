import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MODO NAO INTERATIVO
    public static void main(String[] args) throws IOException {
        if (!TestesUnitarios.testesUnitarios()) {
            return;
        }
        int func = 0;
        String inputFich = "";
        int k = 0;
        String baseImagens = "";
        String outputFile = "";
        boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false, flag5 = false;
        if (args.length == 0) {
            menu();
        } else {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equals("-f")) {
                    if (i + 1 <= args.length) {
                        func = Integer.valueOf(args[i + 1]);
                        flag1 = true;
                    }
                } else if (args[i].equals("-k")) {
                    if (i + 1 <= args.length) {
                        inputFich = args[i + 1];
                        flag2 = true;
                    }
                } else if (args[i].equals("-i")) {
                    if (i + 1 <= args.length) {
                        k = Integer.valueOf(args[i + 1]);
                        flag3 = true;
                    }
                } else if (args[i].equals("-d")) {
                    if (i + 1 <= args.length) {
                        baseImagens = args[i + 1];
                        flag4 = true;
                    }
                } else {
                    if (i + 1 == args.length) {
                        outputFile = args[i];
                        flag5 = true;
                    }
                }
            }

            if (flag4){
                if (!doesPastaExiste(baseImagens)){
                    return;
                }
            }

            if (!flag5) {
                System.out.println("Ficheiro de output não indicado");
                return;
            }

            if (!validarTipoDeFicheiro(outputFile, "txt")) {
                System.out.println("Ficheiro de output não é do tipo txt. Ficheiro vai ser guardado com tipo txt");
                outputFile += ".txt";
            }

            if (!flag1) {
                System.out.println("Não foi escolhida nenhuma funcionalidade");
                return;
            }

            switch (func) {
                case 1:
                    if (flag2) {
                        if (flag3) {
                            if (validarCaminhoFicheiro(inputFich)){
                                if (k < -1 || k > preencherMatriz(inputFich)[0].length || k == 0){
                                    System.out.println("Número de Eigenfaces inválido");
                                    break;
                                }

                                if(validarMatriz(preencherMatriz(inputFich)) && verificarNumerosFuncionalidade1(preencherMatriz(inputFich))){
                                    funcionalidade1(inputFich, k, 1, outputFile, func);
                                }
                                else{
                                    System.out.println("Matriz Inválida");
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Falta de input: Número de Eigenfaces. Flag: -i");
                            return;
                        }
                    } else {
                        System.out.println("Falta de input: Ficheiro. Flag: -k");
                        break;
                    }
                    break;
                case 2:
                    if (flag4) {
                        if (flag3) {
                            if (!isEmpty(baseImagens)){
                                if (k < -1 || k > new File(baseImagens).listFiles().length || k == 0){
                                    System.out.println("Número de Eigenfaces inválido");
                                    break;
                                }else{
                                    funcionalidade2(baseImagens, k, 1, outputFile, func);
                                }
                            }else{
                                System.out.println("A base de imagens está vazia");
                                break;
                            }
                        } else {
                            System.out.println("Falta de input: Número de Eigenfaces. Flag: -i");
                            break;
                        }
                    } else {
                        System.out.println("Falta de input: Base de imagens. Flag: -d");
                        break;
                    }
                    break;
                case 3:
                    if (flag2) {
                        if (flag4) {
                            if (flag3) {
                                if (!isEmpty(baseImagens)){
                                    if (k < -1 || k > new File(baseImagens).listFiles().length || k == 0){
                                        System.out.println("Número de Eigenfaces inválido");
                                        break;
                                    }else{
                                        funcionalidade3(inputFich, baseImagens, k, 1, outputFile, func);
                                    }
                                }else{
                                    System.out.println("A base de imagens está vazia");
                                    break;
                                }
                            } else {
                                System.out.println("Falta de input: Número de Eigenfaces. Flag: -i");
                                break;
                            }
                        } else {
                            System.out.println("Falta de input: Base de imagens. Flag: -d");
                            break;
                        }
                    } else {
                        System.out.println("Falta de input: Ficheiro. Flag: -k");
                        break;
                    }
                    break;
                case 4:
                    if (flag4) {
                        if (flag3) {
                            if (!isEmpty(baseImagens)){
                                if (k < -1 || k > new File(baseImagens).listFiles().length || k == 0){
                                    System.out.println("Número de Eigenfaces inválido");
                                    break;
                                }else{
                                    funcionalidade4(baseImagens, k, 1, outputFile, func);
                                }
                            }else{
                                System.out.println("A base de imagens está vazia");
                                break;
                            }
                        } else {
                            System.out.println("Falta de input: Número de Eigenfaces. Flag: -i");
                            break;
                        }
                    } else {
                        System.out.println("Falta de input: Base de imagens. Flag: -d");
                        break;
                    }

                    break;
                default:
                    System.out.println("Erro: A funcionalidade escolhida não existe");
            }

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MODO INTERATIVO
    public static void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        // Variável booleana para manter o menu ativo até o utilizador usar a opção sair
        boolean ativo = true;

        final String OUTPUT_FILE = "output.txt";

        while (ativo) {
            System.out.println("****************************************");
            System.out.println("                 MENU           ");
            System.out.println("****************************************");
            System.out.println("  1. Decompor Matriz                  ");
            System.out.println("  2. Reconstruir Imagens              ");
            System.out.println("  3. Obter Imagem Semelhante          ");
            System.out.println("  4. Gerar Nova Imagem                ");
            System.out.println("  5. Sair                           ");
            System.out.println("****************************************");
            System.out.println();
            System.out.print("Escolha uma opção (1-5): ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    // Executa a funcionalidade 1
                    System.out.print("Indique o caminho do ficheiro CSV: ");
                    String ficheiroCSV = scanner.nextLine();

                    if (validarCaminhoFicheiro(ficheiroCSV) && validarMatriz(preencherMatriz(ficheiroCSV)) && verificarNumerosFuncionalidade1(preencherMatriz(ficheiroCSV))) {
                        System.out.print("Quantos valores/vetores próprios devem ser usados? (Escolha um valor entre 1 e " + preencherMatriz(ficheiroCSV)[0].length + " ou -1 para o valor máximo): ");
                        int k;
                        do {
                            k = scanner.nextInt();
                            if (k < -1 || k > preencherMatriz(ficheiroCSV)[0].length || k == 0) System.out.print("Valor de k inválido, tente novamente: ");
                        } while (k < -1 || k > preencherMatriz(ficheiroCSV)[0].length || k == 0);
                        funcionalidade1(ficheiroCSV, k, 0, OUTPUT_FILE, opcao);
                    }
                    break;

                case 2:
                    // Executa a funcionalidade 2
                    System.out.print("Nome da pasta com os ficheiros csv: ");
                    String pasta = scanner.nextLine();
                    if (!doesPastaExiste(pasta)){
                        return;
                    }
                    if (isEmpty(pasta)){
                        System.out.println("Pasta está vazia");
                        return;
                    }
                    System.out.print("Quantas eigenfaces devem ser usadas para reconstrução? (Escolha um valor entre 1 e " + new File(pasta).listFiles().length + " ou -1 para o valor máximo): ");
                    int k1;
                    do {
                        k1 = scanner.nextInt();
                        if (k1 < -1 || k1 > new File(pasta).listFiles().length || k1 == 0) System.out.print("Valor de k inválido, tente novamente: ");
                    } while (k1 < -1 || k1 > new File(pasta).listFiles().length || k1 == 0);
                    funcionalidade2(pasta, k1, 0, OUTPUT_FILE, opcao);
                    break;

                case 3:
                    // Executa a funcionalidade 3
                    System.out.print("Caminho da imagem: ");
                    String img = scanner.nextLine();
                    System.out.print("Nome da pasta com os ficheiros csv: ");
                    String pasta1 = scanner.nextLine();
                    if (!doesPastaExiste(pasta1)){
                        return;
                    }
                    if (isEmpty(pasta1)){
                        System.out.println("Pasta está vazia");
                        return;
                    }
                    System.out.print("Quantas eigenfaces devem ser usadas? (Escolha um valor entre 1 e " + new File(pasta1).listFiles().length + " ou -1 para o valor máximo): ");
                    int k2;
                    do {
                        k2 = scanner.nextInt();
                        if (k2 < -1 || k2 > new File(pasta1).listFiles().length || k2 == 0) System.out.print("Valor de k inválido, tente novamente: ");
                    } while (k2 < -1 || k2 > new File(pasta1).listFiles().length || k2 == 0);
                    funcionalidade3(img, pasta1, k2, 0, OUTPUT_FILE, opcao);
                    break;

                case 4:
                    // Executa a funcionalidade 4
                    System.out.print("Nome da pasta com os ficheiros csv: ");
                    String pasta2 = scanner.nextLine();
                    if (!doesPastaExiste(pasta2)){
                        return;
                    }
                    if (isEmpty(pasta2)){
                        System.out.println("Pasta está vazia");
                        return;
                    }
                    System.out.print("Quantas eigenfaces devem ser usadas para reconstrução? (Escolha um valor entre 1 e " + new File(pasta2).listFiles().length + " ou -1 para o valor máximo): ");
                    int k3;
                    do {
                        k3 = scanner.nextInt();
                        if (k3 < -1 || k3 > new File(pasta2).listFiles().length || k3 == 0) System.out.print("Valor de k inválido, tente novamente: ");
                    } while (k3 < -1 || k3 > new File(pasta2).listFiles().length || k3 == 0);
                    funcionalidade4(pasta2, k3, 0, OUTPUT_FILE, opcao);
                    break;

                case 5:
                    // Termina o código
                    ativo = false;
                    break;

                default:
                    // Mensagem de erro default
                    System.out.println();
                    System.out.println("Não existe essa opção!");
                    System.out.println();
            }
        }
        scanner.close();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FUNCIONALIDADES
    // Funcionalidade 1
    public static void funcionalidade1(String ficheiroCSV, int k, int modo, String outputFile, int opcao) throws IOException {
        int teste = 0;
        // Verifica se a matriz e a quantidade de vetores/valores prórpios é válida
        k = ajustarK(preencherMatriz(ficheiroCSV), k, teste);
        decomporMatriz(ficheiroCSV, k, modo, outputFile, opcao);
    }

    // Funcionalidade 2
    public static void funcionalidade2(String pasta, int k, int modo, String outputFile, int opcao) throws IOException {
        PrintWriter out = new PrintWriter(outputFile);

        String tipoDeFicheiro = "csv";

        File diretorio = new File(pasta);
        File[] ficheiros = diretorio.listFiles();
        if (!validarCaminhoFicheiro(pasta)) return;

        int posicao = 0;
        int numDeFicheiros = ficheiros.length;

        // Validações necessárias
        for (int i = 0; i < numDeFicheiros; i++) {
            if (!validarTipoDeFicheiro(ficheiros[i].getName(), tipoDeFicheiro)) return;
        }

        if (!verificarTamanho(pasta, numDeFicheiros, ficheiros)) return;

        for(int i= 0; i< numDeFicheiros; i++) {
            double[][] matriz = preencherMatriz(pasta + "/" + ficheiros[i].getName());
            if (!verificarNumeros(matriz)) return;
            if(!isSquareMatrix(matriz)) return;
        }
        double[][] matriz = preencherMatriz(pasta + "/" + ficheiros[0].getName());
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
        int teste = 0;

        // Ajuste do K conforme o número que o utilizador introduziu
        k = ajustarK(matrizNova, k, teste);

        double[] vetorMedia = mediaColunas(matrizNova);

        double[][] matrizA = obterMatrizPhi(matrizNova, vetorMedia);
        double[][] matrizAT = transporMatriz(matrizA);

        if (!canMultiplyMatrizes(matrizAT, matrizA)) return;
        double[][] matrizAT_A = multiplicarMatrizes(matrizAT, matrizA);

        double[][] vetoresPropriosAT_A = obterVetoresProprios(matrizAT_A);
        vetoresPropriosAT_A = transporMatriz(vetoresPropriosAT_A);

        if (!canMultiplyMatrizes(matrizA, vetoresPropriosAT_A)) return;
        double[][] vetorU = multiplicarMatrizes(matrizA, vetoresPropriosAT_A);
        vetorU = normalizarColunas(vetorU);

        // Para correr no modo interativo
        if (modo == 0) {
            System.out.println("Vetor µ:");
            for (int i = 0; i < vetorMedia.length; i++) {
                System.out.printf("%.2f ", vetorMedia[i]);
            }
            System.out.println();
            System.out.println();
            System.out.println("Matriz resultante da multiplicação AT * A:");
            mostrarMatriz(matrizAT_A);
            System.out.println();
            System.out.println("Número de eigenfaces:");
            System.out.println(k);
            System.out.println();
        } else {
            // Para correr no modo não interativo
            out.println("Vetor µ:");
            for (int i = 0; i < vetorMedia.length; i++) {
                out.printf("%.2f ", vetorMedia[i]);
            }
            out.println();
            out.println();
            out.println("Matriz resultante da multiplicação AT * A:");
            mostrarMatrizFicheiro(matrizAT_A, out);
            out.println("Número de eigenfaces:");
            out.println(k);
            out.println();
        }

        processarReconstrucao(matrizA, vetorU, vetorMedia, matrizNova, k, ficheiros, modo, out, opcao);
        out.close();
    }

    // Funcionalidade 3
    public static void funcionalidade3(String img, String pasta, int k, int modo, String outputFile, int opcao) throws IOException {
        final double MENOR = 999999999;
        final String MENOR_NOME = "";
        PrintWriter out = new PrintWriter(outputFile);
        String tipoDeFicheiro = "csv";

        // Validações necessárias
        if (!validarCaminhoFicheiro(img)) return;
        if (!validarTipoDeFicheiro(img, tipoDeFicheiro)) return;
        if (!isSquareMatrix(preencherMatriz(img))) return;
        if (!verificarNumeros(preencherMatriz(img))) return;
        if (!validarCaminhoFicheiro(pasta)) return;
        if (isEmpty(pasta)) return;

        File diretorio = new File(pasta);
        File[] ficheiros = diretorio.listFiles();

        int numDeFicheiros = ficheiros.length;

        if (!verificarTamanho(pasta, numDeFicheiros, ficheiros)) return;

        double[] vetorImg = criacaoDeArrayVetor(preencherMatriz(img));

        int tamanho = (int) Math.pow(preencherMatriz((pasta + "/" + ficheiros[0].getName())).length, 2);
        double[][] matrizNova = new double[tamanho][numDeFicheiros];
        for (int j = 0; j < numDeFicheiros; j++) {
            double[][] matriz = preencherMatriz(pasta + "/" + ficheiros[j].getName());
            if (!verificarNumeros(matriz)) return;
            if(!isSquareMatrix(matriz)) return;
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

        int teste = 0;
        k = ajustarK(matrizNova, k, teste);
        vetoresPropriosAT_A = normalizarColunas(vetoresPropriosAT_A);
        double[][] vetoresPropriosAT_AOrdenadosExtraidos = extrairArray(vetoresPropriosAT_A, k);
        double[][] matrizUFatiada = multiplicarMatrizes(matrizA, vetoresPropriosAT_AOrdenadosExtraidos);
        matrizUFatiada = normalizarColunas(matrizUFatiada);

        // Cálculo do vetor omegaNovo
        if (!canSubtrairArrays(vetorImg, vetorMedia)) return;
        double[] fiNova = subtrairArrays(vetorImg, vetorMedia);
        if (!canMultiplyMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiNova))) return;
        double[][] wNovo = multiplicarMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiNova));
        double[] omegaNovo = criacaoDeArrayVetor(wNovo);

        // Ordenar os pesos da imagem introduzida
        omegaNovo = ordenarVetor(omegaNovo);
        omegaNovo = inverterVetor(omegaNovo);

        double menor = MENOR;
        String menorNome = MENOR_NOME;

        // Para correr no modo interativo
        if (modo == 0) {
            System.out.println();
            System.out.println("Número de eigenfaces usados: " + k);
            System.out.println();

            System.out.println("Pesos da imagem introduzida: ");
            // Mostrar os k pesos da imagem introduzida
            for (int a = 0; a < k; a++) {
                System.out.printf("%-8.2f ", omegaNovo[a]);
            }
            System.out.println();
            System.out.println();
        } else {
            // Para correr no modo não interativo
            out.println();
            out.println("Número de eigenfaces usados: " + k);
            out.println();

            out.println("Pesos da imagem introduzida: ");
            // Mostrar os k pesos da imagem introduzida
            for (int a = 0; a < k; a++) {
                out.printf("%-8.2f ", omegaNovo[a]);
            }
            out.println();
            out.println();
        }


        for (int i = 0; i < numDeFicheiros; i++) {
            double soma = 0;
            // Cálculo do vetor omegaJ
            if (!canSubtrairArrays(criacaoDeArrayVetor(preencherMatriz(ficheiros[i].getPath())), vetorMedia)) return;
            double[] fiJ = subtrairArrays(criacaoDeArrayVetor(preencherMatriz(ficheiros[i].getPath())), vetorMedia);
            if (!canMultiplyMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiJ))) return;
            double[][] wJ = multiplicarMatrizes(transporMatriz(matrizUFatiada), transformarVetorEmColuna(fiJ));
            double[] omegaJ = criacaoDeArrayVetor(wJ);

            // Ordenar os pesos de cada imagem
            omegaJ = ordenarVetor(omegaJ);
            omegaJ = inverterVetor(omegaJ);

            // Para correr no modo interativo
            if (modo == 0) {
                System.out.printf("Pesos da imagem \"%s\":%n", ficheiros[i].getName().split("[.]")[0]);
                // Mostrar os k pesos de cada imagem
                for (int a = 0; a < k; a++) {
                    System.out.printf("%-8.2f ", omegaJ[a]);
                }
                System.out.println();
            } else {
                // Para correr no modo não interativo
                out.printf("Pesos da imagem \"%s\":%n", ficheiros[i].getName().split("[.]")[0]);
                // Mostrar os k pesos de cada imagem
                for (int a = 0; a < k; a++) {
                    out.printf("%-8.2f ", omegaJ[a]);
                }
                out.println();
            }

            // Calcular a distância euclidiana
            for (int j = 0; j < k; j++) {
                soma += Math.pow(omegaJ[j] - omegaNovo[j], 2);
            }
            double distanciaEuclidiana = Math.sqrt(soma);

            // Para correr no modo interativo
            if (modo == 0) {
                System.out.printf("Distância da imagem \"%s\" à imagem introduzida: %.2f%n", ficheiros[i].getName().split("[.]")[0], distanciaEuclidiana);
            } else {
                // Para correr no modo não interativo
                out.printf("Distância da imagem \"%s\" à imagem introduzida: %.2f%n", ficheiros[i].getName().split("[.]")[0], distanciaEuclidiana);
            }


            // Verificar qual é a menor distância euclidiana
            if (distanciaEuclidiana < menor) {
                menor = distanciaEuclidiana;
                menorNome = ficheiros[i].getName();
            }

            // Para correr no modo interativo
            if (modo == 0) {
                System.out.println();
            } else {
                // Para correr no modo não interativo
                out.println();
            }
        }

        // Para correr no modo interativo
        if (modo == 0) {
            // Mostrar a menor distância euclidiana e o nome da imagem correspondente
            System.out.printf("Menor distância euclidiana: %.2f%n", menor);
            System.out.println("Caminho da imagem com menor distância: " + pasta + "/" + menorNome);
        } else {
            // Para correr no modo não interativo
            // Mostrar a menor distância euclidiana e o nome da imagem correspondente
            out.printf("Menor distância euclidiana: %.2f%n", menor);
            out.println("Caminho da imagem com menor distância: " + pasta + "/" + menorNome);

        }
        // Transformar matriz em JPG
        String pastaOutput = "Identificacao/" + menorNome.split("[.]")[0] + ".jpg";
        writeArrayAsImage(convertDoubleArrayToInteger(preencherMatriz(pasta + "/" + menorNome)), pastaOutput);
        System.out.println();
        System.out.println("Imagem transformada em JPG: " + pastaOutput);
        System.out.println();
        out.close();
    }

    // Funcionalidade 4
    public static void funcionalidade4(String pasta, int k, int modo, String outputFile, int opcao) throws IOException {
        PrintWriter out = new PrintWriter(outputFile);
        String tipoDeFicheiro = "csv";
        Scanner scanner = new Scanner(System.in);

        if (!validarCaminhoFicheiro(pasta)) return;

        File diretorio = new File(pasta);
        File[] ficheiros = diretorio.listFiles();

        int posicao = 0;
        int numDeFicheiros = ficheiros.length;

        // Validações necessárias
        for (int i = 0; i < numDeFicheiros; i++) {
            if (!validarTipoDeFicheiro(ficheiros[i].getName(), tipoDeFicheiro)) return;
        }

        if (!verificarTamanho(pasta, numDeFicheiros, ficheiros)) return;

        double[][] matriz = preencherMatriz(pasta + "/" + ficheiros[0].getName());
        if (!verificarNumeros(matriz)) return;
        double[][] matrizNova = new double[(int) Math.pow(matriz.length, 2)][numDeFicheiros];
        for (int j = 0; j < matrizNova[0].length; j++) {
            matriz = preencherMatriz(pasta + "/" + ficheiros[j].getName());
            if (!verificarNumeros(matriz)) return;
            if(!isSquareMatrix(matriz)) return;
            double[] matrizVetor = criacaoDeArrayVetor(matriz);
            for (int i = 0; i < matrizVetor.length; i++) {
                matrizNova[i][j] = matrizVetor[posicao];
                posicao++;
            }
            posicao = 0;
        }

        // Ajuste do K conforme o número que o utilizador introduziu
        k = ajustarK(matrizNova, k, opcao);
        System.out.println();
        System.out.printf("Eigenfaces usadas na reconstrução: %d%n", k);

        double[] vetorMedia = mediaColunas(matrizNova);

        double[][] matrizA = obterMatrizPhi(matrizNova, vetorMedia);
        double[][] matrizAT = transporMatriz(matrizA);

        if (!canMultiplyMatrizes(matrizAT, matrizA)) return;
        double[][] matrizAT_A = multiplicarMatrizes(matrizAT, matrizA);

        double[][] vetoresPropriosAT_A = obterVetoresProprios(matrizAT_A);
        vetoresPropriosAT_A = transporMatriz(vetoresPropriosAT_A);
        double[] valoresPropriosAT_A = obterValoresProprios(matrizAT_A, vetoresPropriosAT_A);

        if (!canMultiplyMatrizes(matrizA, vetoresPropriosAT_A)) return;
        double[][] vetorU = multiplicarMatrizes(matrizA, vetoresPropriosAT_A);
        vetorU = normalizarColunas(vetorU);

        double[][] somatorio = new double[vetorU.length][1];

        for (int i = 1; i < k; i++) {
            double Wi = obterValorRandom(valoresPropriosAT_A[i - 1]);
            double[][] Ei = extrairColuna(vetorU, i - 1);
            for (int j = 0; j < Ei.length; j++) {
                somatorio[j][0] += Wi * Ei[j][0];
            }
        }
        for (int i = 0; i < somatorio.length; i++) {
            somatorio[i][0] += vetorMedia[i];
        }

        somatorio = colunaParaMatrizQuadrada(somatorio);

        String diretorioFicheiro = "Output";
        String nomeReconstruido = "_gerada.csv";
        final int func = 4;
        // Para correr no modo interativo
        if (modo == 0) {
            System.out.println();
            System.out.println("Imagem Gerada:");
            mostrarMatriz(somatorio);
        } else {
            // Para correr no modo não interativo
            out.println("Imagem Gerada:");
            mostrarMatrizFicheiro(somatorio, out);
        }
        outputParaFicheiro("imagem", somatorio, diretorioFicheiro, nomeReconstruido, func);

        // Utilizado caso a matriz possua entradas fora do intervalo [0,255]
        if (verificarValoresNumIntervalo(somatorio)) {
            nomeReconstruido = "_gerada_transformada.csv";
            // Para correr no modo interativo
            if (modo == 0) {
                System.out.println("Imagem Gerada Transformada:");
                mostrarMatriz(normalizarMatriz(somatorio));
            } else {
                // Para correr no modo não interativo
                out.println("Imagem Gerada Transformada:");
                mostrarMatrizFicheiro(normalizarMatriz(somatorio), out);
            }
            outputParaFicheiro("imagem", somatorio, diretorioFicheiro, nomeReconstruido, func);
        }
        diretorioFicheiro = "Output/imagem_gerada.jpg";
        writeArrayAsImage(convertDoubleArrayToInteger(somatorio), diretorioFicheiro);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PROCESSAMENTO
    // Extrai uma coluna específica de uma matriz
    public static double[][] extrairColuna(double[][] matriz, int col) {
        double[][] coluna = new double[matriz.length][1];
        for (int j = 0; j < matriz.length; j++) {
            coluna[j][0] = matriz[j][col];
        }
        return coluna;
    }

    // Devolve um valor aleatório num dado intervalo
    public static double obterValorRandom(double valoresProprio) {
        double max = Math.sqrt(valoresProprio);
        double min = -Math.sqrt(valoresProprio);
        return (Math.random() * (max - min)) + min;
    }

    public static double[][] preencherMatriz(String ficheiroCSV) throws FileNotFoundException {
        File csv = new File(ficheiroCSV);
        Scanner sc = new Scanner(csv);

        int numLinhas = 0;
        int numColunas = 0;
        while (sc.hasNextLine()) {
            String linha = sc.nextLine().trim();
            linha = linha.replaceAll(",+", ",");
            if (!linha.isEmpty()) {
                numLinhas++;
                String[] valores = linha.split(",");
                numColunas = Math.max(numColunas, valores.length);
            }
        }

        sc.close();
        double[][] matriz = new double[numLinhas][numColunas];
        sc = new Scanner(csv);

        int row = 0;
        while (sc.hasNextLine()) {
            String linha = sc.nextLine().trim();
            linha = linha.replaceAll(",+", ",");
            if (!linha.isEmpty()) {
                String[] valores = linha.split(",");

                for (int col = 0; col < valores.length; col++) {
                    try {
                        String valorCorrigido = valores[col].replace(" ", "");
                        matriz[row][col] = Double.parseDouble(valorCorrigido.trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao tentar converter o valor: " + valores[col].trim());
                        matriz[row][col] = 0;
                    }
                }
                row++;
            }
        }

        sc.close();
        return matriz;
    }

    // Decomposição da matriz
    private static void decomporMatriz(String ficheiroCSV, int k, int modo, String outputFile, int opcao) throws IOException {
        PrintWriter out = new PrintWriter(outputFile);

        double[][] matriz = preencherMatriz(ficheiroCSV);

        double[][] vetoresProprios = obterVetoresProprios(matriz);

        double[] valoresProprios = obterValoresProprios(matriz, vetoresProprios);

        vetoresProprios = tratamentoDeVetores(vetoresProprios);

        // Para correr no modo interativo
        if (modo == 0) {
            System.out.println();
            System.out.printf("Quantidade de Valores/Vetores próprios usados: %d %n", k);

            System.out.println();


            // Imprime todos os valores próprios usados na decomposição
            System.out.println("Valores próprios usados:");
            for (int i = 0; i < k; i++) {
                System.out.printf("Valor próprio %d: %-5.2f%n", i + 1, valoresProprios[i]);
            }

            System.out.println();

            // Imprime todos os vetores próprios usados na decomposição
            System.out.println("Vetores próprios usados:");
            for (int i = 0; i < k; i++) {
                System.out.print("Vetor próprio " + (i + 1) + ": ");
                for (int j = 0; j < vetoresProprios.length; j++) {
                    System.out.printf("%-6.2f ", vetoresProprios[j][i]);
                }
                System.out.println();
            }
            System.out.println();
        } else {
            // Para correr no modo não interativo
            out.println();
            out.printf("Quantidade de Valores/Vetores próprios usados: %d %n", k);

            out.println();


            // Imprime todos os valores próprios usados na decomposição
            out.println("Valores próprios usados:");
            for (int i = 0; i < k; i++) {
                out.printf("Valor próprio %d: %.2f%n", i + 1, valoresProprios[i]);
            }

            out.println();

            // Imprime todos os vetores próprios usados na decomposição
            out.println("Vetores próprios usados:");
            for (int i = 0; i < k; i++) {
                out.print("Vetor próprio " + (i + 1) + ": ");
                for (int j = 0; j < vetoresProprios.length; j++) {
                    out.printf("%.2f ", vetoresProprios[j][i]);
                }
                out.println();
            }
            out.println();
        }


        // Cria a matriz Pk com os vetores próprios como colunas
        // Cria a matriz diagonal Dk com os valores próprios
        double[][] Pk = fatiarMatriz(vetoresProprios, 0, vetoresProprios.length, 0, k);
        double[][] Dk = criarMatrizDiagonal(fatiarArray(valoresProprios, 0, k));

        if (modo == 0) {
            // Obtém e imprime a matriz reconstruída
            System.out.println("Matriz reconstruída");
            if (!canMultiplyMatrizes(Pk, Dk)) return;
            if (!canMultiplyMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk))) return;
            double[][] Ak = multiplicarMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk));
            mostrarMatriz(Ak);

            System.out.println();

            // Obtém e imprime o Erro Absoluto Médio
            double erro = calcularErroAbsolutoMedio(matriz, Ak);
            System.out.println("Erro Absoluto Médio");
            System.out.printf("%.2f%n", erro);
            System.out.println();
            String diretorioFicheiro = "Output";
            String nomeReconstruido = "_reconstruída.csv";
            outputParaFicheiro(ficheiroCSV, Ak, diretorioFicheiro, nomeReconstruido, opcao);
            System.out.println();
        } else {

            // Obtém e imprime a matriz reconstruída
            out.println("Matriz reconstruída");
            if (!canMultiplyMatrizes(Pk, Dk)) return;
            if (!canMultiplyMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk))) return;
            double[][] Ak = multiplicarMatrizes(multiplicarMatrizes(Pk, Dk), transporMatriz(Pk));
            mostrarMatrizFicheiro(Ak, out);

            out.println();

            // Obtém e imprime o Erro Absoluto Médio
            double erro = calcularErroAbsolutoMedio(matriz, Ak);
            out.println("Erro Absoluto Médio");
            out.printf("%.2f%n", erro);
            out.println();
            String diretorioFicheiro = "Output";
            String nomeReconstruido = "_reconstruída.csv";
            outputParaFicheiro(ficheiroCSV, Ak, diretorioFicheiro, nomeReconstruido, opcao);
            out.println();
        }
        out.close();
    }

    // Escreve a matriz no ficheiro
    private static void mostrarMatrizFicheiro(double[][] matriz, PrintWriter out) throws FileNotFoundException {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                out.printf("%-7.2f ", matriz[i][j]);
            }
            out.println();
        }
    }

    // Obter os vetores próprios de uma dada matriz
    static double[][] obterVetoresProprios(double[][] matriz) {
        EigenDecomposition decomposicao = new EigenDecomposition(toRealMatrix(matriz));
        double[][] vetoresProprios = new double[matriz.length][];
        for (int i = 0; i < matriz.length; i++) {
            vetoresProprios[i] = decomposicao.getEigenvector(i).toArray();
        }
        return vetoresProprios;
    }

    // Obter os valores próprios de uma dada matriz
    static double[] obterValoresProprios(double[][] matriz, double[][] vetoresProprios) {
        EigenDecomposition decomposicao = new EigenDecomposition(toRealMatrix(matriz));
        double[] valoresProprios = decomposicao.getRealEigenvalues();
        valoresProprios = ordenarArray(valoresProprios, vetoresProprios);

        return valoresProprios;
    }

    // Transpor e normalizar uma matriz
    static double[][] tratamentoDeVetores(double[][] vetoresProprios) {
        vetoresProprios = transporMatriz(vetoresProprios);
        vetoresProprios = normalizarColunas(vetoresProprios);
        return vetoresProprios;
    }

    // Cálculo da transposta da matriz
    static double[][] transporMatriz(double[][] matriz) {
        double[][] transposta = new double[matriz[0].length][matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                transposta[j][i] = matriz[i][j];
            }
        }
        return transposta;
    }

    // Normalização dos vetores próprios
    static double[][] normalizarColunas(double[][] matriz) {
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
    static double[][] fatiarMatriz(double[][] matriz, int linhaInicio, int linhaFim, int colunaInicio, int colunaFim) {
        double[][] resultado = new double[linhaFim - linhaInicio][colunaFim - colunaInicio];
        for (int i = linhaInicio; i < linhaFim; i++) {
            for (int j = colunaInicio; j < colunaFim; j++) {
                resultado[i - linhaInicio][j - colunaInicio] = matriz[i][j];
            }
        }
        return resultado;
    }

    // Cria uma matriz diagonal a partir de um array
    static double[][] criarMatrizDiagonal(double[] valores) {
        double[][] matrizDiagonal = new double[valores.length][valores.length];
        // Adiciona os valores próprios à matriz
        for (int i = 0; i < valores.length; i++) {
            matrizDiagonal[i][i] = valores[i];
        }
        return matrizDiagonal;
    }

    // Efetua a multiplicação de matrizes
    static double[][] multiplicarMatrizes(double[][] a, double[][] b) {
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

    // Método para calcular o erro absoluto médio
    static double calcularErroAbsolutoMedio(double[][] original, double[][] reconstruida) {
        double somaErros = 0;
        int totalElementos = original.length;

        for (int i = 0; i < totalElementos; i++) {
            somaErros += Math.abs(original[i][0] - reconstruida[i][0]);
        }

        return somaErros / totalElementos;
    }

    // Método onde se realiza todo o processamento da reconstrução de uma imagem
    public static void processarReconstrucao(double[][] matrizA, double[][] eigenfaces, double[] vetorMedia, double[][] matrizImagensOriginais, int numEigenfaces, File[] ficheiros, int modo, PrintWriter out, int opcao) throws IOException {
        double[][] matrizImagensReconstruidas = new double[matrizImagensOriginais.length][matrizImagensOriginais[0].length];
        for (int i = 0; i < matrizImagensReconstruidas[0].length; i++) {
            double[][] imagemReconstruida = reconstruirImagem(matrizA, eigenfaces, vetorMedia, i, numEigenfaces, modo, out, ficheiros[i].getName().split("[.]")[0]);
            double[][] imagemReconstruidaAux = colunaParaMatrizQuadrada(imagemReconstruida);

            for (int j = 0; j < matrizImagensReconstruidas.length; j++) {
                matrizImagensReconstruidas[j][i] = imagemReconstruida[j][0];
            }

            double[][] imagemOriginal = extrairColunaComoMatriz(matrizImagensOriginais, i);
            double EAM = calcularErroAbsolutoMedio(imagemReconstruida, imagemOriginal);

            String diretorioFicheiro = "Eigenfaces";
            String nomeReconstruido = "_reconstruida.csv";
            outputParaFicheiro(ficheiros[i].getName(), imagemReconstruidaAux, diretorioFicheiro, nomeReconstruido, opcao);

            // Utilizado caso a matriz possua entradas fora do intervalo [0,255]
            if (verificarValoresNumIntervalo(imagemReconstruidaAux)) {
                normalizarMatriz(imagemReconstruidaAux);
                nomeReconstruido = "_reconstruida_transformada.csv";
                outputParaFicheiro(ficheiros[i].getName(), imagemReconstruidaAux, diretorioFicheiro, nomeReconstruido, opcao);
            }

            diretorioFicheiro = "ImagensReconstruídas/" + ficheiros[i].getName().split("[.]")[0] + ".jpg";
            writeArrayAsImage(convertDoubleArrayToInteger(imagemReconstruidaAux), diretorioFicheiro);
        }
    }

    // Normaliza a matriz caso esta possua valores fora do intervalo [0, 255]
    public static double[][] normalizarMatriz(double[][] imagemReconstruidaAux) {
        final int MAX = 255;
        double maximo = imagemReconstruidaAux[0][0];
        double minimo = imagemReconstruidaAux[0][0];
        for (int i = 0; i < imagemReconstruidaAux.length; i++) {
            for (int j = 0; j < imagemReconstruidaAux[0].length; j++) {
                if (imagemReconstruidaAux[i][j] > maximo) {
                    maximo = imagemReconstruidaAux[i][j];
                } else if (imagemReconstruidaAux[i][j] < minimo) {
                    minimo = imagemReconstruidaAux[i][j];
                }
            }
        }

        for (int i = 0; i < imagemReconstruidaAux.length; i++) {
            for (int j = 0; j < imagemReconstruidaAux[0].length; j++) {
                imagemReconstruidaAux[i][j] = MAX * ((imagemReconstruidaAux[i][j] - minimo) / (maximo - minimo));
            }
        }
        return imagemReconstruidaAux;
    }

    // Reconstruir Imagens
    public static double[][] reconstruirImagem(double[][] matrizA, double[][] eigenfaces, double[] vetorMedia, int index, int numEigenfaces, int modo, PrintWriter out, String name) {
        // Para correr no modo interativo
        if (modo == 0) {
            System.out.printf("Pesos utilizados da imagem \"%s\":%n", name);

            double[][] matrizSomatorio = new double[vetorMedia.length][1];
            double[][] matrizVetorA = extrairColunaComoMatriz(matrizA, index);

            for (int i = 1; i < numEigenfaces; i++) {
                double[][] matrizVetorUj = extrairColunaComoMatriz(eigenfaces, i - 1);
                double[][] matrizVetorUjT = transporMatriz(matrizVetorUj);

                double[][] matrizVetorPesos = multiplicarMatrizes(matrizVetorUjT, matrizVetorA);

                System.out.printf("%-8.2f ", matrizVetorPesos[0][0]);

                double[][] matrizVetorUjWj = multiplicarMatrizes(matrizVetorUj, matrizVetorPesos);
                for (int j = 0; j < matrizVetorUjWj.length; j++) {
                    matrizSomatorio[j][0] += matrizVetorUjWj[j][0];
                }
            }
            System.out.println();
            double[][] imagemReconstruida = new double[vetorMedia.length][1];
            for (int i = 0; i < vetorMedia.length; i++) {
                imagemReconstruida[i][0] = vetorMedia[i] + matrizSomatorio[i][0];
            }

            return imagemReconstruida;
        } else {
            // Para correr no modo não interativo
            out.printf("Pesos utilizados da imagem \"%s\":%n", name);

            double[][] matrizSomatorio = new double[vetorMedia.length][1];
            double[][] matrizVetorA = extrairColunaComoMatriz(matrizA, index);

            for (int i = 1; i < numEigenfaces; i++) {
                double[][] matrizVetorUj = extrairColunaComoMatriz(eigenfaces, i - 1);
                double[][] matrizVetorUjT = transporMatriz(matrizVetorUj);

                double[][] matrizVetorPesos = multiplicarMatrizes(matrizVetorUjT, matrizVetorA);

                out.printf("%-8.2f ", matrizVetorPesos[0][0]);

                double[][] matrizVetorUjWj = multiplicarMatrizes(matrizVetorUj, matrizVetorPesos);
                for (int j = 0; j < matrizVetorUjWj.length; j++) {
                    matrizSomatorio[j][0] += matrizVetorUjWj[j][0];
                }
            }
            out.println();
            double[][] imagemReconstruida = new double[vetorMedia.length][1];
            for (int i = 0; i < vetorMedia.length; i++) {
                imagemReconstruida[i][0] = vetorMedia[i] + matrizSomatorio[i][0];
            }

            return imagemReconstruida;
        }

    }

    // Transforma uma matriz coluna numa matriz quadrada
    public static double[][] colunaParaMatrizQuadrada(double[][] matrizColuna) {
        double[] vetor = criacaoDeArrayVetor(matrizColuna);
        double[][] matrizQuadrada = new double[(int) Math.sqrt(vetor.length)][(int) Math.sqrt(vetor.length)];
        int cont = 0;
        for (int i = 0; i < matrizQuadrada.length; i++) {
            for (int j = 0; j < matrizQuadrada[0].length; j++) {
                matrizQuadrada[i][j] = vetor[cont++];
            }
        }
        return matrizQuadrada;
    }

    // Extrai coluna de uma matriz em forma de matriz coluna
    static double[][] extrairColunaComoMatriz(double[][] matriz, int coluna) {
        double[][] resultado = new double[matriz.length][1];
        for (int i = 0; i < matriz.length; i++) {
            resultado[i][0] = matriz[i][coluna];
        }
        return resultado;
    }

    // Calcula o vetor média (coluna)
    static double[] mediaColunas(double[][] matrizM) {
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

    // Obtem a matriz composta por vários phi's
    static double[][] obterMatrizPhi(double[][] matrizM, double[] vetorMedia) {
        double[][] matrizA = new double[matrizM.length][matrizM[0].length];
        for (int i = 0; i < matrizM.length; i++) {
            for (int j = 0; j < matrizM[0].length; j++) {
                matrizA[i][j] = matrizM[i][j] - vetorMedia[i];
            }
        }
        return matrizA;
    }

    // Cria um array com os valores de Dk
    static double[] fatiarArray(double[] array, int inicio, int fim) {
        double[] resultado = new double[fim - inicio];
        for (int i = inicio; i < fim; i++) {
            resultado[i - inicio] = array[i];
        }
        return resultado;
    }

    // Ordena os valores e vetores próprios
    static double[] ordenarArray(double[] array, double[][] matriz) {
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

    // Corta os espaços de uma String que contém as linhas de um ficheiro separadas por |
    static String cortarEspacos(String matrizStr) {
        // Remove espaços diretamente
        matrizStr = matrizStr.replace(" ", "");

        // Remove múltiplas vírgulas consecutivas
        while (matrizStr.contains(",,")) {
            matrizStr = matrizStr.replace(",,", ",");
        }

        return matrizStr;
    }

    // Corta os espaços entre números (23 41 -> 2341)
    static double tirarEspacos(String valore) {
        double num = 0;
        for (int i = 0; i < valore.length(); i++) {
            if (valore.charAt(i) != ' ') {
                num = num * 10 + Character.getNumericValue(valore.charAt(i));
            }
        }
        return num;
    }

    // Converte de double para Real Matrix
    public static RealMatrix toRealMatrix(double[][] matriz) {
        return MatrixUtils.createRealMatrix(matriz);
    }

    // Tranformação de matrizes em vetores
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

    // Ordena um dado vetor por ordem crescente
    public static double[] ordenarVetor(double[] omegaJ) {
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

    // Converte um array bidimensional do tipo double para o tipo inteiro
    public static int[][] convertDoubleArrayToInteger(double[][] arr) {
        int[][] arrNovo = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arrNovo[i][j] = (int) arr[i][j];
            }
        }
        return arrNovo;
    }

    // Transforma uma matriz do tipo inteiro num ficheiro JPG
    public static void writeArrayAsImage(int[][] array, String outputFilePath) throws IOException {
        int height = array.length;
        int width = array[0].length;

        // Cria a BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Define as intensidades dos pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int intensity = array[y][x];
                if (intensity < 0 || intensity > 255) {
                    throw new IllegalArgumentException("A intensidade do pixel deve estar entre 0 e 255.");
                }
                int rgb = (intensity << 16) | (intensity << 8) | intensity; // Define o mesmo valor para R, G, B
                image.setRGB(x, y, rgb);
            }
        }

        // Escreve a imagem no ficheiro
        File outputFile = new File(outputFilePath);
        ImageIO.write(image, "jpg", outputFile);
    }

    // Inverter um vetor
    public static double[] inverterVetor(double[] arr) {
        double[] aux = new double[arr.length];
        int cont = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            aux[cont++] = arr[i];
        }
        return aux;
    }

    // Transforma uma linha em coluna (Transpor um array em uma matriz)
    public static double[][] transformarVetorEmColuna(double[] vetor) {
        int colunas = 1;
        int linhas = vetor.length;
        double[][] vetorColuna = new double[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            vetorColuna[i][0] = vetor[i];
        }
        return vetorColuna;
    }

    // Subtração de dois vetores
    public static double[] subtrairArrays(double[] vetor1, double[] vetor2) {
        double[] novoVetor = new double[vetor1.length];
        for (int i = 0; i < vetor1.length; i++) {
            novoVetor[i] = vetor1[i] - vetor2[i];
        }
        return novoVetor;
    }

    // Devolve as k colunas de um dado vetor
    public static double[][] extrairArray(double[][] matriz, int k) {
        double[][] matrizExtraida = new double[matriz.length][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < matriz.length; j++) {
                matrizExtraida[j][i] = matriz[j][i];
            }
        }
        return matrizExtraida;
    }

    // Mostra uma dada matriz
    public static void mostrarMatriz(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%-7.2f ", matriz[i][j]);
            }
            System.out.println();
        }
    }

    // Escreve uma matriz num ficheiro CSV
    public static void outputParaFicheiro(String nomeFicheiro, double[][] imagemReconstruida, String diretorioFicheiro, String nomeReconstruido, int opcao) throws IOException {
        // Obtém o valor do caminho do ficheiro csv
        File ficheiro = new File(nomeFicheiro);
        String nomeSemCsv = ficheiro.getName().replaceFirst("[.][^.]+$", "");

        // Define o caminho de saída do ficheiro
        String nomeArquivo = diretorioFicheiro + "/" + nomeSemCsv + nomeReconstruido;
        File arquivoSaida = new File(nomeArquivo);

        // Cria o arquivo de escrita caso o mesmo não exista
        if (!arquivoSaida.exists()) {
            arquivoSaida.createNewFile();
        }

        // Escreve no ficheiro CSV os valores da matriz Ak
        PrintWriter printWriter = new PrintWriter(nomeArquivo);

        for (int j = 0; j < imagemReconstruida.length; j++) {
            for (int l = 0; l < imagemReconstruida[j].length; l++) {
                // Escreve os valores da matriz com auxílio do "." (funcionalidade 1)
                if (opcao == 1) {
                    printWriter.printf(String.format(Locale.US, "%.2f", imagemReconstruida[j][l]));
                } else {
                    // Escreve os valores da matriz com auxílio da "," (restantes funcionalidades)
                    printWriter.printf("%.0f", imagemReconstruida[j][l]);
                }
                // Adiciona uma "," entre valores
                if (l < imagemReconstruida[j].length - 1) {
                    printWriter.print(",");
                }
            }
            printWriter.println();
        }
        printWriter.close();
        System.out.println("Matriz reconstruída guardada na pasta '" + diretorioFicheiro + "' com o nome '" + nomeArquivo + "'");
        System.out.println();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VERIFICACOES

    private static boolean doesPastaExiste(String pasta){
        File f = new File(pasta);
        if (!f.exists()){
            System.out.println("Pasta " + pasta + " não existe");
            return false;
        }else{
            if (!f.isDirectory()){
                System.out.println("Pasta " + pasta + " não é um diretório");
                return false;
            }
        }
        return true;
    }


    // Ajusta o valor de k consoante a matriz de um dado ficheiro
    static int ajustarK(double[][] matriz, int k, int teste) throws FileNotFoundException {
        if (k == -1 || k > matriz[0].length) {
            if (teste == 1)
                System.out.println("AVISO: quantidade de valores/vetores próprios ajustada para a quantidade máxima");
            k = matriz[0].length;
        }
        return k;
    }

    // Valida se o caminho fornecido existe
    static boolean validarCaminhoFicheiro(String ficheiroCSV) {
        File file = new File(ficheiroCSV);
        if (!file.exists()) {
            System.out.println("O caminho indicado não existe");
            return false;
        }
        return true;
    }

    // Verifica se o ficheiro enviado por parâmetro é do tipo enviado
    static boolean validarTipoDeFicheiro(String nomeDoFicheiro, String tipoDeFicheiro) {
        String[] tipoDoFicheiro = nomeDoFicheiro.split("[.]");
        return tipoDoFicheiro[tipoDoFicheiro.length - 1].equals(tipoDeFicheiro);
    }

    // Verifica se o tamanho de todas as matrizes de uma pasta é igual
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

    // Verifica se uma dada matriz é quadrada
    public static boolean isSquareMatrix(double[][] matriz) {
        if (matriz.length != matriz[0].length) {
            System.out.println("A matriz não é quadrada");
            return false;
        }
        return true;
    }

    // Verifica se uma dada matriz quadrada tem índice menor ou igual a 256
    public static boolean isSquareMatrixSmallerThan(double[][] matriz, int maxTamanho) {
        if (matriz.length > maxTamanho) {
            System.out.println("O tamanho da matriz é superior a 256!");
            return false;
        }
        return true;
    }

    // Verifica se uma matriz é simétrica
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

    // Efetua todas as validações necessárias de uma dada matriz
    static boolean validarMatriz(double[][] matriz) throws FileNotFoundException {
        if (!isSquareMatrix(matriz)) return false;

        if (!isSquareMatrixSmallerThan(matriz, 256)) return false;

        if (!isSquareMatrixSimetric(matriz)) return false;

        return true;
    }

    // Verifica se uma pasta está vazia
    public static boolean isEmpty(String pasta) throws IOException {
        File dir = new File(pasta);
        File[] ficheiros = dir.listFiles();
        if (!dir.exists()) {
            Files.createDirectories(Path.of(pasta));
        }
        if (ficheiros != null) {
            if (ficheiros.length == 0) {
                return true;
            }
        }

        return false;
    }

    // Verifica se é possível fazer a subtração de duas matrizes
    private static boolean canSubtrairArrays(double[] vetor1, double[] vetor2) {
        if (vetor1.length != vetor2.length) {
            System.out.println("Os vetores não têm o mesmo tamanho (Subtração de matrizes não pode ser realizada)");
            return false;
        }
        return true;
    }

    // Verifica se é possível fazer a multiplicação de duas matrizes
    private static boolean canMultiplyMatrizes(double[][] matrizA, double[][] matrizB) {
        // Compara o número de colunas da matriz A com o número de linhas da matriz B
        if (matrizA[0].length != matrizB.length) {
            return false;
        }
        return true;
    }

    public static boolean verificarNumeros(double[][] matriz) {
        final int LIMITEMINIMO = 0;
        final int LIMITEMAXIMO = 255;

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                double valor = matriz[i][j];
                if ((valor % 1) != 0) {
                    System.out.println("O ficheiro csv apresenta erros: Valores não inteiros encontrados");
                    return false;
                }
                if (valor < LIMITEMINIMO || valor > LIMITEMAXIMO) {
                    System.out.println("O ficheiro csv apresenta erros: Valores fora do intervalo");
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean verificarNumerosFuncionalidade1(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                double valor = matriz[i][j];
                if ((valor % 1) != 0) {
                    System.out.println("O ficheiro csv apresenta erros: Valores não inteiros encontrados");
                    return false;
                }
            }
        }
        return true;
    }




    // Verifica se a matriz possui valores fora do intervalo [0, 255]
    public static boolean verificarValoresNumIntervalo(double[][] imagemReconstruida) {
        for (int i = 0; i < imagemReconstruida.length; i++) {
            for (int j = 0; j < imagemReconstruida[0].length; j++) {
                if (imagemReconstruida[i][j] > 255 || imagemReconstruida[i][j] < 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
