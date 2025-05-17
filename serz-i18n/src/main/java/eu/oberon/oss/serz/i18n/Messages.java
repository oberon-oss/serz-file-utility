package eu.oberon.oss.serz.i18n;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import static org.apache.logging.log4j.Level.INFO;

/**
 * An enumeration of predefined message keys for use in logging and exception handling,
 * backed by resources from a resource bundle. Each message is associated with a key,
 * retrieved from a resource bundle, and optionally enhanced with logging levels or exception types.
 * This enum implements the {@code IMessagesHelper} interface for unified logging and exception creation.
 * <p>
 * Each message can be used for either
 * <ul>
 * <li>Logging a formatted message at a specific log level.</li>
 * <li>Creating an exception with a formatted message and a defined exception type.</li>
 * <li>Provide messages 'as-is', useful for static text etc.</li>
 * </ul>
 * <p>
 * Each instance of {@code Messages} provides access to a corresponding {@code MessagesHelper}
 * instance, which handles the actual implementation of messaging functionality.
 *
 * @author fhdumay
 * @since 1.0.0
 */
@Log4j2
@Getter
public enum Messages implements IMessagesHelper {
    CLI_OPT_DIR_NAME("serz.utility.cli.directory"),
    CLI_OPT_CONVERSION_TYPE("serz.utility.cli.conversion.type"),
    CLI_OPT_RECURSIVE("serz.utility.cli.recursive"),
    CLI_OPT_FILE_PATTERNS("serz.utility.cli.file.pattern"),
    CLI_REQUEST_INFO("serz.utility.cli.processing.request.info", INFO),
    CLI_RESPONSE_INFO("serz.utility.cli.processing.response.info", INFO),
    CLI_ERROR_NOT_A_DIR("serz.utility.cli.error.not.directory", IllegalArgumentException.class),
    ;

    private final String resourceBundleKey;
    private final IMessagesHelper messagesHelper;

    Messages(String resourceBundleKey) {
        this.resourceBundleKey = resourceBundleKey;
        messagesHelper = new MessagesHelper(ResourceBundleProvider.getEntry(resourceBundleKey));
    }

    Messages(String resourceBundleKey, Level logLevel) {
        this.resourceBundleKey = resourceBundleKey;
        messagesHelper = new MessagesHelper(ResourceBundleProvider.getEntry(resourceBundleKey), logLevel);
    }

    Messages(String resourceBundleKey, Class<? extends Exception> exceptionClass) {
        this.resourceBundleKey = resourceBundleKey;
        messagesHelper = new MessagesHelper(ResourceBundleProvider.getEntry(resourceBundleKey), exceptionClass);
    }


    @Override
    public void logMessage(Logger logger, Object... params) {
        messagesHelper.logMessage(logger, params);
    }

    @Override
    public Exception createException(Object... params) {
        return messagesHelper.createException(params);
    }
}
