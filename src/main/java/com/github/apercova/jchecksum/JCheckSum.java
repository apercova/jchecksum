package com.github.apercova.jchecksum;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import com.github.apercova.jchecksum.converter.CustomCharsetConverter;
import com.github.apercova.jchecksum.converter.CustomMessageDigestConverter;
import com.github.apercova.jchecksum.converter.UriToFileConverter;
import com.github.apercova.quickcli.annotation.CLIArgument;
import com.github.apercova.quickcli.annotation.CLICommand;
import com.github.apercova.quickcli.annotation.CLIDatatypeConverter;
import com.github.apercova.quickcli.Command;
import com.github.apercova.quickcli.CommandFactory;
import com.github.apercova.quickcli.exception.CLIArgumentException;
import com.github.apercova.quickcli.exception.ExecutionException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link Command} implementation for jchecksum command.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
@CLICommand(value = "jchecksum", description = "Calculates checksum from file/text-caption")
public final class JCheckSum extends Command<Void> {

    public static final String SERIAL_UID_ENCODING = "JUID";
    public static final String B64_ENCODING = "B64";
    public static final String HEX_ENCODING = "HEX";

    @CLIArgument(name = "-a", aliases = {"--algorithm"}, value = "MD5", usage = "Hashing algorithm")
    @CLIDatatypeConverter(CustomMessageDigestConverter.class)
    private MessageDigest alg;
    @CLIArgument(name = "-e", aliases = {"--encoding"}, usage = "Output encoding. options: [HEX,B64], default: HEX", value = "HEX")
    private String encoding;
    @CLIArgument(name = "-eo", aliases = {"--encode-only"}, usage = "If set, source only gets encoded")
    private Boolean encodeOnly;
    @CLIArgument(name = "-f", aliases = {"--file"}, usage = "File Path")
    @CLIDatatypeConverter(UriToFileConverter.class)
    private File file;
    @CLIArgument(name = "-t", aliases = {"--text"}, usage = "Text caption")
    private String text;
    @CLIArgument(name = "-cs", aliases = {"--charset"}, usage = "Encoding charset", value = "UTF-8")
    @CLIDatatypeConverter(CustomCharsetConverter.class)
    private Charset cs;
    @CLIArgument(name = "-la", usage = "List available algorithms")
    private Boolean listAlgs;
    @CLIArgument(name = "-lc", usage = "List available charsets")
    private Boolean listCs;
    @CLIArgument(name = "-le", usage = "List available encoding options")
    private Boolean listEnc;
    @CLIArgument(name = "-h", aliases = {"--help"}, usage = "List available options")
    private Boolean showHelp;
    @CLIArgument(name = "-m", aliases = {"--match"}, usage = "If set, checksum result gets compared against suplied pattern")
    private String match;

    @Override
    public Void execute() throws ExecutionException {
        if (showHelp) {
            printUsage();
        } else if (listAlgs) {
            listHashAlgorithms();
        } else if (listCs) {
            listCharsets();
        } else if (listEnc) {
            listEncoders();
        } else {
            if (file != null && !file.isDirectory()) {
                Logger.getLogger(JCheckSum.class.getName()).log(Level.FINE, "File: %s%n", file.getAbsolutePath());
            }
            if (encodeOnly) {
                encode();
            } else {
                digest();
            }
        }
        return null;
    }

    /**
     * Calculate encoded-only value based on command arguments
     */
    private void encode() {
        if (file != null && !file.isDirectory()) {
            byte[] buffer = new byte[1024];

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(baos, buffer.length);
            BufferedInputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(file), buffer.length);
                int read;
                while ((read = is.read(buffer)) > 0) {
                    bos.write(buffer, 0, read);
                    bos.flush();
                }
                writer.printf("%s", this.encode(baos.toByteArray()));
            } catch (IOException e) {
                writer.printf(locale, "IO error at reading file: %s. %s%n", file, e.getMessage());
                printUsage();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(JCheckSum.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        } else {
            writer.printf("%s", this.encode(this.text.getBytes(this.cs)));
        }
    }

    /**
     * Calculate digest/hash sum based on command arguments.
     *
     * @return digest/hash sum.
     */
    private void digest() {
        if (file != null && !file.isDirectory()) {
            byte[] buffer = new byte[1024];
            BufferedInputStream is = null;
            try {
                is = new BufferedInputStream(
                        new FileInputStream(file),
                        buffer.length);
                int read;
                while ((read = is.read(buffer)) > 0) {
                    alg.update(buffer, 0, read);
                }

            } catch (IOException e) {
                writer.printf(locale, "IO error at reading file: %s. %s%n", file, e.getMessage());
                printUsage();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(JCheckSum.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        } else {
            text = (text == null) ? "" : text;
            alg.update(text.getBytes(cs));
        }

        String encoded = this.encode(alg.digest());
        if (match != null && !match.isEmpty()) {
            encoded = String.valueOf(Pattern.matches(match, encoded));
            writer.printf(locale, "%s", encoded);
        } else {
            writer.printf(locale, "%s", encoded);
        }
    }

    /**
     * Encode bytearray based on command arguments
     *
     * @param bytes source byte array.
     * @return Encoded string.
     */
    private String encode(byte[] digest) {
        String encString;
        if (B64_ENCODING.equalsIgnoreCase(encoding)) {
            encString = DatatypeConverter.printBase64Binary(digest);
        } else if (SERIAL_UID_ENCODING.equalsIgnoreCase(encoding)) {
            encString = DatatypeConverter.printHexBinary(digest).toLowerCase();
            BigInteger bn = new BigInteger(encString, 16);
            encString = String.valueOf(Math.abs(bn.longValue()));
        } else {
            encString = DatatypeConverter.printHexBinary(digest).toLowerCase();
        }
        return encString;
    }

    /**
     * Prints a list of JVM available {@link MessageDigest} hash algorithms.
     */
    private void listHashAlgorithms() {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            List<Service> algos = new ArrayList<Service>();

            Set<Service> services = provider.getServices();
            for (Service service : services) {
                if (service.getType().equalsIgnoreCase(MessageDigest.class.getSimpleName())) {
                    algos.add(service);
                }
            }

            if (!algos.isEmpty()) {
                writer.printf(locale, "--- Supported digest algorithms ---%n");
                writer.printf(locale, "--- Provider %s, version %.2f ---%n", provider.getName(), provider.getVersion());
                for (Service service : algos) {
                    String algo = service.getAlgorithm();
                    writer.printf(locale, "%s%n", algo);
                }
            }
        }
    }

    /**
     * Prints a list of JVM available {@link Charset}.
     */
    private void listCharsets() {
        writer.printf(locale, "--- Available charsets ---%n");
        for (String k : Charset.availableCharsets().keySet()) {
            writer.printf(locale, "%s%n", k);
        }
    }

    /**
     * Prints a list of command available encoder options.
     */
    private void listEncoders() {
        writer.printf(locale, "--- Available encoders ---%n");
        writer.printf(locale, "%s : Hexadecimal (default)%n", HEX_ENCODING);
        writer.printf(locale, "%s : Base64%n", B64_ENCODING);
        writer.printf(locale, "%s :Java Serial Version UID%n", SERIAL_UID_ENCODING);
    }

    /**
     * Execution entry point for standalone use.
     *
     * @param args Command arguments.
     */
    public static void main(String[] args) {

        Writer writer = new OutputStreamWriter(System.out);
        Command<Void> command = null;
        try {
            command = CommandFactory.create(args, JCheckSum.class, writer);
            command.execute();
        } catch (CLIArgumentException ex) {
            Logger.getLogger(JCheckSum.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            if (command != null) {
                command.getWriter().printf(command.getLocale(), "%s%n", ex.getMessage());
                command.printUsage();
            }
        } catch (ExecutionException ex) {
            Logger.getLogger(JCheckSum.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            if (command != null) {
                command.getWriter().printf(command.getLocale(), "%s%n", ex.getMessage());
                command.printUsage();
            }
        } finally {
            try {
                if (command != null) {
                    command.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(JCheckSum.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

}
