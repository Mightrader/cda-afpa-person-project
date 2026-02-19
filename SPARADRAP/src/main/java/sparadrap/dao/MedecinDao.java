package sparadrap.dao;

import sparadrap.Medecins;
import sparadrap.db.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinDao {

    private static final String SELECT_ALL   = "SELECT id, nom FROM medecins ORDER BY nom";
    private static final String SELECT_BY_ID = "SELECT id, nom FROM medecins WHERE id = ?";
    private static final String INSERT       = "INSERT INTO medecins(nom) VALUES (?)";
    private static final String UPDATE       = "UPDATE medecins SET nom = ? WHERE id = ?";
    private static final String DELETE       = "DELETE FROM medecins WHERE id = ?";

    public List<Medecins> findAll() {
        List<Medecins> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Medecins m = new Medecins();
                m.setId(id);
                m.setNom(nom);
                result.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des médecins", e);
        }
        return result;
    }

    public Medecins findById(int id) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medecins m = new Medecins();
                    m.setId(rs.getInt("id"));
                    m.setNom(rs.getString("nom"));
                    return m;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du médecin id=" + id, e);
        }
        return null;
    }

    public Medecins insert(Medecins medecin) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, medecin.getNom());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Insertion du médecin impossible (aucune ligne ajoutée)");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    medecin.setId(rs.getInt(1));
                }
            }
            return medecin;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion du médecin", e);
        }
    }

    public void update(Medecins medecin) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE)) {

            ps.setString(1, medecin.getNom());
            ps.setInt(2, medecin.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du médecin id=" + medecin.getId(), e);
        }
    }

    public void delete(int id) {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du médecin id=" + id, e);
        }
    }

    public void delete(Medecins medecin) {
        if (medecin != null) {
            delete(medecin.getId());
        }
    }
}
