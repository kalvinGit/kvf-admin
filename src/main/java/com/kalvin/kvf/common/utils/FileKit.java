package com.kalvin.kvf.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Create by Kalvin on 2020/6/7.
 */
public class FileKit {

    private final static Logger log = LoggerFactory.getLogger(FileKit.class);

    public static InputStream readFile(String relativePath) {
        return FileKit.class.getClassLoader().getResourceAsStream(relativePath);
    }

    public static String readString(String relativePath) {
        InputStream inputStream = FileKit.readFile(relativePath);
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
