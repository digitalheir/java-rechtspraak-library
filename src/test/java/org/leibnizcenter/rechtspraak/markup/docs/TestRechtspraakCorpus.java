package org.leibnizcenter.rechtspraak.markup.docs;

import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.TrainWithMallet;
import org.leibnizcenter.rechtspraak.features.info.InfoPatterns;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.leibnizcenter.rechtspraak.util.numbering.ArabicSectionNumber;
import org.leibnizcenter.rechtspraak.util.numbering.NumberingNumber;
import org.leibnizcenter.rechtspraak.util.numbering.SubSectionNumber;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Tests for correct feature vector sequence initialization
 * Created by maarten on 13-3-16.
 */
public class TestRechtspraakCorpus {

    @Test
    public void main() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Collection<List<RechtspraakToken>> corpus = new HashSet<>(2);
        List<RechtspraakToken> richDoc = getTokenList(builder, "ECLI:NL:CBB:2013:345");
        // TODO assert stuff
//        Assert.assertTrue();richDoc
        corpus.add(richDoc);

        RechtspraakTokenList paraDoc = getTokenList(builder, "ECLI:NL:RVS:2015:3394");

        corpus.add(paraDoc);
        // TODO assert stuff
        for (RechtspraakToken token : paraDoc) {
            Assert.assertEquals(token.getTag(), Label.OUT);
        }

        // Test the features that our tokens are assigned
        Instance instance = TrainWithMallet.getInstance(paraDoc, true);
        TokenSequence ts = (TokenSequence) instance.getData();

        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(ts.get(i).getFeatureValue("WITHIN_FIRST_5_BLOCKS"), 1.0, 0.001);
        }
        // para: 201404465/1/R3.

        Assert.assertEquals(ts.get(0).getFeatureValue("IS_ALL_CAPS"), 1.0, 0.001);
        Assert.assertEquals(ts.get(0).getFeatureValue("CONTAINS_LIKELY_ZAAKNR"), 1.0, 0.001);
        //Assert.assertEquals(ts.get(0).getFeatureValue("LESS_THAN_5_WORDS"),1.0,0.001);
        Assert.assertEquals(ts.get(0).getFeatureValue("FIRST_QUARTILE"), 1.0, 0.001);


        Assert.assertTrue(InfoPatterns.InfoPatternsNormalizedContains.START_W_DATUM.matches(paraDoc.get(1).getToken().normalizedText));
        Assert.assertTrue(InfoPatterns.InfoPatternsNormalizedContains.START_W_AFDELING.matches(paraDoc.get(2).getToken().normalizedText));


        Assert.assertTrue(TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.matches(paraDoc.get(29).getToken().normalizedText));
        for (int i = 6; i <= 15; i++) {
            Assert.assertEquals(ts.get(i).getFeatureValue(InfoPatterns.InfoPatternsUnormalizedContains.CONTAINS_BRACKETED_TEXT.name()), 1.0, 0.001);
        }
        // TODO is part of list
        NumberingNumber numbering = paraDoc.get(5).getToken().numbering;
        Assert.assertEquals(numbering.getTerminal(), ".");
        Assert.assertEquals(numbering.mainNum(), 1);
        Assert.assertTrue((InfoPatterns.InfoPatternsNormalizedMatches.EN_VS_CONTRA.matches(paraDoc.get(26).getToken().normalizedText)));
        Assert.assertTrue((InfoPatterns.InfoPatternsNormalizedContains.END_W_ROLE.matches(paraDoc.get(28).getToken().normalizedText)));
        Assert.assertTrue(TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS.matches(paraDoc.get(29).getToken().normalizedText));
        Assert.assertTrue(TitlePatterns.TitlesNormalizedMatchesHighConf.CONSIDERATIONS.matches(paraDoc.get(36).getToken().normalizedText));

        numbering = paraDoc.get(42).getToken().numbering;
        Assert.assertTrue(numbering instanceof SubSectionNumber);
        Assert.assertTrue(numbering.equals(new SubSectionNumber(
                Lists.newArrayList(new ArabicSectionNumber(2), new ArabicSectionNumber(2)),
                ".")
        ));
        Assert.assertFalse(numbering.equals(new SubSectionNumber(
                        Lists.newArrayList(new ArabicSectionNumber(2), new ArabicSectionNumber(2)), ""
                )
        ));

        RechtspraakCorpus rsCorpus = new RechtspraakCorpus(corpus);
        Assert.assertTrue(rsCorpus.size() > 0);
    }

    public RechtspraakTokenList getTokenList(DocumentBuilder builder, String ecli) throws SAXException, IOException {
        InputStream is = TestRechtspraakCorpus.class.getResourceAsStream("/docs/" + ecli.replaceAll(":", ".") + ".xml");
        Document doc = builder.parse(new InputSource(new InputStreamReader(is)));
        return RechtspraakTokenList.from(ecli, doc, Xml.getContentRoot(doc));
    }
}
