package Encryption;
import java.io.*;

public class VigenereCipher {
    public static void encrypt() {
        try {
            // Substitua "chave" pela sua palavra-chave
            String chave = "chave";

            // Ler o conteúdo do arquivo de entrada
            String inputFileName = "files/hex/movies.db";
            byte[] inputFileContent = readBinaryFile(inputFileName);

            // Cifrar o conteúdo do arquivo
            byte[] cipherText = cipher(inputFileContent, chave);

            // Escrever o texto cifrado em um novo arquivo
            String outputFileName = "files/encryption/vigenere/encrypted.bin";
            writeBinaryFile(outputFileName, cipherText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt() {
        try {
            // Substitua "chave" pela sua palavra-chave
            String chave = "chave";

            // Texto cifrado no novo arquivo
            String encryptedFile = "files/encryption/vigenere/encrypted.bin";

            // Ler o texto cifrado do arquivo
            byte[] cipherTextFromFile = readBinaryFile(encryptedFile);

            // Decifrar o texto cifrado
            byte[] decryptedText = decipher(cipherTextFromFile, chave);

            // Escrever o texto decifrado em um novo arquivo
            String outputDecipherFileName = "files/encryption/vigenere/decrypted.bin";
            writeBinaryFile(outputDecipherFileName, decryptedText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Função para cifrar um texto usando o algoritmo de Vigenère
    private static byte[] cipher(byte[] data, String chave) {
        byte[] resultado = new byte[data.length];
        int dataLength = data.length;
        int chaveLength = chave.length();

        for (int i = 0; i < dataLength; i++) {
            byte dataByte = data[i];
            char chaveChar = chave.charAt(i % chaveLength);

            resultado[i] = (byte) (dataByte + chaveChar);
        }

        return resultado;
    }

    // Função para decifrar um texto cifrado usando o algoritmo de Vigenère
    private static byte[] decipher(byte[] data, String chave) {
        byte[] resultado = new byte[data.length];
        int dataLength = data.length;
        int chaveLength = chave.length();

        for (int i = 0; i < dataLength; i++) {
            byte dataByte = data[i];
            char chaveChar = chave.charAt(i % chaveLength);

            resultado[i] = (byte) (dataByte - chaveChar);
        }

        return resultado;
    }

    // Função para ler o conteúdo de um arquivo binário
    private static byte[] readBinaryFile(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(fileName)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }

    // Função para escrever conteúdo em um arquivo binário
    private static void writeBinaryFile(String fileName, byte[] data) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.write(data);
        }
    }
}
