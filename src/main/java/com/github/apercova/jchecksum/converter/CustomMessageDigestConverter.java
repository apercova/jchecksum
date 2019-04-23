package com.github.apercova.jchecksum.converter;

import java.security.MessageDigest;

import com.github.apercova.quickcli.DatatypeConverter;
import com.github.apercova.quickcli.exception.DatatypeConverterException;
import java.security.NoSuchAlgorithmException;

/**
 * Custom {@link MessageDigest} converter.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class CustomMessageDigestConverter implements DatatypeConverter<MessageDigest> {

    @Override
    public MessageDigest parse(String value) throws DatatypeConverterException {
        try {
            return MessageDigest.getInstance(value);
        } catch (NoSuchAlgorithmException e) {
            throw new DatatypeConverterException(e);
        }
    }

    @Override
    public String format(MessageDigest value) throws DatatypeConverterException {
        try {
            return value.getAlgorithm();
        } catch (Exception e) {
            throw new DatatypeConverterException(e);
        }
    }
}
