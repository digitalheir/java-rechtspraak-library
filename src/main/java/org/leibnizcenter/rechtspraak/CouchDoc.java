package org.leibnizcenter.rechtspraak;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import generated.OpenRechtspraak;
import nl.rechtspraak.psi.Procedure;
import nl.rechtspraak.schema.rechtspraak_1.RechtspraakContent;
import org.joda.time.DateTime;
import org.purl.dc.terms.*;
import org.w3._1999._02._22_rdf_syntax_ns_.Description;

import javax.xml.transform.TransformerException;
import java.io.NotSerializableException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.leibnizcenter.rechtspraak.RechtspraakNlInterface.xmlToHtml;

/**
 * Created by Maarten on 29/09/2015.
 */
public class CouchDoc {
    public String _id;

    @SerializedName("owl:sameAs")
    private String sameAs;
    @SerializedName("@context")
    public Object[] context = {
            "https://rechtspraak.cloudant.com/assets/assets/context.jsonld",
            new ContextValues()
    };
    private String ecli;
    public String corpus = "Rechtspraak.nl";
    @SerializedName("@type")
    public String _type = "frbr:LegalWork";
    //        dcterms:source
    public String source;
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
    protected List<Procedure> procedure;
    protected Periode temporal;
    protected List<String> replaces;
    protected RechtsResource type;
    protected JsonElement simplifiedContent;
    protected Attachments _attachments;

    public CouchDoc(OpenRechtspraak doc, String xmlStr) throws TransformerException, URISyntaxException, NotSerializableException {
        //Set attachments
        this._attachments = new Attachments(xmlStr);

        //Set content json
        RechtspraakContent content = RechtspraakNlInterface.getUitspraakOrConclusie(doc);
        simplifiedContent = content.toJson();


        // Set metadata
        Description desc1 = doc.getRDF().getDescription().get(0);
        Description desc2 = doc.getRDF().getDescription().get(1);

        // desc2.getIdentifier() is the deeplink URI; desc1.getIdentifier() is the ECLI
        assert desc2.getIdentifier().endsWith(desc1.getIdentifier());
        assert desc2.getIdentifier().startsWith("http://");
        ecli = desc1.getIdentifier();
        sameAs = desc2.getIdentifier();
        assert ecli.startsWith("ECLI");
        _id = ecli;
        // Source XML
        source = "http://data.rechtspraak.nl/uitspraken/content?id=" + ecli;
        desc1.getAccessRights();

        // HTML page
        page = "http://rechtspraak.lawreader.nl/ecli/" + ecli;

        /**
         * @see {@link Abstract}
         */
        assert desc1.getAbstract() == null;
        _abstract = desc2.getAbstract();


        assert desc1.getAccessRights().equals(desc2.getAccessRights());
        accessRights = desc1.getAccessRights();

        // only desc1 should have coverage
        if (desc1.getCoverage() != null && desc1.getCoverage().length() > 0) {
            String coverageCode = desc1.getCoverage().trim().toLowerCase(Locale.US);

            List<Label> labels = new ArrayList<>();
            if ("nl".equals(coverageCode)) {
                labels.add(new Label("Nederland", coverageCode));
            }
            Label[] aLabels = labels.toArray(new Label[labels.size()]);
            this.coverage = new Coverage(coverageCode, aLabels);
        }
        assert desc2.getCoverage() == null;

        // only desc1 should have have hasVersion
        assert desc2.getHasVersion() == null;
        if (desc1.getHasVersion() != null) {
            hasVersion = new ArrayList<>(desc1.getHasVersion().getList().getLi().size());
            for (String s : desc1.getHasVersion().getList().getLi()) {
                hasVersion.add(s);
            }
        }

        // relation
        Preconditions.checkState(desc2.getRelation().size() == 0);
        if (desc1.getRelation().size() > 0) {
            relation = new ArrayList<>(desc1.getRelation().size());
            for (Relation r : desc1.getRelation()) {
                String otherEcli = r.getResourceIdentifier(),
                        type = r.getType(),
                        aanleg = r.getAanleg();
                assert otherEcli.startsWith("ECLI");
                assert type != null;
                assert aanleg != null;
                assert !otherEcli.equals(aanleg);

                relation.add(new FormeleRelatie(
                        otherEcli,
                        type, aanleg,
                        FormeleRelatie.getLabels(r)
                ));
            }
        }

        //zaaknummer
        assert desc2.getZaaknummer() == null;
        String strZaaknummer = desc1.getZaaknummer().getValue();
        if (strZaaknummer != null && strZaaknummer.trim().length() > 0) {
            String[] zknrs = strZaaknummer.split(",");
            zaaknummer = new ArrayList<>(zknrs.length);
            // A string like '97/8236 TW, 97/8241 TW' is probably two case numbers
            for (String zknr : zknrs) {
                zaaknummer.add(zknr.trim());
            }
        }

        //subject
        Preconditions.checkState(desc2.getSubject().size() == 0);
        if (desc1.getSubject().size() > 0) {
            subject = new ArrayList<>(desc1.getSubject().size());
            for (Subject s : desc1.getSubject()) {
                assert s.getResourceIdentifier().startsWith("http");
                subject.add(
                        new RechtsResource(
                                s.getResourceIdentifier(),
                                getLabels(s.getValue(), "nl")
                        )
                );
            }
        }

        // desc1 getModified is about metadata
        metadataModified = new DateTime(desc1.getModified().toGregorianCalendar()).toString();
        // desc2 getModified is about content
        contentModified = new DateTime(desc2.getModified().toGregorianCalendar()).toString();

        //desc1 issues is about the metadata
        issued = new DateTime(desc1.getIssued().getValue().toGregorianCalendar()).toString();
        //desc2 issues is about the content
        htmlIssued = new DateTime(desc2.getIssued().getValue().toGregorianCalendar()).toString();

        couchDbUpdated = new DateTime().toString();

        //creator
        assert desc2.getCreator() == null;
        creator = new Instantie(desc1.getCreator());

        //date; uitspraakdatum
        assert desc2.getDate() == null;
        date = new DateTime(desc1.getDate().getValue().toGregorianCalendar()).toString();

        language = new RechtsResource(
                desc1.getLanguage(),
                ("nl".equals(desc1.getLanguage()) ? new Label("Nederlands", "nl") : null)
        );

        Publisher d1Publisher = desc1.getPublisher();
        Publisher d2Publisher = desc2.getPublisher();
        assert d1Publisher.getValue().trim().equals(d2Publisher.getValue().trim());
        assert d1Publisher.getResourceIdentifier().trim().equals(d2Publisher.getResourceIdentifier().trim());

        publisher = new RechtsResource(
                d1Publisher.getResourceIdentifier(),
                new Label(
                        d1Publisher.getValue(), "nl"
                )
        );

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


        //procedure
        Preconditions.checkState(desc2.getProcedure().size() == 0);
        if (desc1.getProcedure() != null) {
            List<Procedure> prs = desc1.getProcedure();
            this.procedure = new ArrayList<>(prs.size());
            for (Procedure pr : prs) {
                new RechtsResource(
                        pr.getResourceIdentifier(),
                        pr.getValue().trim().length() > 0
                                ? new Label[]{new Label(pr.getValue().trim(), pr.getLanguage())}
                                : (new Label[]{}
                        ));
            }
        }

        // about
        assert desc1.getAbout() == null;
        assert desc2.getAbout().contains("id=ECLI");
        about = desc2.getAbout();


        // title
        assert desc1.getTitle() == null;
        title = new RechtsValue(desc2.getTitle().getValue(), desc2.getTitle().getLanguage());

        //type
        assert desc2.getType() == null;
        if (desc1.getType() != null) {
            Type t = desc1.getType();
            Label lbls;
            type = new RechtsResource(
                    t.getResourceIdentifier(),
                    getLabels(t.getValue(), t.getLanguage())
            );
        }

        //replaces
        Preconditions.checkState(desc2.getReplaces().size() == 0);

        if (desc1.getReplaces().size() > 0) {
            replaces = new ArrayList<>(desc1.getReplaces().size());
            for (Replaces r : desc1.getReplaces()) {
                replaces.add(r.getValue().trim());
            }
        }

        //temporal
        assert desc2.getTemporal() == null;
        if (desc1.getTemporal() != null) {
            Temporal t = desc1.getTemporal();
            temporal = new Periode(
                    t.getStart().getValue(),
                    t.getEnd().getValue()
            );
        }

        //references
    }


    protected List<References> references;

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
        @SerializedName("@id")
        private String id;

        @SerializedName("rdfs:label")
        Label label[];

        Coverage(String id, Label... labels) {
            label = labels;
            this.id = id;
        }
    }

    @SuppressWarnings("unused")
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

        @SerializedName("@value")
        String language;

        RechtsValue(String value) {
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
            this.id = id;
            label = labels;
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
            assert (crResId == null && crPsiResId != null) ||
                    (crResId != null && crPsiResId == null);
            this.scheme = cr.getScheme().trim();
        }
    }

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

    public static class Periode {
        String startDate;
        String endDate;

        Periode(String startDate,
                String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    static class Attachments {
        @SerializedName("data.xml")
        Attachment xml;
        @SerializedName("data.htm")
        Attachment htm;

        public Attachments(String xmlStr) throws TransformerException, URISyntaxException {
            this.xml = new Attachment(xmlStr, "text/xml;charset=utf-8");
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
}