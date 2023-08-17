package com.amcio.mcsm.argparser;

import java.util.Arrays;
import java.util.Iterator;

public class ArgParser {
    String[] args;
    public ArgParser(String[] args) {
        this.args = args;
    }

    public boolean isOption(String option) {
        return Arrays.stream(args).anyMatch(str -> str.contains(option));
    }

    public String getValue(String option) {
        Iterator<String> argIterator = Arrays.stream(args).iterator();
        while (argIterator.hasNext()) {
            if (argIterator.next().equals(option) && argIterator.hasNext()) {
                String value = argIterator.next();
                if (value.startsWith("-")) {
                    throw new IllegalArgumentException("No value was assigned to option: " + option);
                }
                return value;
            }
        }
        return null;
    }
    // TODO: This function is now too simple, it will output one of the values instead of the path.
    //  The path should be the last element in args
    public String getPath() {
        return Arrays.stream(args)
                .filter(str -> !str.startsWith("-"))
                .findAny()
                .orElse("./");
    }
}
