package fr.esir.regulation;

/**
 * Created by Nicolas on 21/02/2015.
 */
public class NbPerson {
    private long startDate;
    private long endDate;
    private int nb_pers;

    public NbPerson(long startDate, long endDate, int nb_pers) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.nb_pers = nb_pers;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public int getNb_pers() {
        return nb_pers;
    }
}

