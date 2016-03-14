package org.leibnizcenter.rechtspraak;

import cc.mallet.fst.CRF;
import cc.mallet.fst.SimpleTagger;
import cc.mallet.fst.TokenAccuracyEvaluator;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.TokenSequence2FeatureVectorSequence;
import cc.mallet.types.*;
import cc.mallet.util.CommandOption;
import org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.markup.*;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.features.IsPartOfList;
import org.leibnizcenter.rechtspraak.markup.features.patterns.Patterns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;

import static org.leibnizcenter.rechtspraak.markup.RechtspraakCorpus.listXmlFiles;

/**
 * Created by maarten on 11-3-16.
 */
public class TrainWithMallet {
    private final static File xmlFiles = new File(Const.PATH_TRAIN_TEST_XML_FILES_LINUX);
    private static Alphabet dataAlphabet = new Alphabet();
    private static Alphabet labelAlphabet = new LabelAlphabet();
    public static final Pipe pipe = new TokenSequence2FeatureVectorSequence(dataAlphabet, true, false);
    private static Random r = new Random(69L);

    private static final int MAX_DOCS = -1;

    public static void main(String[] args) {
        // Load a corpus
        InstanceList il = new InstanceList(pipe);
        List<File> xmlFiles = listXmlFiles(TrainWithMallet.xmlFiles, MAX_DOCS, false);
        for (RechtspraakTokenList doc : new RechtspraakTokenList.FileIterable(xmlFiles)) {
            Instance instance = getInstance(doc, false);
            il.addThruPipe(instance);
        }


        InstanceList[] trainingLists =
                il.split(
                        r, new double[]{0.7,
                                1.0 - 0.7});
        InstanceList trainingData = trainingLists[0];
        InstanceList testData = trainingLists[1];
        TokenAccuracyEvaluator eval = new TokenAccuracyEvaluator(
                new InstanceList[]{trainingData, testData}, new String[]{"Training", "Testing"}
        );

        CRF crf = constructCrf(dataAlphabet, labelAlphabet);

        SimpleTagger.train(trainingData, testData, eval, ordersOption.value, defaultOption.value,
                forbiddenOption.value, allowedOption.value,
                connectedOption.value, iterationsOption.value,
                gaussianVarianceOption.value, crf);

        saveToFile(crf, new File(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF));
    }

    public static Instance getInstance(RechtspraakTokenList doc, boolean preserveInfo) {
        TokenSequence ts = getTokenSequence(doc, preserveInfo);
        LabelSequence ls = getLabelSequence(doc);
        return new Instance(ts, ls, null, null);
    }

    public static TokenSequence getTokenSequence(RechtspraakTokenList doc, boolean preserveInfo) {
        TokenSequence ts = new TokenSequence(doc.size());
        for (int i = 0; i < doc.size(); i++) {
            TaggedToken<RechtspraakElement, Label> taggedToken = doc.get(i);
            RechtspraakElement token = taggedToken.getToken();
            Token t = new Token(preserveInfo ? token.getTextContent().trim() : null);
            setFeatureValues(doc, i, t);
            if (preserveInfo) t.setProperty(Const.RECHTSPRAAK_TOKEN, token);
            ts.add(t);
        }
        return ts;
    }

    public static LabelSequence getLabelSequence(RechtspraakTokenList doc) {
        LabelSequence ls = new LabelSequence(labelAlphabet);
        for (TaggedToken<RechtspraakElement, Label> taggedToken : doc) {
            ls.add(taggedToken.getTag().toString());
        }
        return ls;
    }

    public static void setFeatureValues(RechtspraakTokenList sequence, int indexInSequence, Token t) {
        RechtspraakElement token = sequence.get(indexInSequence).getToken();
        for (Patterns.OnNormalizedText p : Patterns.OnNormalizedText.values()) {
            if (Patterns.matches(p, token)) {
                t.setFeatureValue(p.toString(), 1.0);
            }
        }
        for (Patterns.OnUnnormalizedText p : Patterns.OnUnnormalizedText.values()) {
            if (Patterns.matches(p, token)) {
                t.setFeatureValue(p.toString(), 1.0);
            }
        }
        if (token.numbering != null) t.setFeatureValue("HAS_NUMBERING", 1.0);
        if (IsPartOfList.isPartOfList(sequence, indexInSequence)) t.setFeatureValue("LIKELY_PART_OF_LIST", 1.0);
        if (token.isSpaced) t.setFeatureValue("IS_SPACED", 1.0);
        if (token.isAllCaps) t.setFeatureValue("IS_ALL_gitCAPS", 1.0);
        if (token.wordCount < 10) t.setFeatureValue("LESS_THAN_10_WORDS", 1.0);
        if (token.wordCount < 5) t.setFeatureValue("LESS_THAN_5_WORDS", 1.0);

        // TODO contains name
    }

    private static CRF constructCrf(Alphabet inputAlphabet, Alphabet outputAlphabet) {
        CRF crf = new CRF(inputAlphabet, outputAlphabet);

        // construct the finite state machine
        crf.addState(Label.INFO.toString(), new String[]{
                Label.SECTION_TITLE.toString(),
                Label.OUT.toString(),
                Label.INFO.toString()
        });
        crf.addState(Label.SECTION_TITLE.toString(), new String[]{
                Label.SECTION_TITLE.toString(),// Should not happen too often
                Label.INFO.toString(),// Should not happen too often
                Label.OUT.toString()
        });
        crf.addState(Label.OUT.toString(), new String[]{
                Label.INFO.toString(),// Should not happen too often
                Label.SECTION_TITLE.toString(),
                Label.OUT.toString()
        });

        //    CRFOptimizableBy* objects (terms in the objective function)
        //    CRF trainer
        //    evaluator and writer

        return crf;
    }

    private static void saveToFile(CRF crf, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(crf);
            fos.close();
            System.out.println("Written CRF to " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final CommandOption.Double gaussianVarianceOption = new CommandOption.Double
            (SimpleTagger.class, "gaussian-variance", "DECIMAL", true, 10.0,
                    "The gaussian prior variance used for training.", null);

    private static final CommandOption.Boolean trainOption = new CommandOption.Boolean
            (SimpleTagger.class, "train", "true|false", true, false,
                    "Whether to train", null);

    private static final CommandOption.String testOption = new CommandOption.String
            (SimpleTagger.class, "test", "lab or seg=start-1.continue-1,...,start-n.continue-n",
                    true, null,
                    "Test measuring labeling or segmentation (start-i, continue-i) accuracy", null);

    private static final CommandOption.File modelOption = new CommandOption.File
            (SimpleTagger.class, "model-file", "FILENAME", true, null,
                    "The filename for reading (train/run) or saving (train) the model.", null);

    private static final CommandOption.Double trainingFractionOption = new CommandOption.Double
            (SimpleTagger.class, "training-proportion", "DECIMAL", true, 0.5,
                    "Fraction of data to use for training in a random split.", null);

    private static final CommandOption.Integer randomSeedOption = new CommandOption.Integer
            (SimpleTagger.class, "random-seed", "INTEGER", true, 0,
                    "The random seed for randomly selecting a proportion of the instance list for training", null);

    private static final CommandOption.IntegerArray ordersOption = new CommandOption.IntegerArray
            (SimpleTagger.class, "orders", "COMMA-SEP-DECIMALS", true, new int[]{1},
                    "List of label Markov orders (main and backoff) ", null);

    private static final CommandOption.String forbiddenOption = new CommandOption.String(
            SimpleTagger.class, "forbidden", "REGEXP", true,
            "\\s", "label1,label2 transition forbidden if it matches this", null);

    private static final CommandOption.String allowedOption = new CommandOption.String(
            SimpleTagger.class, "allowed", "REGEXP", true,
            ".*", "label1,label2 transition allowed only if it matches this", null);

    private static final CommandOption.String defaultOption = new CommandOption.String(
            SimpleTagger.class, "default-label", "STRING", true, "O",
            "Label for initial context and uninteresting tokens", null);

    private static final CommandOption.Integer iterationsOption = new CommandOption.Integer(
            SimpleTagger.class, "iterations", "INTEGER", true, 500,
            "Number of training iterations", null);

    private static final CommandOption.Boolean viterbiOutputOption = new CommandOption.Boolean(
            SimpleTagger.class, "viterbi-output", "true|false", true, false,
            "Print Viterbi periodically during training", null);

    private static final CommandOption.Boolean connectedOption = new CommandOption.Boolean(
            SimpleTagger.class, "fully-connected", "true|false", true, true,
            "Include all allowed transitions, even those not in training data", null);

    private static final CommandOption.String weightsOption = new CommandOption.String(
            SimpleTagger.class, "weights", "sparse|some-dense|dense", true, "some-dense",
            "Use sparse, some dense (using a heuristic), or dense features on transitions.", null);

    private static final CommandOption.Boolean continueTrainingOption = new CommandOption.Boolean(
            SimpleTagger.class, "continue-training", "true|false", false, false,
            "Continue training from model specified by --model-file", null);

    private static final CommandOption.Integer nBestOption = new CommandOption.Integer(
            SimpleTagger.class, "n-best", "INTEGER", true, 1,
            "How many answers to output", null);

    private static final CommandOption.Integer cacheSizeOption = new CommandOption.Integer(
            SimpleTagger.class, "cache-size", "INTEGER", true, 100000,
            "How much state information to memoize in n-best decoding", null);

    private static final CommandOption.Boolean includeInputOption = new CommandOption.Boolean(
            SimpleTagger.class, "include-input", "true|false", true, false,
            "Whether to include the input features when printing decoding output", null);

    private static final CommandOption.Boolean featureInductionOption = new CommandOption.Boolean(
            SimpleTagger.class, "feature-induction", "true|false", true, false,
            "Whether to perform feature induction during training", null);

    private static final CommandOption.Integer numThreads = new CommandOption.Integer(
            SimpleTagger.class, "threads", "INTEGER", true, 1,
            "Number of threads to use for CRF training.", null);

}
