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
package de.ingrid.codelists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.ingrid.codelists.comm.ICodeListCommunication;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.ICodeListPersistency;
import de.ingrid.codelists.persistency.InitialCodeListReaderPersistency;
import de.ingrid.codelists.util.CodeListUtils;

public class CodeListService {
    private final static Logger log = LogManager.getLogger(CodeListService.class);

    // injected by Spring
    @Autowired(required = false)
    private ICodeListCommunication      comm;

    // injected by Spring
    private List<ICodeListPersistency>  persistencies;
    
    // injected by Spring
    private int                         defaultPersistency;
    
    private List<CodeList>              codelists;
    
    public CodeListService() {
        this.codelists             = new ArrayList<CodeList>();
    }
    
    /**
     * Fetch all codelists from server and make them locally persistent
     * in the defined targets (XML or DB).
     */
    public List<CodeList> updateFromServer() {
        return updateFromServer(-1L);
    }
    
    /**
     * Fetch all codelists that have been modified since 'timestamp' from the server 
     * and make them locally persistent in the defined targets (XML or DB).
     * @param timestamp
     * @return changed codelists received from repository or 'null' if an error occurred
     */
    public List<CodeList> updateFromServer(Long timestamp) {
        if (comm == null) {
            log.warn("No communication defined to retrieve codelists!");
            return null;
        }
        
        List<CodeList> lastModifiedCodelists = new ArrayList<CodeList>();
        
        // request repository and receive response which contains all codelists
        String response = comm.sendRequest(timestamp);
        
        if (response != null) {
            // transform codelists into java objects
            lastModifiedCodelists = CodeListUtils.getCodeListsFromResponse(response);
          
            // only update if there were any modifications!
            if (lastModifiedCodelists.size() > 0) {
                log.info(lastModifiedCodelists.size() + " modified codelist(s) received.");
                
                // merge codelist
                mergeModifiedCodelists(lastModifiedCodelists);
                
                // persist codelists in file/db
                persistToAll(lastModifiedCodelists);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No modified codelists have been received.");
                }
            }
        } else {
            log.error("Communication problem to the codelist repository!");
            return null;
        }
        
        return lastModifiedCodelists;
    }
    
    private void mergeModifiedCodelists(List<CodeList> modifiedCodelists) {
        for (CodeList codeList : modifiedCodelists) {
            setCodelist(codeList.getId(), codeList);                
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
    
    @SuppressWarnings("unchecked")
    public List<CodeList> getCodeLists() {
        // read codelists if it's the first time
        if (this.codelists.isEmpty() && persistencies != null) {
            this.codelists = (List<CodeList>) persistencies.get(defaultPersistency).read();
            // log an error if codelists could not be read!
            if (this.codelists == null || this.codelists.isEmpty()) {
                log.warn("No Codelists could be read using initial ones!");
                this.codelists = getInitialCodelists();
                
                // if no codelists are stored yet, make sure to store the initial set, so that the next
                // time the codelists are read from data dir
                persistToAll();
            }
        }
        return this.codelists;
    }

    public List<CodeList> getInitialCodelists() {
        InitialCodeListReaderPersistency p = new InitialCodeListReaderPersistency();
        return p.read();
    }

    public String getCodeListValue(String lstId, String entryId, String lang) {
        String localizedEntry = null;
        CodeList cl = getCodeList(lstId);
        if(cl != null){
        	for (CodeListEntry entry : cl.getEntries()) {
                if (entry.getId().equalsIgnoreCase(entryId)) {
                    localizedEntry = entry.getField(lang);
                    // fallback to english value
                    if (localizedEntry == null)
                        localizedEntry = entry.getField("en");
                    break;
                }
            }
        }
        return localizedEntry;
    }
    
    /** Map String to codelist entry
     * @param lstId id of syslist
     * @param entryValue string to map
     * @param lang which lang of codelist to use ("de", "en") ? PASS "" or null to check all entries independent from localisation !!!
     * @return found entry value or null
     */
    public String getCodeListEntryId(String lstId, String entryValue, String lang) {
    	return getCodeListEntryId(lstId, entryValue, lang, false);
    }
    
    /** Map String to codelist entry
     * @param lstId id of syslist
     * @param entryValue string to map
     * @param lang which lang of codelist to use ("de", "en") ? PASS "" or null to check all entries independent from localisation !!!
     * @param doRobustComparison pass true if comparison should be robust, e.g. values match even if "—" != "-" !
     * @return found entry value or null
     */
    public String getCodeListEntryId(String lstId, String entryValue, String lang, Boolean doRobustComparison) {
        CodeList cl = getCodeList(lstId);
        if(cl != null){
        	// more robust comparison, see INGRID-2334
        	if (doRobustComparison) {
        		entryValue = entryValue.trim().replace("—", "").replace("-", "").replace(" ", "");        		
        	}
	        for (CodeListEntry entry : cl.getEntries()) {
	        	Collection<String> localisedEntryValues = new ArrayList<String>();
	        	if (lang != null && lang.length() > 0) {
		        	localisedEntryValues.add(entry.getField(lang));
	        	} else {
	        		localisedEntryValues = entry.getFields().values();
	        	}
	        	for (String localisedEntryValue : localisedEntryValues) {
		        	if (doRobustComparison && localisedEntryValue != null) {
		        		localisedEntryValue = localisedEntryValue.trim().replace("—", "").replace("-", "").replace(" ", "");
		        	}
		            if (entryValue.equalsIgnoreCase(localisedEntryValue)) {
		                return entry.getId();
		            }	        		
	        	}
	        }
        }
        return null;
    }
    
    /**
     * This function should only be used by the codelist repository where the timestamp
     * is also set.
     * @param id
     * @param data
     */
    public CodeList setCodelist(String id, String data) {
        CodeList cl = CodeListUtils.getCodeListFromJsonGeneric(data);
        // add modification date
        cl.setLastModified(System.currentTimeMillis());
        setCodelist(id, cl);
        return cl;
    }
        
    public void setCodelist(String id, CodeList cl) { 
        // remove old codelist if it exists
        CodeList oldCl = getCodeList(cl.getId());
        if (oldCl != null)
            this.codelists.remove(oldCl);
        
        cl.setId(id);
        cl.setLastModified(System.currentTimeMillis());
        this.codelists.add(cl);
    }
    
    public boolean persistToAll() {
        return persistToAll(this.codelists);
    }
    
    public boolean persistToAll(List<CodeList> modifiedCodelists) {
        try {
            for (ICodeListPersistency persistTarget : persistencies) {
                if (persistTarget.canDoPartialUpdates()) {
                    return persistTarget.writePartial(modifiedCodelists);
                } else {
                    return persistTarget.write(this.codelists);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean persistTo(int where) {
        this.persistencies.get(where).write(this.codelists);
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

    public Long getLastModifiedTimestamp() {
        Long time = -1L;
        for (CodeList codelist : getCodeLists()) {
            if (codelist.getLastModified() > time)
                time = codelist.getLastModified();
        }
        return time;
    }
    
    public Long getLatestTimestampFromInitialCodelist() {
        Long time = -1L;
        for (CodeList codelist : getInitialCodelists()) {
            if (codelist.getLastModified() > time)
                time = codelist.getLastModified();
        }
        return time;
    }

    public void removeCodelist(String id) {
        for (ICodeListPersistency persistTarget : persistencies) {
            persistTarget.remove( id );
        }
        
    }
}
