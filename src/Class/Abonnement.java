package Class;

public class Abonnement {

    private String type;
    private String zone;
    private int coaching;
    private int vip;
    private int promotion;
    private float tarif;

    public Abonnement(String type, String zone, int coaching, int vip, int promotion, float tarif) {
        this.type = type;
        this.zone = zone;
        this.coaching = coaching;
        this.vip = vip;
        this.promotion = promotion;
        this.tarif = tarif;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCoaching() {
        return coaching;
    }

    public void setCoaching(int coaching) {
        this.coaching = coaching;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public float getTarif() {
        return tarif;
    }

    public void setTarif(float tarif) {
        this.tarif = tarif;
    }
}
