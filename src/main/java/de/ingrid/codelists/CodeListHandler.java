package de.ingrid.codelists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.util.CodeListUtils;

public class CodeListHandler {
    
    public void configure() {
        // initialize for db/xml access
    }

    public List<CodeList> fetchFromServer(String url, String lastModified) {
        return fetchFromServer(url, null, null, lastModified);
    }
    
    public List<CodeList> fetchFromServer(String url, String username, String password, String lastModified) {
        HttpClient client = getClient();
        HttpMethod method = new GetMethod(url);
        String result = "";
        try {
            int status = client.executeMethod(method);
            
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    result=result.concat(inputLine);
                }
                in.close();
            }
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
        // convert JSON to JAVA Object
        List<CodeList> codelists = CodeListUtils.getCodeListsFromJsonGeneric(result);
        
        return codelists;
    }
    
    public List<CodeList> readAll() {
        return null;
    }
    
    public CodeList read(String id) {
        return null;
    }
    
    public boolean write(CodeList codeList) {
        return false;
    }
    
    private HttpClient getClient() {
        HttpClient client = new HttpClient();//httpClientParams, httpConnectionManager);
        
        Credentials defaultcreds = new UsernamePasswordCredentials("ingrid", "ingrid");
        //client.getState().setCredentials(new AuthScope("localhost", 80, AuthScope.ANY_REALM), defaultcreds);
        client.getState().setCredentials(AuthScope.ANY, defaultcreds);
        return client;
    }
}
