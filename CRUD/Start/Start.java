package CRUD.Start;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import CRUD.Movie.Movie;

public class Start {
    /* Read CSV - Variables  */
    protected static String fileCSV = "files/movies_imdb_1000.csv";
    protected static BufferedReader br = null;
    protected static String line = "";
    protected static String csvDelimiter = ";";

    /* Writting & Reading in File (Hex Database) - Variables */
    protected static RandomAccessFile arq;
    protected static byte[] bytesArray;


    public static void populate(String filename) throws IOException {
        /* Initializing RandomAccessFile */
        arq = new RandomAccessFile(filename, "rw");
        
        /* Read CSV */
        br = new BufferedReader(new FileReader(fileCSV));
        br.readLine();
            
        arq.writeInt(0);

        int id = 1;
        long p1;

        while ((line = br.readLine()) != null) {

            String[] movie = line.split(csvDelimiter);

            // Converting year (str) to year (int)
            int year = Integer.parseInt(movie[2]);

            // Converting genre (str) to genre (ArrayList<String>)
            ArrayList<String> genre = new ArrayList<String>();
            String[] genres = movie[3].split(",");
            for (String g : genres) {
                genre.add(g);
            }

            // Converting rating (str) to rating (float)
            float rating = Float.parseFloat(movie[4]);

            // Converting score (str) to score (int)
            int score;
            try {
                score = Integer.parseInt(movie[5]);
            } catch (NumberFormatException e) {
                score = -1;
            }
                
            Movie m = new Movie(id, movie[0], movie[1], year, genre, rating, score);

            /* Writting in File (Hex Database) */
            bytesArray = m.toByteArray(); 
            
            // Write gravestone
            arq.writeChar('-');
            // Write record length
            arq.writeInt(bytesArray.length);
            // Write record data
            arq.write(bytesArray);

            p1 = arq.getFilePointer();

            // Write in the first four bytes the number of records
            arq.seek(0);
            arq.writeInt(id);

            arq.seek(p1);

            id++;

        }

        // Closing BufferedReader and File
        br.close();
        arq.close();
    }

}