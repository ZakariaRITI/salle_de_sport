package Class;

import java.sql.Date;
import java.sql.Time;

public class Seance {

    private int id_seance;
    private Date date_seance;
    private Time heure;
    private char acces;
    private String type;
    private int id_coach;

    public Seance(int id_seance,Date date_seance, Time heure, char acces, String type, int id_coach) {
        this.id_seance = id_seance;
        this.date_seance = date_seance;
        this.heure = heure;
        this.type = type;
        this.acces = acces;
        this.id_coach = id_coach;
    }

    public Date getDate_seance() {
        return date_seance;
    }

    public void setDate_seance(Date date_seance) {
        this.date_seance = date_seance;
    }


    public char getAcces() {
        return acces;
    }

    public void setAcces(char acces) {
        this.acces = acces;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId_coach() {
        return id_coach;
    }

    public void setId_coach(int id_coach) {
        this.id_coach = id_coach;
    }

    public int getId_seance() {
        return id_seance;
    }

    public void setId_seance(int id_seance) {
        this.id_seance = id_seance;
    }
}
