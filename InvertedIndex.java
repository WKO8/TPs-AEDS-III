import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class InvertedIndex {
    // Attributes
    private static RandomAccessFile file;
    private static byte[] bytesArray;

    // Constructor
    public InvertedIndex() {}

    // Methods

    protected static void updateFiles(String filename) throws IOException, ParseException {
        ByYear.invertedIndexByYear(filename);
        ByScore.invertedIndexByScore(filename);
    }

    protected static void readFiles() throws IOException, ParseException {
        ByYear.readFromFile("database/invIdx/invByYear.md");
        ByScore.readFromFile("database/invIdx/invByScore.md");
    }

    protected static void query(String filename, int year, int score) throws IOException, ParseException {
        ByYear bY = new ByYear();
        bY.indexes = bY.search("database/invIdx/invByYear.md", year).indexes;
        String byYear = bY.indexes;

        ByScore bS = new ByScore();
        bS.indexes = bS.search("database/invIdx/invByScore.md", score).indexes;
        String byScore = bS.indexes;

        String[] byYearSpllited = byYear.split(" ");
        String[] byScoreSplitted = byScore.split(" ");

        ArrayList<String> res = new ArrayList<String>();

        for (int i = 0; i < byYearSpllited.length; i++) {
            for (int j = 0; j < byScoreSplitted.length; j++) {
                if (byYearSpllited[i].compareTo(byScoreSplitted[j]) == 0) res.add(byYearSpllited[i]);
            }
        }

        System.out.println("Result (IDs): " + res.toString());
    }

    // Classes
    public static class ByYear {
        // Attributes
        private static ArrayList<Integer> years;
        private static String[] yearsIndexes;
        private int year;
        public String indexes;

        // Constructor
        public ByYear() {}

        // Methods

        protected static void invertedIndexByYear(String filename) throws IOException, ParseException {
            // Initializing RandomAccessFile
            file = new RandomAccessFile(filename, "r");
            // Fill ArrayList
            getYears();
            // Sort ArrayList
            Collections.sort(years);
            // Fill List
            getYearsIndexes();
            // Name of file
            String fileInvByYear = "database/invIdx/invByYear.md";
            // Save data in file
            saveToFile(fileInvByYear);
            // Read from file
            // readFromFile(fileInvByYear);
        }

        private static void getYears() throws IOException, ParseException {
            // Last ID
            file.readInt();

            years = new ArrayList<Integer>();
            Movie movie = new Movie();
            int len;

            file.seek(0);
            // Ignore the first four bytes
            file.readInt();

            long pointer = file.getFilePointer();

            for (int i = 0; i < file.length(); i++) {
                file.seek(pointer);
                char gravestone;
                try {
                    gravestone = file.readChar();   
                } catch (EOFException e) {
                    break;
                }

                // Record length       
                len = file.readInt();
                // Record bytes
                bytesArray = new byte[len];    
                file.read(bytesArray);

                pointer = file.getFilePointer();

                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);
                    boolean insertYear = true;

                    int yearSelected = movie.fromDateToYear(movie.getDate());
                    for (int year : years) {
                        if (yearSelected == year) insertYear = false;
                    }
                        if (insertYear) years.add(yearSelected);
                }
            }              
        }

        private static void getYearsIndexes() throws IOException, ParseException {
            /* Read */
            Movie movie = new Movie();

            int len;

            file.seek(0);

            // Ignore the first four bytes
            file.readInt();

            ByYear.yearsIndexes = new String[ByYear.years.size()];

            for (int i = 0; i < file.length(); i++) {
                char gravestone;
                try {
                    gravestone = file.readChar();             
                } catch (EOFException e) {
                    break;
                }

                len = file.readInt();
                bytesArray = new byte[len];
                file.read(bytesArray);

                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);
                    int movieYear = movie.fromDateToYear(movie.getDate());
                    for (int year : ByYear.years) {
                        if (movieYear == year) {
                            int idx = ByYear.years.indexOf(year);
                            if (ByYear.yearsIndexes[idx] == null) ByYear.yearsIndexes[idx] = "";
                            ByYear.yearsIndexes[idx] += (Integer.toString(movie.getID()) + " ");
                        }
                    }
                }
            }
    }

        private ByYear search(String filename, int yearIn) throws IOException {
            /* Initializing RandomAccessFile */
            file = new RandomAccessFile(filename, "r");

            /* Read */
            ByYear invIdx = new ByYear();
            int len = 0;
            file.seek(0);
            for (int i = 0; i < file.length(); i++) {
                try {
                    len = file.readInt();
                } catch (EOFException e) {
                    break;
                }

                bytesArray = new byte[len];
                file.read(bytesArray);
                invIdx.fromByteArray(bytesArray);

                if (invIdx.year == yearIn) {
                    return invIdx;
                }
            }
            file.close();

            return null;
        }

        private static void saveToFile(String filename) throws IOException {
            file = new RandomAccessFile(filename, "rw");
            file.seek(0);

            for (int year : years) {
                int idx = years.indexOf(year);
                bytesArray = toByteArray(year, yearsIndexes[idx]);
                int len = bytesArray.length;

                file.writeInt(len);
                file.write(bytesArray);
            }
            file.close();
        }

        private static void readFromFile(String filename) throws IOException {
            /* Initializing RandomAccessFile */
            file = new RandomAccessFile(filename, "r");

            /* Read */
            ByYear invIdx = new ByYear();
            int len;
            file.seek(0);

            for (int i = 0; i < file.length(); i++) {
                try {
                    len = file.readInt();
                } catch (EOFException e) {
                    break;
                }
                bytesArray = new byte[len];
                file.read(bytesArray);
                invIdx.fromByteArray(bytesArray);

                System.out.println(invIdx);
            }
            file.close();
        }

        public String toString() {
            return "\nYear: " + year +
                    "\nReferences: " + indexes;
        }
            
        private static byte[] toByteArray(int year, String idxString) throws IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bytes);

            dos.writeInt(year);
            dos.writeUTF(idxString);

            return bytes.toByteArray();
        }

        private void fromByteArray(byte[] bytesArray) throws IOException {
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesArray);
            DataInputStream dis = new DataInputStream(bytesIn);

            year = dis.readInt();
            indexes = dis.readUTF();
        }

    
    }

    public static class ByScore {
        // Attributes
        private static ArrayList<Integer> scores;
        private static String[] scoresIndexes;
        protected int score;
        public String indexes;

        // Constructor
        public ByScore() {}

        // Methods
        
        protected static void invertedIndexByScore(String filename) throws IOException, ParseException {
            // Initializing RandomAccessFile
            file = new RandomAccessFile(filename, "r");
            // Fill ArrayList
            getScores();
            // Sort ArrayList
            Collections.sort(scores);
            // Fill List
            getScoresIndexes();
            // Name of file
            String fileInvByScore = "database/invIdx/invByScore.md";
            // Save data in file
            saveToFile(fileInvByScore);
            // Read from file
            // readFromFile(fileInvByScore);
        }

        private static void getScores() throws IOException, ParseException {
            // Last ID
            file.readInt();

            scores = new ArrayList<Integer>();
            Movie movie = new Movie();
            int len;

            file.seek(0);
            // Ignore the first four bytes
            file.readInt();

            for (int i = 0; i < file.length(); i++) {
                char gravestone;
                try {
                    gravestone = file.readChar();                
                } catch (EOFException e) {
                    break;
                }
                // Record length       
                len = file.readInt();
                // Record bytes
                bytesArray = new byte[len];    
                file.read(bytesArray);

                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);

                    boolean insertScore = true;
                    int scoreSelected = movie.getScore();

                    for (int score : scores) {
                        if (scoreSelected == score) insertScore = false;
                    }
                    if (insertScore) scores.add(scoreSelected);
                }
            }
        }

        private static void getScoresIndexes() throws IOException, ParseException {
            /* Read */
            Movie movie = new Movie();

            int len;

            file.seek(0);

            // Ignore the first four bytes
            file.readInt();

            scoresIndexes = new String[scores.size()];

            for (int i = 0; i < file.length(); i++) {
                char gravestone;
                try {
                    gravestone = file.readChar();                
                } catch (EOFException e) {
                    break;
                }

                len = file.readInt();
                
                bytesArray = new byte[len];
                
                file.read(bytesArray);
                
                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);

                    int movieScore = movie.getScore();

                    for (int score : scores) {
                        if (movieScore == score) {
                            int idx = scores.indexOf(score);
                            if (scoresIndexes[idx] == null) scoresIndexes[idx] = "";
                            scoresIndexes[idx] += (Integer.toString(movie.getID()) + " ");
                        }
                    }
                }
            }
    }

        private ByScore search(String filename, int scoreIn) throws IOException {
            /* Initializing RandomAccessFile */
            file = new RandomAccessFile(filename, "r");

            /* Read */
            ByScore invIdx = new ByScore();
            int len;
            file.seek(0);

            for (int i = 0; i < file.length(); i++) {
                try {
                    len = file.readInt();
                } catch (EOFException e) {
                    break;
                }
                bytesArray = new byte[len];
                file.read(bytesArray);
                invIdx.fromByteArray(bytesArray);

                if (invIdx.score == scoreIn) return invIdx;
            }
            file.close();

            return null;
        }

        private static void saveToFile(String filename) throws IOException {
            file = new RandomAccessFile(filename, "rw");
            file.seek(0);

            for (int score : scores) {
                int idx = scores.indexOf(score);
                bytesArray = toByteArray(score, scoresIndexes[idx]);
                int len = bytesArray.length;

                file.writeInt(len);
                file.write(bytesArray);
            }
            file.close();
        }

        private static void readFromFile(String filename) throws IOException {
            /* Initializing RandomAccessFile */
            file = new RandomAccessFile(filename, "r");

            /* Read */
            ByScore invIdx = new ByScore();
            int len;
            file.seek(0);

            for (int i = 0; i < file.length(); i++) {
                try {
                    len = file.readInt();
                } catch (EOFException e) {
                    break;
                }
                bytesArray = new byte[len];
                file.read(bytesArray);
                invIdx.fromByteArray(bytesArray);

                System.out.println(invIdx);
            }
            file.close();
        }


        public String toString() {
            return "\nScore: " + score +
                    "\nReferences: " + indexes;
        }
            
        private static byte[] toByteArray(int score, String idxString) throws IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bytes);

            dos.writeInt(score);
            dos.writeUTF(idxString);

            return bytes.toByteArray();
        }

        private void fromByteArray(byte[] bytesArray) throws IOException {
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesArray);
            DataInputStream dis = new DataInputStream(bytesIn);

            score = dis.readInt();
            indexes = dis.readUTF();
        }

    }
}
