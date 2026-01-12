/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2026 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
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

public class DbCodeListPersistency implements ICodeListPersistency {
    
    private String dbName;
    private String username;
    private String password;

    public DbCodeListPersistency() {}
    
    @Override
    public List<CodeList> read() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean write(List<CodeList> codelists) {
        // TODO Auto-generated method stub
        return false;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
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
        // not supported
        return false;
    }

}
