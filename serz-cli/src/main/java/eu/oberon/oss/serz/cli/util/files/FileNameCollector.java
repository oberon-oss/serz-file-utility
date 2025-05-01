package eu.oberon.oss.serz.cli.util.files;


import eu.oberon.oss.serz.ConversionType;
import eu.oberon.oss.serz.cli.util.picocli.ResourceBundleProvider;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileFilter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Log4j2
public class FileNameCollector {

    private final ConversionType conversionType;
    private final File directory;
    private final Pattern filePattern;
    private final boolean recursive;
    private final FileFilter fileFilter;

    public static List<File> collectFiles(ConversionType conversionType, File directory, Pattern filePattern, boolean recursive) {
        return new FileNameCollector(conversionType, directory, filePattern, recursive).generateListOfFiles();
    }

    private int directoriesProcessed = 0;
    private int filesProcessed = 0;
    private int filesMatchingFilter = 0;

    private FileNameCollector(ConversionType conversionType, File directory, Pattern filePattern, boolean recursive) {
        this.conversionType = conversionType;
        this.directory = directory;
        this.filePattern = filePattern;
        this.recursive = recursive;

        fileFilter = file -> {
            if (file.isDirectory()) {
                directoriesProcessed++;
                return true;
            }

            if (file.isFile()) {
                filesProcessed++;
            }

            if (filePattern.matcher(file.getName()).matches()) {
                filesMatchingFilter++;
                return true;
            }
            return false;
        };
    }

    private List<File> generateListOfFiles() {
        String formatString;
        formatString = ResourceBundleProvider.getEntry("serz.utility.cli.processing.request.info");
        LOGGER.info(formatString, conversionType.getParameterValue(), directory, filePattern, recursive);

        List<File> files = new ArrayList<>();

        long timeStarted = System.currentTimeMillis();
        processEntry(directory, files);

        String elapsed = getElapsedTime(Duration.ofMillis(System.currentTimeMillis() - timeStarted));

        formatString = ResourceBundleProvider.getEntry("serz.utility.cli.processing.response.info");
        LOGGER.info(formatString, elapsed, directoriesProcessed, filesProcessed, filesMatchingFilter);
        return files;
    }

    private String getElapsedTime(Duration duration) {
        return String.format("%02d:%02d:%02d.%03d",
                duration.toHours(),
                duration.toMinutes() - (duration.toHours() * 60),
                duration.toSeconds() - (duration.toMinutes() * 60),
                duration.toMillis() - (duration.toSeconds() * 60)
        );
    }

    private void processEntry(File path, List<File> files) {
        File[] collectedEntries = path.listFiles(fileFilter);
        if (collectedEntries != null) {
            for (File file : collectedEntries) {
                if (file.isFile() && file.canRead()) {
                    files.add(file);
                } else if (recursive && file.isDirectory() && file.canRead()) {
                    processEntry(file, files);
                }
            }
        } else {
            LOGGER.warn("Path '{}' is not a directory, or an IO error occurred", path);
        }
    }
}
