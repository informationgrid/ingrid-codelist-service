package de.ingrid.codelists.persistency;

import java.io.InputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

import de.ingrid.codelists.model.CodeList;

public class InitialCodeListReaderPersistency implements ICodeListPersistency {

    public static String pathToXml = "codelists_initial.xml";
    
    public InitialCodeListReaderPersistency() {}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        try {
            InputStream input = this.getClass().getResourceAsStream("/codelists_initial.xml");
            return (List<CodeList>) xStream.fromXML(input);
        } catch (StreamException e) {
            //return new ArrayList<CodeList>();
        }
        return null;
    }

    @Override
    public boolean write(List<CodeList> data) {
        // this class is not supposed to change the data!!!
        return false;
    }

    
    @Override
    public boolean writePartial(List<CodeList> codelists) {
        //new Exception();
        return false;
    }

    
    @Override
    public boolean canDoPartialUpdates() {
        return false;
    }

}
