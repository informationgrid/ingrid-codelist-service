package de.ingrid.codelists.persistency;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

import de.ingrid.codelists.model.CodeList;

public class InitialCodeListReaderPersistency implements ICodeListPersistency {

	private static Log log = LogFactory.getLog(InitialCodeListReaderPersistency.class);

    public static String pathToXml = "codelists_initial.xml";
    
    public InitialCodeListReaderPersistency() {}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        try {
            InputStream input = this.getClass().getResourceAsStream("/codelists_initial.xml");
            Reader reader = new InputStreamReader(input, "UTF-8");
            return (List<CodeList>) xStream.fromXML(reader);
        } catch (Exception e) {
        	log.warn("Problems reading initial codelists from file codelists_initial.xml.", e);
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
