package eu.oberon.oss.serz.cli.util.files;


import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Collects file names for processing.
 *
 * @author fhdumay
 * @since 1.0.0
 */
@Getter
@Log4j2
public class FileNameCollector {

    private final File directory;
    private final Pattern filePattern;
    private final boolean recursive;
    private final FileFilter fileFilter;

    private final FileNameCollectorStatistics statistics = new FileNameCollectorStatistics();

    /**
     * Runs a file collection based on the provided parameters.
     *
     * @param directory   The directory to process.
     * @param filePattern The file pattern to
     * @param recursive   Specifies if subdirectories of the specified 'directory' should be processed recursively.
     * @return A list containing 0 or more file entries that were found.
     * @since 1.0.
     */
    public  static FileNameCollectorResult collectFiles(File directory, Pattern filePattern, boolean recursive) {
        FileNameCollector collector = new FileNameCollector(directory, filePattern, recursive);
        List<File> files = collector.generateListOfFiles();
        return new FileNameCollectorResult(files,collector.statistics);
    }

    private FileNameCollector(File directory, Pattern filePattern, boolean recursive) {
        this.directory = directory;
        this.filePattern = filePattern;
        this.recursive = recursive;

        fileFilter = file -> {
            if (file.isDirectory()) {
                statistics.incrementDirectoriesProcessed();
                return true;
            }

            if (file.isFile()) {
                statistics.incrementFilesProcessed();
            }

            if (filePattern.matcher(file.getName()).matches()) {
                statistics.incrementFilesMatchingFilter();
                return true;
            }
            return false;
        };
    }

    private List<File> generateListOfFiles() {
        List<File> files = new ArrayList<>();
        statistics.start();
        processEntry(directory, files);
        statistics.stop();
        LOGGER.info(statistics.toString());
        return files;
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
