package de.ingrid.codelists.persistency;

import java.util.List;

import de.ingrid.codelists.model.CodeList;

public interface ICodeListPersistency {

    public List<CodeList> read();
    
    public boolean write(List<CodeList> codelists);
}
