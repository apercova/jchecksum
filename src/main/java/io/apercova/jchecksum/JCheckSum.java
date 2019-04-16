package io.apercova.jchecksum;

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

import io.apercova.jcheksum.converter.CustomCharsetConverter;
import io.apercova.jcheksum.converter.CustomMessageDigestConverter;
import io.apercova.quickcli.annotation.CLIArgument;
import io.apercova.quickcli.annotation.CLICommand;
import io.apercova.quickcli.annotation.CLIDatatypeConverter;
import io.apercova.quickcli.Command;
import io.apercova.quickcli.CommandFactory;
import io.apercova.quickcli.exception.CLIArgumentException;
import io.apercova.quickcli.exception.ExecutionException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
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
public final class JCheckSum extends Command<String> {

    public static final String SERIAL_UID_ENCODING = "JUID";
    public static final String B64_ENCODING = "B64";
    public static final String HEX_ENCODING = "HEX";

    @CLIArgument(name = "-a", aliases = {"-algorithm"}, value = "MD5", usage = "Hashing algorithm")
    @CLIDatatypeConverter(CustomMessageDigestConverter.class)
    private MessageDigest alg;
    @CLIArgument(name = "-e", aliases = {"-encoding"}, usage = "Output encoding. options: [HEX,B64], default: HEX", value = "HEX")
    private String encoding;
    @CLIArgument(name = "-f", aliases = {"-file"}, usage = "File Path")
    private String file;
    @CLIArgument(name = "-t", aliases = {"-text"}, usage = "Text caption")
    private String text;
    @CLIArgument(name = "-cs", aliases = {"-charset"}, usage = "Encoding charset", value = "UTF-8")
    @CLIDatatypeConverter(CustomCharsetConverter.class)
    private Charset cs;
    @CLIArgument(name = "-la", usage = "List available algorithms")
    private Boolean listAlgs;
    @CLIArgument(name = "-lc", usage = "List available charsets")
    private Boolean listCs;
    @CLIArgument(name = "-h", aliases = {"-help"}, usage = "List available options")
    private Boolean showHelp;
    @CLIArgument(name = "-m", aliases = {"-match"}, usage = "Compares suplied pattern against checksum")
    private String match;

    private byte[] encbytes;
    private String encString;

    @Override
    public String execute() throws ExecutionException {
        String res = "";
        if (showHelp) {
            printUsage();
        } else if (listAlgs) {
            listHashAlgorithms();
        } else if (listCs) {
            listCharsets();
        } else {
            res = calculateSum();
        }
        return res;
    }

    /**
     * Calculate sum based on command arguments.
     */
    private void digest() {

        byte[] buffer = new byte[1024];

        if (file != null && !(file.length() == 0)) {
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

        encbytes = alg.digest();
    }

    private void encode() {
        if (B64_ENCODING.equalsIgnoreCase(encoding)) {
            this.encString = DatatypeConverter.printBase64Binary(this.encbytes);
        } else if (SERIAL_UID_ENCODING.equalsIgnoreCase(encoding)) {
            this.encString = DatatypeConverter.printHexBinary(encbytes).toLowerCase();
            BigInteger bn = new BigInteger(this.encString, 16);
            BigInteger ml = new BigInteger(Long.MAX_VALUE+"");
            this.encString = String.valueOf(Math.abs(bn.longValue()));            
        }
        else {
            this.encString = DatatypeConverter.printHexBinary(this.encbytes).toLowerCase();
        }
    }

    private void print() {
        if (match != null && !match.isEmpty()) {
            writer.printf(locale, "%s%n", Pattern.matches(match, this.encString));
        } else {
            writer.printf(locale, "%s%n", this.encString);
        }
    }

    private String calculateSum() {
        this.digest();
        this.encode();
        this.print();
        return this.encString;
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
                writer.printf(locale, " --- Provider %s, version %.2f --- %n", provider.getName(), provider.getVersion());
                for (Service service : algos) {
                    String algo = service.getAlgorithm();
                    writer.printf(locale, "Algorithm name: \"%s\"%n", algo);
                }
            }
        }
    }

    /**
     * Prints a list of JVM available {@link Charset}.
     */
    private void listCharsets() {
        for (String k : Charset.availableCharsets().keySet()) {
            writer.printf(locale, "Charset name: \"%s\"%n", k);
        }
    }

    /**
     * Execution entry point.
     *
     * @param args Command arguments.
     */
    public static void main(String[] args) {

        Writer writer = new OutputStreamWriter(System.out);
        Command<String> command = null;
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
