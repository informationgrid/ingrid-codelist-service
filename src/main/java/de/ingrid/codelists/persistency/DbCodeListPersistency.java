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

}
