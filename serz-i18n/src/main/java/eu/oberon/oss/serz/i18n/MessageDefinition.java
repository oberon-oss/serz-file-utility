package eu.oberon.oss.serz.i18n;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.FormattedMessage;
import org.apache.logging.log4j.message.MessageFormatMessageFactory;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static eu.oberon.oss.serz.i18n.FormatStringType.*;

/**
 * Represents the definition of a message format, including the format type,
 * expected replacement parameters, and utilities for formatting messages.
 * This class supports identifying and processing different message format
 * conventions, such as those used by {@link String#format} and logging frameworks.
 *
 * @author fhdumay
 * @since 1.0.0
 */
@Log4j2
@Getter
public class MessageDefinition {
    private final FormatStringType formatStringType;
    private final int replacementCount;
    private final String formatString;
    private final MessageFormatMessageFactory factory = new MessageFormatMessageFactory();

    /**
     * Constructs a {@code MessageDefinition} instance, representing the format of a message string.
     * This constructor analyzes the given format string to determine its type and the number of
     * replacement parameters it expects. The format string must conform to one of the supported
     * format conventions; otherwise, it is considered a non-formatted string. If the format string
     * is ambiguous (matches multiple format conventions), an exception is thrown.
     *
     * @param formatString the message format string to analyze; it determines the format type and
     *                     the number of replacement parameters for the message. The format string
     *                     may correspond to {@link String#format}-style or logging framework-style
     *                     formatting conventions.
     *
     * @throws IllegalArgumentException if the format string matches multiple format conventions
     * @since 1.0.0
     */
    public MessageDefinition(String formatString) {
        int[] counters = new int[3];

        counters[0] = analyze(STRING_MESSAGE_FORMAT, formatString);
        counters[1]= analyze(LOG_MESSAGE_FORMAT, formatString);
        counters[2] = analyze(TEXT_MESSAGE_FORMAT, formatString);

        int formatCounter= 0;
        for (int i : counters) {
            if (i > 0) formatCounter++;
        }

        if (formatCounter > 1) {
            throw new IllegalArgumentException("Format string '" + formatString + "' is ambiguous!");
        }

        if (counters[0] != 0) {
            formatStringType = STRING_FORMAT;
            replacementCount = counters[0];
        } else if (counters[1] != 0) {
            formatStringType = LOG_FORMAT;
            replacementCount = counters[1];
        } else if (counters[2] != 0) {
            formatStringType = TEXT_FORMAT;
            replacementCount = counters[2];
        }
        else {
            formatStringType = NON_FORMATTED_STRING;
            replacementCount = 0;
        }
        this.formatString = formatString;
    }

    /**
     * Returns a formatted string based on the specified format type and replacement parameters.
     * If the format type is STRING_FORMAT, the method utilizes {@link String#format}.
     * If the format type is LOG_FORMAT, it uses a logging-specific formatting style.
     * For non-formatted strings, the original format string is returned as-is.
     *
     * @param replacementParameters the values to replace placeholders in the format string.
     *
     * @return the formatted string with replacements applied, or the original format string for non-formatted types.
     *
     * @throws IllegalArgumentException if the replacement parameters are null, or their count does not match the expected count.
     * @since 1.0.0
     */
    public String getFormattedString(@Nullable Object... replacementParameters) {
        if (replacementParameters == null && replacementCount > 0) {
            throw new IllegalArgumentException("Replacement parameters are missing!");
        }

        if (replacementParameters != null && replacementParameters.length != replacementCount) {
            throw new IllegalArgumentException("Replacement parameters count mismatch! Expected " + replacementCount + ", but got " + replacementParameters.length);
        }

        return switch (formatStringType) {
            case STRING_FORMAT -> String.format(formatString, replacementParameters);
            case TEXT_FORMAT -> factory.newMessage(formatString, replacementParameters).toString();
            case LOG_FORMAT -> new FormattedMessage(formatString, replacementParameters).toString();
            default -> formatString;
        };
    }

    private static final Pattern LOG_MESSAGE_FORMAT;
    private static final Pattern STRING_MESSAGE_FORMAT;
    private static final Pattern TEXT_MESSAGE_FORMAT;

    static {
        TEXT_MESSAGE_FORMAT = Pattern.compile("\\{\\d+}");
        LOG_MESSAGE_FORMAT = Pattern.compile("\\{}");
        STRING_MESSAGE_FORMAT = Pattern.compile("%(?:[a-z]|%\\d+(?:\\.\\d+)?[a-z])");
    }

    private static int analyze(Pattern pattern, String format) {
        Matcher logMatcher = pattern.matcher(format);
        int replacementCount = 0;

        while (logMatcher.find()) {
            replacementCount++;
        }

        return replacementCount;
    }
}
