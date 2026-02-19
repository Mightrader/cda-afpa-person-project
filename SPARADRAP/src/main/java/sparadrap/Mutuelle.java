package sparadrap;

public class Mutuelle {
    private String nom;
    private double tauxPriseEnCharge;
    private String telephone;
    private String email;
    private String ville;
    public Mutuelle() {}
    public Mutuelle(String nom, double taux){
        this.nom=nom;
        this.tauxPriseEnCharge=taux;
    }
    public Mutuelle(String nom, String telephone, String email, String ville, double taux){
        this.nom=nom;
        this.telephone=telephone;
        this.email=email;
        this.ville=ville;
        this.tauxPriseEnCharge=taux;
    }
    public String getNom(){ return nom;
    } public void setNom(String nom){ this.nom=nom;
    }
    public double getTauxPriseEnCharge(){ return tauxPriseEnCharge;
    } public void setTauxPriseEnCharge(double t){ this.tauxPriseEnCharge=t;
    }
    public String getTelephone(){ return telephone;
    } public void setTelephone(String t){ this.telephone=t;
    }
    public String getEmail(){ return email;
    } public void setEmail(String e){ this.email=e;
    }
    public String getVille(){ return ville;
    } public void setVille(String v){ this.ville=v;
    }
    @Override public String toString(){ return nom==null?"Mutuelle":nom;
    }
}
