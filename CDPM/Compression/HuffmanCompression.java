package CDPM.Compression;

import java.io.*;
import java.util.*;

class HuffmanNode {
    char data;
    int frequency;
    HuffmanNode left, right;

    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
        this.left = this.right = null;
    }
}

class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequency - y.frequency;
    }
}

public class HuffmanCompression {

    private static Map<Character, String> huffmanCodes = new HashMap<>();

    public static void main() {
        String filePath = "movies_imdb_1000.csv"; // Substitua pelo caminho do seu arquivo CSV
        long startTime = System.currentTimeMillis();
        compress(filePath);
        long compressionTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        decompress(filePath.replace(".csv", "Huffman1.bin"), filePath.replace(".csv", "Huffman_codes.txt"));
        long decompressionTime = System.currentTimeMillis() - startTime;

        System.out.println("Compression and Decompression completed successfully.");

        double compressionRatio = calculateCompressionRatio(filePath, filePath.replace(".csv", "Huffman1.bin"));
        System.out.println("Compression ratio: " + compressionRatio%.2f + "%");

        System.out.println("Compression time: " + compressionTime + " milliseconds");
        System.out.println("Decompression time: " + decompressionTime + " milliseconds");
    }

    public static void compress(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("files/" + filePath));
            StringBuilder data = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            reader.close();

            Map<Character, Integer> frequencyMap = getFrequency(data.toString());
            HuffmanNode root = buildHuffmanTree(frequencyMap);
            generateHuffmanCodes(root, "");

            String compressedData = compressData(data.toString(), huffmanCodes);
            writeCompressedFile(filePath, compressedData, huffmanCodes);

            // System.out.println("Compressed file saved successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decompress(String compressedFilePath, String codesFilePath) {
        try {
            Map<Character, String> huffmanCodes = readHuffmanCodes(codesFilePath);
            String compressedData = readCompressedFile(compressedFilePath);

            HuffmanNode root = buildHuffmanTreeFromCodes(huffmanCodes);
            String decompressedData = decompressData(compressedData, root);

            writeDecompressedFile(compressedFilePath.replace("Huffman1.bin", "HuffmanDecompressed.csv"), decompressedData);

            // System.out.println("Decompressed file saved successfully.");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Character, Integer> getFrequency(String data) {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char c : data.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        return frequencyMap;
    }

    private static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(new MyComparator());

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode mergedNode = new HuffmanNode('\0', left.frequency + right.frequency);
            mergedNode.left = left;
            mergedNode.right = right;

            priorityQueue.add(mergedNode);
        }

        return priorityQueue.poll();
    }

    private static void generateHuffmanCodes(HuffmanNode root, String code) {
        if (root != null) {
            if (root.left == null && root.right == null) {
                huffmanCodes.put(root.data, code);
            }

            generateHuffmanCodes(root.left, code + "0");
            generateHuffmanCodes(root.right, code + "1");
        }
    }

    private static String compressData(String data, Map<Character, String> huffmanCodes) {
        StringBuilder compressedData = new StringBuilder();

        for (char c : data.toCharArray()) {
            compressedData.append(huffmanCodes.get(c));
        }

        return compressedData.toString();
    }

    private static void writeCompressedFile(String filePath, String compressedData, Map<Character, String> huffmanCodes) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream("files/compressed/" + filePath.replace(".csv", "Huffman1.bin")))) {

            for (int i = 0; i < compressedData.length(); i += 8) {
                String byteString = compressedData.substring(i, Math.min(i + 8, compressedData.length()));
                int byteValue = Integer.parseInt(byteString, 2);
                outputStream.write(byteValue);
            }

            writeHuffmanCodesFile("files/compressed/" + filePath.replace(".csv", "Huffman_codes.txt"), huffmanCodes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeHuffmanCodesFile(String filePath, Map<Character, String> huffmanCodes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Character, String> readHuffmanCodes(String filePath) throws IOException {
        Map<Character, String> huffmanCodes = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                char character = parts[0].charAt(0);
                String code = parts[1];
                huffmanCodes.put(character, code);
            }
        }

        return huffmanCodes;
    }

    private static String readCompressedFile(String filePath) throws IOException {
        StringBuilder compressedData = new StringBuilder();

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            int byteRead;

            while ((byteRead = inputStream.read()) != -1) {
                compressedData.append(String.format("%8s", Integer.toBinaryString(byteRead & 0xFF)).replace(' ', '0'));
            }
        }

        return compressedData.toString();
    }

    private static HuffmanNode buildHuffmanTreeFromCodes(Map<Character, String> huffmanCodes) {
        HuffmanNode root = new HuffmanNode('\0', 0);

        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            char character = entry.getKey();
            String code = entry.getValue();
            buildTreeFromCode(root, character, code);
        }

        return root;
    }

    private static void buildTreeFromCode(HuffmanNode root, char character, String code) {
        for (char bit : code.toCharArray()) {
            if (bit == '0') {
                if (root.left == null) {
                    root.left = new HuffmanNode('\0', 0);
                }
                root = root.left;
            } else if (bit == '1') {
                if (root.right == null) {
                    root.right = new HuffmanNode('\0', 0);
                }
                root = root.right;
            }
        }

        root.data = character;
    }

    private static String decompressData(String compressedData, HuffmanNode root) {
        StringBuilder decompressedData = new StringBuilder();
        HuffmanNode current = root;

        for (char bit : compressedData.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else if (bit == '1') {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                decompressedData.append(current.data);
                current = root;
            }
        }

        return decompressedData.toString();
    }

    private static void writeDecompressedFile(String filePath, String decompressedData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.replace("files/compressed", "files/decompressed")))) {
            writer.write(decompressedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
