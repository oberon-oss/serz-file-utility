package eu.oberon.oss.serz.cli.util.picocli;

import java.util.ResourceBundle;

public class ResourceBundleProvider {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("serz-resource-bundle");

    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

    public static String getEntry(String key) {
        return getResourceBundle().getString(key);
    }
}
