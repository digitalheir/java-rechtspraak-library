package org.leibnizcenter.rechtspraak;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.gson.annotations.SerializedName;
import generated.OpenRechtspraak;
import nl.rechtspraak.psi.Procedure;
import org.joda.time.DateTime;
import org.leibnizcenter.rechtspraak.enricher.Enrich;
import org.leibnizcenter.util.Xml;
import org.leibnizcenter.xml.NotImplemented;
import org.leibnizcenter.xml.TerseJson;
import org.leibnizcenter.xml.helpers.DomHelper;
import org.purl.dc.terms.*;
import org.w3._1999._02._22_rdf_syntax_ns_.Description;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.leibnizcenter.rechtspraak.RechtspraakNlInterface.xmlToHtml;

/**
 * To serialize in JSON for use in CouchDB
 * <p>
 * Created by Maarten on 29/09/2015.
 */
@SuppressWarnings("WeakerAccess")
public class CouchDoc {
    public String _id;
    @SuppressWarnings("unused")
    public String _rev;
    @SerializedName("@context")
    public Object[] context = {
            "https://rechtspraak.cloudant.com/assets/assets/context.jsonld",
            new ContextValues()
    };
    public String corpus = "Rechtspraak.nl";
    @SerializedName("@type")
    public String _type = "frbr:LegalWork";
    public String source;
    @SerializedName("owl:sameAs")
    public String sameAs;
    public String ecli;
    @SerializedName("abstract")
    protected Abstract _abstract;
    protected String accessRights;
    protected Coverage coverage;
    protected List<String> hasVersion;
    protected List<FormeleRelatie> relation;
    protected List<String> zaaknummer;
    protected List<RechtsResource> subject;
    protected String metadataModified;
    protected String contentModified;
    protected String issued;
    protected String htmlIssued;
    protected String page;
    protected RechtsResource creator;
    protected String date;
    protected RechtsResource language;
    protected RechtsResource publisher;
    protected RechtsResource spatial;
    protected String couchDbUpdated;
    protected String about;
    protected RechtsValue title;
    protected List<RechtsResource> procedure;
    protected Periode temporal;
    protected List<String> replaces;
    protected RechtsResource type;
    protected Attachments _attachments;
    protected List<RechtsResource> references;
    protected Object xml;

    public CouchDoc(OpenRechtspraak doc, String xmlStr, Enrich enricher, DocumentBuilderFactory factory) throws TransformerException, URISyntaxException, IOException, SAXException, ParserConfigurationException, NotImplemented, IllegalAccessException, InstantiationException, ClassNotFoundException {

        //Set content json
        //RechtspraakContent content = RechtspraakNlInterface.getUitspraakOrConclusie(doc);
        //simplifiedContent = content.toJson();// remove
        xml = new TerseJson(TerseJson.Options.with(TerseJson.WhitespaceBehaviour.Preserve))
                .convert(DomHelper.parse(xmlStr));

        /**
         * ECLI:NL:CRVB:2013:1886, ECLI:NL:RBZWB:2013:901 only have 1 description tag: desc1
         */
        Description desc1;
        Description desc2 = null;
        if (doc.getRDF().getDescription().size() == 2) {
            desc1 = doc.getRDF().getDescription().get(0);
            desc2 = doc.getRDF().getDescription().get(1);
        } else {
            desc1 = doc.getRDF().getDescription().get(0);
        }

        ecli = getEcli(desc1);
        _id = ecli;

        //Set attachments
        this._attachments = new Attachments(xmlStr, enricher, factory, ecli);

        couchDbUpdated = new DateTime().toString();
        sameAs = getSameAs(desc1, desc2, ecli);

        // Source XML
        source = "http://data.rechtspraak.nl/uitspraken/content?id=" + ecli;

        // HTML page
        page = "http://rechtspraak.lawreader.nl/ecli/" + ecli;

        _abstract = getAbstract(desc1, desc2);
        accessRights = getAccessRights(desc1, desc2);
        this.coverage = getCoverage(desc1, desc2);
        this.hasVersion = getHasVersion(desc1, desc2);
        this.relation = getFormeleRelaties(desc1, desc2);
        this.zaaknummer = getZaaknummers(desc1, desc2);
        this.subject = getSubjects(desc1, desc2);

        // desc1 getModified is about metadata
        metadataModified = getModified(desc1);
        // desc2 getModified is about content
        if (desc2 != null) {
            contentModified = getModified(desc2);
        }

        //desc1 issues is about the metadata
        issued = getIssued(desc1);
        if (desc2 != null) {
            //desc2 issues is about the content
            htmlIssued = getIssued(desc2);
        }
        this.creator = getCreator(desc1, desc2);

        //date; uitspraakdatum
        date = getDate(desc1, desc2);
        language = new RechtsResource(
                desc1.getLanguage(),
                ("nl".equals(desc1.getLanguage()) ? new Label("Nederlands", "nl") : null)
        );
        this.publisher = getPublisher(desc1, desc2);
        this.spatial = getSpatial(desc1, desc2);
        this.procedure = getProcedure(desc1, desc2);
        this.about = getAbout(desc1, desc2);
        this.title = getTitle(desc1, desc2);
        this.type = getType(desc1, desc2);
        this.replaces = getReplaces(desc1, desc2);
        this.temporal = getPeriode(desc1, desc2);
        this.references = getReferences(desc1, desc2);
    }

    /**
     * Get title. (NB:
     * <a href="http://deeplink.rechtspraak.nl/uitspraak?id=ECLI:NL:CRVB:2013:BZ6112">ECLI:NL:CRVB:2013:BZ6112</a>
     * doesn't have a title.)
     */
    private static RechtsValue getTitle(Description desc1, Description desc2) {
        Preconditions.checkState(desc1.getTitle() == null);
        RechtsValue t = null;
        if (desc2 != null && desc2.getTitle() != null) {
            t = new RechtsValue(desc2.getTitle().getValue(), desc2.getTitle().getLanguage());
        }
        return t;
    }

    private static List<String> getZaaknummers(Description desc1, Description desc2) {
        List<String> zaaknummer = null;
        Preconditions.checkState(desc2 == null || desc2.getZaaknummer() == null);
        if (desc1.getZaaknummer() != null) {
            String strZaaknummer = desc1.getZaaknummer().getValue();
            if (strZaaknummer != null && strZaaknummer.trim().length() > 0) {
                String[] zknrs = strZaaknummer.split(",");
                zaaknummer = new ArrayList<>(zknrs.length);
                // A string like '97/8236 TW, 97/8241 TW' is probably two case numbers
                for (String zknr : zknrs) {
                    zaaknummer.add(zknr.trim());
                }
            }
        }
        return zaaknummer;
    }

    private static List<String> getHasVersion(Description desc1, Description desc2) {
        // only desc1 should have have hasVersion
        ArrayList<String> hasVersion = null;
        Preconditions.checkState(desc2 == null || desc2.getHasVersion() == null);
        if (desc1.getHasVersion() != null) {
            hasVersion = new ArrayList<>(desc1.getHasVersion().getList().getLi().size());
            for (String s : desc1.getHasVersion().getList().getLi()) {
                hasVersion.add(s);
            }
        }
        return hasVersion;
    }

    private static Coverage getCoverage(Description desc1, Description desc2) {
        // only desc1 should have coverage
        Coverage c = null;
        if (desc1.getCoverage() != null && desc1.getCoverage().length() > 0) {
            String coverageCode = desc1.getCoverage().trim().toLowerCase(Locale.US);

            List<Label> labels = new ArrayList<>();
            if ("nl".equals(coverageCode)) {
                labels.add(new Label("Nederland", coverageCode));
            }
            Label[] aLabels = labels.toArray(new Label[labels.size()]);
            c = new Coverage(coverageCode, aLabels);
        }
        Preconditions.checkState(desc2 == null || desc2.getCoverage() == null);
        return c;
    }

    private static String getAccessRights(Description desc1, Description desc2) {
        if (desc2 != null) {
            Preconditions.checkState(desc1.getAccessRights().equals(desc2.getAccessRights()));
        }
        return desc1.getAccessRights();
    }

    private static String getSameAs(Description desc1, Description desc2, String ecli) {
        if (desc2 != null) {
            // desc2.getIdentifier() is the deeplink URI; desc1.getIdentifier() is the ECLI
            Preconditions.checkState(desc2.getIdentifier().endsWith(desc1.getIdentifier()));
            Preconditions.checkState(desc2.getIdentifier().startsWith("http://"));
            return desc2.getIdentifier();
        } else {
            return "http://deeplink.rechtspraak.nl/uitspraak?id=" + ecli;
        }
    }

    /**
     * @see {@link Abstract}
     */
    private static Abstract getAbstract(Description desc1, Description desc2) {
        Preconditions.checkState(desc1.getAbstract() == null);
        if (desc2 != null) {
            return desc2.getAbstract();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    public static Label[] getDutchLabels(String val) {
        return getLabels(val, "nl");
    }

    public static Label[] getLabels(String value, String lang) {
        if (value != null && value.trim().length() > 0) {
            return new Label[]{
                    new Label(value.trim(), lang)
            };
        } else {
            return null;
        }
    }

    private List<RechtsResource> getReferences(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getReferences().size() == 0);

        List<RechtsResource> r = null;
        if (desc1.getReferences().size() > 0) {
            r = new ArrayList<>(desc1.getReferences().size());
            for (References ref : desc1.getReferences()) {
                String bwbId = ref.getResourceIdentifierBwb();
                String cvdrId = ref.getResourceIdentifierCvdr();

                // Exactly one of either must be set
                Preconditions.checkArgument((Objects.nonNull(bwbId) && Objects.isNull(cvdrId)) || (Objects.nonNull(cvdrId) && Objects.isNull(bwbId)));

                String identifier = bwbId == null ? cvdrId : bwbId;
                RechtsResource rr = new RechtsResource(
                        identifier,
                        getLabels(ref.getValue(), "nl")
                );
                r.add(rr);
            }
        }
        return r;
    }

    private Periode getPeriode(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getTemporal() == null);
        Periode temp = null;
        if (desc1.getTemporal() != null) {
            Temporal t = desc1.getTemporal();
            temp = new Periode(
                    t.getStart().getValue(),
                    t.getEnd().getValue()
            );
        }
        return temp;
    }

    private List<String> getReplaces(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getReplaces().size() == 0);
        List<String> replaces = null;
        if (desc1.getReplaces().size() > 0) {
            replaces = new ArrayList<>(desc1.getReplaces().size());
            for (Replaces r : desc1.getReplaces()) {
                replaces.add(r.getValue().trim());
            }
        }
        return replaces;
    }

    private RechtsResource getType(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getType() == null);

        RechtsResource type = null;
        if (desc1.getType() != null) {
            Type t = desc1.getType();
            type = new RechtsResource(
                    t.getResourceIdentifier(),
                    getLabels(t.getValue(), t.getLanguage())
            );
        }
        return type;
    }

    private String getAbout(Description desc1, Description desc2) {
        Preconditions.checkState(desc1.getAbout() == null);

        String about = null;
        if (desc2 != null) {
            Preconditions.checkState(desc2.getAbout().contains("id=ECLI"));
            about = desc2.getAbout();
        }
        return about;
    }

    private List<RechtsResource> getProcedure(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getProcedure() == null || desc2.getProcedure().size() == 0);

        List<RechtsResource> procedure = null;
        if (desc1.getProcedure() != null) {
            List<Procedure> prs = desc1.getProcedure();
            procedure = new ArrayList<>(prs.size());
            for (Procedure pr : prs) {
                RechtsResource res = new RechtsResource(
                        pr.getResourceIdentifier(),
                        pr.getValue().trim().length() > 0
                                ? new Label[]{new Label(pr.getValue().trim(), pr.getLanguage())}
                                : (new Label[]{}
                        ));
                procedure.add(res);
            }
        }
        return procedure;
    }

    private RechtsResource getSpatial(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getSpatial() == null);
        RechtsResource spatial = null;
        if (desc1.getSpatial() != null) {
            Spatial spat = desc1.getSpatial();
            try {
                spatial = new RechtsResource(
                        URLEncoder.encode(spat.getValue(), "UTF-8"),
                        new Label(spat.getValue(), "nl")
                );
            } catch (UnsupportedEncodingException e) {
                throw new Error(e);
            }
        }
        return spatial;
    }

    private RechtsResource getPublisher(Description desc1, Description desc2) {
        Publisher d1Publisher = desc1.getPublisher();
        if (desc2 != null) {
            Publisher d2Publisher = desc2.getPublisher();
            Preconditions.checkState(d1Publisher.getValue().trim().equals(d2Publisher.getValue().trim()));
            Preconditions.checkState(d1Publisher.getResourceIdentifier().trim().equals(d2Publisher.getResourceIdentifier().trim()));
        }

        return new RechtsResource(
                d1Publisher.getResourceIdentifier(),
                new Label(
                        d1Publisher.getValue(), "nl"
                )
        );
    }

    private String getDate(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getDate() == null);
        return new DateTime(desc1.getDate().getValue().toGregorianCalendar()).toString();
    }

    private Instantie getCreator(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getCreator() == null);
        return new Instantie(desc1.getCreator());
    }

    private String getIssued(Description desc1) {
        return new DateTime(desc1.getIssued().getValue().toGregorianCalendar()).toString();
    }

    private String getModified(Description description) {
        return new DateTime(description.getModified().toGregorianCalendar()).toString();
    }

    private List<RechtsResource> getSubjects(Description desc1, Description desc2) {
        List<RechtsResource> subject = null;
        Preconditions.checkState(desc2 == null || desc2.getSubject().size() == 0);
        if (desc1.getSubject().size() > 0) {
            subject = new ArrayList<>(desc1.getSubject().size());
            for (Subject s : desc1.getSubject()) {
                Preconditions.checkState(s.getResourceIdentifier().startsWith("http"));
                subject.add(
                        new RechtsResource(
                                s.getResourceIdentifier(),
                                getLabels(s.getValue(), "nl")
                        )
                );
            }
        }
        return subject;
    }

    private List<FormeleRelatie> getFormeleRelaties(Description desc1, Description desc2) {
        Preconditions.checkState(desc2 == null || desc2.getRelation().size() == 0);
        List<FormeleRelatie> relation = null;
        if (desc1.getRelation().size() > 0) {
            relation = new ArrayList<>(desc1.getRelation().size());
            for (Relation r : desc1.getRelation()) {
                String otherEcli = r.getResourceIdentifier(),
                        type = r.getType(),
                        aanleg = r.getAanleg();
                Preconditions.checkState(otherEcli.startsWith("ECLI"));
                Preconditions.checkState(type != null);
                Preconditions.checkState(aanleg != null);
                Preconditions.checkState(!otherEcli.equals(aanleg));

                relation.add(new FormeleRelatie(
                        otherEcli,
                        type, aanleg,
                        FormeleRelatie.getLabels(r)
                ));
            }
        }
        return relation;
    }

    private String getEcli(Description desc1) {
        String ecli = desc1.getIdentifier();
        Preconditions.checkState(ecli.startsWith("ECLI"));
        return ecli;
    }

    static class Label {
        @SerializedName("@value")
        String value;

        @SerializedName("@language")
        String language;

        Label(String value, String language) {
            this.value = value;
            this.language = language;
        }
    }

    static class Coverage {
        @SerializedName("rdfs:label")
        Label label[];
        @SerializedName("@id")
        private String id;

        Coverage(String id, Label... labels) {
            label = labels;
            this.id = id;
            Preconditions.checkNotNull(this.id);
        }
    }

    @SuppressWarnings({"unused"})
    static class ContextValues {
        String Vervangt = "http://purl.org/dc/terms/replaces",
                Procedure = "http://psi.rechtspraak.nl/procedure",
                Instantie = "http://purl.org/dc/terms/creator",
                Zaaknr = "http://psi.rechtspraak.nl/zaaknummer";

        ContextValues() {
        }
    }

    static class FormeleRelatie {
        @SerializedName("rdfs:label")
        Label[] label;
        @SerializedName("@id")
        String id;
        String aanleg;
        String type;

        FormeleRelatie(String ecli, String type, String aanleg, Label... labels) {
            Preconditions.checkNotNull(ecli);
            Preconditions.checkNotNull(type);
            Preconditions.checkNotNull(aanleg);
            label = labels;
            id = ecli;
            this.type = type;
            this.aanleg = aanleg;
        }


        public static Label[] getLabels(Relation r) {
            if (r.getValue() != null) {
                return new Label[]{
                        new Label(r.getValue(), "nl")
                };
            } else {
                return new Label[0];
            }
        }
    }

    public static class RechtsValue {
        @SerializedName("@value")
        String value;

        @SerializedName("@language")
        String language;

        @SuppressWarnings("unused")
        public RechtsValue(String value) {
            this.value = value;
        }

        public RechtsValue(String value, String language) {
            this.value = value;
            this.language
                    = language;

        }
    }

    public static class RechtsResource {
        @SerializedName("rdfs:label")
        Label[] label;
        @SerializedName("@id")
        String id;

        RechtsResource(String id, Label... labels) {
            Preconditions.checkNotNull(id);
            this.id = id;
            label = labels;
        }
    }

    public static class Periode {
        String startDate;
        String endDate;

        Periode(String startDate,
                String endDate) {
            Preconditions.checkNotNull(startDate);
            Preconditions.checkNotNull(endDate);
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    static class Attachments {
        @SerializedName("data.xml")
        Attachment xml;
        @SerializedName("data.htm")
        Attachment htm;

        public Attachments(String xmlStr, Enrich enricher, DocumentBuilderFactory factory, String ecli) throws TransformerException, URISyntaxException, IllegalAccessException, ClassNotFoundException, InstantiationException, IOException, SAXException, ParserConfigurationException {
            this.xml = new Attachment(xmlStr, "text/xml;charset=utf-8");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            if (!Xml.containsTag(Xml.getContentRoot(doc), "section")) {
                System.out.println(ecli);
                enricher.enrich(ecli, doc);
                xmlStr = Xml.toString(doc);
            }

            this.htm = new Attachment(xmlToHtml(xmlStr), "text/html;charset=utf-8");
        }

        class Attachment {
            String content_type;
            String data;

            Attachment(String data, String contentType) {
                this.data = BaseEncoding.base64().encode(data.getBytes());
                this.content_type = contentType;
            }
        }
    }

    private class Instantie extends RechtsResource {
        String scheme;

        public Instantie(Creator cr) {
            super(
                    (cr.getResourceIdentifier() == null ? cr.getPsiResourceIdentifier().trim() : cr.getResourceIdentifier().trim())
                    , new Label(cr.getValue().trim(), "nl")
            );
            String crResId = cr.getResourceIdentifier();
            String crPsiResId = cr.getPsiResourceIdentifier();
            Preconditions.checkState((crResId == null && crPsiResId != null) ||
                    (crResId != null && crPsiResId == null));
            this.scheme = cr.getScheme().trim();
        }
    }
}