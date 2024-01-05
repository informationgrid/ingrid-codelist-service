/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2024 wemove digital solutions GmbH
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
package de.ingrid.codelists.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The model for an entry of a CodeList.
 * 
 * @author Andre
 *
 */
public class CodeListEntry  {
    /**
     * 
     */
    private static final long serialVersionUID = 4574387734455432251L;
    
    private String              id;
    private String              description;
    private Map<String, String> localisations;
    private String 				data;

    // private CodeListEntryStatus status;
    
    public CodeListEntry() {
        localisations = new HashMap<>();
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    /**
     * Use setField(...) instead
     * @param lang
     * @param localisedEntry
     */
    @Deprecated
    public void setLocalisedEntry(String lang, String localisedEntry) {
        this.localisations.put(lang, localisedEntry);
    }
    
    public void setField(String key, String value) {
        this.localisations.put(key, value);
    }
    
    /**
     * Use getField(...) instead
     */
    @Deprecated
    public String getLocalisedEntry(String lang) {
        String value = this.localisations.get(lang);
        return value == null ? "" : value;
    }
    
    public String getField(String key) {
        String value = this.localisations.get(key);
        return value == null ? "" : value;
    }

    /**
     * Use getFields(...) instead
     */
    @Deprecated
    public Map<String, String> getLocalisations() {
        return this.localisations;
    }
    
    public Map<String, String> getFields() {
        return this.localisations;
    }
    
    /**
     * Use setFields(...) instead
     */
    @Deprecated
    public void setLocalisations(Map<String, String> map) {
        this.localisations = map;
    }
    
    public void setFields(Map<String, String> map) {
        this.localisations = map;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public String getDescription() {
        return description;
    }

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

    /*public CodeListEntryStatus getStatus() {
        return status;
    }

    public void setStatus(CodeListEntryStatus status) {
        this.status = status;
    }*/

    
    
    /*
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getId());
        out.writeObject(getLocalisations());
        
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        setId((String) in.readObject());
        setLocalisations((Map<String, String>) in.readObject());
        
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeListEntry that = (CodeListEntry) o;
        return id.equals(that.id) &&
                localisations.equals(that.localisations) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, localisations, data);
    }
}
