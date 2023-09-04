package com.amcio.mcsm.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class UnsafeURL {
    /**
     * A very unsafe factory, multiple times in this project I can be 100% certain that the URL is correct.
     * This is to deduplicate this behaviour.
     * @param str The URL string
     * @return A URL object
     */
    public static URL of(String str) {
        URL url = null;
        try {
            url = new URI(str).toURL();
        } catch (URISyntaxException | MalformedURLException ignored) { }
        assert url != null;

        return url;
    }
}
