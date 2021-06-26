package sesam.android.commandeenligne;

public class Books {
    private String titre;
    private String auteur;
    private String cover;
    private String quantity;

    public Books(String titre, String auteur, String cover, String quantity) {
        this.titre = titre;
        this.auteur = auteur;
        this.cover = cover;
        this.quantity = quantity;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCover(){
        return cover;
    }

    public String getQuantity() {
        return quantity;
    }
}
