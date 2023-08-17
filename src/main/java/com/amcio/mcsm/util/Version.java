package com.amcio.mcsm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {

    private final int major;
    private final int minor;
    private final int patch;

    public Version(String str) {
        Matcher matcher = Pattern.compile("^\\d\\.\\d\\d?(\\.\\d\\d?)?$").matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid version format: " + str);
        }
        String[] components = str.split("\\.");
        this.major = Integer.parseInt(components[0]);
        this.minor = Integer.parseInt(components[1]);
        if (components.length == 3) {
            this.patch = Integer.parseInt(components[2]);
        } else {
            this.patch = 0;
        }
    }

    @Override
    public int compareTo(Version version) {
        if (this.major != version.major) {
            return Integer.compare(this.major, version.major);
        }
        if (this.minor != version.minor) {
            return Integer.compare(this.minor, version.minor);
        }
        return Integer.compare(this.patch, version.patch);
    }

    public boolean isGreaterThan(Version other) {
        return compareTo(other) > 0;
    }

    public boolean isGreaterThan(String version) {
        return compareTo(new Version(version)) > 0;
    }

    public boolean isLessThan(Version other) {
        return compareTo(other) < 0;
    }

    public boolean isLessThan(String version) {
        return compareTo(new Version(version)) < 0;
    }
    public boolean equals(Version other) {
        return compareTo(other) == 0;
    }

    public boolean equals(String version) {
        return compareTo(new Version(version)) == 0;
    }

    public String toString() {
        if (patch != 0) {
            return major + "." + minor + "." + patch;
        }
        return major + "." + minor;
    }
}
