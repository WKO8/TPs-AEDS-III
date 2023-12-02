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
                        VigenereCipher.encrypt();
                    } else if (escolhaCriptografia == 2) {
                        VigenereCipher.decrypt();
                    }
                    break;
                case 2:
                    if (escolhaCriptografia == 1) {
                        TranspositionCipher.encrypt();
                    } else if (escolhaCriptografia == 2) {
                        TranspositionCipher.decrypt();
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

