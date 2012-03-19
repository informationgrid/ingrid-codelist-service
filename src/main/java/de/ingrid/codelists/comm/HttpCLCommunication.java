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
import org.apache.log4j.Logger;

public class HttpCLCommunication implements ICodeListCommunication {
    
    private final static Logger log = Logger.getLogger(HttpCLCommunication.class);

    // injected by Spring
    private String requestUrl;
    
    // injected by Spring
    private String username;
    
    // injected by Spring
    private String password;
    
    public HttpCLCommunication() {}
    
    @Override
    public String sendRequest() {
        if (log.isDebugEnabled()) {
            log.debug("Requesting codelists from CodeList-Repository ...");
        }
        HttpClient client = getClient();
        HttpMethod method = new GetMethod(requestUrl);
        String result = "";
        int status = -1;
        try {
            status = client.executeMethod(method);
            
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
            log.error("Problem when accessing url: " + requestUrl + " (Status Code: " + status + ") Message: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            log.error("Problem handling the http-stream. Message: " + e.getMessage()); 
            e.printStackTrace();
            return null;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Request finished.");
        }
        
        return result;
    }
    
    private HttpClient getClient() {
        HttpClient client = new HttpClient();
        
        Credentials defaultcreds = new UsernamePasswordCredentials(this.username, this.password);
        //client.getState().setCredentials(new AuthScope("localhost", 80, AuthScope.ANY_REALM), defaultcreds);
        client.getState().setCredentials(AuthScope.ANY, defaultcreds);
        return client;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

}
