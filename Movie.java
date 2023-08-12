import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

/* Movie class */
public class Movie {
    protected int id;
    protected String link;
    protected String name;
    protected Date date;
    protected ArrayList<String> genre;
    protected float rating;
    protected int score;

    // Constructors
    public Movie(int id, String link, String name, Date date, ArrayList<String> genre, float rating, int score) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.date = date;
        this.genre = genre;
        this.rating = rating;
        this.score = score;
    }

    public Movie() {
        this.id = -1;
        this.link = null;
        this.name = null;
        this.date = null;
        this.genre = new ArrayList<>();
        this.rating = 0F;
        this.score = -1;
    }

    /* Getters and Setters */

    // Getters
    public int getID() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }
    
    public Date getDate() {
        return date;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public float getRating() {
        return rating;
    }

    public int getScore() {
        return score;
    }

    // Setters
    public void setID(int id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    /* Read method - split with ; and , */
    public void read (String line) throws ParseException {

        int i = 0;

        String[] lineSplitted = line.split(";");

        lineSplitted[i] = lineSplitted[i].replaceAll("\\uFEFF", "");

        id = Integer.parseInt(lineSplitted[i]);
        i++;

        if (lineSplitted[i].charAt(0) == '\"') {
            if (lineSplitted[i].substring(1).contains("\"")) {
                name = lineSplitted[i];
            } else {
                name = lineSplitted[i];
                i++;

                while (lineSplitted[i].contains("\"") == false) 
                {
                    name += lineSplitted[i];
                    i++;
                }

                name += lineSplitted[i];
                name.replaceAll("\"", "");
            }

        } else {
            name = lineSplitted[i];
        }

        i++;
        
        // Random date if date is empty
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        if (lineSplitted[i].length() != 0) {

            try {
                date = format.parse(lineSplitted[i]);

            } catch (ParseException e) {
                e.printStackTrace();        
            }

        } else {
            date = format.parse("01/01/2023");
        }

        i++;
        
        int j = 0;
        String[] splitGenre = lineSplitted[i].split(",");

        genre.add(splitGenre[j].substring(2));
        genre.set(j, genre.get(j).replaceAll("\"", ""));

        j++;
        
        if (splitGenre.length > j) {
            while (splitGenre [j].contains("}") == false) {
                genre.add(splitGenre [j]);
                genre.set(j, genre.get(j).replaceAll("\"", ""));
                j++;
            }
        }

        i++;

        rating = Float.parseFloat(lineSplitted[i]);
        i++;

        score = Integer.parseInt(lineSplitted[i]);
        i++;
    }
    
    /* Write method
     * 
     * 
     * Change to toString()
     * 
     * 
    */
    public void print() {
        System.out.println();
        System.out.println("Link: " + link);
        System.out.println("Name: " + name);
        
        // Ajustando o formato da data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);	
        Calendar reviewcal = Calendar.getInstance();
        reviewcal.setTime(date);
        System.out.print("Date: " + sdf.format(reviewcal.getTime()));

        System.out.println("Genre: " + genre);
        System.out.println("Rating: " + rating);
        System.out.println("Score: " + score);
        System.out.println();
        System.out.println();
    }
    
    /* Method for convert to bytes */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);

        toByteString(link, out);
        toByteString(name, out);
        
        // Escrever o tempo transcorrido em milissegundos a partir de uma data
        out.writeLong(date.getTime());

        out.writeInt(genre.size());

        for (String genre : genre) {
            toByteString(genre, out);
        }

        out.writeFloat(rating);
        out.writeInt(score);

        return bytes.toByteArray();
    }

    /* Method for convert String to bytes - write the string length in hex */
    public void toByteString(String data, DataOutputStream out) throws IOException {
        byte[] Stringbytes = data.getBytes("UTF-8");

        out.writeInt(Stringbytes.length);
        out.write(Stringbytes);
    }
    
    /* Method for convert from byte to respective data type */
    public void fromByteArray(int startByte, String filename) throws IOException {
        RandomAccessFile filebytes = new RandomAccessFile(filename, "rw");

        filebytes.seek(startByte);
        filebytes.readInt();

        // Creates an array of bytes with the size and reads them before converting to String
        byte[] bytes = new byte[filebytes.readInt()];
        filebytes.read(bytes);    
        link = new String(bytes);

        byte[] bytes2 = new byte[filebytes.readInt()];
        filebytes.read (bytes2);    
        name = new String(bytes2);

        long auxdata = filebytes.readLong();
        date = new Date(auxdata);
       
        int sizeList = filebytes.readInt();

        for (int i = 0; i < sizeList; i++) {
            int test = filebytes.readInt();
            byte[] bytes3 = new byte [test];
            filebytes.read(bytes3);
            genre.add(new String(bytes3));
        }
        
        rating = filebytes.readInt();
        score = filebytes.readFloat();

        filebytes.close();
    }
}