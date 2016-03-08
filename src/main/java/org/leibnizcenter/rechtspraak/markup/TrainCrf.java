package org.leibnizcenter.rechtspraak.markup;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.crf.crf.CrfModel;
import org.crf.crf.CrfTags;
import org.crf.crf.CrfUtilities;
import org.crf.crf.run.CrfInferencePerformer;
import org.crf.crf.run.CrfTrainer;
import org.crf.crf.run.CrfTrainerFactory;
import org.crf.crf.run.ExampleMain;
import org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.util.Xml;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.leibnizcenter.rechtspraak.markup.RechtspraakCorpus.listXmlFiles;


/**
 * :p
 * Created by maarten on 28-2-16.
 */
public final class TrainCrf implements Runnable {
    private final static File xmlFiles = new File("B:\\rechtspraak-rich-docs\\");
    private Map<Label, Set<Label>> canFollow = new LinkedHashMap<>();
    private Map<Label, Set<Label>> canPrecede = new LinkedHashMap<>();

    public static void main(String[] a) {
        TrainCrf app = new TrainCrf();
        app.run();
    }


    @Override
    public void run() {
        // Load a corpus into the memory
        RechtspraakCorpus corpus = new RechtspraakCorpus(listXmlFiles(xmlFiles, 3000));

        // Create trainer factory
        CrfTrainerFactory<RechtspraakElement, Label> trainerFactory = new CrfTrainerFactory<>();

        // Create tags (because we know that info can't follow any other tags)
        Set<Label> tags = Sets.immutableEnumSet(Label.INFO, Label.SECTION_TITLE, Label.OUT);

        establishCanFollowRelation(null, Label.INFO);
        establishCanFollowRelation(Label.INFO, Label.INFO);
        establishCanFollowRelation(Label.INFO, Label.OUT);
        establishCanFollowRelation(Label.INFO, Label.SECTION_TITLE);
        establishCanFollowRelation(Label.SECTION_TITLE, Label.OUT);
        establishCanFollowRelation(Label.SECTION_TITLE, Label.SECTION_TITLE);
        establishCanFollowRelation(Label.OUT, Label.OUT);
        establishCanFollowRelation(Label.OUT, Label.SECTION_TITLE);

        CrfTags<Label> crfTags = new CrfTags<>(tags, canFollow, canPrecede);

        // Create trainer
        CrfTrainer<RechtspraakElement, Label> trainer = trainerFactory.createTrainer(
                corpus,
                RechtspraakFeatureGenerator::new,
                RechtspraakFilterFactory::new,
                crfTags
        );


        // Run training with the loaded corpus.
        trainer.train(corpus);

        // Get the model
        CrfModel<RechtspraakElement, Label> crfModel = trainer.getLearnedModel();

        // Save the model into the disk.
        File file = new File("example.ser");
        ExampleMain.save(crfModel, file);

        ////////

        // Later... Load the model from the disk
        // noinspection unchecked
        CrfModel<RechtspraakElement, Label> crf = (CrfModel<RechtspraakElement, Label>) ExampleMain.load(file);

        // Create a CrfInferencePerformer, to find tags for test data
        CrfInferencePerformer<RechtspraakElement, Label> inferencePerformer = new CrfInferencePerformer<>(crf);

        // Test:
        List<File> testFiles = listXmlFiles(xmlFiles, 1);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            for (File xmlFile : testFiles) {
                String ecli = xmlFile.getName().replaceAll("\\.xml$", "").replaceAll("\\.", ":");
                FileInputStream is = new FileInputStream(xmlFile);
                Document doc = builder.parse(new InputSource(new InputStreamReader(is)));

                List<RechtspraakElement> instance =
                        Lists.transform(RechtspraakTokenList.from(ecli, Xml.getContentRoot(doc)), TaggedToken::getToken);
                List<TaggedToken<RechtspraakElement, Label>> result = inferencePerformer.tagSequence(instance);

                // Print the result:
                result.stream()
                        //.filter(taggedToken -> taggedToken.getTag() != Label.INFO)
                        .forEach(taggedToken -> System.out.println(
                                "[" + taggedToken.getTag() + "] " + taggedToken.getToken().getTextContent()
                        ));
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    private void establishCanFollowRelation(Label follows, Label precedes) {
        CrfUtilities.putInMapSet(canPrecede, precedes, follows);
        CrfUtilities.putInMapSet(canFollow, follows, precedes);
    }
}
