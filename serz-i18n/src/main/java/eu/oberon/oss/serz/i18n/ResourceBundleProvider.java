package eu.oberon.oss.serz.i18n;

import java.util.ResourceBundle;

public class ResourceBundleProvider {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("serz-resource-bundle");


    private ResourceBundleProvider() {

    }
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

    public static String getEntry(String key) {
        return getResourceBundle().getString(key);
    }
}
