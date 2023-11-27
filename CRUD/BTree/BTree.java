package CRUD.BTree;
import CRUD.Movie.Movie;

public class BTree {
    private static class Page {
        int n; Movie r[]; Page p[];
        
        public Page(int mm) {
            this.n = 0; this.r = new Movie[mm]; this.p = new Page[mm+1];
        }
    }

    private Page root;
    private int m, mm;

    public BTree(int m) {
        this.root = null; this.m = m; this.mm = 2*m;
    }

    // CRUD - Methods 

    /* Insert */
    private void insertOnPage(Page page, Movie reg, Page pageRt) {
        int k = page.n - 1;
        while ((k >= 0) && (reg.compare(page.r[k]) < 0)) {
            page.r[k+1] = page.r[k]; page.p[k+2] = page.p[k+1]; k--;
        }
        page.r[k+1] = reg; page.p[k+2] = pageRt; page.n++;
    }
    
    public void insert(Movie reg) {
        Movie regReturn[] = new Movie[1];
        boolean increased[] = new boolean[1];
        Page pageReturn = this.insert(reg, this.root, regReturn, increased);
        if (increased[0]) {
            Page pageTemp = new Page(this.mm);
            pageTemp.r[0] = regReturn[0];
            pageTemp.p[0] = this.root;
            pageTemp.p[1] = pageReturn;
            this.root = pageTemp; this.root.n++;
        } else this.root = pageReturn;
    }

    private Page insert(Movie reg, Page page, Movie[] regReturn, boolean[] increased) {
        Page pageReturn = null;
        if (page == null) { increased[0] = true; regReturn[0] = reg; }
        else {
            int i = 0;
            while ((i < page.n-1) && (reg.compare(page.r[i]) > 0)) i++;
            if (reg.compare(page.r[i]) == 0) {
                System.out.println("Error: Record already exists");
                increased[0] = false;
            } else {
                if (reg.compare(page.r[i]) > 0) i++;
                pageReturn = insert(reg, page.p[i], regReturn, increased);
                if (increased[0])
                    if (page.n < this.mm) {
                        this.insertOnPage(page, regReturn[0], pageReturn);
                        increased[0] = false; pageReturn = page;
                    } else {
                        Page pageTemp = new Page(this.mm); pageTemp.p[0] = null;
                        if (i <= this.m) {
                            this.insertOnPage(pageTemp, page.r[this.mm-1], page.p[this.mm]);
                            page.n--;
                            this.insertOnPage(page, regReturn[0], pageReturn);
                        } else this.insertOnPage(pageTemp, regReturn[0], pageReturn);
                        for (int j = this.m+1; j < this.mm; j++) {
                            this.insertOnPage(pageTemp, page.r[j], page.p[j+1]);
                            page.p[j+1] = null;
                        }
                        page.n = this.m; pageTemp.p[0] = page.p[this.m+1];
                        regReturn[0] = page.r[this.m]; pageReturn = pageTemp;
                    }
            }
        }
        return (increased[0] ? pageReturn : page);
    }

    /* Search */
    private Movie search(Movie reg, Page page) {
        if (page == null) return null; // Record not found
        else {
            int i = 0;
            while ((i < page.n-1) && (reg.compare(page.r[i]) > 0)) i++;
            if (reg.compare(page.r[i]) == 0) return page.r[i];
            else if (reg.compare(page.r[i]) < 0) return search(reg, page.p[i]);
            else return search(reg, page.p[i+1]);
        }
    }

    public Movie search(Movie reg) {
        return this.search(reg, this.root);
    }

    /* Remove */
    public void remove(Movie reg) {
        boolean decreased[] = new boolean[1];
        this.root = this.remove(reg, this.root, decreased);
        if (decreased[0] && (this.root.n == 0)) {
            this.root = this.root.p[0];
        }
    }

    private Page remove(Movie reg, Page page, boolean[] decreased) {
        if (page == null) {
            System.out.println("Error: Record not found");
            decreased[0] = false;
        } else {
            int idx = 0;
            while ((idx < page.n-1) && (reg.compare(page.r[idx]) > 0)) idx++;
            if (reg.compare(page.r[idx]) == 0) {
                if (page.p[idx] == null) {
                    page.n--; decreased[0] = page.n < this.m;
                    for (int j = idx; j < page.n; j++) {
                        page.r[j] = page.r[j+1]; page.p[j] = page.p[j+1];
                    }
                    page.p[page.n] = page.p[page.n+1];
                    page.p[page.n+1] = null;
                } else {
                    decreased[0] = predecessor(page, idx, page.p[idx]);
                    if (decreased[0]) decreased[0] = restore(page.p[idx], page, idx);
                }
            } else {
                if (reg.compare(page.r[idx]) > 0) idx++;
                page.p[idx] = remove(reg, page.p[idx], decreased);
                if (decreased[0]) decreased[0] = restore(page.p[idx], page, idx);
            }
        }
        return page;
    }

    /* Auxiliary methods */
    private boolean predecessor(Page page, int idx, Page pageParent) {
        boolean decreased = true;
        if (pageParent.p[pageParent.n] != null) {
            decreased = predecessor(page, idx, pageParent.p[pageParent.n]);
            if (decreased) decreased = restore(pageParent.p[pageParent.n], pageParent, pageParent.n);
        } else {
            page.r[idx] = pageParent.r[--pageParent.n]; decreased = pageParent.n < this.m;
        }
        return decreased;
    }

    private boolean restore(Page pagePag, Page pageParent, int posParent) {
        boolean decreased = true;
        if (posParent < pageParent.n) {
            Page aux = pageParent.p[posParent+1];
            int dispAux = (aux.n - this.m + 1)/2;
            pagePag.r[pagePag.n++] = pageParent.r[posParent]; pagePag.p[pagePag.n] = aux.p[0];
            aux.p[0] = null;
            if (dispAux > 0) {
                for (int j = 0; j < dispAux - 1; j++) {
                    this.insertOnPage(pagePag, aux.r[j], aux.p[j + 1]);
                    aux.p[j+1] = null;
                }
                pageParent.r[posParent] = aux.r[dispAux-1];
                aux.n = aux.n - dispAux;
                for (int j = 0; j < aux.n; j++) aux.r[j] = aux.r[j+dispAux];
                for (int j = 0; j <= aux.n; j++) aux.p[j] = aux.p[j+dispAux];
                aux.p[aux.n+dispAux] = null;
                decreased = false;
            } else {
                for (int j = 0; j < this.m; j++) {
                    this.insertOnPage(pagePag, aux.r[j], aux.p[j+1]);
                    aux.p[j+1] = null;
                }
                aux = pageParent.p[posParent+1] = null;
                for (int j = posParent; j < pageParent.n-1; j++) {
                    pageParent.r[j] = pageParent.r[j+1]; pageParent.p[j+1] = pageParent.p[j+2];
                }
                pageParent.p[pageParent.n--] = null;
                decreased = pageParent.n < this.m;
            }
        } else {
            Page aux = pageParent.p[posParent-1];
            int dispAux = (aux.n - this.m + 1)/2;
            for (int j = pagePag.n-1; j >= 0; j--) pagePag.r[j+1] = pagePag.r[j];
            pagePag.r[0] = pageParent.r[posParent-1];
            for (int j = pagePag.n; j >= 0; j--) pagePag.p[j+1] = pagePag.p[j];
            pagePag.n++;
            if (dispAux > 0) {
                for (int j = 0; j < dispAux - 1; j++) {
                    this.insertOnPage(pagePag, aux.r[aux.n-j-1], aux.p[aux.n-j]);
                    aux.p[aux.n-j] = null;
                }
                pagePag.p[0] = aux.p[aux.n - dispAux + 1];
                aux.p[aux.n - dispAux + 1] = null;
                pageParent.r[posParent-1] = aux.r[aux.n - dispAux];
                aux.n = aux.n - dispAux; decreased = false;
            } else {
                for (int j = 0; j < this.m; j++) {
                    this.insertOnPage(aux, pagePag.r[j], pagePag.p[j+1]);
                    pagePag.p[j+1] = null;
                }
                pagePag = null;
                pageParent.p[pageParent.n--] = null;
                decreased = pageParent.n < this.m;
            }
        }
        return decreased;
    }

}
