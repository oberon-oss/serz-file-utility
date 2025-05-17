package eu.oberon.oss.serz.i18n;

import org.apache.logging.log4j.Logger;

/**
 * An interface for handling messages through logging and exception creation. This interface
 * is designed to provide a unified mechanism for logging parameterized messages and creating
 * exceptions with formatted messages.
 */
public interface IMessagesHelper {
    /**
     * Logs a message using the provided logger with optional parameters for formatting.
     * The message format, logging level, and behavior depend on the implementation and
     * the message definition associated with the instance.
     *
     * @param logger the logger used to log the message
     * @param params optional parameters to be included in the formatted message
     */
    void logMessage(Logger logger, Object... params);

    /**
     * Creates an exception using the specified parameters to format the message.
     * The exception's type and behavior are determined by the underlying implementation.
     *
     * @param params the parameters used to format the exception message
     *
     * @return an instance of an Exception subclass with a formatted message
     *
     * @throws IllegalStateException if the exception type is not defined, or the message is not intended for exception creation
     * @throws MessagesException     will be thrown in
     */
    Exception createException(Object... params);
}
