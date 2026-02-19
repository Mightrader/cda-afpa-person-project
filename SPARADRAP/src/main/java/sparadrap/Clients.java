package sparadrap;

import sparadrap.model.Client;

public class Clients extends Client {

    public Clients() {
        super();
    }

    public Clients(int id, String nom, String prenom, String telephone, String ville) {
        super(id, nom, prenom);
        setTelephone(telephone);
        setVille(ville);
    }

    public Clients(int id,
                   String nom,
                   String prenom,
                   String adresse,
                   String codePostal,
                   String ville,
                   String telephone,
                   String email,
                   String numeroSecuriteSociale,
                   Mutuelle mutuelle) {
        super(id, nom, prenom, adresse, codePostal, ville, telephone, email, numeroSecuriteSociale, mutuelle);
    }

    public void setMedecinReferent() {
    }
}
