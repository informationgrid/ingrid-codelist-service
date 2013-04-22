package de.ingrid.codelists.model;

import java.util.HashMap;
import java.util.Map;

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
    private static final long serialVersionUID = 5219802039802156694L;
    
    private String              id;
    private String              description;
    private Map<String, String> localisations;
    private String 				data;
    
    public CodeListEntry() {
        localisations = new HashMap<String, String>();
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public void setLocalisedEntry(String lang, String localisedEntry) {
        this.localisations.put(lang, localisedEntry);
    }
    
    public String getLocalisedEntry(String lang) {
        String value = this.localisations.get(lang);
        return value == null ? "" : value;
    }

    public Map<String, String> getLocalisations() {
        return this.localisations;
    }
    
    public void setLocalisations(Map<String, String> map) {
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
}
