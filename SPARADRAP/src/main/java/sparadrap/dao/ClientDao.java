package sparadrap.dao;

import sparadrap.Clients;
import sparadrap.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de l'accès aux données pour l'entité Client.
 * <p>
 * Cette classe encapsule toutes les requêtes SQL vers la table <code>clients</code>.
 */
public class ClientDao {

    private static final String SELECT_ALL   = "SELECT id, nom FROM clients ORDER BY nom";
    private static final String SELECT_BY_ID = "SELECT id, nom FROM clients WHERE id = ?";
    private static final String INSERT       = "INSERT INTO clients(nom) VALUES (?)";
    private static final String UPDATE       = "UPDATE clients SET nom = ? WHERE id = ?";
    private static final String DELETE       = "DELETE FROM clients WHERE id = ?";

    /**
     * Retourne la liste de tous les clients.
     */
    public List<Clients> findAll() {
        List<Clients> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Clients client = new Clients();
                client.setId(id);
                client.setNom(nom);
                result.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des clients", e);
        }
        return result;
    }

    /**
     * Recherche un client par identifiant.
     */
    public Clients findById(int id) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Clients client = new Clients();
                    client.setId(rs.getInt("id"));
                    client.setNom(rs.getString("nom"));
                    return client;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du client id=" + id, e);
        }
        return null;
    }

    /**
     * Insère un nouveau client en base et met à jour son identifiant.
     */
    public Clients insert(Clients client) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getNom());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Insertion du client impossible (aucune ligne ajoutée)");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
            return client;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du client", e);
        }
    }

    /**
     * Met à jour les informations d'un client existant.
     */
    public void update(Clients client) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {

            ps.setString(1, client.getNom());
            ps.setInt(2, client.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du client id=" + client.getId(), e);
        }
    }

    /**
     * Supprime un client par identifiant.
     */
    public void delete(int id) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du client id=" + id, e);
        }
    }

    /**
     * Supprime un client existant.
     */
    public void delete(Clients client) {
        if (client != null) {
            delete(client.getId());
        }
    }
}
