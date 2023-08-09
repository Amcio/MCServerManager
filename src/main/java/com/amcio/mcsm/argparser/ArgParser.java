package com.amcio.mcsm.argparser;

import java.util.Arrays;

public class ArgParser {
    String[] args;
    public ArgParser(String[] args) {
        this.args = args;
    }

    public boolean isOption(String option) {
        return Arrays.stream(args).anyMatch(str -> str.contains(option));
    }
    public String getPath() {
        return Arrays.stream(args)
                .filter(str -> !str.startsWith("-"))
                .findAny()
                .orElse("./");
    }
}
