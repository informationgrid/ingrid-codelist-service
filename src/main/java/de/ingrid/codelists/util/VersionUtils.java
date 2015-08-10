package de.ingrid.codelists.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VersionUtils {
    private static final String VERSION_INFO = "version.info";

    public static String getCurrentVersion() {
        Path path = Paths.get( "data", VERSION_INFO );
        if (path.toFile().exists()) {
            byte[] encoded;
            try {
                encoded = Files.readAllBytes(path);
                return new String(encoded, Charset.forName( "UTF-8" ));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "0";
        } else {
            return "0";
        }
    }
    
    public static void writeVersionInfo( String text ) throws FileNotFoundException {
        PrintWriter out = new PrintWriter( "data/" + VERSION_INFO );
        out.print( text );
        out.close();
    }
}
