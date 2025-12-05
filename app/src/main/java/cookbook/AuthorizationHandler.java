package cookbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

public class AuthorizationHandler {

    private static final Path APP_PATH = Paths.get(AuthorizationHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    private static String auth = null;

    private static void loadAuth() {
        Path authPath = APP_PATH.getParent().resolve("authorization.txt");

        if (!authPath.toFile().exists()) {
            auth = JOptionPane.showInputDialog("API Key");
            try {Files.write(authPath, auth.getBytes());}
            catch (IOException e) {e.printStackTrace();}
            return;
        };

        try {auth = Files.readString(authPath);}
        catch (IOException e) {e.printStackTrace();};
    }

    public static String getAuthorization() {
        if (auth == null) loadAuth();
        return auth;
    }
}
