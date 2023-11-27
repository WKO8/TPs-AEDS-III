package CRUD.Sorting;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

import CRUD.Movie.Movie;

public class ExternalSort {
    protected static RandomAccessFile arq;
    protected static byte[] bytesArray;
    private static String filename = "files/hex/movies.db";

    /* Menu */
    public static void menu() throws IOException {
        int option = -1;
        Scanner sc = new Scanner(System.in);
        while (option != 4) {
            System.out.println("------- External sorting -------");
            System.out.println("- 1) Common                    -");
            System.out.println("- 2) Variable size blocks      -");
            System.out.println("- 3) Selection by substitution -");         
            System.out.println("- 4) Exit                      -");
            System.out.println("--------------------------------");

            System.out.print("Choose an option -> ");

            option = sc.nextInt();
            clearBuffer(sc);

            switch (option) {
                case 1:
                    System.out.println("------ Common Balanced Interleaving ------");

                    System.out.println("- Block size: ");
                    int blockSize = sc.nextInt();
                    clearBuffer(sc);

                    System.out.println("- Path quantity: ");
                    int pathQuantity = sc.nextInt();
                    clearBuffer(sc);

                    Commom.commomFiles(blockSize, pathQuantity);
                    break;
                    
                case 2:
                    System.out.println("------ Balanced Interleaving with Variable Size Blocks ------");


                    break;

                case 3:
                    System.out.println("----- Balanced Interleaving with Selection by Substitution -----");
                    
                    break;

                case 4:
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

    // External Interleaving methods

    /* Common */
    private static class Commom {
        // With ArrayList<Movie>
        // public static boolean commom(int blockSize, int pathQuantity) throws IOException {
        //     ArrayList<Movie> movies = getRecords(filename);
        //     ArrayList<Movie> fileTemp1 = new ArrayList<Movie>();            
        //     ArrayList<Movie> fileTemp2 = new ArrayList<Movie>();
        //     ArrayList<Movie> fileTemp3 = new ArrayList<Movie>();            
        //     ArrayList<Movie> fileTemp4 = new ArrayList<Movie>();                
        //     ArrayList<Movie> toSort = new ArrayList<Movie>();
            
        //     /* Partition - Step 1 */

        //     int fileNum = 1;
        //     int recordsCount = 1;
        //     boolean sort = false;

        //     for (int i = 0; i <= movies.size(); i++) {
                
        //         if (recordsCount == blockSize) {
        //             if (fileNum == pathQuantity) {
        //                 fileNum = 1;
        //                 sort = true;

        //             } else {
        //                 fileNum++;
        //                 sort = true;
        //             }
        //             recordsCount = 1;
        //         }

        //         if (fileNum == 1) {
        //             if (toSort.size() > 0 && sort) {
        //                 toSort = InternalSort.InsertionSort.sort(toSort);
        //                 for (Movie movie : toSort) { fileTemp2.add(movie); }
        //                 sort = false;
        //                 toSort.clear();
        //             }
        //             if (movies.size()-1 > i) toSort.add(movies.get(i));


        //         } else if (fileNum == 2) {
        //             if (toSort.size() > 0 && sort) {
        //                 toSort = InternalSort.InsertionSort.sort(toSort);
        //                 for (Movie movie : toSort) { fileTemp1.add(movie); }
        //                 sort = false;
        //                 toSort.clear();
        //             }
        //             if (movies.size()-1 > i) toSort.add(movies.get(i));
        //         }
        //         recordsCount++;
        //     }

        //     // System.out.println("\n" + "---- File Temp 1 ----" + "\n");
        //     // for (Movie movie : fileTemp1) {
        //     //     System.out.println(movie);
        //     // }

        //     // System.out.println("\n" + "---- File Temp 2 ----" + "\n");
        //     // for (Movie movie : fileTemp2) {
        //     //     System.out.println(movie);
        //     // }

        //     System.out.println("\n" + "File Temp 1: " + fileTemp1.size() + "\n" + "File Temp 2: " + fileTemp2.size() + "\n");

        //     System.out.println("\n" + "Número de registros pegos da memória secundária: " + movies.size() + "\n");

        //     for (Movie record : fileTemp1) {
        //         System.out.println(record.id);
        //     }

        //     for (Movie record : fileTemp2) {
        //         System.out.println(record.id);
        //     }
            
        //     /* Balanced Interleaving - Step 2.1 */
            
        //     int pointer1 = 0;
        //     int pointer2 = 0;
        //     int sumSizes = fileTemp1.size() + fileTemp2.size();
        //     boolean p1Final = false;
        //     boolean p2Final = false;
        //     fileNum = 3;
        //     recordsCount = 1;

        //     for (int i = 0; i < sumSizes+1; i++) {
        //         if (recordsCount == blockSize) {
        //             if (fileNum == pathQuantity+2) {
        //                 fileNum = 3;
        //             } else {
        //                 fileNum++;
        //             }
        //             recordsCount = 1;
        //         }

        //         while ((!p1Final) && (fileTemp1.get(pointer1).id < fileTemp2.get(pointer2).id)) {
        //             if (fileNum == 3) fileTemp3.add(fileTemp1.get(pointer1));
        //             else fileTemp4.add(fileTemp1.get(pointer1));
        //             if (fileTemp1.size()-1 > pointer1) pointer1++;
        //             else p1Final = true;
        //         }
                
        //         while ((!p2Final) &&(fileTemp2.get(pointer2).id < fileTemp1.get(pointer1).id)) {
        //             if (fileNum == 3) fileTemp3.add(fileTemp2.get(pointer2));
        //             else fileTemp4.add(fileTemp2.get(pointer2));
        //             if (fileTemp2.size()-1 > pointer2 + 1) pointer2++;
        //             else p2Final = true;
        //         }

        //         while (!p1Final && p2Final) {
        //             if (fileNum == 3) fileTemp3.add(fileTemp1.get(pointer1));
        //             else fileTemp4.add(fileTemp1.get(pointer1));
        //             if (fileTemp1.size()-1 > pointer1) pointer1++;
        //             else p1Final = true;
        //         }

        //         while (!p2Final && p1Final) {
        //             if (fileNum == 3) fileTemp3.add(fileTemp2.get(pointer2));
        //             else fileTemp4.add(fileTemp2.get(pointer2));
        //             if (fileTemp2.size()-1 > pointer2 + 1) pointer2++;
        //             else p2Final = true;
        //         }
        //         recordsCount++;
        //     }

        //     // System.out.println("\n" + "---- File Temp 3 ----" + "\n");
        //     // for (Movie movie : fileTemp3) {
        //     //     System.out.println(movie);
        //     // }

        //     // System.out.println("\n" + "---- File Temp 4 ----" + "\n");
        //     // for (Movie movie : fileTemp4) {
        //     //     System.out.println(movie);
        //     // }

        //     System.out.println("\n" + "File Temp 3: " + fileTemp3.size() + "\n" + "File Temp 4: " + fileTemp4.size() + "\n");

        //     for (Movie record : fileTemp3) {
        //         System.out.println(record.id);
        //     }

        //     for (Movie record : fileTemp4) {
        //         System.out.println(record.id);
        //     }

        //     /* Balanced Interleaving - Step 2.2 */

        //     fileTemp1.clear();
        //     fileTemp2.clear();
            
        //     int pointer3 = 0;
        //     int pointer4 = 0;
        //     sumSizes = fileTemp3.size() + fileTemp4.size();
        //     boolean p3Final = false;
        //     boolean p4Final = false;
        //     fileNum = 1;
        //     recordsCount = 1;

        //     for (int i = 0; i < sumSizes+1; i++) {
        //         if (recordsCount == blockSize) {
        //             if (fileNum == pathQuantity) {
        //                 fileNum = 1;
        //             } else {
        //                 fileNum++;
        //             }
        //             recordsCount = 1;
        //         }
        //         recordsCount++;

        //         while ((!p3Final) && (fileTemp3.get(pointer3).id < fileTemp4.get(pointer4).id)) {
        //             if (fileNum == 1) fileTemp1.add(fileTemp3.get(pointer3));
        //             else fileTemp2.add(fileTemp3.get(pointer3));
        //             if (fileTemp3.size()-1 > pointer3) pointer3++;
        //             else p3Final = true;
        //         }
                
        //         while ((!p4Final) &&(fileTemp4.get(pointer4).id < fileTemp3.get(pointer3).id)) {
        //             if (fileNum == 1) fileTemp1.add(fileTemp4.get(pointer4));
        //             else fileTemp2.add(fileTemp4.get(pointer4));
        //             if (fileTemp4.size()-1 > pointer4 + 1) pointer4++;
        //             else p4Final = true;
        //         }

        //         while (!p3Final && p4Final) {
        //             if (fileNum == 1) fileTemp1.add(fileTemp3.get(pointer3));
        //             else fileTemp2.add(fileTemp3.get(pointer3));
        //             if (fileTemp3.size()-1 > pointer3) pointer3++;
        //             else p3Final = true;
        //         }

        //         while (!p4Final && p3Final) {
        //             if (fileNum == 1) fileTemp1.add(fileTemp4.get(pointer4));
        //             else fileTemp2.add(fileTemp4.get(pointer4));
        //             if (fileTemp4.size()-1 > pointer4 + 1) pointer4++;
        //             else p4Final = true;
        //         }
                
        //     }

        //     // System.out.println("\n" + "---- File Temp 1 ----" + "\n");
        //     // for (Movie movie : fileTemp1) {
        //     //     System.out.println(movie);
        //     // }

        //     // System.out.println("\n" + "---- File Temp 2 ----" + "\n");
        //     // for (Movie movie : fileTemp2) {
        //     //     System.out.println(movie);
        //     // }

        //     System.out.println("\n" + "File Temp 1: " + fileTemp1.size() + "\n" + "File Temp 2: " + fileTemp2.size() + "\n");

        //     // for (Movie record : fileTemp1) {
        //     //     System.out.println("FileTemp1 ID: " + record.id);
        //     // }

        //     // for (Movie record : fileTemp2) {
        //     //     System.out.println("FileTemp2 ID: " + record.id);
        //     // }

        //     return true;
        // }
        
        // With Files
        public static boolean commomFiles(int blockSize, int pathQuantity) throws IOException {
            ArrayList<Movie> movies = getRecords(filename);

            RandomAccessFile fileTemp1 = new RandomAccessFile("files/temp/filetemp1.db", "rw");            
            RandomAccessFile fileTemp2 = new RandomAccessFile("files/temp/filetemp2.db", "rw");

            ArrayList<Movie> toSort = new ArrayList<Movie>();
            
            /* Partition - Step 1 */

            int fileNum = 1;
            int recordsCount = 1;
            boolean sort = false;

            for (int i = 0; i <= movies.size(); i++) {
                
                if (recordsCount == blockSize) {
                    if (fileNum == pathQuantity) {
                        fileNum = 1;
                        sort = true;

                    } else {
                        fileNum++;
                        sort = true;
                    }
                    recordsCount = 1;
                }

                if (fileNum == 1) {
                    if (toSort.size() > 0 && sort) {
                        toSort = InternalSort.Sort.sort(toSort);
                        for (Movie movie : toSort) { 
                            bytesArray = movie.toByteArray();
                            int len = bytesArray.length;
                            fileTemp2.writeInt(len);
                            fileTemp2.write(bytesArray);
                        }
                        sort = false;
                        toSort.clear();
                    }
                    if (movies.size()-1 > i) toSort.add(movies.get(i));


                } else if (fileNum == 2) {
                    if (toSort.size() > 0 && sort) {
                        toSort = InternalSort.Sort.sort(toSort);
                        for (Movie movie : toSort) { 
                            bytesArray = movie.toByteArray();
                            int len = bytesArray.length;
                            fileTemp1.writeInt(len);
                            fileTemp1.write(bytesArray); 
                        }
                        sort = false;
                        toSort.clear();
                    }
                    if (movies.size()-1 > i) toSort.add(movies.get(i));
                }
                recordsCount++;
            }

            fileTemp1.close();
            fileTemp2.close();            

            System.out.println("\n" + "FILE TEMP 1" + "\n");
            read("files/temp/filetemp1.db");

            System.out.println("\n" + "FILE TEMP 2" + "\n");
            read("files/temp/filetemp2.db");

            
            /* 
             * Balanced Interleaving - Step 2.1 
             */

            // fileTemp1 = new RandomAccessFile("files/temp/filetemp1.db", "rw");            
            // fileTemp2 = new RandomAccessFile("files/temp/filetemp2.db", "rw");
            // RandomAccessFile fileTemp3 = new RandomAccessFile("files/temp/filetemp3.db", "rw");            
            // RandomAccessFile fileTemp4 = new RandomAccessFile("files/temp/filetemp4.db", "rw");

            // long pointer1 = 0;
            // long pointer2 = 0;            
            // long pointer3 = 0;
            // long pointer4 = 0;

            // boolean EOFFileTemp1 = false;            
            // boolean EOFFileTemp2 = false;


            // fileNum = 1;
            // recordsCount = 1;

            // for (int i = 0; i < movies.size(); i++) {
            //     if (recordsCount == blockSize) {
            //         if (fileNum == pathQuantity) {
            //             fileNum = 1;
            //         } else {
            //             fileNum++;
            //         }
            //         recordsCount = 1;
            //     }

            //     if (fileNum == 1) {
            //         pointer1 = fileTemp1.getFilePointer();
            //         pointer2 = fileTemp2.getFilePointer();                    
            //         pointer3 = fileTemp3.getFilePointer();                    
            //         pointer4 = fileTemp4.getFilePointer();

            //         /* Read */
            //         Movie movie1 = new Movie();                    
            //         Movie movie2 = new Movie();

            //         fileTemp1.seek(pointer1);                    
            //         fileTemp2.seek(pointer2);
            //         fileTemp3.seek(pointer3);                    
            //         fileTemp4.seek(pointer4);

            //         if (!EOFFileTemp1 && !EOFFileTemp2) {

            //             System.out.println("\nPointer2: " + pointer2);

            //             int len1 = fileTemp1.readInt();
            //             int len2 = fileTemp2.readInt();

            //             pointer1 = fileTemp1.getFilePointer();
            //             pointer2 = fileTemp2.getFilePointer();

            //             /* Record FileTemp1 */
            //             bytesArray = new byte[len1];
            //             fileTemp1.read(bytesArray);
            //             movie1.fromByteArray(bytesArray);

            //             /* Record FileTemp2 */
            //             bytesArray = new byte[len2];
            //             fileTemp2.read(bytesArray);
            //             movie2.fromByteArray(bytesArray);

            //             if (movie1.id < movie2.id) {
            //                 long sumLen1 = pointer1 + len1;
            //                 fileTemp1.seek(sumLen1);

            //                 pointer1 = fileTemp1.getFilePointer();

            //                 if (pointer1 >= fileTemp1.length()) {
            //                     EOFFileTemp1 = true;
            //                 }

            //                 fileTemp3.writeInt(len1);
            //                 fileTemp3.write(movie1.toByteArray());                        
            //                 pointer3 = fileTemp3.getFilePointer();

            //             } else {
            //                 long sumLen2 = pointer2 + len2;
            //                 fileTemp2.seek(sumLen2);

            //                 pointer2 = fileTemp2.getFilePointer();
            //                 System.out.println("\nLength: " + fileTemp2.length());
            //                 if (pointer2 >= fileTemp2.length()) {
                                
            //                     EOFFileTemp2 = true;
            //                 }

            //                 fileTemp3.writeInt(len2);
            //                 fileTemp3.write(movie2.toByteArray());
            //                 pointer3 = fileTemp3.getFilePointer();

            //             }
            //         }

            //         if (EOFFileTemp2 && !EOFFileTemp1) {
            //             int len1 = fileTemp1.readInt();
            //             pointer1 = fileTemp1.getFilePointer();

            //             /* Record FileTemp1 */
            //             bytesArray = new byte[len1];
            //             fileTemp1.read(bytesArray);
            //             movie1.fromByteArray(bytesArray);

            //             pointer1 = fileTemp1.getFilePointer();

            //             if (pointer1 >= fileTemp1.length()) {
            //                 EOFFileTemp1 = true;
            //             }

            //             fileTemp3.writeInt(len1);
            //             fileTemp3.write(movie1.toByteArray());                        
            //             pointer3 = fileTemp3.getFilePointer();
            //         }

            //         if (EOFFileTemp1 && !EOFFileTemp2) {
            //             int len2 = fileTemp2.readInt();
            //             pointer2 = fileTemp2.getFilePointer();
                        
            //             /* Record FileTemp2 */
            //             bytesArray = new byte[len2];
            //             fileTemp2.read(bytesArray);
            //             movie2.fromByteArray(bytesArray);

            //             pointer2 = fileTemp2.getFilePointer();

            //             if (pointer2 >= fileTemp2.length()) {
            //                 EOFFileTemp2 = true;
            //             }

            //             fileTemp3.writeInt(len2);
            //             fileTemp3.write(movie2.toByteArray());                        
            //             pointer3 = fileTemp3.getFilePointer();
            //         }
                                      
            //     } else {
            //         pointer1 = fileTemp1.getFilePointer();
            //         pointer2 = fileTemp2.getFilePointer();                    
            //         pointer3 = fileTemp3.getFilePointer();                    
            //         pointer4 = fileTemp4.getFilePointer();


            //         /* Read */
            //         Movie movie1 = new Movie();                    
            //         Movie movie2 = new Movie();

            //         fileTemp1.seek(pointer1);
            //         fileTemp2.seek(pointer2);
            //         fileTemp3.seek(pointer3);
            //         fileTemp4.seek(pointer4);

            //         if (!EOFFileTemp1 && !EOFFileTemp2) {
            //             int len1 = fileTemp1.readInt();
            //             int len2 = fileTemp2.readInt();

            //             pointer1 = fileTemp1.getFilePointer();
            //             pointer2 = fileTemp2.getFilePointer();

            //             /* Record FileTemp1 */
            //             bytesArray = new byte[len1];
            //             fileTemp1.read(bytesArray);
            //             movie1.fromByteArray(bytesArray);

            //             /* Record FileTemp2 */
            //             bytesArray = new byte[len2];
            //             fileTemp2.read(bytesArray);
            //             movie2.fromByteArray(bytesArray);

            //             if (movie1.id < movie2.id) {
            //                 long sumLen1 = pointer1 + len1;
            //                 fileTemp1.seek(sumLen1);

            //                 pointer1 = fileTemp1.getFilePointer();

            //                 if (pointer1 >= fileTemp1.length()) {
            //                     EOFFileTemp1 = true;
            //                 }

            //                 fileTemp4.writeInt(len1);
            //                 fileTemp4.write(movie1.toByteArray());                        
            //                 pointer4 = fileTemp4.getFilePointer();

            //             } else {
            //                 long sumLen2 = pointer2 + len2;
            //                 fileTemp2.seek(sumLen2);

            //                 pointer2 = fileTemp2.getFilePointer();

            //                 if (pointer2 >= fileTemp2.length()) {
            //                     EOFFileTemp2 = true;
            //                 }

            //                 fileTemp4.writeInt(len2);
            //                 fileTemp4.write(movie2.toByteArray());
            //                 pointer4 = fileTemp4.getFilePointer();
            //             }
            //         }

            //         if (EOFFileTemp2 && !EOFFileTemp1) {
            //             int len1 = fileTemp1.readInt();
            //             pointer1 = fileTemp1.getFilePointer();

            //             /* Record FileTemp1 */
            //             bytesArray = new byte[len1];
            //             fileTemp1.read(bytesArray);
            //             movie1.fromByteArray(bytesArray);

            //             pointer1 = fileTemp1.getFilePointer();

            //             if (pointer1 >= fileTemp1.length()) {
            //                 EOFFileTemp1 = true;
            //             }

            //             fileTemp4.writeInt(len1);
            //             fileTemp4.write(movie1.toByteArray());                        
            //             pointer4 = fileTemp4.getFilePointer();
            //         }

            //         if (EOFFileTemp1 && !EOFFileTemp2) {
            //             int len2 = fileTemp2.readInt();
            //             pointer2 = fileTemp2.getFilePointer();
                        
            //             /* Record FileTemp2 */
            //             bytesArray = new byte[len2];
            //             fileTemp2.read(bytesArray);
            //             movie2.fromByteArray(bytesArray);

            //             pointer2 = fileTemp2.getFilePointer();

            //             if (pointer2 >= fileTemp2.length()) {
            //                 EOFFileTemp2 = true;
            //             }

            //             fileTemp4.writeInt(len2);
            //             fileTemp4.write(movie2.toByteArray());                        
            //             pointer4 = fileTemp4.getFilePointer();
            //         }
            //     }
               
            //     recordsCount++;
            // }

            // fileTemp1.close();                    
            // fileTemp2.close();            
            // fileTemp3.close();            
            // fileTemp4.close();

            // read("files/temp/filetemp3.db");            
            // read("files/temp/filetemp4.db");
            /* Balanced Interleaving - Step 2.2 */

            return true;
        }


        public static ArrayList<Movie> getRecords(String filename) throws IOException {
            arq = new RandomAccessFile(filename, "rw");
            arq.seek(0);

            // Ignore the first four bytes of the last ID
            int totalRecords = arq.readInt();

            ArrayList<Movie> records = new ArrayList<Movie>();

            int len;
            int i = 0;

            while (i < totalRecords) {
                if (arq.getFilePointer() < arq.length()) {
                    char gravestone = arq.readChar();
                    len = arq.readInt();

                    Movie movie = new Movie();

                    bytesArray = new byte[len];
                    arq.read(bytesArray);

                    if (gravestone != '*') {
                        movie.fromByteArray(bytesArray);
                        records.add(movie);
                        i++;
                    }
                } else {
                    break;
                }
            }

            for (Movie record : records) {
                System.out.println(record.getID());
            }

            System.out.println("\n" + records.size());
            
            return records;
        }
        
        /* Read all records */
        public static void read(String filename) throws IOException {
            /* Initializing RandomAccessFile */
            arq = new RandomAccessFile(filename, "rw");

            /* Read */
            Movie movie = new Movie();

            int len;

            arq.seek(0);
            long pointer = arq.getFilePointer();

            while (pointer < arq.length()) {
                len = arq.readInt();
                
                bytesArray = new byte[len];
                arq.read(bytesArray);
                movie.fromByteArray(bytesArray);

                System.out.println(movie.getID());

                pointer = arq.getFilePointer();
            }
            arq.close();
        }


    }

}   
