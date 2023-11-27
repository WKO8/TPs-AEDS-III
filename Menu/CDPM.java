package Menu;
import java.util.Scanner;

import CDPM.Compression.HuffmanCompression;
import CDPM.Compression.LZWCompression;
import CDPM.PatternMatching.BoyerMoorePatternMatching;
import CDPM.PatternMatching.KMPPatternMatching;

public class CDPM {
    public static void menu() {
        Scanner scanner = new Scanner(System.in);

        String filePath = "movies_imdb_1000.csv";
        String compressedFilePath = "files/compressed/movies_imdb_1000LZW1.lzw";
        String decompressedFilePath = "files/decompressed/movies_imdb_1000LZWDecompressed.csv";

        int escolha;
        long HuffmanDecompressionTime = 0;
        long LZWDecompressionTime = 0;

        do {
            exibirMenu();
            System.out.print("Escolha uma opçao: ");
            escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    System.out.println("--- Compressao ---");

                    long startTimeHuffmanCompression = System.currentTimeMillis();
                    HuffmanCompression.compress(filePath);
                    long HuffmanCompressionTime = System.currentTimeMillis() - startTimeHuffmanCompression;
                    double reductionPercentageHuffman =  HuffmanCompression.calculateCompressionRatio("files/" + filePath, "files/compressed/" + filePath.replace(".csv", "Huffman1.bin"));
                    System.out.println("Tempo de execuçao: " + HuffmanCompressionTime + "ms");
                    System.out.println("Compressao em Huffman concluída com sucesso.\n");
                    
                    long startTimeLZWCompression = System.currentTimeMillis();
                    LZWCompression.compress(filePath, compressedFilePath);
                    long LZWCompressionTime = System.currentTimeMillis() - startTimeLZWCompression;
                    double reductionPercentageLZW = LZWCompression.calculateCompressionRatio("files/" + filePath, "files/compressed/" + filePath.replace(".csv", "LZW1.lzw"));
                    System.out.println("Tempo de execuçao: " + LZWCompressionTime + "ms");
                    System.out.println("Compressao em LZW concluída com sucesso.");


                    if (LZWCompressionTime < HuffmanCompressionTime) {
                        System.out.println("\nAlgoritmo de LZW é mais rápido para compressão.");
                    } else {
                        System.out.println("\nAlgoritmo de Huffman é mais rápido para compressão.");
                    }

                    if (reductionPercentageLZW > reductionPercentageHuffman) {
                        System.out.println("Algoritmo de LZW comprime mais o arquivo.");
                    } else {
                        System.out.println("Algoritmo de Huffman comprime mais o arquivo.");
                    }

                    break;

                case 2:
                    System.out.println("--- Descompressao ---");
                    System.out.println("=== VERSOES ===");
                    System.out.println("| 1) Huffman  |");
                    System.out.println("| 2) LZW      |");
                    System.out.println("| 3) Voltar   |");
                    System.out.println("===============");
                    System.out.println("Qual versao deseja realizar a descompressao? ");

                    int versao = scanner.nextInt();

                    long startTimeDecompression = System.currentTimeMillis();

                    if (versao == 1) {
                        HuffmanCompression.decompress("files/compressed/" + filePath.replace(".csv", "Huffman1.bin"), "files/compressed/" + filePath.replace(".csv", "Huffman_codes.txt"));
                        HuffmanDecompressionTime = System.currentTimeMillis() - startTimeDecompression;
                        System.out.println("\nTempo de execuçao: " + HuffmanDecompressionTime + "ms");
                        System.out.println("Descompressao em Huffman concluída com sucesso.");
                    } else if (versao == 2) {
                        LZWCompression.decompress(compressedFilePath, decompressedFilePath);
                        LZWDecompressionTime = System.currentTimeMillis() - startTimeDecompression;
                        System.out.println("\nTempo de execuçao: " + LZWDecompressionTime + "ms");
                        System.out.println("Descompressao em LZW concluída com sucesso.");
                    } else {
                        break;
                    }

                    if (LZWDecompressionTime != 0 && HuffmanDecompressionTime != 0) {
                        if (LZWDecompressionTime < HuffmanDecompressionTime) {
                        System.out.println("\nAlgoritmo de LZW é mais rápido para descompressão.");
                        } else {
                        System.out.println("\nAlgoritmo de Huffman é mais rápido para descompressão.");
                        }
                    }

                    break;

                case 3:
                    System.out.println("--- Casamento de padroes ---");
                    System.out.println("===== MÉTODOS =====");
                    System.out.println("| 1) Boyer Moore  |");
                    System.out.println("| 2) KMP          |");
                    System.out.println("===================");
                    System.out.println("Escolha um método: ");
                    
                    int metodo = scanner.nextInt();

                    System.out.println("Padrao que deseja procurar: ");
                    String padrao = scanner.next();
                    

                    if (metodo == 1) {
                        BoyerMoorePatternMatching.main(padrao);
                    } else if (metodo == 2) {
                        KMPPatternMatching.main(padrao);
                    }

                    break;
                case 4:
                    System.out.println("Você escolheu sair.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (escolha != 4);

        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n=== Menu de Opçoes ===");
        System.out.println("| 1) Compressao      |");
        System.out.println("| 2) Descompressao   |");        
        System.out.println("| 3) Casamento de    |");
        System.out.println("|    padroes         |");
        System.out.println("| 4) Sair            |");
        System.out.println("======================");
    }
}

