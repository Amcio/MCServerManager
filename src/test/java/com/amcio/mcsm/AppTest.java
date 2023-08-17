package com.amcio.mcsm;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.util.Version;
import org.junit.Test;

/**
 * Unit test for MinecraftServerManager.
 */
public class AppTest 
{
    @Test
    public void argParserFindOption() {
        ArgParser argParser = new ArgParser(new String[]{"-fabric"});
        assertTrue(argParser.isOption("-fabric"));
        assertFalse(argParser.isOption("-forge"));
    }

    @Test
    public void versionToString() {
        assertEquals("1.7", new Version("1.7").toString());
        assertEquals("1.20.1", new Version("1.20.1").toString());
    }

    @Test
    public void versionComparison() {
        Version version1 = new Version("1.7.10");
        Version version2 = new Version("1.8.2");
        int result = version1.compareTo(version2);
        assertTrue(result < 0);
    }

}
