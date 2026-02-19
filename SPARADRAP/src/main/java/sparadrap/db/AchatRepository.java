package sparadrap.db;

import java.sql.*;

public class AchatRepository {

    public void createSchema() {
        try (Connection c = Database.getConnection(); Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS clients(id IDENTITY PRIMARY KEY, nom VARCHAR(100) UNIQUE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS medecins(id IDENTITY PRIMARY KEY, nom VARCHAR(100) UNIQUE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS mutuelles(id IDENTITY PRIMARY KEY, nom VARCHAR(100) UNIQUE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS medicaments(id IDENTITY PRIMARY KEY, nom VARCHAR(100) UNIQUE)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS achats(" +
                    "id IDENTITY PRIMARY KEY, client_id BIGINT, medecin_id BIGINT, mutuelle_id BIGINT, " +
                    "medicament_id BIGINT, qte INT, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void seed() {
        upsert("clients", "nom", "Dupont Jean");
        upsert("medecins", "nom", "Dr Martin Alice");
        upsert("mutuelles", "nom", "Axa Santé");
        upsert("medicaments", "nom", "Doliprane");
    }

    public void savePurchase(String client, String medecin, String mutuelle, String medicament, int qte) {
        seedName("clients", client);
        seedName("medecins", medecin);
        if (mutuelle != null && !mutuelle.isBlank()) seedName("mutuelles", mutuelle);
        seedName("medicaments", medicament);

        Long clientId  = idByName("clients", "nom", client);
        Long medId     = idByName("medecins", "nom", medecin);
        Long mutId     = (mutuelle == null || mutuelle.isBlank()) ? null : idByName("mutuelles", "nom", mutuelle);
        Long medocId   = idByName("medicaments", "nom", medicament);

        String sql = "INSERT INTO achats(client_id, medecin_id, mutuelle_id, medicament_id, qte) VALUES(?,?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, clientId);
            ps.setObject(2, medId);
            ps.setObject(3, mutId);
            ps.setObject(4, medocId);
            ps.setInt(5, qte);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void upsert(String table, String col, String value) {
        String sql = "MERGE INTO " + table + "(" + col + ") KEY(" + col + ") VALUES(?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void seedName(String table, String name) {
        if (name == null || name.isBlank()) return;
        upsert(table, "nom", name);
    }

    private Long idByName(String table, String col, String value) {
        String sql = "SELECT id FROM " + table + " WHERE " + col + "=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
