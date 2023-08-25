import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;

public class CRUD {
    protected static RandomAccessFile arq;
    protected static byte[] bytesArray;

    // CRUD methods

    /* Insert */
    public static void insert(Movie movie) throws IOException {
        /* Initializing RandomAccessFile */
        arq = new RandomAccessFile("database/movies.db", "rw");
        
        arq.seek(0);
        int lastID = arq.readInt();
        movie.id = lastID + 1;

        arq.seek(0);
        arq.writeInt(movie.id);

        bytesArray = movie.toByteArray();

        arq.seek(arq.length());

        // Write gravestone
        arq.writeChar('-');
        // Write record length
        arq.writeInt(bytesArray.length);
        // Write record data
        arq.write(bytesArray);

        arq.close();
    }

    /* Read all records */
    public static void read() throws IOException {
        /* Initializing RandomAccessFile */
        arq = new RandomAccessFile("database/movies.db", "rw");

        /* Read */
        Movie movie = new Movie();

        int len;

        arq.seek(0);

        // Ignore the first four bytes
        arq.readInt();

        for (int i = 0; i < arq.length(); i++) {
            // Ignore the gravestone's bytes
            try {
                arq.readChar();                
            } catch (EOFException e) {
                break;
            }

            len = arq.readInt();
            
            bytesArray = new byte[len];
            
            arq.read(bytesArray);
            
            movie.fromByteArray(bytesArray);

            System.out.println(movie);
        }
    }

    /* Search by ID*/
    public static Movie search(int id) throws IOException {
        arq = new RandomAccessFile("database/movies.db", "rw");
        
        arq.seek(0);

        int lastID = arq.readInt();

        Movie movie = new Movie();

        int len;

        for (int i = 0; i < lastID; i++) {
            char gravestone = arq.readChar();
            len = arq.readInt();

            bytesArray = new byte[len];
            arq.read(bytesArray);

            if (gravestone != '*') {
                movie.fromByteArray(bytesArray);
                if (movie.id == id) {
                    return movie;
                }
            }
        }

        arq.close();

        return null;
    }

    /* Search by title */
    public static Movie search(String title) throws IOException {
        arq = new RandomAccessFile("database/movies.db", "rw");
        
        arq.seek(0);

        int lastID = arq.readInt();

        Movie movie = new Movie();

        int len;

        for (int i = 0; i < lastID; i++) {
            char gravestone = arq.readChar();
            len = arq.readInt();

            bytesArray = new byte[len];
            arq.read(bytesArray);

            if (gravestone != '*') {
                movie.fromByteArray(bytesArray);
                if (movie.title.equals(title)) {
                    return movie;
                }
            }
        }

        arq.close();

        return null;
    }

    /* Search by year */
    public static ArrayList<String> searchByYear(int year) throws IOException, ParseException {
        arq = new RandomAccessFile("database/movies.db", "rw");
        
        arq.seek(0);

        int lastID = arq.readInt();

        Movie movie = new Movie();

        int len;
        ArrayList<String> moviesArray = new ArrayList<String>();

        for (int i = 0; i < lastID; i++) {
            char gravestone = arq.readChar();
            len = arq.readInt();

            bytesArray = new byte[len];
            arq.read(bytesArray);

            if (gravestone != '*') {
                movie.fromByteArray(bytesArray);
                if (movie.fromDateToYear(movie.getDate()) == year) {
                    moviesArray.add(movie.toString());
                }
            }
        }

        arq.close();

        return moviesArray;
    }

    /* Search by genre */
    public static ArrayList<String> searchByGenre(String genre) throws IOException, ParseException {
            arq = new RandomAccessFile("database/movies.db", "rw");
            
            arq.seek(0);

            int lastID = arq.readInt();

            Movie movie = new Movie();

            int len;
            ArrayList<String> moviesArray = new ArrayList<String>();

            for (int i = 0; i < lastID; i++) {
                char gravestone = arq.readChar();
                len = arq.readInt();

                bytesArray = new byte[len];
                arq.read(bytesArray);

                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);
                    for (String genreName : movie.genre) {
                        if (genreName.indexOf(genre) != -1) {
                            moviesArray.add(movie.toString());
                        }
                    }
                }
            }

            arq.close();

            return moviesArray;
    }

    /* Search by rating */
    public static ArrayList<String> search(float rating) throws IOException, ParseException {
            arq = new RandomAccessFile("database/movies.db", "rw");
            
            arq.seek(0);

            int lastID = arq.readInt();

            Movie movie = new Movie();

            int len;
            ArrayList<String> moviesArray = new ArrayList<String>();

            for (int i = 0; i < lastID; i++) {
                char gravestone = arq.readChar();
                len = arq.readInt();

                bytesArray = new byte[len];
                arq.read(bytesArray);

                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);
                    if (movie.rating == rating) {
                        moviesArray.add(movie.toString());
                    }
                }
            }

            arq.close();

            return moviesArray;
    }

    /* Search by score */
    public static ArrayList<String> searchByScore(int score) throws IOException, ParseException {
            arq = new RandomAccessFile("database/movies.db", "rw");
            
            arq.seek(0);

            int lastID = arq.readInt();

            Movie movie = new Movie();

            int len;
            ArrayList<String> moviesArray = new ArrayList<String>();

            for (int i = 0; i < lastID; i++) {
                char gravestone = arq.readChar();
                len = arq.readInt();

                bytesArray = new byte[len];
                arq.read(bytesArray);

                if (gravestone != '*') {
                    movie.fromByteArray(bytesArray);
                    if (movie.score == score) {
                        moviesArray.add(movie.toString());
                    }
                }
            }

            arq.close();

            return moviesArray;
    }

    /* Update */
    public static boolean update(Movie movieArg) throws IOException {
        arq = new RandomAccessFile("database/movies.db", "rw");
        
        arq.seek(0);

        int lastID = arq.readInt();

        int len;
        long pointer;
        boolean resp = false;

        Movie movie = new Movie();

        for (int i = 0; i < lastID; i++) {
            pointer = arq.getFilePointer();
            char gravestone = arq.readChar();
            len = arq.readInt();

            bytesArray = new byte[len];
            arq.read(bytesArray);

            if (gravestone != '*') {
                movie.fromByteArray(bytesArray);
                if (movie.id == movieArg.id) {
                    bytesArray = movieArg.toByteArray();
                    if (bytesArray.length <= len) {
                        arq.seek(pointer);
                        arq.writeChar(gravestone);
                        arq.writeInt(len);
                        arq.write(bytesArray);
                    } else {
                        /* Mark record as deleted */
                        arq.seek(pointer);
                        arq.writeChar('*');
                        
                        /* Go to EOF */
                        arq.seek(arq.length()-1);

                        // Write gravestone
                        arq.writeChar('-');
                        // Write record length
                        arq.writeInt(bytesArray.length);
                        // Write record data
                        arq.write(bytesArray);

                        /* Update the metadata of the last id */
                        arq.seek(0);
                        arq.writeInt(movieArg.id);
                    }
                    resp = true;
                }
            }
        }

        arq.close();

        return resp;
    }

    /* Delete */
    public static boolean delete(int id) throws IOException {
        arq = new RandomAccessFile("database/movies.db", "rw");
        
        arq.seek(0);

        // Ignore the first four bytes
        arq.readInt();

        Movie movie = new Movie();

        int len;
        long pointer;

        for (int i = 0; i < arq.length(); i++) {
            pointer = arq.getFilePointer();
            char gravestone = arq.readChar();
            len = arq.readInt();

            bytesArray = new byte[len];
            arq.read(bytesArray);

            if (gravestone != '*') {
                movie.fromByteArray(bytesArray);
                if (movie.id == id) {
                    arq.seek(pointer);
                    arq.writeChar('*');
                    return true;
                }
            } else {
                movie.fromByteArray(bytesArray);
                if (movie.id == id) {
                    return true;
                }
            }
        }

        arq.close();

        return false;
    }


}
