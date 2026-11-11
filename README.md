# LAPR1 2024/25 — Eigenfaces (DIJ_08)

Este projeto foi feito para LAPR1. Aplica a decomposição em valores e vetores próprios (PCA / **Eigenfaces**) a matrizes e a imagens de rostos em tons de cinzento. Cada imagem é guardada num ficheiro CSV com uma matriz 64×64 de valores entre 0 e 255. Os cálculos de valores e vetores próprios usam a biblioteca [Apache Commons Math 3.6.1](https://commons.apache.org/proper/commons-math/), que já está incluída no repositório (`commons-math3-3.6.1.jar`).

## Funcionalidades

| # | Funcionalidade | Descrição |
|---|---|---|
| 1 | Decompor matriz | Lê uma matriz simétrica de um CSV, calcula os valores e vetores próprios e reconstrói a matriz com `k` deles. Também mostra o erro absoluto médio (EAM). |
| 2 | Reconstruir imagens | Calcula as eigenfaces de uma pasta de imagens e reconstrói cada uma com `k` eigenfaces. |
| 3 | Imagem semelhante | Projeta uma imagem no espaço das eigenfaces e encontra a mais parecida na base de imagens. |
| 4 | Gerar nova imagem | Gera uma imagem nova (aleatória) a partir das `k` eigenfaces da base de imagens. |

Com `k = -1` são usados todos os valores e vetores próprios ou eigenfaces disponíveis.

## Estrutura

```
src/Main.java              código da aplicação (menus e funcionalidades)
src/TestesUnitarios.java   testes unitários (correm sempre no arranque da aplicação)
input/                     base de imagens de exemplo (40 CSV 64x64)
exampleInputFunc1.csv      matriz de exemplo para a funcionalidade 1
image_078.csv              imagem de exemplo para a funcionalidade 3
inputTeste.csv             ficheiro usado pelos testes unitários
commons-math3-3.6.1.jar    dependência
```

## Compilar e executar pelo terminal

Requisitos: **JDK 11 ou superior** (`javac` e `java`). Todos os comandos correm a partir da raiz do repositório.

### 1. Compilar

```bash
javac -encoding UTF-8 -cp commons-math3-3.6.1.jar -d out src/*.java
```

> Se só tiveres o comando `java` (sem `javac`), troca `javac` por `java -m jdk.compiler/com.sun.tools.javac.Main`.

No Windows, o separador do classpath é `;` em vez de `:` nos comandos seguintes.

### 2. Criar as pastas de saída

A aplicação grava os resultados nestas pastas, mas não as cria sozinha:

```bash
mkdir -p Output Eigenfaces ImagensReconstruídas Identificacao
```

### 3. Correr os testes unitários (opcional)

```bash
java -cp out:commons-math3-3.6.1.jar TestesUnitarios
```

> Na primeira execução, o teste da pasta vazia cria a pasta `testeUnitario` e falha. A partir da segunda, passa.

### 4. Executar

**Modo interativo** (menu):

```bash
java -cp out:commons-math3-3.6.1.jar Main
```

**Modo não interativo** (argumentos):

```bash
java -cp out:commons-math3-3.6.1.jar Main -f <func> [-k <ficheiro.csv>] -i <k> [-d <pasta>] <output.txt>
```

| Flag | Significado |
|---|---|
| `-f` | funcionalidade (1–4) |
| `-k` | ficheiro CSV de entrada (funcionalidades 1 e 3) |
| `-i` | número de valores/vetores próprios ou eigenfaces a usar (`-1` = todos) |
| `-d` | pasta da base de imagens (funcionalidades 2, 3 e 4) |
| último argumento | ficheiro `.txt` onde fica o relatório |

Exemplos com os ficheiros do repositório:

```bash
java -cp out:commons-math3-3.6.1.jar Main -f 1 -k exampleInputFunc1.csv -i 2 resultado.txt
java -cp out:commons-math3-3.6.1.jar Main -f 2 -i 5 -d input resultado.txt
java -cp out:commons-math3-3.6.1.jar Main -f 3 -k image_078.csv -i 5 -d input resultado.txt
java -cp out:commons-math3-3.6.1.jar Main -f 4 -i 5 -d input resultado.txt
```

## Onde ficam os resultados

| Funcionalidade | Pasta |
|---|---|
| 1 | `Output/` (matriz reconstruída) |
| 2 | `Eigenfaces/` (CSV) e `ImagensReconstruídas/` (JPG) |
| 3 | `Identificacao/` (JPG da imagem mais semelhante) |
| 4 | `Output/` (`imagem_gerada.csv` / `.jpg`) |
