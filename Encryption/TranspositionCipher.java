package Encryption;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TranspositionCipher {

    private static String inputFile = "files/hex/movies.db";
    private static String encryptedFile = "files/encryption/columnar/encrypted.db";
    private static String decryptedFile = "files/encryption/columnar/decrypted.db";
    private static int[] columnOrder = {2, 0, 1}; // Order of columns

    public static void encrypt() throws IOException {
        processFile(inputFile, encryptedFile, columnOrder, true);
        System.out.println("Cifragem de colunas concluída.");
    }

    public static void decrypt() throws IOException {
        processFile(encryptedFile, decryptedFile, columnOrder, false);
        System.out.println("Decifragem de colunas concluída.");
    }

    private static void processFile(String inputFile, String outputFile, int[] columnOrder, boolean encrypt) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] processedBuffer;
                if (encrypt) {
                    processedBuffer = encryptBuffer(buffer, columnOrder, bytesRead);
                } else {
                    processedBuffer = decryptBuffer(buffer, columnOrder, bytesRead);
                }
                outputStream.write(processedBuffer, 0, bytesRead);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private static byte[] encryptBuffer(byte[] buffer, int[] columnOrder, int bytesRead) {
        byte[] result = new byte[bytesRead];

        for (int i = 0; i < bytesRead; i++) {
            int column = i % columnOrder.length;
            int newIndex = (i / columnOrder.length) * columnOrder.length + columnOrder[column];
            
            // Verificar se o newIndex está dentro dos limites do array
            if (newIndex < bytesRead) {
                result[newIndex] = buffer[i];
            }
        }

        return result;
    }

    private static byte[] decryptBuffer(byte[] buffer, int[] columnOrder, int bytesRead) {
        byte[] result = new byte[bytesRead];

        for (int i = 0; i < bytesRead; i++) {
            int column = i % columnOrder.length;
            int newIndex = (i / columnOrder.length) * columnOrder.length + columnOrder[column];
            
            // Verificar se o newIndex está dentro dos limites do array
            if (newIndex < bytesRead) {
                result[i] = buffer[newIndex];
            }
        }

        return result;
    }

}
