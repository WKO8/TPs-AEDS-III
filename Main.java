import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class Main {
    /* Read CSV - Variables  */
    protected static String fileCSV = "movies_imdb_1000.csv";
    protected static BufferedReader br = null;
    protected static String line = "";
    protected static String csvDelimiter = ";";

    /* Writting & Reading in File (Hex Database) - Variables */
    protected static RandomAccessFile arq;
    protected static byte[] bytesArray;
    

    public static void main(String[] args) {

        /* Initializing RandomAccessFile */
        try {
            arq = new RandomAccessFile("database/movies.db", "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        /* Read CSV */
        try {

            br = new BufferedReader(new FileReader(fileCSV));
            br.readLine();

            int id = 1;
            while ((line = br.readLine()) != null) {

                String[] movie = line.split(csvDelimiter);


                // Converting year (str) to year (int)
                int year = Integer.parseInt(movie[2]);

                // Converting genre (str) to genre (ArrayList<String>)
                ArrayList<String> genre = new ArrayList<String>();
                String[] genres = movie[5].split(",");
                for (String g : genres) {
                    genre.add(g);
                }

                // Converting rating (str) to rating (float)
                float rating = Float.parseFloat(movie[6]);

                // Converting score (str) to score (int)
                int score;
                try {
                    score = Integer.parseInt(movie[8]);
                } catch (NumberFormatException e) {
                    score = -1;
                }
                

                Movie m = new Movie(id, movie[0], movie[1], year, genre, rating, score);

                /* Writting & Reading in File (Hex Database) */
                try {
                    /* Write */

                    long p1 = arq.getFilePointer();
                    
                    bytesArray = m.toByteArray(); 

                    arq.writeInt(bytesArray.length);
                    arq.write(bytesArray);


                    /* Read */
                    Movie m2 = new Movie();
                    int len;

                    arq.seek(p1);

                    len = arq.readInt();
                    
                    bytesArray = new byte[len];
                    
                    arq.read(bytesArray);
                    
                    m2.fromByteArray(bytesArray);

                    System.out.println(m2);

                } catch(Exception e) {
                    e.printStackTrace();
                }

                System.out.flush();

                id++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    arq.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
