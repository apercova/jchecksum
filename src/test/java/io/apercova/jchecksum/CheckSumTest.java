package io.apercova.jchecksum;

import io.apercova.quickcli.Command;
import io.apercova.quickcli.CommandFactory;
import java.io.StringWriter;
import java.io.Writer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link Command} implementation for jchecksum command.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */

public final class CheckSumTest extends Command<Void> {

    String[] args;
    Writer writer;
    
    @Before
    public void init() {
    }

    @Test
    public void test() throws Exception {
        writer = new StringWriter();
        args = new String[]{
            "-a", "sha1",
            "-t", "",
            "-cs", "utf-8",
            "-e", "b64"
        };
        JCheckSum command = CommandFactory.create(args, JCheckSum.class, writer);
        assertTrue(command != null);
        command.execute();
        assertEquals("Missing sha1(\"\");alg=iso-8859-1;enc=b64", "2jmj7l5rSw0yVb/vlWAYkK/YBwk=", writer.toString());
        
        writer = new StringWriter();
        args = new String[]{
            "-a", "sha1",
            "-t", "1234567890ñ",
            "-cs", "iso-8859-1",
            "-e", "hex"
        };
        command = CommandFactory.create(args, JCheckSum.class, writer);
        assertTrue("Error creating command", command != null);
        command.execute();
        assertEquals("Missing sha1(\"1234567890ñ\");alg=iso-8859-1;enc=hex", "534aa90bd7aace87cb732cf472c58933204a097e", writer.toString());

        writer = new StringWriter();
        args = new String[]{
            "-a", "sha1",
            "-t", "1234567890",
            "-cs", "iso-8859-1",
            "-e", "juid"
        };
        command = CommandFactory.create(args, JCheckSum.class, writer);
        assertTrue("Error creating command", command != null);
        command.execute();
        assertEquals("Missing sha1(\"1234567890\");alg=iso-8859-1;enc=juid", "5734283031892443494", writer.toString());
        
        writer = new StringWriter();
        args = new String[]{
            "--text", "1234567890ABCDEF",
            "--charset", "utf-8",
            "--encoding", "b64",
            "--encode-only"
        };
        command = CommandFactory.create(args, JCheckSum.class, writer);
        assertTrue("Error creating command", command != null);
        command.execute();
        assertEquals("Missing sha1(\"1234567890ABCDEF\");alg=utf-8;enc=b64;enc-only", "MTIzNDU2Nzg5MEFCQ0RFRg==", writer.toString());
    }

}
