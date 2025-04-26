package eu.oberon.oss.util.picocli;

import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class ApplicationPropertyProvider {
    private final Properties properties = new Properties();

    public String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

    private ApplicationPropertyProvider() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                throw new IllegalStateException("Unable to load from file 'application.properties'!");
            }
        } catch (Exception e) {
            LOGGER.fatal("Fatal error loading properties: {}", e.getMessage(), e);
        }
    }

    private static final ApplicationPropertyProvider INSTANCE = new ApplicationPropertyProvider();

    public static ApplicationPropertyProvider getInstance() {
        return INSTANCE;
    }

}
