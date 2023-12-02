import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import CRUD.Searching.InvertedIndex;
import CRUD.Sorting.ExternalSort;

import Menu.CRUD;
import Menu.CDPM;
import Menu.Encryption;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        int escolha;
        do {
            exibirMenu();
            System.out.print("Choose an option: ");
            escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    CRUD.menu();
                    break;
                case 2:
                    ExternalSort.menu();
                    break;
                case 3:
                    InvertedIndex.menu();
                    break;
                case 4:
                    CDPM.menu();
                    break;
                case 5:
                    Encryption.menu();
                case 6:
                    System.out.println("You chose to leave.");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (escolha != 6);

        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n======= Menu =======");
        System.out.println("| 1) CRUD           |");
        System.out.println("| 2) External Sort  |");         
        System.out.println("| 3) Inverted Index |");        
        System.out.println("| 4) CDPM           |");          
        System.out.println("| 5) Encryption     |");        
        System.out.println("| 6) Sair           |");
        System.out.println("=====================");
        System.out.println("- CRUD: Create, Read, Update, Delete");
        System.out.println("- CDPM: Compression, Decompression, Pattern Matching\n");
    }
}