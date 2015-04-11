package hr.fer.zemris.ecf.lab.engine.console;

import java.io.*;

public class Test {

    /**
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) {
//        Console console = ConsoleFactory.createConsole();
//        console.pardump("C:\\Temp\\ECF_1.3.2\\ECF_1.3.2\\examples\\GAonemax\\VS\\Debug\\GAOneMax.exe", "parameters.txt");
        try {
            Process process = new ProcessBuilder("ls", "-la", "/var/tmp").start();
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is)));
            process.waitFor();
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}