package sparadrap.controller;

import sparadrap.Mutuelle;
import sparadrap.Pharmacie;

import java.util.ArrayList;
import java.util.List;

public class MutuellesController {

    private final Pharmacie service;

    public MutuellesController(Pharmacie service) {
        this.service = service;
    }

    public List<Mutuelle> listMutuelles() {
        return new ArrayList<>(service.getMutuelles());
    }

    public Mutuelle createMutuelle(String nom,
                                   String telephone,
                                   String email,
                                   String ville,
                                   double taux) {
        return service.addMutuelle(nom, telephone, email, ville, taux);
    }

    public void updateMutuelle(Mutuelle mutuelle,
                               String nom,
                               String telephone,
                               String email,
                               String ville,
                               double taux) {
        service.updateMutuelle(mutuelle, nom, telephone, email, ville, taux);
    }

    public void deleteMutuelle(Mutuelle mutuelle) {
        service.deleteMutuelle(mutuelle);
    }
}
