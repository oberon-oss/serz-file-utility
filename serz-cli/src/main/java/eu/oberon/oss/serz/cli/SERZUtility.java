package eu.oberon.oss.serz.cli;


import eu.oberon.oss.serz.ConversionType;
import eu.oberon.oss.serz.cli.util.files.FileNameCollector;
import eu.oberon.oss.serz.cli.util.picocli.VersionProvider;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import static eu.oberon.oss.serz.cli.SERZUtility.APPLICATION_NAME;

@CommandLine.Command(
        versionProvider = VersionProvider.class,
        name = "java -jar " + APPLICATION_NAME + ".jar",
        mixinStandardHelpOptions = true,
        usageHelpAutoWidth = true
)
@Log4j2
public class SERZUtility implements Callable<Integer> {
    public static final String APPLICATION_NAME = "serz-cli";

    @CommandLine.Option(
            names = {"-d", "--directory"}, required = true,
            descriptionKey = "serz.utility.cli.directory"
    )
    private File directory;

    @CommandLine.Option(
            names = {"-c", "--conversion-type"}, required = true,
            descriptionKey = "serz.utility.cli.conversion-type",
            converter = ConversionTypeConverter.class
    )
    private ConversionType conversionType;

    @CommandLine.Option(
            names = {"-r", "--recursive"},
            defaultValue = "false", showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            descriptionKey = "serz.utility.cli.recursive"
    )
    private boolean recursive;

    @CommandLine.Option(
            names = {"-p", "--file-pattern"},
            defaultValue = "'.*'", showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            descriptionKey = "serz.utility.cli.file.pattern"
    )
    private Pattern filePattern;


    @Override
    public Integer call() {
        if (filePattern.toString().startsWith("'") && filePattern.toString().endsWith("'")) {
            filePattern = Pattern.compile(filePattern.toString().substring(1, filePattern.toString().length() - 1));
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(String.format(RESOURCE_BUNDLE.getString("serz.utility.cli.error.not.directory"), directory));
        }

        try {
            directory = directory.getCanonicalFile();
        } catch (IOException e) {
            LOGGER.fatal("Error encountered {}, aborting request.", e.getMessage(), e);
        }

        String string = RESOURCE_BUNDLE.getString("serz.utility.cli.processing.request.info");
        LOGGER.info(string, conversionType.getParameterValue(), directory, filePattern, recursive);

        List<File> files = new FileNameCollector(conversionType, directory, filePattern, recursive).generateListOfFiles();
        LOGGER.info("Selected {} files for processing.",files.size());
        return 0;
    }

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("serz-resource-bundle");

    public static void main(String[] args) {
        System.getProperties().setProperty("picocli.usage.width", "AUTO");

        CommandLine commandLine = new CommandLine(new SERZUtility());
        commandLine.setResourceBundle(RESOURCE_BUNDLE);
        System.exit(commandLine.execute(args));
    }

    private static class ConversionTypeConverter implements CommandLine.ITypeConverter<ConversionType> {
        @Override
        public @Nullable ConversionType convert(String value) {
            return ConversionType.getByParameterValue(value);
        }

    }
}
