package hr.fer.zemris.ecf.lab.model.info;

/**
 * Created by Domagoj on 08/04/15.
 */
public class InfoService {

    private static String lastSelectedPath;
    private static String ecfPath;

    public static void setLastSelectedPath(String lastSelectedPath) {
        InfoService.lastSelectedPath = lastSelectedPath;
    }

    public static String getLastSelectedPath() {
        return lastSelectedPath;
    }

    public static String getEcfPath() {
        return ecfPath;
    }

    public static void setEcfPath(String ecfPath) {
        InfoService.ecfPath = ecfPath;
    }
}
