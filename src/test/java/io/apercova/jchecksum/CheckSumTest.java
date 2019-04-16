package io.apercova.jchecksum;

import io.apercova.quickcli.Command;
import io.apercova.quickcli.CommandFactory;
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
    
    @Before
    public void init() {
        args = new String[]{
            "-a", "sha1",
            "-text", "",
            "-cs", "iso-8859-1",
            "-e", "b64"
        };
    }

    @Test
    public void test() throws Exception {
        args = new String[]{
            "-a", "sha1",
            "-text", "",
            "-cs", "utf-8",
            "-e", "b64"
        };
        JCheckSum command = CommandFactory.create(args, JCheckSum.class);
        assertTrue(command != null);
        assertEquals("Missing sha1(\"\");alg=iso-8859-1;enc=b64", "2jmj7l5rSw0yVb/vlWAYkK/YBwk=", command.execute());
        
        args = new String[]{
            "-a", "sha1",
            "-text", "1234567890ñ",
            "-cs", "iso-8859-1",
            "-e", "hex"
        };
        command = CommandFactory.create(args, JCheckSum.class);
        assertTrue("Error creating command", command != null);
        assertEquals("Missing sha1(\"1234567890ñ\");alg=iso-8859-1;enc=hex", "534aa90bd7aace87cb732cf472c58933204a097e", command.execute());

    }

}
