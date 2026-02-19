package sparadrap.model;

import java.util.Objects;
import sparadrap.exception.ValidationException;
import sparadrap.util.regex.Regex;

/**
 * Représente une personne de base dans la pharmacie.
 * <p>
 * Cette classe porte les informations communes aux clients, médecins, etc.
 * Elle vérifie les valeurs reçues dans les setters (codage défensif).
 */
public class Personne {

    private int id;
    private String nom;
    private String prenom;
    private String adresse;
    private String codePostal;
    private String ville;
    private String telephone;
    private String email;

    /**
     * Constructeur sans argument.
     */
    public Personne() {
    }

    /**
     * Constructeur minimal avec identifiant, nom et prénom.
     *
     * @param id     identifiant technique
     * @param nom    nom de famille
     * @param prenom prénom
     */
    public Personne(int id, String nom, String prenom) {
        this.id = id;
        setNom(nom);
        setPrenom(prenom);
    }

    /**
     * Constructeur complet avec coordonnées.
     */
    public Personne(int id,
                    String nom,
                    String prenom,
                    String adresse,
                    String codePostal,
                    String ville,
                    String telephone,
                    String email) {
        this.id = id;
        setNom(nom);
        setPrenom(prenom);
        setAdresse(adresse);
        setCodePostal(codePostal);
        setVille(ville);
        setTelephone(telephone);
        setEmail(email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new ValidationException("L'identifiant doit être positif");
        }
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        this.nom = nom.trim();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        if (prenom == null || prenom.isBlank()) {
            throw new ValidationException("Le prénom est obligatoire");
        }
        this.prenom = prenom.trim();
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse == null ? null : adresse.trim();
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        if (codePostal != null && !codePostal.isBlank()) {
            String value = codePostal.trim();
            if (!Regex.isValid(value, Regex.CODE_POSTAL)) {
                throw new ValidationException("Code postal invalide: " + value);
            }
            this.codePostal = value;
        } else {
            this.codePostal = null;
        }
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville == null ? null : ville.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone != null && !telephone.isBlank()) {
            String value = telephone.trim();
            if (!Regex.isValid(value, Regex.TELEPHONE)) {
                throw new ValidationException("Téléphone invalide: " + value);
            }
            this.telephone = value;
        } else {
            this.telephone = null;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isBlank()) {
            String value = email.trim();
            if (!Regex.isValid(value, Regex.EMAIL)) {
                throw new ValidationException("Email invalide: " + value);
            }
            this.email = value;
        } else {
            this.email = null;
        }
    }

    /**
     * @return nom + prénom sur une seule chaîne.
     */
    public String getNomComplet() {
        String n = nom == null ? "" : nom;
        String p = prenom == null ? "" : prenom;
        return (n + " " + p).trim();
    }

    @Override
    public String toString() {
        return getNomComplet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personne)) return false;
        Personne personne = (Personne) o;
        return id == personne.id &&
                Objects.equals(nom, personne.nom) &&
                Objects.equals(prenom, personne.prenom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom);
    }
}
