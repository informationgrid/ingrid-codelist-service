package de.ingrid.codelists.persistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import de.ingrid.codelists.model.CodeList;

public class XmlCodeListPersistency implements ICodeListPersistency {

    private String pathToXml;
    
    public XmlCodeListPersistency() {}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        try {
            return (List<CodeList>) xStream.fromXML(new FileInputStream(pathToXml));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //throw new Exception();
        }
        return null;
    }

    @Override
    public boolean write(List<CodeList> data) {
        XStream xStream = new XStream();
        try {
            checkForFile(this.pathToXml);
            xStream.toXML(data, new FileOutputStream(this.pathToXml));
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void checkForFile(String filePath) {
        File f = new File(filePath);
        File parentDir = f.getParentFile();
        // create dir if the file is inside a subdirectory and does not exist already
        if (parentDir != null && !parentDir.exists() || !parentDir.isDirectory()) {
            parentDir.mkdir();
        }
    }

    public void setPathToXml(String pathToXml) {
        this.pathToXml = pathToXml;
    }

}
