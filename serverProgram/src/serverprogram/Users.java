package serverprogram;

public class Users {
    private String name;
    private int ID;
    private String adresseIP;

    public Users(String name, int ID, String adresseIP) {
        this.name = name;
        this.ID = ID;
        this.adresseIP = adresseIP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP(String adresseIP) {
        this.adresseIP = adresseIP;
    }
}
