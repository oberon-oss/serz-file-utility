package eu.oberon.oss.serz.i18n;

/**
 * Enumerates the possible types of message formats
 *
 * @author fhdumay
 * @since 1.0.0
 */
public enum FormatStringType {

    /**
     * Represents a message format type where the string is used as-is without any formatting or replacements.
     * This is typically used for messages that do not contain placeholders for dynamic content.
     */
    NON_FORMATTED_STRING,
    /**
     * Represents a message format type where the string contains placeholders
     * that follow the formatting conventions of {@link String#format}.
     * <p>
     * Placeholders in this format typically use syntax like `%s`, `%d`, etc.,
     * enabling dynamic content insertion at runtime during logging operations.
     * <p>
     * Placeholders are replaced with dynamic content during runtime.
     * Typically used when messages need to support parameterized formatting.
     *
     * @since 1.0.0
     */
    STRING_FORMAT,

    /**
     * Represents a message format type where the string contains placeholders
     * compatible with the format used by logging frameworks, such as in Log4j.
     * <p>
     * Represents a message format type where the string contains placeholders
     * compatible with the format used by logging frameworks, such as in Log4j.
     * <p>
     * Parameter replacements are specified by '{}' or '{0...n}'.
     * This format is commonly used for structured log messages that support
     * parameterized values.
     *
     * @since 1.0.0
     */
    LOG_FORMAT


}
