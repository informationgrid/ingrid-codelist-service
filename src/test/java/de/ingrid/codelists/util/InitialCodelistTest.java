/*-
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2025 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelists.util;

import java.util.List;

import org.codehaus.jettison.json.JSONException;

import static org.junit.jupiter.api.Assertions.fail;
import org.codehaus.jettison.json.JSONObject;
import org.junit.jupiter.api.Test;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.InitialCodeListReaderPersistency;

public class InitialCodelistTest {

    @Test
    void test() {

        try {
            InitialCodeListReaderPersistency p = new InitialCodeListReaderPersistency();
            List<CodeList> l = p.read();
            for (CodeList cl : l) {
                for (CodeListEntry cle : cl.getEntries()) {
                    String data = cle.getData();
                    if (data != null && data.startsWith("{") && data.endsWith("}")) {
                        try {
                            new JSONObject( data );
                        } catch (JSONException e) {
                            fail("Error in JSON data field: " + data);
                        }
                    }
                }
            }
        } catch (Exception e) {
            fail("Error validating initial Codelist: " + e);
        }

    }

}
