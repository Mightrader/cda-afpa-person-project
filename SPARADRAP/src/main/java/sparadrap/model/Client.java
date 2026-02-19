package sparadrap.model;

import sparadrap.Mutuelle;

/**
 * Représente un client de la pharmacie.
 * <p>
 * Hérite des informations de base de {@link Personne} et ajoute
 * des informations spécifiques comme la mutuelle.
 */
public class Client extends Personne {

    private String numeroSecuriteSociale;
    private Mutuelle mutuelle;

    public Client() {
        super();
    }

    public Client(int id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    public Client(int id,
                  String nom,
                  String prenom,
                  String adresse,
                  String codePostal,
                  String ville,
                  String telephone,
                  String email,
                  String numeroSecuriteSociale,
                  Mutuelle mutuelle) {
        super(id, nom, prenom, adresse, codePostal, ville, telephone, email);
        setNumeroSecuriteSociale(numeroSecuriteSociale);
        this.mutuelle = mutuelle;
    }

    public String getNumeroSecuriteSociale() {
        return numeroSecuriteSociale;
    }

    public void setNumeroSecuriteSociale(String numeroSecuriteSociale) {
        if (numeroSecuriteSociale == null || numeroSecuriteSociale.isBlank()) {
            this.numeroSecuriteSociale = null;
        } else {
            this.numeroSecuriteSociale = numeroSecuriteSociale.trim();
        }
    }

    public Mutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(Mutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
