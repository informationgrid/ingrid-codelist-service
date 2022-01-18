/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
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

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.security.AnyTypePermission;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;

import de.ingrid.codelists.model.CodeList;

public class InitialCodeListReaderPersistency implements ICodeListPersistency {

	private static Log log = LogFactory.getLog(InitialCodeListReaderPersistency.class);

    public InitialCodeListReaderPersistency() {}
    
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        try {
            List<CodeList> list = new ArrayList<>();
            
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
            Resource[] codelistResources = context.getResources("classpath*:/initial/codelist_*.xml");
            
            for (Resource codelistResource : codelistResources) {
                Reader codelistReader = new InputStreamReader(codelistResource.getInputStream(), "UTF-8");
                
                list.add( (CodeList) xStream.fromXML( codelistReader ) ); 
                
                codelistReader.close();
            }
            
            context.close();
            
            return list;
        } catch (Exception e) {
        	log.warn("Problems reading initial codelists", e);
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
        return false;
    }

    
    @Override
    public boolean canDoPartialUpdates() {
        return false;
    }

    @Override
    public boolean remove(String id) {
        // not needed
        return false;
    }

}
