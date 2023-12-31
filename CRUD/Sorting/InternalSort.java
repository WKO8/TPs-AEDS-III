package CRUD.Sorting;
import java.util.ArrayList;

import CRUD.Movie.Movie;

public class InternalSort {
    protected static class Sort {
        protected static ArrayList<Movie> sort(ArrayList<Movie> arr) {
            int n = arr.size();
            for (int i = 1; i < n; ++i) {
                int key = arr.get(i).getID();
                int j = i - 1;
    
                /* Move elements of arr[0..i-1], that are
                greater than key, to one position ahead
                of their current position */
                while (j >= 0 && arr.get(j).getID() > key) {
                    arr.get(j + 1).setID(arr.get(j).getID());
                    j = j - 1;
                }
                arr.get(j+1).setID(key);
            }

            return arr;
        }
    }
}
