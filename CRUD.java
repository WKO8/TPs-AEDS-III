import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class CRUD {
    protected static RandomAccessFile arq;
    protected static byte[] bytesArray;

    /* Menu */
    public static void menu() throws IOException {
        int option = -1;
        Scanner sc = new Scanner(System.in);
        while (option != 5) {
            System.out.println("\n------ CRUD ------");
            System.out.println("- 1) Populate    -");
            System.out.println("- 2) Read (ID)   -");
            System.out.println("- 3) Update      -");        
            System.out.println("- 4) Delete (ID) -");        
            System.out.println("- 5) Exit        -");
            System.out.println("------------------");

            System.out.print("Choose an option -> ");
            
            option = sc.nextInt();
            clearBuffer(sc);

            switch (option) {
                case 1:
                    Start.populate();
                    System.out.println("\nFile populated successfully.");
                    break;
                    
                case 2:
                    System.out.println("------ READ ------");
                    System.out.print("- ID: ");
                    int idGiven = sc.nextInt();
                    clearBuffer(sc);
                    System.out.println("------------------");

                    Movie movieResp = search(idGiven);

                    if (movieResp != null) { System.out.println(movieResp); }
                    else { System.out.println("Record doesn't exist.");}

                    break;

                case 3:
                    System.out.println("----- UPDATE -----");
                    // ID
                    System.out.print("Movie's ID: ");
                    int id = sc.nextInt();
                    clearBuffer(sc);

                    // Link
                    System.out.print("Movie's link: ");
                    String link = sc.nextLine();

                    
                    // Title 
                    System.out.print("Movie's title: ");
                    String title = sc.nextLine();

                    // Year
                    System.out.print("Movie's year: ");
                    int year = sc.nextInt();
                    clearBuffer(sc);

                    // Genres
                    
                    System.out.print("Movie's genres: ");
                    String genresGiven = sc.nextLine();
                    
                    ArrayList<String> genres = new ArrayList<String>();

                    String[] genresSplitted = genresGiven.split(",");
                    for (String genre : genresSplitted) { genres.add(genre.trim()); }

                    // Rating
                    System.out.print("Movie's rating: ");
                    float rating = sc.nextFloat();

                    // Score
                    clearBuffer(sc);
                    System.out.print("Movie's score: ");
                    int score = sc.nextInt();
                    clearBuffer(sc);

                    // Movie object instantiation
                    Movie movie = new Movie(id, link, title, year, genres, rating, score);  

                    System.out.println("------------------");

                    boolean resp = update(movie);
                    if (resp) { System.out.println("Data updated successfully."); }
                    else { System.out.println("Something went wrong."); }
                    
                    break;

                case 4:
                    System.out.println("----- DELETE -----");
                    System.out.print("- ID: ");
                    int idDelGiven = sc.nextInt();
                    clearBuffer(sc);
                    System.out.println("------------------");

                    boolean respDel = delete(idDelGiven);
                    if (respDel) { System.out.println("Record deleted successfully."); }
                    else { System.out.println("Something went wrong."); }

                    break;
                
                case 5:
                    break;
                    
                default:
                    System.out.println("You must provide a valid option.");
            }   
        }
        sc.close();
    }

    public static void clearBuffer(Scanner sc) {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
    }

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
