package cz.cvut.kbss.jsonld.jackson.environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Environment {

    /**
     * Reads test data from the test resources classpath folder.
     *
     * @param fileName File to read
     * @return Content of the file
     */
    public static String readData(String fileName) {
        final File file = new File(Environment.class.getClassLoader().getResource(fileName).getFile());
        assert file.exists();
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read test file " + fileName, e);
        }
    }
}
