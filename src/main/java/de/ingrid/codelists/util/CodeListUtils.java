package de.ingrid.codelists.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
    
    public static List<CodeList> getCodeListsFromJsonGeneric(String data) {
        List<CodeList> codelists = new ArrayList<CodeList>();
        
        try {
            JSONArray jsonCodelists = new JSONArray(data);
            for (int i=0; i<jsonCodelists.length(); i++) {
                codelists.add(getCodeListFromObject(jsonCodelists.getJSONObject(i)));
            }
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
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
            cl.setLastModified(jsonObject.getLong("lastModified"));
            
            List<CodeListEntry> entries = new ArrayList<CodeListEntry>();
            JSONArray jsonEntriesArray = jsonObject.getJSONArray("entries");
            for (int i=0; i<jsonEntriesArray.length(); i++) {
                CodeListEntry cle = new CodeListEntry();
                JSONObject jsonEntryObject = jsonEntriesArray.getJSONObject(i);
                cle.setId(jsonEntryObject.getString("id"));
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
}
