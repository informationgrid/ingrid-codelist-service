/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2017 wemove digital solutions GmbH
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

import java.util.List;

import de.ingrid.codelists.model.CodeList;

public interface ICodeListPersistency {
    
    public List<?> read();
    
    // write all codelists to the destination
    public boolean write(List<CodeList> codelists);
    
    // write only modified codelists to the destination by merging
    public boolean writePartial(List<CodeList> codelists);
    
    // is the persistence layer able to do partial updates of codelists?
    public boolean canDoPartialUpdates();
}
