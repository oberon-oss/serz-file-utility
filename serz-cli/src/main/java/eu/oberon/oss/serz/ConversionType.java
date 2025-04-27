package eu.oberon.oss.serz;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum ConversionType {
    XML_TO_BIN(".xml", "xml-to-bin"),
    BIN_TO_XML(".bin", "bin-to-xml");


    private final String extension;
    private final String parameterValue;

    ConversionType(String extension, String parameterValue) {
        this.extension = extension;
        this.parameterValue = parameterValue;
    }

    public static @Nullable ConversionType getByExtension(String extension) {
        for (ConversionType conversionType : ConversionType.values()) {
            if (conversionType.getExtension().equalsIgnoreCase(extension)) {
                return conversionType;
            }
        }
        return null;
    }

    public static @Nullable ConversionType getByParameterValue(String parameterValue) {
        for (ConversionType conversionType : ConversionType.values()) {
            if (conversionType.getParameterValue().equalsIgnoreCase(parameterValue)) {
                return conversionType;
            }
        }
        return null;
    }
}
