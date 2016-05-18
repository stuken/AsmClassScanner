package com.skenny.asmclassscanner;

public interface ConstantPoolTags
{
    /**
     * CONSTANT_Fieldref constant pool tag.
     */
    public static final int FIELD_TAG = 9;

    /**
     * CONSTANT_Methodref constant pool tag.
     */
    public static final int METH_TAG = 10;

    /**
     * CONSTANT_InterfaceMethodref constant pool tag.
     */
    public static final int IMETH_TAG = 11;

    /**
     * CONSTANT_Long constant pool tag.
     */
    public static final int LONG_TAG = 5;

    /**
     * CONSTANT_Double constant pool tag.
     */
    public static final int DOUBLE_TAG = 6;

    /**
     * CONSTANT_Utf8 constant pool tag.
     */
    public static final int UTF8_TAG = 1;

    /**
     * CONSTANT_Class constant pool tag.
     */
    public static final int CLASS_TAG = 7;

    /**
     * CONSTANT_Integer constant pool tag.
     */
    public static final int INT_TAG = 3;

    /**
     * CONSTANT_Float constant pool tag.
     */
    public static final int FLOAT_TAG = 4;

    /**
     * CONSTANT_String constant pool tag.
     */
    public static final int STR_TAG = 8;

    /**
     * CONSTANT_NameAndType constant pool tag.
     */
    public static final int NAME_TYPE_TAG = 12;

    /**
     * CONSTANT_MethodHandle constant pool tag.
     */
    public static final int HANDLE_TAG = 15;

    /**
     * CONSTANT_MethodType constant pool tag.
     */
    public static final int MTYPE_TAG = 16;

    /**
     * CONSTANT_InvokeDynamic constant pool tag.
     */
    public static final int INVOKE_DYNAMIC_TAG = 18;
}