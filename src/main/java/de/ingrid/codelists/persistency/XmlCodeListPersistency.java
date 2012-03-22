package de.ingrid.codelists.persistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

import de.ingrid.codelists.model.CodeList;

public class XmlCodeListPersistency implements ICodeListPersistency {

    private String pathToXml;
    
    public XmlCodeListPersistency() {}
    
    @SuppressWarnings("unchecked")
    @Override
    public List<CodeList> read() {
        XStream xStream = new XStream();
        try {
            checkForFile(this.pathToXml);
            return (List<CodeList>) xStream.fromXML(new FileInputStream(this.pathToXml));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //throw new Exception();
        } catch (StreamException e) {
            return new ArrayList<CodeList>();
        }
        return null;
    }

    @Override
    public boolean write(List<CodeList> data) {
        XStream xStream = new XStream();
        try {
            checkForFile(this.pathToXml);
            
            FileOutputStream fos = new FileOutputStream(this.pathToXml);
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            xStream.toXML(data, writer);
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void checkForFile(String filePath) {
        File f = new File(filePath);
        File parentDir = f.getParentFile();
        // create dir if the file is inside a subdirectory and does not exist already
        if (parentDir != null && (!parentDir.exists() || !parentDir.isDirectory())) {
            parentDir.mkdir();
        }
        // check for the file now!
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void setPathToXml(String pathToXml) {
        this.pathToXml = pathToXml;
    }


    @Override
    public boolean writePartial(List<CodeList> codelists) {
        return false;
    }

    
    @Override
    public boolean canDoPartialUpdates() {
        return false;
    }

}
