package de.ingrid.codelists.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.thoughtworks.xstream.XStream;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;

public class CodeListUtils {

    public static String SORT_INCREMENT = "inc";
    
    public static String SORT_DECREMENT = "dec";
    
    public static List<CodeList> sortCodeList(List<CodeList> list, final String what, final String how) {
        Collections.sort(list, new Comparator<CodeList>() {

            @Override
            public int compare(CodeList o1, CodeList o2) {
                int res = 0;
                
                if ("name".equals(what)) {
                    res = o1.getName().compareTo(o2.getName());
                } else if ("id".equals(what)) {
                    res = o1.getId().compareTo(o2.getId());
                }
                return (how.equals(SORT_INCREMENT) ? res : res*-1);
            }
        });
        return list;
    }

    public static CodeList getCodeListFromJsonGeneric(String data) {
        
        try {
            return getCodeListFromObject(new JSONObject(data));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Generates a list of CodeList objects from a response which could be a
     * string in json format or a serialized xml from these objects.
     * @param data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<CodeList> getCodeListsFromResponse(String data) {
        List<CodeList> codelists = new ArrayList<CodeList>();
        
        try {
            JSONArray jsonCodelists = new JSONArray(data);
            for (int i=0; i<jsonCodelists.length(); i++) {
                codelists.add(getCodeListFromObject(jsonCodelists.getJSONObject(i)));
            }
            
        } catch (JSONException e) {
            // try to convert it from xml notation (response from InGrid communication
            try {
                XStream xs = new XStream();
                codelists = (List<CodeList>) xs.fromXML(data);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        
        return codelists;
    }

    private static CodeList getCodeListFromObject(JSONObject jsonObject) {
        CodeList cl = new CodeList();
        
        try {
            cl.setId(jsonObject.getString("id"));
            cl.setName(jsonObject.getString("name"));
            cl.setDescription(jsonObject.getString("description"));
            cl.setDefaultEntry(jsonObject.optString("defaultEntry", ""));
            cl.setLastModified(jsonObject.optLong("lastModified",-1));
            
            List<CodeListEntry> entries = new ArrayList<CodeListEntry>();
            JSONArray jsonEntriesArray = jsonObject.getJSONArray("entries");
            for (int i=0; i<jsonEntriesArray.length(); i++) {
                CodeListEntry cle = new CodeListEntry();
                JSONObject jsonEntryObject = jsonEntriesArray.getJSONObject(i);
                cle.setId(jsonEntryObject.getString("id"));
                cle.setDescription(jsonEntryObject.getString("description"));
                JSONArray jsonLocalisationsArray = jsonEntryObject.getJSONArray("localisations"); 
                for (int j=0; j<jsonLocalisationsArray.length(); j++) {
                    cle.setLocalisedEntry(
                            jsonLocalisationsArray.getJSONArray(j).getString(0),
                            jsonLocalisationsArray.getJSONArray(j).getString(1)
                    );
                }
                entries.add(cle);
            }
            
            cl.setEntries(entries);
            
        } catch (JSONException e) {
            e.printStackTrace();
            cl = null;
        }
        
        return cl;
    }

    public static String getXmlFromObject(Object obj) {
        XStream xstream = new XStream(/*new JsonHierarchicalStreamDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer writer) {
                return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
            }
        }*/);
        
        return xstream.toXML(obj);
    }
}
