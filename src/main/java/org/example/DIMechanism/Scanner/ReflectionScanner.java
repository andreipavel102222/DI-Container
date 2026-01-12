package org.example.DIMechanism.Scanner;

import org.example.DIMechanism.Annotations.Component;
import org.example.DIMechanism.Components.ComponentMetadata;
import org.example.DIMechanism.Components.ReflectionComponentMetadata;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionScanner implements Scanner{

    public List<ComponentMetadata> scan(String packageName) {
        try{
            String path = packageName.replace('.', '/');
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources;
            try {
                resources = cl.getResources(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<ComponentMetadata> classes = new ArrayList<>();

            while(resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if(url.getProtocol().equals("file")){
                    classes.addAll(findClassesInDirectory(new File(url.toURI()), packageName));
                }
                else if(url.getProtocol().equals("jar")){
                    classes.addAll(findClassesInJar(url, path));
                }
            }
            return classes;
        }
        catch(RuntimeException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ComponentMetadata> findClassesInDirectory(File directory, String packageName) {
        List<ComponentMetadata> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) return classes;

        for(File file: files){
            if(file.isDirectory()){
                classes.addAll(
                        Objects.requireNonNull(findClassesInDirectory(file, packageName + "." + file.getName()))
                );
            }
            else{
                if(file.getName().endsWith(".class")){
                    String className = packageName + "." + file.getName().replace(".class", "");
                    Class<?> clazz = loadClass(className);
                    if(clazz.isAnnotationPresent(Component.class)) {
                        classes.add(new ReflectionComponentMetadata(clazz));
                    }
                }
            }
        }
        return classes;
    }

    private List<ComponentMetadata> findClassesInJar(URL jarUrl, String path) {
        List<ComponentMetadata> classes = new ArrayList<>();

        try {
            JarURLConnection connection =
                    (JarURLConnection) jarUrl.openConnection();

            JarFile jarFile = connection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith(path)
                        && name.endsWith(".class")
                        && !entry.isDirectory()) {

                    String className = name
                            .replace('/', '.')
                            .replace(".class", "");

                    Class<?> clazz = loadClass(className);
                    if(clazz.isAnnotationPresent(Component.class)) {
                        classes.add(new ReflectionComponentMetadata(clazz));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }

    private Class<?> loadClass(String className){
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            return Class.forName(className, false, cl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
