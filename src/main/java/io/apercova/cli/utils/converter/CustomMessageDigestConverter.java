package io.apercova.cli.utils.converter;

import java.security.MessageDigest;

import io.apercova.quickcli.DatatypeConverter;
import io.apercova.quickcli.DatatypeConverterException;

/**
 * Custom {@link MessageDigest} converter.
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class CustomMessageDigestConverter implements DatatypeConverter<MessageDigest>{
	
	public MessageDigest parse(String value) throws DatatypeConverterException {
		try {
			return MessageDigest.getInstance(value);
		} catch (Exception e) {
			throw new DatatypeConverterException(e);
		}
	}

	public String format(MessageDigest value) throws DatatypeConverterException {
		try {
			return value.getAlgorithm();
		} catch (Exception e) {
			throw new DatatypeConverterException(e);
		}
	}
}
