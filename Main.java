import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import CRUD.Searching.InvertedIndex;
import CRUD.Sorting.ExternalSort;
import Menu.CDPM;
import Menu.CRUD;


public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        int escolha;
        do {
            exibirMenu();
            System.out.print("Escolha uma opçao: ");
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
                    System.out.println("Você escolheu sair.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (escolha != 5);

        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n======= Menu =======");
        System.out.println("| 1) CRUD           |");
        System.out.println("| 2) External Sort  |");         
        System.out.println("| 3) Inverted Index |");        
        System.out.println("| 4) CDPM           |");        
        System.out.println("| 5) Sair           |");
        System.out.println("=====================");
        System.out.println("- CRUD: Create, Read, Update, Delete");
        System.out.println("- CDPM: Compression, Decompression, Pattern Matching\n");
    }
}