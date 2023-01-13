/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2023 wemove digital solutions GmbH
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

import java.io.IOException;
import java.net.ProxySelector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpCLCommunication implements ICodeListCommunication {
    
    private final static Logger log = LogManager.getLogger(HttpCLCommunication.class);

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
        HttpGet method = new HttpGet(requestUrl + "?lastModifiedDate=" + timestamp);
        String result;
        int status;
        HttpResponse response;
        try {
            response = client.execute(method);
            status = response.getStatusLine().getStatusCode();
            
            if (status == HttpStatus.SC_OK) {
                result = EntityUtils.toString( response.getEntity() );
            } else {
                log.error("Could not connect to Codelist-Repository. Status: " + status); 
                return null;
            }
        } catch (IOException e) {
            log.error("Problem handling the http-stream. Message: " + e.getMessage()); 
            return null;
        } catch (Exception e) {
            log.error("Could not connect to repository! Probably wrong method call: " + method.getURI() + "?");
            return null;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Request finished.");
        }
        
        return result;
    }
    
    private HttpClient getClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        
        CredentialsProvider provider = new BasicCredentialsProvider();

        // Create the authentication scope
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);

        // Create credential pair
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);

        // Inject the credentials
        provider.setCredentials(scope, credentials);

        // Set the default credentials provider
        builder.setDefaultCredentialsProvider(provider);

        // pickup JRE wide proxy configuration
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        builder.setRoutePlanner( routePlanner );

        return builder.build();
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
