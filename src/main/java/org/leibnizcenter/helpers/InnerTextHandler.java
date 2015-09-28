package org.leibnizcenter.helpers;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class InnerTextHandler implements DomHandler<String, StreamResult> {
    private StringWriter xmlWriter = new StringWriter();

    public StreamResult createUnmarshaller(ValidationEventHandler errorHandler) {
        return new StreamResult(xmlWriter);
    }

    public String getElement(StreamResult rt) {
        return rt.getWriter().toString();
    }

    public Source marshal(String n, ValidationEventHandler errorHandler) {
        try {
            String xml = n.trim();
            StringReader xmlReader = new StringReader(xml);
            return new StreamSource(xmlReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
