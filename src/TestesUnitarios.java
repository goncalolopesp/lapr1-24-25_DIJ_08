import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestesUnitarios {

    public static void main(String[] args) throws IOException {
        System.out.println(testesUnitarios() ? "Todos os testes passaram" : "Há testes a falhar");
    }

    static boolean testesUnitarios() throws IOException {
        double[][] matrizA = {{1, 2}, {2, 1}};
        double[][] matrizB = {{1, 3}, {3, 1}};

        // Testar Multiplicação
        {
            double[][] matrizC = {{7, 5}, {5, 7}};
            if (!testarMultiplicacao(matrizA, matrizB, matrizC)) {
                System.out.println("Multiplicação está errada!");
                return false;
            }
        }

        // Testar Transposição
        {
            double[][] matrizD = {{1, 2}, {2, 1}};
            if (!testarTransposta(matrizA, matrizD)) {
                System.out.println("Transposição errada!");
                return false;
            }
        }

        // Testar Subtração
        {
            double[][] matrizE = {{0, -1}, {-1, 0}};
            if (!testarSubtracao(matrizA, matrizB, matrizE)) {
                System.out.println("Subtração errada");
                return false;
            }
        }

        // Testar a criação de um vetor de média de colunas
        {
            double[] vetorMedia = {1.5, 1.5};
            if (!testarMedia(matrizA, vetorMedia)) {
                System.out.println("Media errada");
                return false;
            }
        }

        // Testar ordenação de vetores
        {
            double[] ordenada = {1, 1, 3, 3};
            if (!testarOrdenar(Main.criacaoDeArrayVetor(matrizB), ordenada)) {
                System.out.println("Ordenação errada");
                return false;
            }
        }

        // Testar a criação de uma matriz diagonal
        {
            double[][] matrizDiagonal = {{2, 0}, {0, 2}};
            double[] vetorParaTransformarEmDiagonal = {2, 2};
            if (!testarTransformarEmDiagonal(vetorParaTransformarEmDiagonal, matrizDiagonal)) {
                System.out.println("Transformação em matriz diagonal errada");
                return false;
            }
        }

        // Testar a criação de uma matriz quadrada a partir de um array
        {
            double[][] matrizColuna = {{1}, {2}, {3}, {4}};
            double[][] matrizQuadradaEsperada = {{1, 2}, {3, 4}};
            if (!testarTransformarEmMatrizQuadrada(matrizColuna, matrizQuadradaEsperada)) {
                System.out.println("Transformação em matriz quadrada errada");
                return false;
            }
        }

        // Testar preencher matriz
        {
            String nomeDoFicheiroParaTestar = "inputTeste.csv";
            double[][] matrizPreenchidaEsperada = {{1, 2}, {2, 1}};
            if (!testarPreencherMatriz(nomeDoFicheiroParaTestar, matrizPreenchidaEsperada)) {
                System.out.println("Preenchimento da matriz errado");
                return false;
            }
        }

        // Testar a fatiação de uma matriz
        {
            double[][] matrizCompleta = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
            double[][] matrizFatia = {{1, 2}, {1, 2}};
            int linhaInicio = 0;
            int linhaFim = 2;
            int colunaInicio = 0;
            int colunaFim = 2;
            if (!testarFatiarMatriz(matrizCompleta, linhaInicio, linhaFim, colunaInicio, colunaFim, matrizFatia)) {
                System.out.println("Fatiamento da matriz errado");
                return false;
            }
        }

        // Testar a fatiação de um array
        {
            double[] vetorCompleto = {1, 2, 3, 4};
            double[] vetorFatia = {1, 2};
            int inicio = 0;
            int fim = 2;
            if (!testarFatiarArray(vetorCompleto, inicio, fim, vetorFatia)) {
                System.out.println("Fatiamento do array errado");
                return false;
            }
        }

        // Testar a validação do tipo de ficheiro
        {
            String nome = "testeUnit.csv";
            String tipo = "csv";
            boolean expected = true;
            if (testarValidarTipoDeFicheiro(nome, tipo) != expected) {
                System.out.println("Erro na validação do tipo de ficheiro");
                return false;
            }
        }


        // Testar a verificação se uma pasta está vazia
        {
            String nomeDaPasta = "testeUnitario";
            boolean expected = true;
            if (testarisEmpty(nomeDaPasta) != expected) {
                System.out.println("Erro a verificar se a pasta está vazia");
                return false;
            }
        }


        // Testar a verificação do caminho do ficheiro
        {
            String caminhoParaOFicheiro = "inputTeste.csv";
            boolean expected = true;
            if (testarValidarCaminhoParaFicheiro(caminhoParaOFicheiro) != expected) {
                System.out.println("Erro na verificação do caminho de um ficheiro");
                return false;
            }
        }

        // Testar a transformação de vetor em matriz coluna
        {
            double[] vetor = {1, 2, 3};
            double[][] esperado = {{1}, {2}, {3}};
            if (!testarTransformarVetorEmMatrizColuna(vetor, esperado)) {
                System.out.println("Erro na transformação de um vetor em uma matriz coluna");
                return false;
            }
        }

        // Testar o método de tirar espaços na String de um número
        {
            String fichCsv = "8    8";
            double esperado = 88;
            if (!testarTirarEspacos(fichCsv, esperado)) {
                System.out.println("Erro a tirar os espaços");
                return false;
            }
        }

        // Testar o método de cortar espaços num ficheiro em forma de String
        {
            String fichCSV = "8   8,  ,,,  121 | 3,,,,,,,,,,,, 5";
            String esperado = "88,121|3,5";
            if (!testarCortarEspacos(fichCSV, esperado)) {
                System.out.println("Erro ao cortar espaços");
                return false;
            }
        }

        // Testar a verificação do tamanho das matrizes dos ficheiros de uma pasta
        {
            String pasta = "input";
            if (!testarVerificarTamanho(pasta)) {
                System.out.println("Os ficheiros da dada pasta não têm o mesmo tamanho");
                return false;
            }
        }

        // Testar a criação da matriz Phi
        {
            double[][] arr = {{3, 3}, {3, 3}};
            double[] med = {1, 1};
            double[][] expected = {{2, 2}, {2, 2}};
            if (!testarObterMatrizPhi(arr, med, expected)) {
                System.out.println("Erro na criação da matriz Phi");
                return false;
            }
        }

        // Testar a inversão de um vetor
        {
            double[] vet = {1, 2, 3, 4};
            double[] expected = {4, 3, 2, 1};
            if (!testarInversaoVetor(vet, expected)) {
                System.out.println("Erro ao inverter vetor");
                return false;
            }
        }

        // Testar a conversão de uma matriz double para uma matriz inteira
        {
            double[][] arr = {{1.7, 2.4}, {1.5, 4.6}};
            int[][] expected = {{1, 2}, {1, 4}};
            if (!testarConversao(arr, expected)) {
                System.out.println("Erro na conversão de double para int");
                return false;
            }
        }

        // Testar a extração de matrizes
        {
            double[][] arr = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
            double[][] expected = {{1, 2}, {4, 5}, {7, 8}};
            if (!testarExtracao(arr, 2, expected)) {
                System.out.println("Erro na extração da matriz");
                return false;
            }
        }

        // Testar as verificações
        {
            double[][] matriz = {{1, 2}, {2, 1}};
            if (!testarValidacaoMatriz(matriz)) {
                System.out.println("Erro na validação das matrizes");
                return false;
            }
        }

        // Testar o método ordenarArray
        {
            double[] arr = {5, 4, 2, 6};
            double[][] matriz = {{3, 5, 6, 1}, {6, 3, 6, 2}, {1, 2, 3, 4}, {8, 5, 4, 6}};
            double[] expectedArray = {6, 5, 4, 2};
            double[][] expectedMatriz = {{8, 5, 4, 6}, {3, 5, 6, 1}, {6, 3, 6, 2}, {1, 2, 3, 4}};

            if (!testarOrdenarArray(arr, matriz, expectedArray, expectedMatriz)) {
                System.out.println("Erro no método ordenarArray");
                return false;
            }

        }

        // Testar conjunto de valores
        {
            if (!testarConjunto(matrizA, matrizB)) {
                System.out.println("Erro no método verificarNumeros");
                return false;
            }
        }

        // Testar o ajuste de K
        {
            final int K = -1;
            if (!testarAjusteK(matrizA, K)) {
                System.out.println("Erro no método ajustarK");
                return false;
            }
        }

        // Testar extrair coluna como matriz
        {
            if (!testarExtrairColuna(matrizA)) {
                System.out.println("Erro no método extrairColunaComoMatriz");
                return false;
            }
        }

        // Testar EAM
        {
            double[][] matrizReconstruida = {{1, 2}, {2, 1}};
            if (!testarEAM(matrizA, matrizReconstruida)) {
                System.out.println("Erro no método cacularErroAbsolutoMedio");
                return false;
            }
        }

        // Testar a devolução de um valor random dentro de um certo intervalo
        {
            final int VALORPROPRIOEXEMPLO = 9;
            if (!testarDevolucaoDeValorRandom(VALORPROPRIOEXEMPLO)) {
                System.out.println("Erro no método obterValorRandom");
                return false;
            }
        }

        // Testar a normalização das colunas (que correspondem a vetores próprios) de uma matriz
        {
            if (!testarNormalizacaoColunas(matrizA)) {
                System.out.println("Erro no método normalizarColunas");
                return false;
            }
        }

        // Testar a normalização de uma matriz devido à existência de entradas fora do intervalo [0,255]
        {
            double[][] matrizTeste = {{-1, 1}, {2, 256}};
            if (!testarNormalizacaoMatrizes(matrizTeste)) {
                System.out.println("Erro no método normalizarMatriz");
                return false;
            }
        }

        // Testar a geração de valores e vetores próprios
        {
            double[][] matrizTeste = {{1, 3}, {3, 1}};
            int numDeValoresProprios = 2;
            double[] expectedValoresProprios = {4, -2};
            double[][] expectedVetoresProprios = {{0.71, 0.71}, {0.71, -0.71}};

            if (!testarGeracaoValoresEVetoresProprios(matrizTeste, numDeValoresProprios, expectedValoresProprios, expectedVetoresProprios)){
                System.out.println("Erro na geração de valores e vetores próprios");
                return false;
            }

        }

        return true;
    }

    private static boolean testarGeracaoValoresEVetoresProprios(double[][] matrizTeste, int numDeValoresProprios, double[] expectedValoresProprios, double[][] expectedVetoresProprios) {

        double[][] vetoresProprios = Main.obterVetoresProprios(matrizTeste);

        double[] valoresProprios = Main.obterValoresProprios(matrizTeste, vetoresProprios);
        vetoresProprios = Main.tratamentoDeVetores(vetoresProprios);
        if (valoresProprios.length != expectedValoresProprios.length){
            return false;
        }
        if (vetoresProprios.length != expectedVetoresProprios.length){
            return false;
        }
        if (vetoresProprios[0].length != expectedVetoresProprios[0].length){
            return false;
        }

        for (int i = 0; i < valoresProprios.length; i++) {
            valoresProprios[i] = Math.round(valoresProprios[i]);
        }

        for (int i = 0; i < valoresProprios.length; i++) {
            if (valoresProprios[i] != expectedValoresProprios[i]){
                return false;
            }
        }

        for (int i = 0; i < vetoresProprios.length; i++) {
            for (int j = 0; j < vetoresProprios[i].length; j++) {
                vetoresProprios[i][j] = Math.round(vetoresProprios[i][j] * 100.0) / 100.0;
            }
        }

        for (int i = 0; i < vetoresProprios.length; i++) {
            for (int j = 0; j < vetoresProprios[i].length; j++) {
                if (vetoresProprios[i][j] != expectedVetoresProprios[i][j]) {
                    System.out.println("erro 5");
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean testarMultiplicacao(double[][] matrizA, double[][] matrizB, double[][] esperado) {

        double[][] resultado = Main.multiplicarMatrizes(matrizA, matrizB);

        if (esperado.length != resultado.length || esperado[0].length != resultado[0].length) {
            return false;
        }

        for (int i = 0; i < esperado.length; i++) {
            for (int j = 0; j < esperado[0].length; j++) {
                if (esperado[i][j] != resultado[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarOrdenarArray(double[] arr, double[][] matriz, double[] expectedArray, double[][] expectedMatriz) {
        double[] resultado = Main.ordenarArray(arr, matriz);
        if (matriz.length != expectedMatriz.length || matriz[0].length != expectedMatriz[0].length) {
            return false;
        }

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != expectedMatriz[i][j]) {
                    return false;
                }
            }
        }

        if (resultado.length != expectedArray.length) {
            return false;
        }

        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] != expectedArray[i]) {
                return false;
            }
        }

        return true;
    }

    private static boolean testarValidacaoMatriz(double[][] matriz) throws FileNotFoundException {
        if (!Main.validarMatriz(matriz)) {
            return false;
        }
        return true;
    }

    private static boolean testarExtracao(double[][] arr, int k, double[][] expected) {

        double[][] result = Main.extrairArray(arr, k);
        if (result.length != expected.length || result[0].length != expected[0].length) {
            return false;
        }

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (result[i][j] != expected[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean testarConversao(double[][] arr, int[][] expected) {
        int[][] result = Main.convertDoubleArrayToInteger(arr);
        if (result.length != expected.length || result[0].length != expected[0].length) {
            return false;
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (result[i][j] != expected[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarInversaoVetor(double[] vet, double[] expected) {
        double[] result = Main.inverterVetor(vet);
        if (result.length != expected.length) {
            return false;
        }

        for (int i = 0; i < result.length; i++) {
            if (result[i] != expected[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean testarObterMatrizPhi(double[][] arr, double[] med, double[][] expected) {
        double[][] result = Main.obterMatrizPhi(arr, med);
        if (result.length != expected.length || result[0].length != expected[0].length) {
            return false;
        }

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                if (result[i][j] != expected[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean testarVerificarTamanho(String pasta) throws FileNotFoundException {
        File dir = new File(pasta);
        File[] ficheiros = dir.listFiles();
        if (!Main.verificarTamanho(pasta, ficheiros.length, ficheiros)) {
            return false;
        }
        return true;
    }

    private static boolean testarCortarEspacos(String fichCSV, String esperado) {
        if (!(Main.cortarEspacos(fichCSV).equals(esperado))) {
            return false;
        }
        return true;
    }

    private static boolean testarTirarEspacos(String fichCsv, double esperado) {
        if (!(Main.tirarEspacos(fichCsv) == esperado)) {
            return false;
        }
        return true;
    }

    private static boolean testarTransformarVetorEmMatrizColuna(double[] vetor, double[][] esperado) {

        double[][] resultado = Main.transformarVetorEmColuna(vetor);

        if (esperado.length != resultado.length || resultado[0].length != esperado[0].length) {
            return false;
        }

        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[i].length; j++) {
                if (resultado[i][j] != esperado[i][j]) {
                    return false;
                }
            }
        }

        return true;

    }

    private static boolean testarValidarCaminhoParaFicheiro(String caminhoParaOFicheiro) {
        if (!Main.validarCaminhoFicheiro(caminhoParaOFicheiro)) {
            return false;
        }
        return true;
    }

    private static boolean testarisEmpty(String nomeDaPasta) throws IOException {
        if (Main.isEmpty(nomeDaPasta)) {
            return true;
        }
        return false;
    }

    private static boolean testarValidarTipoDeFicheiro(String nome, String tipo) {
        if (!Main.validarTipoDeFicheiro(nome, tipo)) {
            return false;
        }
        return true;
    }

    private static boolean testarFatiarArray(double[] vetorCompleto, int inicio, int fim, double[] vetorFatia) {
        double[] resultado = Main.fatiarArray(vetorCompleto, inicio, fim);
        if (resultado.length != vetorFatia.length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] != vetorFatia[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean testarFatiarMatriz(double[][] matrizCompleta, int linhaInicio, int linhaFim, int colunaInicio, int colunaFim, double[][] matrizFatia) {
        double[][] resultado = Main.fatiarMatriz(matrizCompleta, linhaInicio, linhaFim, colunaInicio, colunaFim);
        if (resultado.length != matrizFatia.length || resultado[0].length != matrizFatia[0].length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[0].length; j++) {
                if (resultado[i][j] != matrizFatia[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarPreencherMatriz(String nomeDoFicheiroParaTestar, double[][] matrizPreenchidaEsperada) throws IOException {
        double[][] resultado = Main.preencherMatriz(nomeDoFicheiroParaTestar);
        if (resultado.length != matrizPreenchidaEsperada.length || resultado[0].length != matrizPreenchidaEsperada[0].length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[0].length; j++) {
                if (resultado[i][j] != matrizPreenchidaEsperada[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarTransformarEmMatrizQuadrada(double[][] matrizColuna, double[][] matrizQuadradaEsperada) {
        double[][] resultado = Main.colunaParaMatrizQuadrada(matrizColuna);
        if (resultado.length != matrizQuadradaEsperada.length || resultado[0].length != matrizQuadradaEsperada[0].length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[0].length; j++) {
                if (resultado[i][j] != matrizQuadradaEsperada[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarTransformarEmDiagonal(double[] vetorParaTransformarEmDiagonal, double[][] matrizDiagonal) {
        double[][] resultado = Main.criarMatrizDiagonal(vetorParaTransformarEmDiagonal);
        if (resultado.length != matrizDiagonal.length || resultado[0].length != matrizDiagonal[0].length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[i].length; j++) {
                if (resultado[i][j] != matrizDiagonal[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarOrdenar(double[] doubles, double[] ordenada) {
        double[] resultado = Main.ordenarVetor(doubles);
        if (resultado.length != ordenada.length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] != ordenada[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean testarMedia(double[][] matrizA, double[] vetorMedia) {
        double[] resultado = Main.mediaColunas(matrizA);
        if (resultado.length != vetorMedia.length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] != vetorMedia[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean testarSubtracao(double[][] matrizA, double[][] matrizB, double[][] matrizE) {
        double[] resultado = Main.subtrairArrays(Main.criacaoDeArrayVetor(matrizA), Main.criacaoDeArrayVetor(matrizB));
        if (resultado.length != Main.criacaoDeArrayVetor(matrizE).length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] != Main.criacaoDeArrayVetor(matrizE)[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean testarTransposta(double[][] matrizA, double[][] esperada) {
        double[][] resultado = Main.transporMatriz(matrizA);
        if (resultado.length != esperada.length || resultado[0].length != esperada[0].length) {
            return false;
        }
        for (int i = 0; i < resultado.length; i++) {
            for (int j = 0; j < resultado[0].length; j++) {
                if (resultado[i][j] != esperada[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarNormalizacaoMatrizes(double[][] matriz) {
        double[][] matrizDevolvida = Main.normalizarMatriz(matriz);
        double[][] matrizEsperada = {{0, (double) 510 / 257}, {(double) 765 / 257, 255}};
        for (int i = 0; i < matrizDevolvida.length; i++) {
            for (int j = 0; j < matrizDevolvida[0].length; j++) {
                if (matrizDevolvida[i][j] != matrizEsperada[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarNormalizacaoColunas(double[][] matriz) {
        double[][] matrizDevolvida = Main.normalizarColunas(matriz);
        double[][] matrizEsperada = {{1 / Math.sqrt(5), 2 / Math.sqrt(5)}, {2 / Math.sqrt(5), 1 / Math.sqrt(5)}};
        for (int i = 0; i < matrizDevolvida.length; i++) {
            for (int j = 0; j < matrizDevolvida[0].length; j++) {
                if (matrizDevolvida[i][j] != matrizEsperada[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarDevolucaoDeValorRandom(int valorProprio) {
        double random = Main.obterValorRandom(valorProprio);
        if (random > (-Math.sqrt(valorProprio)) && random < (Math.sqrt(valorProprio))) {
            return true;
        }
        return false;
    }

    private static boolean testarEAM(double[][] matrizA, double[][] matrizReconstruida) {
        double eamEsperado = 0;
        if (Main.calcularErroAbsolutoMedio(matrizA, matrizReconstruida) == eamEsperado) {
            return true;
        }
        return false;
    }

    private static boolean testarExtrairColuna(double[][] matrizA) {
        int coluna = 0;
        double[][] matrizDevolvida = Main.extrairColunaComoMatriz(matrizA, coluna);
        double[][] matrizColunaEsperada = {{1}, {2}};
        for (int i = 0; i < matrizDevolvida[0].length; i++) {
            for (int j = 0; j < matrizDevolvida.length; j++) {
                if (matrizDevolvida[j][i] != matrizColunaEsperada[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean testarAjusteK(double[][] matrizA, int k) throws FileNotFoundException {
        final int teste = 0;
        if (Main.ajustarK(matrizA, k, teste) == matrizA[0].length) {
            return true;
        }
        return false;
    }

    private static boolean testarConjunto(double[][] matrizA, double[][] matrizB) {
        if (Main.verificarNumeros(matrizA) && Main.verificarNumeros(matrizB)) {
            return true;
        }
        return false;
    }
}
