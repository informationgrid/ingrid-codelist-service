/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 wemove digital solutions GmbH
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
package de.ingrid.codelists.persistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

import de.ingrid.codelists.model.CodeList;

public class XmlCodeListPersistency implements ICodeListPersistency {

    private static Log log = LogFactory.getLog(XmlCodeListPersistency.class);
    
    private String pathToXml;
    
    public XmlCodeListPersistency() {}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        try {
            checkForFile(this.pathToXml);
            Reader reader = new InputStreamReader(new FileInputStream(this.pathToXml), "UTF-8");
            return (List<CodeList>) xStream.fromXML(reader);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //throw new Exception();
        } catch (StreamException e) {
            return new ArrayList<CodeList>();
        } catch (UnsupportedEncodingException e) {
            log.warn("Problems reading codelists from file "+this.pathToXml+".", e);
        }
        return null;
    }

    @Override
    public boolean write(List<CodeList> data) {
        XStream xStream = new XStream();
        try {
            checkForFile(this.pathToXml);
            
            FileOutputStream fos = new FileOutputStream(this.pathToXml);
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            // sort list by id to write similar file so it can be patched
            Collections.sort(data);
            xStream.toXML(data, writer);
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void checkForFile(String filePath) {
        File f = new File(filePath);
        File parentDir = f.getParentFile();
        // create dir if the file is inside a subdirectory and does not exist already
        if (parentDir != null && (!parentDir.exists() || !parentDir.isDirectory())) {
            parentDir.mkdir();
        }
        // check for the file now!
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setPathToXml(String pathToXml) {
        this.pathToXml = pathToXml;
    }


    @Override
    public boolean writePartial(List<CodeList> codelists) {
        return false;
    }

    
    @Override
    public boolean canDoPartialUpdates() {
        return false;
    }

}
