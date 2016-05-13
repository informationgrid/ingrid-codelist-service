/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2016 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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
