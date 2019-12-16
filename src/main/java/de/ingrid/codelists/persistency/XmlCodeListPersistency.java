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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import de.ingrid.codelists.model.CodeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class XmlCodeListPersistency<T> implements ICodeListPersistency {

    private static Log log = LogFactory.getLog(XmlCodeListPersistency.class);
    
    private String pathToXml;
    
    @SuppressWarnings("unchecked")
    @Override
    public List<T> read() {
        XStream xStream = new XStream();
        
        try {
            checkForFolder(this.pathToXml);
            List<T> list = new ArrayList<>();
            
            try (Stream<Path> paths = Files.walk(Paths.get( this.pathToXml ))) {
                paths
                    .filter(p -> Files.isRegularFile(p) && p.toString().toLowerCase().endsWith(".xml")                    )
                    .forEach(file -> {
                        Object xml;
                        Reader codelistReader;

                        try {
                            codelistReader = new FileReader(file.toFile());
                            xml = xStream.fromXML( codelistReader );
                            codelistReader.close();

                            if (xml instanceof List) {
                                list.addAll( (List<T>) xml );
                            } else {
                                list.add( (T) xml );
                            }

                        } catch (FileNotFoundException e) {
                            log.error("Could not read file: " + file.getFileName().toString());
                        } catch (Exception e) {
                            // log error and skip to next file
                            log.error("Error converting file to XML", e);
                        }
                    });
            } catch (IOException e1) {
                log.error("Error reading codelists", e1);
            } 
            
            return list;
        } catch (StreamException e) {
            log.error(e);
            return new ArrayList<>();
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
                Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
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
            if (!f.mkdirs()) {
                log.warn("Directory for codelists could not be created");
            }
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
