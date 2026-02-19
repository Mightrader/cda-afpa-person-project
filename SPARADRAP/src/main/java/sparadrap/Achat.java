package sparadrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Achat {
    private Clients clients;
    private Medecins medecins;
    private Mutuelle mutuelle;
    private Medicaments medicaments;
    private int quantite;
    private BigDecimal total;
    private LocalDateTime date;

    public Achat() {}
    public Achat(Clients clients, Medecins medecins, Mutuelle mutuelle, Medicaments medicament, int quantite, BigDecimal total){
        this.clients=clients;
        this.medecins = medecins;
        this.mutuelle=mutuelle;
        this.medicaments=medicament;
        this.quantite=quantite;
        this.total=total;
        this.date=LocalDateTime.now();
    }
    public Clients getClient(){ return clients;
    } public Medecins getMedecins(){ return medecins;
    } public Mutuelle getMutuelle(){ return mutuelle;
    }
    public Medicaments getMedicament(){ return medicaments;
    } public int getQuantite(){ return quantite;
    } public BigDecimal getTotal(){ return total;
    }
    public LocalDateTime getDate(){ return date;
    }
    @Override public String toString(){ return "Achat(" + (clients==null?"?":clients.toString()) + ", " + (medecins ==null?"?": medecins.toString()) + ", " + (medicaments==null?"?":medicaments.getNom()) + " x" + quantite + ")";
    }
}
