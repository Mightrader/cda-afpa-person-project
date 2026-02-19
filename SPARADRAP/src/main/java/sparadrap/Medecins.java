package sparadrap;

import sparadrap.model.Medecin;

public class Medecins extends Medecin {

    public Medecins() {
        super();
    }

    public Medecins(int id, String nom, String prenom, String telephone, String ville) {
        super(id, nom, prenom);
        setTelephone(telephone);
        setVille(ville);
    }

    public Medecins(int id,
                    String nom,
                    String prenom,
                    String adresse,
                    String codePostal,
                    String ville,
                    String telephone,
                    String email,
                    String specialite) {
        super(id, nom, prenom, adresse, codePostal, ville, telephone, email, specialite);
    }
}
