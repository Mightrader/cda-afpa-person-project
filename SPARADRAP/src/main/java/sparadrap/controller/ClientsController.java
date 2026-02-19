package sparadrap.controller;

import sparadrap.Clients;
import sparadrap.dao.ClientDao;

import java.util.List;

/**
 * Contrôleur pour la gestion des clients.
 * <p>
 * Sert d'intermédiaire entre la vue (ClientsPanel) et la couche DAO (ClientDao).
 */
public class ClientsController {

    private final ClientDao dao;

    public ClientsController() {
        this.dao = new ClientDao();
    }

    /**
     * Retourne la liste des clients à afficher dans l'interface.
     */
    public List<Clients> listClients() {
        return dao.findAll();
    }

    /**
     * Crée un nouveau client avec un nom donné.
     */
    public Clients createClient(String nom) {
        Clients client = new Clients();
        client.setNom(nom);
        return dao.insert(client);
    }

    /**
     * Met à jour le nom d'un client existant.
     */
    public void updateClient(Clients client, String nouveauNom) {
        if (client == null) {
            return;
        }
        client.setNom(nouveauNom);
        dao.update(client);
    }

    /**
     * Supprime un client.
     */
    public void deleteClient(Clients client) {
        if (client == null) {
            return;
        }
        dao.delete(client);
    }
}
