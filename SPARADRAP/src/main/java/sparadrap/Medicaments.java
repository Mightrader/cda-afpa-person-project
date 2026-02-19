package sparadrap;

public class Medicaments {
    private final String nom;
    private final String categorie;
    private final double prix;
    private int quantite;

    public Medicaments(String nom,
                       String categorie, double prix, int quantite){ this.nom=nom;
        this.categorie=categorie;
        this.prix=prix; this.quantite=quantite;
    }
    public String getNom(){ return nom;
    }

    public String getCategorie(){ return categorie;
    }

    public double getPrix(){ return prix;
    }

    public int getQuantite(){ return quantite;
    } public void setQuantite(int q){ this.quantite=q;
    }
    @Override public String toString(){ return nom + " (" + String.format(java.util.Locale.FRANCE, "%.2f", prix) + "€)";
    }
}
