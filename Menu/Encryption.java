package Menu;
import java.io.IOException;
import java.util.Scanner;

import Encryption.*;

public class Encryption {
    public static void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int escolha, escolhaCriptografia;

        do {
            // Escolher o algoritmo de cifragem
            exibirMenu();
            System.out.print("Choose an option: ");
            escolha = scanner.nextInt();

            // Escolher entre criptografar ou descriptografar
            exibirMenuCriptografia();
            System.out.print("Choose an option: ");
            escolhaCriptografia = scanner.nextInt();

            switch (escolha) {
                case 1:
                    if (escolhaCriptografia == 1) {
                        long startEncryptTime = System.currentTimeMillis();

                        VigenereCipher.encrypt();

                        long vigenereETime = System.currentTimeMillis() - startEncryptTime;
                        System.out.println("[Encryption - Vigenère] Execution time: " + vigenereETime + "ms");
                    } else if (escolhaCriptografia == 2) {
                        long startDecryptTime = System.currentTimeMillis();

                        VigenereCipher.decrypt();

                        long vigenereDTime = System.currentTimeMillis() - startDecryptTime;
                        System.out.println("[Encryption - Vigenère] Execution time: " + vigenereDTime + "ms");
                    }
                    break;
                case 2:
                    if (escolhaCriptografia == 1) {
                        long startETime = System.currentTimeMillis();

                        TranspositionCipher.encrypt();

                        long transpositionTime = System.currentTimeMillis() - startETime;
                        System.out.println("[Encryption - Transposition] Execution time: " + transpositionTime + "ms");
                    } else if (escolhaCriptografia == 2) {
                        long startDTime = System.currentTimeMillis();

                        TranspositionCipher.decrypt();

                        long transpositionTime = System.currentTimeMillis() - startDTime;
                        System.out.println("[Encryption - Transposition] Execution time: " + transpositionTime + "ms");
                    }   
                    break;
                case 3:
                    System.out.println("You chose to leave.");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (escolha != 3);

        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n=== Options menu ===");
        System.out.println("| 1) Vigenére        |");  
        System.out.println("| 2) Transposition   |");
        System.out.println("| 3) Exit            |");
        System.out.println("======================");
    }

    public static void exibirMenuCriptografia() {
        System.out.println("\n=== Options menu ===");
        System.out.println("| 1) Encrypt         |");  
        System.out.println("| 2) Decrypt         |");
        System.out.println("| 3) Back            |");
        System.out.println("======================");
    }
}

