package sparadrap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public final class LoggerConfig {
    private static volatile boolean initialized = false;

    private LoggerConfig() {}

    public static synchronized void init() {
        if (initialized) return;

        Path logsDir = Paths.get("logs");
        try {
            Files.createDirectories(logsDir);
        } catch (IOException e) {
            System.err.println("Impossible de créer le dossier logs : " + e.getMessage());
        }

        // Reset et configuration du logger racine
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);

        // Console
        ConsoleHandler console = new ConsoleHandler();
        console.setLevel(Level.INFO);
        console.setFormatter(new SimpleFormatter());
        root.addHandler(console);

        // Fichier tournant (5 fichiers de 1 Mo)
        try {
            FileHandler file =
                    new FileHandler(logsDir.resolve("app-%g.log").toString(), 1_000_000, 5, true);
            file.setLevel(Level.INFO);
            file.setFormatter(new SimpleFormatter());
            root.addHandler(file);
        } catch (IOException e) {
            System.err.println("Impossible d'ouvrir le fichier de log : " + e.getMessage());
        }

        initialized = true;
    }

    public static Logger getLogger(Class<?> cls) {
        init();
        return Logger.getLogger(cls.getName());
    }
}
