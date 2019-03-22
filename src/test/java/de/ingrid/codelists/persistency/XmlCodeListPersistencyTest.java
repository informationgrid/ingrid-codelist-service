package de.ingrid.codelists.persistency;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class XmlCodeListPersistencyTest {

    /**
     * Ignore directories or files that are not conform.
     */
    @Test
    public void read() throws IOException {
        XmlCodeListPersistency<Object> persistency = new XmlCodeListPersistency<>();
        ClassPathResource xmlReadTest = new ClassPathResource("xmlReadTest");
        persistency.setPathToXml(xmlReadTest.getFile().getPath());
        List<Object> result = persistency.read();
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
