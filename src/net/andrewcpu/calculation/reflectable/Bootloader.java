package net.andrewcpu.calculation.reflectable;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class Bootloader {
    public static ExternalFunctionData readJarProperties(String jarPath) throws IOException {
        File file = new File(jarPath);
        JarFile jar = new JarFile(file);
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
            JarEntry entry = enums.nextElement();
            if (entry.getName().equals("function.yml")) {
                InputStream is = jar.getInputStream(entry);
                String text = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
                String[] lines = text.split("\n");
                String applicationName = "";
                String mainPath = "";
                for (String line : lines) {
                    String[] parameters = line.split(": ");
                    if (parameters[0].equals("name")) {
                        applicationName = parameters[1];
                    } else if (parameters[0].equals("main")) {
                        mainPath = parameters[1];
                    }
                }
                return new ExternalFunctionData(applicationName, mainPath);
            }
        }
        return null;
    }

    public static List<ReflectableFunction> boot() throws Exception
    {
        List<ReflectableFunction> applicationDataList = new ArrayList<>();
        File directory = new File("functions");
        String[] paths = directory.list();

        for (String path : paths) {
            ExternalFunctionData data = readJarProperties((directory.getAbsolutePath()) + "\\" + path);
            URLClassLoader child = new URLClassLoader(
                    new URL[]{new File(directory.getAbsolutePath() + "\\" + path).toURI().toURL()}, Bootloader.class.getClassLoader());
            ReflectableFunction f = ((ReflectableFunction) Class.forName(data.getMain(), true, child).getConstructor().newInstance());
            f.setName(data.getName());
            applicationDataList.add(f);
        }
        return applicationDataList;
    }
}
