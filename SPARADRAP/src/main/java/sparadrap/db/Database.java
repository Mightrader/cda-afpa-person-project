package sparadrap.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static boolean init = false;

    public static synchronized void init() {
        if (init) return;
        Path dir = Paths.get("data");
        try { Files.createDirectories(dir); } catch (Exception ignored) {}
        try (Connection c = getConnection()) { }
        catch (SQLException e) { throw new RuntimeException(e); }
        init = true;
    }

    public static Connection getConnection() throws SQLException {
        // >>> MODE=PostgreSQL retiré pour que H2 accepte le type IDENTITY
        String url = "jdbc:h2:file:./data/sparadrap;AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false";
        return DriverManager.getConnection(url, "sa", "");
    }
}
