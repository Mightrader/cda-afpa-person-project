package sparadrap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pharmacie {

    private final List<Medecins> medecins = new ArrayList<>();
    private final List<Clients> clients = new ArrayList<>();
    private final List<Medicaments> medicaments = new ArrayList<>();
    private final List<Mutuelle> mutuelles = new ArrayList<>();
    private final ThreadLocal<List<Achat>> achats = ThreadLocal.withInitial(ArrayList::new);

    public Pharmacie() {
        seed();
    }

    private void seed() {
        // Mutuelles de départ
        Mutuelle mA = new Mutuelle("Axa Sante", "0800123456", "contact@axa.fr", "Paris", 0.7);
        Mutuelle mB = new Mutuelle("MMA Sante", "0800654321", "info@mma.fr", "Rennes", 0.6);
        mutuelles.add(mA);
        mutuelles.add(mB);

        // Médecins de départ
        Medecins d1 = new Medecins(1, "Martin", "Alice", "0102030405", "Paris");
        Medecins d2 = new Medecins(2, "Durand", "Paul", "0102030406", "Lyon");
        medecins.add(d1);
        medecins.add(d2);

        // Clients de départ
        Clients c1 = new Clients(1, "Dupont", "Jean", "0600000000", "Rennes");
        c1.setMutuelle(mA);
        c1.setMedecinReferent();

        Clients c2 = new Clients(2, "Petit", "Lucie", "0611111111", "Bordeaux");
        c2.setMutuelle(mB);
        c2.setMedecinReferent();

        Clients c3 = new Clients(3, "Moreau", "Lea", "0622222222", "Lyon"); // sans mutuelle

        clients.add(c1);
        clients.add(c2);
        clients.add(c3);

        // Médicaments de départ
        medicaments.add(new Medicaments("Doliprane", "Analgésique", 2.3, 120));
        medicaments.add(new Medicaments("Ibuprofène", "Anti-inflammatoire", 3.9, 80));
        medicaments.add(new Medicaments("Zovirax", "Antiviral", 6.5, 40));
    }

    public List<Medecins> getMedecins() {
        return Collections.unmodifiableList(medecins);
    }

    public List<Clients> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public List<Medicaments> getMedicaments() {
        return Collections.unmodifiableList(medicaments);
    }

    public List<Mutuelle> getMutuelles() {
        return Collections.unmodifiableList(mutuelles);
    }

    // ---- Gestion des mutuelles (CRUD en mémoire) ----

    public Mutuelle addMutuelle(String nom,
                                String telephone,
                                String email,
                                String ville,
                                double tauxPriseEnCharge) {
        Mutuelle m = new Mutuelle(nom, telephone, email, ville, tauxPriseEnCharge);
        mutuelles.add(m);
        return m;
    }

    public void updateMutuelle(Mutuelle mutuelle,
                               String nom,
                               String telephone,
                               String email,
                               String ville,
                               double tauxPriseEnCharge) {
        if (mutuelle == null) {
            return;
        }
        mutuelle.setNom(nom);
        mutuelle.setTelephone(telephone);
        mutuelle.setEmail(email);
        mutuelle.setVille(ville);
        mutuelle.setTauxPriseEnCharge(tauxPriseEnCharge);
    }

    public void deleteMutuelle(Mutuelle mutuelle) {
        if (mutuelle != null) {
            mutuelles.remove(mutuelle);
        }
    }

    // ---- Achat d'un médicament ----

    public Achat acheter(Clients client,
                         Medecins medecin,
                         Mutuelle mutuelle,
                         Medicaments medicament,
                         int quantite) {

        if (medicament == null || quantite <= 0) return null;
        if (medicament.getQuantite() < quantite) {
            return null;
        }
        if (medicament.getPrix() < 0) return null;

        // cohérence mutuelle / client
        if (client != null) {
            Mutuelle mutuelleClient = client.getMutuelle();
            if (mutuelleClient != null && mutuelle != null && mutuelleClient != mutuelle) {
                return null;
            }
            if (mutuelleClient == null && mutuelle != null) {
                return null;
            }
        }

        // Calcul du total
        BigDecimal total = BigDecimal.valueOf(medicament.getPrix())
                .multiply(BigDecimal.valueOf(quantite));
        if (mutuelle != null) {
            BigDecimal taux = BigDecimal.valueOf(1.0 - mutuelle.getTauxPriseEnCharge());
            total = total.multiply(taux);
        }
        total = total.setScale(2, RoundingMode.HALF_UP);

        // décrémente le stock
        medicament.setQuantite(medicament.getQuantite() - quantite);

        // enregistre l'achat
        Achat a = new Achat(client, medecin, mutuelle, medicament, quantite, total);
        achats.get().add(a);
        return a;
    }
}
