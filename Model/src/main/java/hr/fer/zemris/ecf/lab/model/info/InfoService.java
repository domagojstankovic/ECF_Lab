package hr.fer.zemris.ecf.lab.model.info;

/**
 * Created by Domagoj on 08/04/15.
 */
public class InfoService {

    private static String lastSelectedConfPath;

    public static void setLastSelectedConfPath(String lastSelectedConfPath) {
        InfoService.lastSelectedConfPath = lastSelectedConfPath;
    }

    public static String getLastSelectedConfPath() {
        return lastSelectedConfPath;
    }



    private static String lastSelectedECFexecutable;

    public static String getLastSelectedECFexecutable() {
        return lastSelectedECFexecutable;
    }

    public static void setLastSelectedECFexecutable(String lastSelectedECFexecutable) {
        InfoService.lastSelectedECFexecutable = lastSelectedECFexecutable;
    }
}
