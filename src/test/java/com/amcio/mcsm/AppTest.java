package com.amcio.mcsm;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.MinecraftEngine;
import org.junit.Test;

/**
 * Unit test for MinecraftServerManager.
 */
public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void argParserFindOption() {
        ArgParser argParser = new ArgParser(new String[]{"-fabric"});
        assertTrue(argParser.isOption("-fabric"));
        assertFalse(argParser.isOption("-forge"));
    }
    @Test
    public void regexVersionValidate() {
        assertTrue(MinecraftEngine.validateVersion("1.20.1"));
        assertTrue(MinecraftEngine.validateVersion("1.7.3"));
        assertTrue(MinecraftEngine.validateVersion("1.7.10"));
        assertTrue(MinecraftEngine.validateVersion("1.19"));
        assertFalse(MinecraftEngine.validateVersion("1..20"));
        assertFalse(MinecraftEngine.validateVersion("1.20 "));
    }

}
