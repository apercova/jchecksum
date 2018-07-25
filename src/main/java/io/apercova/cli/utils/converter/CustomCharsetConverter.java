package io.apercova.cli.utils.converter;

import java.nio.charset.Charset;

import io.apercova.quickcli.DatatypeConverterException;

/**
 * Custom {@link Charset} converter.
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class CustomCharsetConverter implements io.apercova.quickcli.DatatypeConverter<Charset> {

	public Charset parse(String value) throws DatatypeConverterException {
		try {
			return value == null || value.length() == 0? Charset.defaultCharset(): Charset.forName(value);
		} catch (Exception e) {
			throw new DatatypeConverterException(e);
		}
	}

	public String format(Charset value) throws DatatypeConverterException {
		try {
			return value.toString();
		} catch (Exception e) {
			throw new DatatypeConverterException(e);
		}
	}
	
}
