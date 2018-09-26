/*
 * **************************************************-
 * InGrid CodeList Service
 * ==================================================
 * Copyright (C) 2014 - 2018 wemove digital solutions GmbH
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


import de.ingrid.ibus.client.BusClient;
import de.ingrid.ibus.client.BusClientFactory;
import de.ingrid.utils.IngridHit;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.FieldQuery;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IngridCLCommunication implements ICodeListCommunication {
    private final static Logger log = LogManager.getLogger(IngridCLCommunication.class);

    @Override
    public String sendRequest(Long timestamp) {
        IngridHits hits = null;
        if (log.isDebugEnabled()) {
            log.debug("Sending request to Management iPlug to receive codelists ...");
        }
        try {
            String queryString = "datatype:management management_request_type:3 ranking:any cache:off";
            IngridQuery query = QueryStringParser.parse(queryString);
            query.addField(new FieldQuery(true, false, "lastModified", String.valueOf(timestamp)));
            
            //log.info("BusClient is: " + BusClientFactory.getBusClient());
            //log.info("Bus is: " + BusClientFactory.getBusClient().getNonCacheableIBus());
            // cacheable iBus needs active default cache which could be a problem if not configured correctly
            BusClient client = BusClientFactory.getBusClient();
            if (client == null) {
                client = BusClientFactory.createBusClient();
            }
            hits = client.getNonCacheableIBus().search(query, 10, 0, 10, 30000);
            if (log.isDebugEnabled()) {
                log.debug("Request finished.");
            }
        } catch (Exception e) {
            log.error("Error while requesting codelists from Management iPlug.", e);
            return null;
        }
        
        IngridHit[] hitsPart = hits.getHits();
        if (hitsPart.length == 0 || hitsPart[0].get("codelists") == null) {
            log.error("Codelists could not be fetched from Management-iPlug!");
            return null;
        }
        
        return (String) hitsPart[0].get("codelists");
    }

}
