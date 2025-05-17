package eu.oberon.oss.serz.i18n;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

@Log4j2
@Getter
public class MessagesHelper implements IMessagesHelper {
    private final MessageDefinition messageDefinition;
    private final @Nullable Constructor<? extends Exception> constructor;
    private final @Nullable Level level;

    public MessagesHelper(String formatString) {
        this(formatString, null, null);
    }

    public MessagesHelper(String formatString, Level level) {
        this(formatString, null, level);
    }

    public MessagesHelper(String formatString, Class<? extends Exception> exceptionClass) {
        this(formatString, exceptionClass, null);
    }

    private MessagesHelper(String formatString, @Nullable Class<? extends Exception> exceptionClass, @Nullable Level level) {
        if (exceptionClass != null && Exception.class.isAssignableFrom(exceptionClass)) {
            try {
                constructor = exceptionClass.getConstructor(String.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Exception class has no constructor with String parameter: " + exceptionClass.getName());
            }
        } else {
            constructor = null;
        }
        this.level = level;
        messageDefinition = new MessageDefinition(formatString);
    }

    @Override
    @SuppressWarnings("java:S2629")
    public void logMessage(@NotNull Logger logger, @Nullable Object... params) {
        if (level == null) {
            throw new IllegalStateException("Message is not to be logged.");
        }
        if (level.isMoreSpecificThan(logger.getLevel())) {
            logger.log(level, messageDefinition.getFormattedString(params));
        }
    }

    @Override
    public Exception createException(Object... params) {
        if (constructor == null) {
            throw new IllegalStateException("Message is not to be used in an exception.");
        }
        try {
            return constructor.newInstance(messageDefinition.getFormattedString(params));
        } catch (Exception e) {
            throw new MessagesException("Failure instantiating exception: {}");
        }
    }

}
