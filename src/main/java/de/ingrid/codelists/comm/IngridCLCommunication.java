package de.ingrid.codelists.comm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.ingrid.ibus.client.BusClientFactory;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;

public class IngridCLCommunication implements ICodeListCommunication {
    private final static Log log = LogFactory.getLog(IngridCLCommunication.class);

    @Override
    public String sendRequest() {
        IngridHits hits = null;
        try {
            String queryString = "datatype:management management_request_type:3 ranking:any cache:off";
            IngridQuery query = QueryStringParser.parse(queryString);
            
            //log.info("BusClient is: " + BusClientFactory.getBusClient());
            //log.info("Bus is: " + BusClientFactory.getBusClient().getNonCacheableIBus());
            // cacheable iBus needs active default cache which could be a problem if not configured correctly
            hits = BusClientFactory.getBusClient().getNonCacheableIBus().search(query, 10, 0, 10, 30000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (String) hits.getHits()[0].get("codelists");
    }

}
