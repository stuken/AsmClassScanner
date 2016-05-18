package com.skenny.asmclassscanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author skenny
 */
public class Scanner {

    private static final String JAR_EXT = "jar";
    private static final String CLASS_EXT = "class";

    private static final String ERROR_MESSAGE = "Please supply valid directory, jar or class file. Exiting.";

    private List<InputStream> getJarClassEntryList(JarFile jarFile) {
        List<InputStream> jarEntryStreamList = new ArrayList<>();
        Enumeration<JarEntry> zipEntries = jarFile.entries();
        for (JarEntry entry : Collections.list(zipEntries)) {
            if (FilenameUtils.isExtension(entry.getName(), CLASS_EXT)) {
                try {
                    jarEntryStreamList.add(jarFile.getInputStream(entry));
                } catch (IOException e) {
                }
            }
        }

        return jarEntryStreamList;
    }

    private void processFile(File inputFile, Map<String, Set<String>> dependencyMap) throws IOException {
        String filePath = inputFile.getAbsolutePath();
        String ext = FilenameUtils.getExtension(filePath);
        switch (ext) {
            case JAR_EXT:
                processAsJar(filePath, dependencyMap);
                break;
            case CLASS_EXT:
                processAsClass(filePath, dependencyMap);
                break;
            default:
                break;
        }
    }

    private void processAsClass(String filePath, Map<String, Set<String>> dependencyMap) throws IOException, FileNotFoundException {
        RandomAccessFile rFile = new RandomAccessFile(filePath, "r");
        byte[] byteCode = new byte[(int) rFile.length()];
        rFile.readFully(byteCode);
        populateDependencyMap(byteCode, dependencyMap);
    }

    private void processAsJar(String filePath, Map<String, Set<String>> dependencyMap) throws IOException {
        JarFile jarFile;
        jarFile = new JarFile(filePath);
        List<InputStream> jarEntryStreamList = getJarClassEntryList(jarFile);
        for (InputStream jarEntryStream : jarEntryStreamList) {
            byte[] byteCode = IOUtils.toByteArray(jarEntryStream);
            populateDependencyMap(byteCode, dependencyMap);
        }
    }

    private void populateDependencyMap(byte[] byteCode, Map<String, Set<String>> dependencyMap) {
        ClassModel model = new ClassModel(byteCode);
        dependencyMap.put(model.getClassName(), model.getDependencyClassSet());
    }

    private void printJsonData(Map<String, Set<String>> dependencyMap, Gson gson) {
        System.out.println(gson.toJson(dependencyMap));
    }

    public void go(String[] args) throws IOException {
        String scanDirPath = args[0];
        String[] validExt = {JAR_EXT, CLASS_EXT};
        Map<String, Set<String>> dependencyMap = new TreeMap<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        File scanFile = new File(scanDirPath);
        if (scanFile.isDirectory()) {
            Collection<File> classPathFiles = FileUtils.listFiles(scanFile, validExt, true);
            for (File inputFile : classPathFiles) {
                processFile(inputFile, dependencyMap);
            }
            printJsonData(dependencyMap, gson);
        } else if (scanFile.isFile()) {
            String ext = FilenameUtils.getExtension(scanFile.getAbsolutePath());
            switch (ext) {
                case JAR_EXT:
                case CLASS_EXT:
                    processFile(scanFile, dependencyMap);
                    printJsonData(dependencyMap, gson);
                    break;
                default:
                    System.err.println(ERROR_MESSAGE);
                    break;
            }
        } else {
            System.err.println(ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner();
        scanner.go(args);
    }
}
