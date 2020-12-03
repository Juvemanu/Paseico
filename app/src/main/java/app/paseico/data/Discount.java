package app.paseico.data;

public class Discount {
    private String name;
    private String percentage;
    private int points;
    private String organiID;
    public Discount() {}

    public Discount(String n, String p, int pts, String organiID) {
        this.name = n;
        this.percentage = p;
        this.points = pts;
        this.organiID = organiID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }




}
