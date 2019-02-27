/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2019 wemove digital solutions GmbH
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

import de.ingrid.codelists.model.CodeList;

public class XmlCodeListPersistency<T> implements ICodeListPersistency {

    private static Log log = LogFactory.getLog(XmlCodeListPersistency.class);
    
    private String pathToXml;
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> read() {
        XStream xStream = new XStream();
        
        try {
            checkForFolder(this.pathToXml);
            // reader = new InputStreamReader(new FileInputStream(this.pathToXml), "UTF-8");
            List<T> list = new ArrayList<T>();
            
            try (Stream<Path> paths = Files.walk(Paths.get( this.pathToXml ))) {
                paths
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        Reader codelistReader = null;
                        try {
                            codelistReader = new FileReader(file.toFile());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } // , "UTF-8");
                        
                        Object xml = xStream.fromXML( codelistReader );
                        try {
                            codelistReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (xml instanceof List) {
                            list.addAll( (List<T>) xml );
                        } else {
                            list.add( (T) xml );
                        }
                        
                    });
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } 
            
            return list;
        } catch (StreamException e) {
            return new ArrayList<T>();
        }
    }

    @Override
    public boolean write(List<CodeList> data) {
        XStream xStream = new XStream();
        FileOutputStream fos = null;
        try {
            checkForFolder(this.pathToXml);
            
            for (CodeList codeList : data) {
                
                fos = new FileOutputStream(this.pathToXml + "/codelist_" + codeList.getId() + ".xml");
                Writer writer = new OutputStreamWriter(fos, "UTF-8");
                xStream.toXML(codeList, writer);
                fos.close();
            }
            
            return true;
        } catch (FileNotFoundException e) {
            log.error( "Codelist file could not be found" );
        } catch (UnsupportedEncodingException e) {
            log.error( "Codelist file has unsupported encoding", e );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void checkForFolder(String folderPath) {
        File f = new File(folderPath);
        
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public void setPathToXml(String pathToXml) {
        this.pathToXml = pathToXml;
    }


    @Override
    public boolean writePartial(List<CodeList> codelists) {
        return write(codelists);
    }

    
    @Override
    public boolean canDoPartialUpdates() {
        return true;
    }

    @Override
    public boolean remove(String id) {
        File file = new File(this.pathToXml + "/codelist_" + id + ".xml");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

}
