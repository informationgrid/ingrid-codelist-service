/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2021 wemove digital solutions GmbH
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
package de.ingrid.codelists.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The model for a CodeList, which are going to be stored.
 * 
 * @author Andre
 *
 */
public class CodeList implements Comparable<CodeList> {
    // the id of the codelist
    private String  id;
    
    // the name of the codelist
    private String  name;
    
    // a description of the codelist
    private String  description;
    
    // the entry that should be selected as default
    private String  defaultEntry;
    
    // several entries of a codelist, which can be localised
    private List<CodeListEntry> entries;
    
    // the date the codelist has been modified the last time
    private long lastModified;
    
    // can the codelist be edited locally?
    //private boolean maintainable; 
    
    public CodeList() {
        this.entries = new ArrayList<CodeListEntry>();
        defaultEntry = "";
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDefaultEntry(String defaultEntry) {
        this.defaultEntry = defaultEntry;
    }

    public String getDefaultEntry() {
        return defaultEntry;
    }
    
    public void setEntries(List<CodeListEntry> entries) {
        this.entries = entries;
    }
    public List<CodeListEntry> getEntries() {
        return entries;
    }
    
    public void addEntry(CodeListEntry entry) {
        this.entries.add(entry);
    }
    
    public void removeEntry(CodeListEntry entry) {
        this.entries.remove(entry);
    }
    
    public void removeEntry(String id) {
        for (CodeListEntry entry : entries) {
            if (entry.getId().equals(id)) {
                entries.remove(entry);
                break;
            }
        }
    }

    public void setLastModified(long date) {
        this.lastModified = date;
    }

    public long getLastModified() {
        return lastModified;
    }

	@Override
	public int compareTo(CodeList cl) {
		return getId().compareTo(cl.getId());
	}

    /*
    public void setMaintainable(boolean maintainable) {
        this.maintainable = maintainable;
    }

    public boolean isMaintainable() {
        return maintainable;
    }
    */

	public String toString() {
	    return id;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeList codeList = (CodeList) o;
        return id.equals(codeList.id) &&
                Objects.equals(name, codeList.name) &&
                entries.equals(codeList.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entries);
    }
}
