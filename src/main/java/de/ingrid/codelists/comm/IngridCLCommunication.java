package de.ingrid.codelists.comm;

import de.ingrid.ibus.client.BusClientFactory;
import de.ingrid.utils.IngridHits;
import de.ingrid.utils.query.IngridQuery;
import de.ingrid.utils.queryparser.QueryStringParser;

public class IngridCLCommunication implements ICodeListCommunication {

    @Override
    public String sendRequest() {
        IngridHits hits = null;
        try {
            String queryString = "datatype:management management_request_type:3 ranking:any cache:off";
            IngridQuery query = QueryStringParser.parse(queryString);
            // cacheable iBus needs active default cache which could be a problem if not configured correctly
            hits = BusClientFactory.getBusClient().getNonCacheableIBus().search(query, 10, 0, 10, 30000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
