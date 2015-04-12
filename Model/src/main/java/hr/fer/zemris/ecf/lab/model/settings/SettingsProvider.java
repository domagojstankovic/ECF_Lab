package hr.fer.zemris.ecf.lab.model.settings;

/**
 * Created by Domagoj on 08/04/15.
 */
public class SettingsProvider {

    private static Settings instance = null;

    private SettingsProvider() {
    }

    public static Settings getSettings() {
        return instance;
    }

    public static void setSettings(Settings settings) {
        if (instance != null) {
            throw new IllegalStateException("Settings implementation has already been set");
        }
        instance = settings;
    }

}
