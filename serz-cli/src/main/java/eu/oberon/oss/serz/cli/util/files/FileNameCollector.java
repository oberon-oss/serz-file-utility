package eu.oberon.oss.serz.cli.util.files;


import eu.oberon.oss.serz.ConversionType;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileFilter;
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

    public FileNameCollector(ConversionType conversionType, File directory, Pattern filePattern, boolean recursive) {
        this.conversionType = conversionType;
        this.directory = directory;
        this.filePattern = filePattern;
        this.recursive = recursive;

        fileFilter = file -> file.getName().endsWith(conversionType.getExtension()) || file.isDirectory();
    }

    public List<File> generateListOfFiles() {
        List<File> files = new ArrayList<>();
        processEntry(directory, recursive, files);
        return files;
    }

    private void processEntry(File path, boolean recursive, List<File> files) {
        for (File file : path.listFiles(fileFilter)) {
            if (file.isFile() && file.canRead()) {
                files.add(file);
            } else if (recursive && file.isDirectory() && file.canRead()) {
                processEntry(file, recursive, files);
            }
        }
    }
}
