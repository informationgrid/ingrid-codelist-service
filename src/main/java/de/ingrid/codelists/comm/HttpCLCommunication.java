/*-
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2025 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.2 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * https://joinup.ec.europa.eu/software/page/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelists.comm;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ProxySelector;

public class HttpCLCommunication implements ICodeListCommunication {

    private final static Logger log = LogManager.getLogger(HttpCLCommunication.class);

    private String requestUrl;
    private String username;
    private String password;

    public HttpCLCommunication() {
    }

    public String sendRequest(Long timestamp) {
        if (log.isDebugEnabled()) {
            log.debug("Requesting codelists from Codelist-Repository ...");
        }
        try (CloseableHttpClient client = getClient()) {
            HttpGet method = new HttpGet(requestUrl + "?lastModifiedDate=" + timestamp);
            String result;

            try (CloseableHttpResponse response = client.execute(method)) {
                int status = response.getCode();
                if (status == HttpStatus.SC_OK) {
                    result = EntityUtils.toString(response.getEntity());
                } else {
                    log.error("Could not connect to Codelist-Repository. Status: " + status);
                    return null;
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Request finished.");
            }

            return result;
        } catch (IOException e) {
            log.error("Problem handling the http-stream. Message: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Could not connect to repository!", e);
            return null;
        }
    }

    private CloseableHttpClient getClient() {
        // Create Credentials Provider
        BasicCredentialsProvider provider = new BasicCredentialsProvider();

        // Create and set credentials
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password.toCharArray());
        provider.setCredentials(new AuthScope(null, -1), credentials);

        // Proxy configuration
        SystemDefaultRoutePlanner routePlanner = createSystemProxyRoutePlanner();

        // Build HttpClient
        return HttpClients.custom()
                .setDefaultCredentialsProvider(provider)
                .setRoutePlanner(routePlanner)
                .build();
    }

    private SystemDefaultRoutePlanner createSystemProxyRoutePlanner() {
        // Use the JRE's global/system proxy configuration
        ProxySelector proxySelector = ProxySelector.getDefault();
        return new SystemDefaultRoutePlanner(proxySelector);
    }

    // Setter methods for Spring dependency injection
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
