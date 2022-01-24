package de.ingrid.codelists.util;

import de.ingrid.codelists.util.VersionUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class VersionUtilsTest {

    @Test
    public final void testVersionFormatConversion() {
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
