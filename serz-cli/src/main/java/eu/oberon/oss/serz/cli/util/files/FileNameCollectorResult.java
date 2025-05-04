package eu.oberon.oss.serz.cli.util.files;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Collects various aspects of the file collection run.
 *
 * @author fhdumay
 * @since 1.0.0
 */
@Getter
public class FileNameCollectorResult {
    private final List<File> fileNames;
    private final FileNameCollectorStatistics statistics;

    /**
     * Creates a new instance.
     *
     * @param fileNames  List of file names that was produced
     * @param statistics Statistics collected during the file collection run.
     * @since 1.0.0
     */
    FileNameCollectorResult(@NotNull List<File> fileNames, FileNameCollectorStatistics statistics) {
        this.fileNames = fileNames;
        this.statistics = statistics;
    }
}
