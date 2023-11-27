package CDPM.PatternMatching;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BoyerMoorePatternMatching {

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
        int m = padrao.length();
        int n = texto.length();
        int comparacoes = 0;

        int[] badChar = prepararBadChar(padrao);

        int s = 0;
        while (s <= n - m) {
            int j = m - 1;

            while (j >= 0 && padrao.charAt(j) == texto.charAt(s + j)) {
                j--;
                comparacoes++;
            }

            if (j < 0) {
                // Padrão encontrado
                return comparacoes;
            } else {
                int shift = Math.max(1, j - badChar[texto.charAt(s + j)]);
                s += shift;
                comparacoes++;
            }
        }

        // Padrão não encontrado
        return -comparacoes;
    }

    private static int[] prepararBadChar(String padrao) {
        int[] badChar = new int[256];

        for (int i = 0; i < badChar.length; i++) {
            badChar[i] = -1;
        }

        for (int i = 0; i < padrao.length(); i++) {
            badChar[padrao.charAt(i)] = i;
        }

        return badChar;
    }
}
