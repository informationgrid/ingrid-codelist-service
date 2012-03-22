package de.ingrid.codelists.persistency;

import java.util.List;

import de.ingrid.codelists.model.CodeList;

public interface ICodeListPersistency {
    
    public List<CodeList> read();
    
    // write all codelists to the destination
    public boolean write(List<CodeList> codelists);
    
    // write only modified codelists to the destination by merging
    public boolean writePartial(List<CodeList> codelists);
    
    // is the persistence layer able to do partial updates of codelists?
    public boolean canDoPartialUpdates();
}
