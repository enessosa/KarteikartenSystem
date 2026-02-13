package helper;

import java.nio.file.*;

public final class DbPaths {

    public static Path appDir(String appName) {
        String os = System.getProperty("os.name").toLowerCase();
        Path base;

        if (os.contains("win")) {
            String appData = System.getenv("APPDATA");
            base = (appData != null) ? Paths.get(appData) : Paths.get(System.getProperty("user.home"), "AppData", "Roaming");
        } else if (os.contains("mac")) {
            base = Paths.get(System.getProperty("user.home"), "Library", "Application Support");
        } else {
            // Linux/Unix
            String xdg = System.getenv("XDG_DATA_HOME");
            base = (xdg != null) ? Paths.get(xdg) : Paths.get(System.getProperty("user.home"), ".local", "share");
        }
        return base.resolve(appName);
    }

    public static String sqliteUrl(String appName, String dbFileName) {
        try {
            Path dir = appDir(appName);
            Files.createDirectories(dir);
            Path dbPath = dir.resolve(dbFileName);
            return "jdbc:sqlite:" + dbPath.toAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Konnte DB-Pfad nicht erstellen", e);
        }
    }
}
