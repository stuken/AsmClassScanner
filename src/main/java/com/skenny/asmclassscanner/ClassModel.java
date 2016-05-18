package com.skenny.asmclassscanner;

import java.util.Set;
import java.util.TreeSet;
import org.objectweb.asm.ClassReader;

/**
 *
 * @author skenny
 */
public class ClassModel extends ClassReader implements ConstantPoolTags {

    private final Set<String> dependencyClassSet;

    public ClassModel(byte[] b) {
        super(b);
        dependencyClassSet = new TreeSet<>();
        this.readConstantPool();
    }

    public Set<String> getDependencyClassSet() {
        return this.dependencyClassSet;
    }

    public final void readConstantPool() {
        char[] buf = new char[maxStringLength];
        int cpSize = items.length;

        String classType;

        for (int i = 1; i < cpSize; i++) {
            int index = items[i];
            int tag = b[index - 1];
            switch (tag) {
                case CLASS_TAG:
                    classType = extractTypeFromUTF8(readUTF8(index, buf));
                    if (!isPrimitiveClass(classType)) {
                        dependencyClassSet.add(classType);
                    }
                    break;
                case LONG_TAG:
                case DOUBLE_TAG:
                    ++i;
                    break;
            }
        }
    }

    private boolean isPrimitiveClass(String classType) {
        switch (classType) {
            case "B":
            case "C":
            case "D":
            case "F":
            case "I":
            case "J":
            case "S":
            case "Z":
                return true;
        }
        return false;
    }

    private static String extractTypeFromUTF8(String cpString) {
        String tmpType = cpString;
        int descType = cpString.lastIndexOf("[");
        if (descType != -1) {
            tmpType = cpString.substring(descType + 1);
        }
        if (tmpType.startsWith("L") && tmpType.endsWith(";")) {
            return tmpType.substring(1, (tmpType.length() - 1));
        }
        return tmpType;
    }
}
