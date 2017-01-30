/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2017 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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
    public String sendRequest(Long timestamp) {
        if (log.isDebugEnabled()) {
            log.debug("Requesting codelists from Codelist-Repository ...");
        }
        HttpClient client = getClient();
        HttpMethod method = new GetMethod(requestUrl + "?lastModifiedDate=" + timestamp);
//        method.s
        String result = "";
        int status = -1;
        try {
            status = client.executeMethod(method);
            
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
                String inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    result = result.concat(inputLine);
                }
                in.close();
            } else {
                log.error("Could not connect to Codelist-Repository. Status: " + status); 
                return null;
            }
        } catch (HttpException e) {
            log.error("Problem when accessing url: " + requestUrl + " (Status Code: " + status + ") Message: " + e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("Problem handling the http-stream. Message: " + e.getMessage()); 
            return null;
        } catch (Exception e) {
            log.error("Could not connect to repository! Probably wrong method call: " + method.getPath() + "?" + method.getQueryString());
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
