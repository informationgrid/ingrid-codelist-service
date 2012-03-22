package de.ingrid.codelists.comm;

public interface ICodeListCommunication {

    /**
     * Receive all codelists that are newer than 'timestamp'. If 'timestamp'
     * is null, all codelists are received.
     * @param timestamp is the date the last time codelists were received (at least one!)
     * @return typically a JSON String containing all codelists
     */
    public String sendRequest(Long timestamp);
    
}
