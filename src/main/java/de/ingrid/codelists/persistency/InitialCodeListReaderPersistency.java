/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2018 wemove digital solutions GmbH
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

import de.ingrid.codelists.model.CodeList;

public class InitialCodeListReaderPersistency implements ICodeListPersistency {

	private static Log log = LogFactory.getLog(InitialCodeListReaderPersistency.class);

    private static String INITIAL_CODELISTS_FILENAME = "codelists_initial.xml";
    
    public InitialCodeListReaderPersistency() {}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        try {
            InputStream input = this.getClass().getResourceAsStream("/" + INITIAL_CODELISTS_FILENAME);
            Reader reader = new InputStreamReader(input, "UTF-8");
            return (List<CodeList>) xStream.fromXML(reader);
        } catch (Exception e) {
        	log.warn("Problems reading initial codelists from file " + INITIAL_CODELISTS_FILENAME, e);
            //return new ArrayList<CodeList>();
        }
        return null;
    }

    @Override
    public boolean write(List<CodeList> data) {
        // this class is not supposed to change the data!!!
        return false;
    }

    
    @Override
    public boolean writePartial(List<CodeList> codelists) {
        //new Exception();
        return false;
    }

    
    @Override
    public boolean canDoPartialUpdates() {
        return false;
    }

}
