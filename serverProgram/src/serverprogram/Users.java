package serverprogram;

public class Users {

    private String name;
    private int ID;
    private String adresseIP;
    private boolean ban_id;
    private boolean ban_ip;

    public Users(String name, int ID, String adresseIP, boolean ban_id, boolean ban_ip) {
        this.name = name;
        this.ID = ID;
        this.adresseIP = adresseIP;
        this.ban_id = ban_id;
        this.ban_ip = ban_ip;
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

    public boolean isBan_id() {
        return ban_id;
    }

    public void setBan_id(boolean ban_id) {
        this.ban_id = ban_id;
    }

    public boolean isBan_ip() {
        return ban_ip;
    }

    public void setBan_ip(boolean ban_ip) {
        this.ban_ip = ban_ip;
    }

}
