package CDPM.Compression;

import java.io.*;
import java.util.*;

public class LZWCompression {

    public static void main() {
        String inputFilePath = "movies_imdb_1000.csv";
        String compressedFilePath = "movies_imdb_1000LZW1.lzw";
        String decompressedFilePath = "movies_imdb_1000LZWDecompressed.csv";

        long startTime = System.currentTimeMillis();

        compress(inputFilePath, compressedFilePath);
        decompress(compressedFilePath, decompressedFilePath);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("Compression and Decompression completed successfully.");
        System.out.println("Total execution time: " + totalTime + " milliseconds");

        double compressionRatio = calculateCompressionRatio(inputFilePath, compressedFilePath);
        System.out.println("Compression ratio: " + compressionRatio + "%");
    
    }

    public static void compress(String inputFilePath, String compressedFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/" + inputFilePath));
             DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(compressedFilePath)))) {

            Map<String, Integer> dictionary = initializeDictionary();
            int currentCode = dictionary.size();

            StringBuilder currentSequence = new StringBuilder();

            int currentChar;
            while ((currentChar = reader.read()) != -1) {
                char ch = (char) currentChar;
                String sequence = currentSequence.toString() + ch;

                if (dictionary.containsKey(sequence)) {
                    currentSequence = new StringBuilder(sequence);
                } else {
                    outputStream.writeInt(dictionary.get(currentSequence.toString()));

                    dictionary.put(sequence, currentCode++);
                    currentSequence = new StringBuilder(String.valueOf(ch));
                }
            }

            if (!currentSequence.toString().isEmpty()) {
                outputStream.writeInt(dictionary.get(currentSequence.toString()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decompress(String compressedFilePath, String decompressedFilePath) {
        try (DataInputStream inputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(compressedFilePath)));
             BufferedWriter writer = new BufferedWriter(new FileWriter(decompressedFilePath))) {

            List<String> dictionary = initializeDictionaryList();
            int currentCode = dictionary.size();

            int previousCode = inputStream.readInt();
            String currentSequence = dictionary.get(previousCode);
            writer.write(currentSequence);

            int code;
            while (inputStream.available() > 0) {
                code = inputStream.readInt();

                String entry;
                if (code < currentCode) {
                    entry = dictionary.get(code);
                } else if (code == currentCode) {
                    entry = currentSequence + currentSequence.charAt(0);
                } else {
                    throw new IllegalStateException("Invalid LZW code during decompression.");
                }

                writer.write(entry);

                dictionary.add(currentSequence + entry.charAt(0));
                currentCode++;

                currentSequence = entry;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> initializeDictionary() {
        Map<String, Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put(String.valueOf((char) i), i);
        }
        return dictionary;
    }

    private static List<String> initializeDictionaryList() {
        List<String> dictionary = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            dictionary.add(String.valueOf((char) i));
        }
        return dictionary;
    }

    public static double calculateCompressionRatio(String originalFilePath, String compressedFilePath) {
        File originalFile = new File(originalFilePath);
        File compressedFile = new File(compressedFilePath);

        long originalFileSize = originalFile.length();
        long compressedFileSize = compressedFile.length();

        double compressionRatio = ((double) compressedFileSize / originalFileSize) * 100;
        double compressionGain = 100 * (Math.log( (float) originalFileSize / (float) compressedFileSize));
        double reductionPercentage = 100 * (1 - (compressionRatio / 100.0));

        System.out.println("Tamanho do arquivo original: " + originalFileSize + " bytes");
        System.out.println("Tamanho do arquivo compactado: " + compressedFileSize + " bytes");
        System.out.printf("Taxa de compressao: %.2f", (compressionRatio));
        System.out.println("%");
        System.out.printf("Ganho de compressao: %.2f", (compressionGain));
        System.out.println("%"); 
        System.out.printf("Percentual de reduçao: %.2f", (reductionPercentage));
        System.out.println("%");

        return reductionPercentage;
    }

}
