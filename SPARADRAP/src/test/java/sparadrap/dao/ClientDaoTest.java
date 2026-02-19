package sparadrap.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sparadrap.Clients;
import sparadrap.db.AchatRepository;
import sparadrap.db.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDaoTest {

    private static ClientDao dao;

    @BeforeAll
    static void initDatabase() {
        // Initialisation de la base
        Database.init();
        AchatRepository repo = new AchatRepository();
        repo.createSchema();

        // Nettoyage la table clients
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("DELETE FROM clients");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du nettoyage de la table clients", e);
        }

        // On remet les données de base
        repo.seed();

        dao = new ClientDao();
    }

    @Test
    void findAllRetourneAuMoinsUnClient() {
        List<Clients> clients = dao.findAll();

        assertNotNull(clients);
        assertFalse(clients.isEmpty(), "La liste des clients ne doit pas être vide");
    }

    @Test
    void insertPuisFindByIdFonctionnent() {
        Clients nouveau = new Clients();
        nouveau.setNom("Client DAO Test");

        dao.insert(nouveau);
        int idGenere = nouveau.getId();

        assertTrue(idGenere > 0, "L'id généré doit être > 0");

        Clients depuisLaBase = dao.findById(idGenere);
        assertNotNull(depuisLaBase, "Le client doit être retrouvé en base");
        assertEquals("Client DAO Test", depuisLaBase.getNom());
    }

    @Test
    void updateModifieBienLeNom() {
        Clients client = new Clients();
        client.setNom("Client test avant");
        dao.insert(client);
        int id = client.getId();

        client.setNom("Client test");
        dao.update(client);

        Clients depuisLaBase = dao.findById(id);
        assertNotNull(depuisLaBase);
        assertEquals("Client test", depuisLaBase.getNom());
    }

    @Test
    void deleteSupprimeLeClient() {
        Clients client = new Clients();
        client.setNom("Client À Supprimer");
        dao.insert(client);
        int id = client.getId();

        dao.delete(id);

        Clients depuisLaBase = dao.findById(id);
        assertNull(depuisLaBase, "Le client supprimé ne doit plus être retrouvé");
    }
}
