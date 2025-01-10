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

import de.ingrid.codelists.util.VersionUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionUtilsTest {

    @Test
    final void testVersionFormatConversion() {
        Map<String, String> versions = new HashMap<>();
        versions.put("560c", "05.06.00c");
        versions.put("560", "05.06.00");
        versions.put("560c_myPatchFile", "05.06.00c_myPatchFile");
        versions.put("560_myPatchFile", "05.06.00_myPatchFile");
        versions.put("05.06.00", "05.06.00");
        versions.put("05.06.00c", "05.06.00c");
        versions.put("346", "03.04.06");
        for (Map.Entry<String, String> entry : versions.entrySet()) {
            assertEquals(entry.getValue(), VersionUtils.convertOldVersionFormat(entry.getKey()));
        }
    }
}
