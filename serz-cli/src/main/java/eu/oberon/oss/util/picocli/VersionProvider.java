package eu.oberon.oss.util.picocli;

import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() throws Exception {
        ApplicationPropertyProvider provider = ApplicationPropertyProvider.getInstance();
        return new String[]{
                "Application : " + provider.getProperty("serz.utility.group.id") + " - " + provider.getProperty("serz.utility.artifact.id"),
                "version     : " + provider.getProperty("serz.utility.version")
        };
    }
}
