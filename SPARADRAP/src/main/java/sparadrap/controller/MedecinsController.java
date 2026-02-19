package sparadrap.controller;

import sparadrap.Medecins;
import sparadrap.dao.MedecinDao;

import java.util.List;

public class MedecinsController {

    private final MedecinDao dao;

    public MedecinsController() {
        this.dao = new MedecinDao();
    }

    public List<Medecins> listMedecins() {
        return dao.findAll();
    }

    public Medecins createMedecin(String nom) {
        Medecins m = new Medecins();
        m.setNom(nom);
        return dao.insert(m);
    }

    public void updateMedecin(Medecins medecin, String nouveauNom) {
        if (medecin == null) {
            return;
        }
        medecin.setNom(nouveauNom);
        dao.update(medecin);
    }

    public void deleteMedecin(Medecins medecin) {
        if (medecin == null) {
            return;
        }
        dao.delete(medecin);
    }
}
