package de.ingrid.codelists.util;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.InitialCodeListReaderPersistency;

public class InitialCodelistTest {

    @Test
    public void test() {

        try {
            InitialCodeListReaderPersistency p = new InitialCodeListReaderPersistency();
            List<CodeList> l = p.read();
            for (CodeList cl : l) {
                for (CodeListEntry cle : cl.getEntries()) {
                    String data = cle.getData();
                    if (data != null && data.startsWith( "{" ) && data.endsWith( "}" )) {
                        try {
                            new JSONObject( data );
                        } catch (JSONException e) {
                            Assert.fail( "Error in JSON data field: " + data );
                        }
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail( "Error validating initial Codelist: " + e );
        }

    }

}
