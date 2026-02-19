package sparadrap.model;

/**
 * Représente un médecin prescripteur.
 * <p>
 * Hérite de {@link Personne} et ajoute une spécialité médicale.
 */
public class Medecin extends Personne {

    private String specialite;

    public Medecin() {
        super();
    }

    public Medecin(int id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    public Medecin(int id,
                   String nom,
                   String prenom,
                   String adresse,
                   String codePostal,
                   String ville,
                   String telephone,
                   String email,
                   String specialite) {
        super(id, nom, prenom, adresse, codePostal, ville, telephone, email);
        setSpecialite(specialite);
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        if (specialite == null || specialite.isBlank()) {
            this.specialite = null;
        } else {
            this.specialite = specialite.trim();
        }
    }

    @Override
    public String toString() {
        String label = getNomComplet();
        if (specialite == null || specialite.isBlank()) {
            return "Dr " + label;
        }
        return "Dr " + label + " (" + specialite + ")";
    }
}
