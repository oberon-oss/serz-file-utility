package eu.oberon.oss.serz.cli.util.picocli;

import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() {
        ApplicationPropertyProvider provider = ApplicationPropertyProvider.getInstance();
        return new String[]{
                "Application : " + provider.getProperty("serz.utility.artifact.id"),
                "version     : " + provider.getProperty("serz.utility.version")
        };
    }
}
