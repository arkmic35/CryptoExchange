package com.michalak.cryptoexchange.util;

import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceUtil {
    @SneakyThrows
    public static String fetchResourceContents(String packageName, String fileName) {
        ClassLoader classLoader = ResourceUtil.class.getClassLoader();
        String packageFolder = packageName.replace('.', '/');
        String path = packageFolder + "/" + fileName;
        URL resource = classLoader.getResource(path);

        if (resource == null) {
            throw new FileNotFoundException("Resource in path " + path + " was not found.");
        } else {
            return Files.readString(Path.of(resource.toURI()));
        }
    }
}
