package eu.oberon.oss.serz.cli;


import eu.oberon.oss.serz.cli.util.files.FileNameCollector;
import eu.oberon.oss.serz.cli.util.files.FileNameCollectorResult;
import eu.oberon.oss.serz.cli.util.picocli.VersionProvider;
import eu.oberon.oss.serz.i18n.ResourceBundleProvider;
import eu.oberon.oss.serz.processor.ConversionType;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static eu.oberon.oss.serz.cli.SERZUtility.APPLICATION_NAME;
import static eu.oberon.oss.serz.i18n.Messages.CLI_ERROR_NOT_A_DIR;

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
            descriptionKey = "serz.utility.cli.conversion.type",
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
            defaultValue = ".*", showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            descriptionKey = "serz.utility.cli.file.pattern"
    )
    private Pattern filePattern;


    @Override
    public Integer call() {

        try {
            // Stripping any user provided single quotes from the pattern to prevent incorrect
            // pattern matching as a result.
            if (filePattern.toString().startsWith("'") && filePattern.toString().endsWith("'")) {
                filePattern = Pattern.compile(filePattern.toString().substring(1, filePattern.toString().length() - 1));
            }

            if (!directory.isDirectory()) {
                throw CLI_ERROR_NOT_A_DIR.createException(directory);
            }

            directory = directory.getCanonicalFile();
            String formatString = ResourceBundleProvider.getEntry("serz.utility.cli.processing.request.info");
            LOGGER.info(formatString, conversionType.getParameterValue(), directory, filePattern, recursive);

            FileNameCollectorResult result = FileNameCollector.collectFiles(directory, filePattern, recursive);
            LOGGER.info("Selected {} files for processing.", result.getFileNames().size());
            return 0;

        } catch (IOException e) {
            LOGGER.fatal("Error encountered {}, aborting request.", e.getMessage(), e);
            return 1;
        } catch (PatternSyntaxException e) {
            LOGGER.fatal("Failed to compile file pattern {}:{}", filePattern, e.getMessage(), e);
            return 2;
        } catch (IllegalArgumentException e) {
            LOGGER.fatal("{}",e.getMessage(), e);
            return 3;
        } catch (Exception e) {
            LOGGER.fatal("Processing error occurred: {}", e.getMessage(), e);
            return 10;
        }
    }


    public static void main(String[] args) {
        System.getProperties().setProperty("picocli.usage.width", "AUTO");

        CommandLine commandLine = new CommandLine(new SERZUtility());
        commandLine.setResourceBundle(ResourceBundleProvider.getResourceBundle());
        System.exit(commandLine.execute(args));
    }

    private static class ConversionTypeConverter implements CommandLine.ITypeConverter<ConversionType> {
        @Override
        public @Nullable ConversionType convert(String value) {
            return ConversionType.getByParameterValue(value);
        }
    }
}
