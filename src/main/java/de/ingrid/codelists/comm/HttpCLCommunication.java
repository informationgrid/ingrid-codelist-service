package de.ingrid.codelists.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpCLCommunication implements ICodeListCommunication {

    private String requestUrl;
    
    public HttpCLCommunication() {}
    
    @Override
    public String sendRequest() {
        HttpClient client = getClient();
        HttpMethod method = new GetMethod(requestUrl);
        String result = "";
        try {
            int status = client.executeMethod(method);
            
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    result = result.concat(inputLine);
                }
                in.close();
            } else {
                (new Exception("Http Status Code was: " + status)).printStackTrace();
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
        //List<CodeList> codelists = CodeListUtils.getCodeListsFromJsonGeneric(result);
        // TODO Auto-generated method stub
        return result;
    }
    
    private HttpClient getClient() {
        HttpClient client = new HttpClient();//httpClientParams, httpConnectionManager);
        
        Credentials defaultcreds = new UsernamePasswordCredentials("ingrid", "ingrid");
        //client.getState().setCredentials(new AuthScope("localhost", 80, AuthScope.ANY_REALM), defaultcreds);
        client.getState().setCredentials(AuthScope.ANY, defaultcreds);
        return client;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

}
