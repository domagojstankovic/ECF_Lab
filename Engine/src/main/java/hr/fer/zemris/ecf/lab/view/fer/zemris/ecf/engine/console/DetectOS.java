package hr.fer.zemris.ecf.lab.view.fer.zemris.ecf.engine.console;

/**
 * This class's main function is to determine the pc's operating system. It's
 * getOS_console() method is used to get needed {@link Console} depending on the
 * pc's OS.
 *
 * @version 1.0
 */
public class DetectOS {

    private static OS os;

    private DetectOS() {
    }

    static {
        String osStr = System.getProperty("os.name").toLowerCase();
        if (isWindows(osStr)) {
            os = OS.WINDOWS;
        } else if (isMac(osStr)) {
            os = OS.MAC;
        } else if (isUnix(osStr)) {
            os = OS.LINUX;
        } else if (isSolaris(osStr)) {
            os = OS.SOLARIS;
        } else {
            os = OS.NOT_SUPPORTED;
        }
    }

    private static boolean isSolaris(String os) {
        return os.indexOf("sunos") >= 0;
    }

    public static boolean isSolaris() {
        return os == OS.SOLARIS;
    }

    private static boolean isUnix(String os) {
        return os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0;
    }

    public static boolean isUnix() {
        return os == OS.LINUX;
    }

    private static boolean isMac(String os) {
        return os.indexOf("mac") >= 0;
    }

    public static boolean isMac() {
        return os == OS.MAC;
    }

    private static boolean isWindows(String os) {
        return os.indexOf("win") >= 0;
    }

    public static boolean isWindows() {
        return os == OS.WINDOWS;
    }

    public static boolean isSupported() {
        return os != OS.NOT_SUPPORTED;
    }
}
