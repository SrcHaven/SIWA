package com.srchaven.siwa.util;

//TODO: JavaDocs
public class FileNameUtils
{
    public static String extractState(String filename)
    {
        return (filename == null) ? "" : filename.substring(14, 16);
    }
}
