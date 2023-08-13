import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
    public Movie(int id, String link, String name, int year, ArrayList<String> genre, float rating, int score) {
        this.id = id;
        this.link = link;
        this.name = name;

        try {
            this.date = fromYearToDate(year);
        } catch (ParseException e) { e.printStackTrace();}
        
        this.genre = genre;
        this.rating = rating;
        this.score = score;
    }

    public Movie() {
        this.id = -1;
        this.link = null;
        this.name = null;
        this.date = new Date();
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
    
    /* Transform date to milliseconds */
    public Date fromYearToDate(int year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date = sdf.parse("01/01/" + year);
        return date;
    }
    
    /* Transform milliseconds to date */
    public Date fromMillisToDate(long millis) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date date = new Date(millis);

        String dateConverted = sdf.format(date);

        return sdf.parse(dateConverted);
    }

    /* toString() method */
    public String toString() {
        return "\nID: " + id +
               "\nLink: " + link + 
               "\nName: " + name +
               "\nDate: " + date +
               "\nGenre: " + genre +
               "\nRating: " + rating +
               "\nScore: " + score;
    }
    
    /* Method for convert data to bytes */
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bytes);

        dos.writeInt(id);
        dos.writeUTF(link);
        dos.writeUTF(name);
        dos.writeLong(date.getTime());

        String genreStr = genre.toString();
        String genreOnly = genreStr.substring(1, genreStr.length() - 1);

        dos.writeUTF(genreOnly);
        dos.writeFloat(rating);
        dos.writeInt(score);

        return bytes.toByteArray();
    }
    
    /* Method for convert from byte to respective data type */
    public void fromByteArray(byte[] bytesArray) throws IOException {
        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesArray);
        DataInputStream dis = new DataInputStream(bytesIn);

        id = dis.readInt();
        link = dis.readUTF();
        name = dis.readUTF();

        try {
            date = fromMillisToDate(dis.readLong());
        } catch (ParseException e) { e.printStackTrace();}

        ArrayList<String> genreArray = new ArrayList<String>(); // Create an ArrayList object
        genreArray.add(dis.readUTF());

        genre = genreArray;
        rating = dis.readFloat();
        score = dis.readInt();
    }
}