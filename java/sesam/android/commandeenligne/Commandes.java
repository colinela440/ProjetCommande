package sesam.android.commandeenligne;

public class Commandes {
    private String number;
    private String client;
    private String quantity;

    public Commandes(String number, String client, String quantity){
        this.number = number;
        this.client = client;
        this.quantity = quantity;
    }

    public String getNumber(){
        return number;
    }

    public String getClient(){
        return client;
    }

    public String getQuantity(){
        return quantity;
    }
}
