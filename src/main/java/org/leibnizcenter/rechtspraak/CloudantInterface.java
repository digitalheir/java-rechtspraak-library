package org.leibnizcenter.rechtspraak;

/**
 * Created by maarten on 28-9-15.
 */
public class CloudantInterface extends RechtspraakNlInterface{


    public static String getXmlUrl(String ecli) {
        return "http://rechtspraak.cloudant.com/ecli/"+ecli+"/data.xml";
    }
}
