package CDPM.PatternMatching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KMPPatternMatching {

    public static void main(String pattern) {
        String filePath = "movies_imdb_1000.csv";
        String padrao = pattern; // Padrão que deseja procurar

        try {
            String texto = lerArquivo("files/" + filePath);
            int comparacoes = buscarPadrao(texto, padrao);

            if (comparacoes > 0) {
                System.out.println("Número de comparaçoes realizadas: " + comparacoes);
            } else {
                System.out.println("Padrao nao encontrado na base de dados.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String lerArquivo(String filePath) throws IOException {
        StringBuilder texto = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                texto.append(linha);
            }
        }

        return texto.toString();
    }

    private static int buscarPadrao(String texto, String padrao) {
        int[] prefixo = calcularPrefixo(padrao);
        int comparacoes = 0;

        int i = 0; // Índice para o texto
        int j = 0; // Índice para o padrão

        while (i < texto.length()) {
            comparacoes++;
            if (padrao.charAt(j) == texto.charAt(i)) {
                j++;
                i++;
            }

            if (j == padrao.length()) {
                // Padrão encontrado
                return comparacoes;
            } else if (i < texto.length() && padrao.charAt(j) != texto.charAt(i)) {
                if (j != 0) {
                    j = prefixo[j - 1];
                } else {
                    i++;
                }
            }
        }

        // Padrão não encontrado
        return -comparacoes;
    }

    private static int[] calcularPrefixo(String padrao) {
        int[] prefixo = new int[padrao.length()];
        int j = 0;

        for (int i = 1; i < padrao.length(); i++) {
            while (j > 0 && padrao.charAt(i) != padrao.charAt(j)) {
                j = prefixo[j - 1];
            }

            if (padrao.charAt(i) == padrao.charAt(j)) {
                j++;
            }

            prefixo[i] = j;
        }

        return prefixo;
    }
}
