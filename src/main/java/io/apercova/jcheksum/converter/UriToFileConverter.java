package io.apercova.jcheksum.converter;

import io.apercova.quickcli.DatatypeConverter;

import io.apercova.quickcli.exception.DatatypeConverterException;
import java.io.File;

/**
 * Custom {@link File} converter.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class UriToFileConverter implements DatatypeConverter<File> {

    @Override
    public File parse(String value) throws DatatypeConverterException {
        try {
            return value != null && value.length() > 0
                    ? new File(value)
                    : null;
        } catch (Exception e) {
            throw new DatatypeConverterException(
                    String.format("IO error at reading file: %s. %s%n", value, e.getMessage()), e);
        }
    }

    @Override
    public String format(File value) throws DatatypeConverterException {
        try {
            return value.getAbsolutePath();
        } catch (Exception e) {
            throw new DatatypeConverterException(
                    String.format("IO error at reading file path from file. %s%n", e.getMessage()), e);
        }
    }

}
