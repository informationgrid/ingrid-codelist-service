package de.ingrid.codelists;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.ingrid.codelists.comm.ICodeListCommunication;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.ICodeListPersistency;
import de.ingrid.codelists.util.CodeListUtils;

public class CodeListService {
    private final static Logger log = Logger.getLogger(CodeListService.class);

    // injected by Spring
    @Autowired
    private ICodeListCommunication      comm;

    // injected by Spring
    private List<ICodeListPersistency>  persistencies;
    
    // injected by Spring
    private int                         defaultPersistency;
    
    private List<CodeList>              codelists;
    
    
    public CodeListService() {}
    
    /**
     * Fetch all codelists from server and make them locally persistent
     * in the defined targets (XML or DB).
     */
    public void updateFromServer() {
        if (comm == null) {
            log.warn("No communication defined to retrieve codelists!");
            return;
        }
        // request repository and receive response which contains all codelists
        String response = comm.sendRequest();
        
        if (response != null) {
            // transform codelists into java objects
            this.codelists = CodeListUtils.getCodeListsFromResponse(response);
            
            // persist codelists in file/db
            persistToAll();
        }
    }
    
    public CodeList getCodeList(String id) {
        getCodeLists();
        
        // if codelists never could be read return null!
        if (this.codelists == null) return null;
        
        // filter codelist by ID
        for (CodeList cl : this.codelists) {
            if (cl.getId().equals(id)) {
                return cl;
            }
        }
        
        return null;
    }
    
    public List<CodeList> getCodeLists() {
        // read codelists if it's the first time
        if (this.codelists == null && persistencies != null) {
            this.codelists = persistencies.get(defaultPersistency).read();
        }
        return this.codelists;
    }

    public String getCodeListValue(String lstId, String entryId, String lang) {
        CodeList cl = getCodeList(lstId);
        if(cl != null){
        	for (CodeListEntry entry : cl.getEntries()) {
                if (entry.getId().equalsIgnoreCase(entryId)) {
                    return entry.getLocalisedEntry(lang);
                }
            }
        }
        return "";
    }
    
    public String getCodeListEntryId(String lstId, String entryValue, String lang) {
        CodeList cl = getCodeList(lstId);
        if(cl != null){
	        for (CodeListEntry entry : cl.getEntries()) {
	            if (entryValue.equalsIgnoreCase(entry.getLocalisedEntry(lang))) {
	                return entry.getId();
	            }
	        }
        }
        return "";
    }
    
    public void setCodelist(String id, String data) {
        CodeList cl = CodeListUtils.getCodeListFromJsonGeneric(data);
        
        // add modification date
        cl.setLastModified(System.currentTimeMillis());
        
        // remove old codelist if it exists
        CodeList oldCl = getCodeList(cl.getId());
        if (oldCl != null)
            this.codelists.remove(oldCl);
        
        cl.setId(id);
        this.codelists.add(cl);
        
        // make persistent
        persistToAll();
        
    }
    
    public boolean persistToAll() {
        try {
            for (ICodeListPersistency persistTarget : persistencies) {
                persistTarget.write(this.codelists);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean persistTo(int where) {
        this.persistencies.get(where).write(codelists);
        return true;
    }
    
    public void setComm(ICodeListCommunication comm) {
        this.comm = comm;
    }

    public void setPersistencies(List<ICodeListPersistency> persistencies) {
        this.persistencies = persistencies;
    }

    public void setDefaultPersistency(int defaultPersistency) {
        this.defaultPersistency = defaultPersistency;
    }
}
