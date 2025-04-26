package eu.oberon.oss.serz.cli;

import eu.oberon.oss.util.picocli.VersionProvider;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        versionProvider = VersionProvider.class,
        name = "SERZ file utility",
        mixinStandardHelpOptions = true
)

public class SERZUtility implements Callable<Integer> {
    @CommandLine.Option(names = {"-f", "--files"}, required = true)
    private String[] inputFilenames;

    @Override
    public Integer call() throws Exception {
        return 0;
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new SERZUtility()).execute(args));
    }
}
