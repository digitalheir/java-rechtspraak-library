package org.leibnizcenter.rechtspraak.tagging.crf;

import cc.mallet.fst.*;
import cc.mallet.pipe.*;
import cc.mallet.types.*;
import cc.mallet.util.CommandOption;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tagging.crf.features.Features;
import org.leibnizcenter.rechtspraak.tokens.*;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.util.Const;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Maarten on 11-03-2016.
 */
public class TrainCrf {
    private static final int MAX_DOCS = 100;

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
            "\\s", "label1,label2 transition forbidden if it find this", null);
    private static final CommandOption.String allowedOption = new CommandOption.String(
            SimpleTagger.class, "allowed", "REGEXP", true,
            ".*", "label1,label2 transition allowed only if it find this", null);
    private static final CommandOption.String defaultOption = new CommandOption.String(
            SimpleTagger.class, "default-label", "STRING", true, "O",
            "Label for initial context and uninteresting tokens", null);
    private static final CommandOption.Integer iterationsOption = new CommandOption.Integer(
            SimpleTagger.class, "iterations", "INTEGER", true, Integer.MAX_VALUE,
            "Number of training iterations", null);
    private static final CommandOption.Boolean viterbiOutputOption = new CommandOption.Boolean(
            SimpleTagger.class, "viterbi-output", "true|false", true, false,
            "Print Viterbi periodically during training", null);
    private static final CommandOption.Boolean connectedOption = new CommandOption.Boolean(
            SimpleTagger.class, "fully-connected", "true|false", true, true,
            "Include all allowed transitions, even those not in training data", null);
    private static final CommandOption.String weightsOption = new CommandOption.String(
            SimpleTagger.class, "weights", "sparse|some-dense|dense", true, "some-dense",
            "Use sparse, some dense (using a heuristic), or dense mostlikelytreefromlist on transitions.", null);
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
            "Whether to include the input mostlikelytreefromlist when printing decoding output", null);
    private static final CommandOption.Boolean featureInductionOption = new CommandOption.Boolean(
            SimpleTagger.class, "feature-induction", "true|false", true, false,
            "Whether to perform feature induction during training", null);
    private static final CommandOption.Integer numThreads = new CommandOption.Integer(
            SimpleTagger.class, "threads", "INTEGER", true, 4,
            "Number of threads to use for CRF training.", null);
    private static Alphabet dataAlphabet = new Alphabet();
    private static Random r = new Random(69L);

    /**
     * Create and train a CRF model from the given training data,
     * optionally testing it on the given test data.
     *
     * @param training     training data
     * @param testing      test data (possibly <code>null</code>)
     * @param eval         accuracy evaluator (possibly <code>null</code>)
     * @param orders       label Markov orders (main and backoff)
     * @param defaultLabel default label
     * @param forbidden    regular expression specifying impossible label
     *                     transitions <em>current</em><code>,</code><em>next</em>
     *                     (<code>null</code> indicates no forbidden transitions)
     * @param allowed      regular expression specifying allowed label transitions
     *                     (<code>null</code> indicates everything is allowed that is not forbidden)
     * @param connected    whether to include even transitions not
     *                     occurring in the training data.
     * @param iterations   number of training iterations
     * @param var          Gaussian prior variance
     * @param numThreads   Number of threads to use
     * @return the trained model
     */

    public static CRF train(InstanceList training, InstanceList testing,
                            TransducerEvaluator eval, int[] orders,
                            String defaultLabel,
                            String forbidden, String allowed,
                            boolean connected, int iterations, double var, CRF crf, int numThreads) {
        Pattern forbiddenPat = Pattern.compile(forbidden);
        Pattern allowedPat = Pattern.compile(allowed);
        if (crf == null) {
            crf = new CRF(training.getPipe(), null);
            String startName =
                    crf.addOrderNStates(
                            training,
                            orders,
                            null,
                            defaultLabel,
                            forbiddenPat,
                            allowedPat,
                            connected);
            for (int i = 0; i < crf.numStates(); i++) {
                crf.getState(i).setInitialWeight(Transducer.IMPOSSIBLE_WEIGHT);
            }
            crf.getState(startName).setInitialWeight(0.0);
        }

        System.out.println("Training on " + training.size() + " instances");
        if (testing != null) {
            System.out.println("Testing on " + testing.size() + " instances");
        }

        assert (numThreads > 0);
        if (numThreads > 1) {
            CRFTrainerByThreadedLabelLikelihood crft = new CRFTrainerByThreadedLabelLikelihood(crf, numThreads);
            crft.setGaussianPriorVariance(var);

            setWeightsOptions(crft);

            if (featureInductionOption.value) {
                throw new IllegalArgumentException("Multi-threaded feature induction is not yet supported.");
            } else {
                trainWhileNotConverged(training, testing, eval, iterations, crft);
            }
            crft.shutdown();
        } else {
            CRFTrainerByLabelLikelihood crft = new CRFTrainerByLabelLikelihood(crf);
            crft.setGaussianPriorVariance(var);

            setWeightsOptions(crft);

            if (featureInductionOption.value) {
                crft.trainWithFeatureInduction(training, null, testing, eval, iterations, 10, 20, 500, 0.5, false, null);
            } else {
                trainWhileNotConverged(training, testing, eval, iterations, crft);
            }
        }

        return crf;
    }

    public static void train(List<File> trainOnObservations, File writeCrfToFile, boolean useNewlines) throws ParserConfigurationException {
        SerialPipes pipe = new SerialPipes(new Pipe[]{
                new TokenSequence2FeatureVectorSequence(dataAlphabet, true, false),
                new Target2LabelSequence()
        });
        InstanceList il = new InstanceList(pipe);
        il.addThruPipe(new RsInstanceIterator(trainOnObservations, true, useNewlines));

        System.gc();

//        InstanceList[] trainingLists =
//                il.split(
//                        r, new double[]{0.8,
//                                1.0 - 0.8});
//        InstanceList trainingData = trainingLists[0];
//        InstanceList testData = trainingLists[1];
//        TokenAccuracyEvaluator eval = new TokenAccuracyEvaluator(
//                new InstanceList[]{trainingData, testData}, new String[]{"Training", "Testing"}
//        );

        CRF crf = constructCrf(pipe);
//        CRF crf = loadCrf(new File(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF));

        Set<String> trnasitions = Label.getAllowedTransitions().stream()
                .map((arr) -> String.join(",", arr[0].name(), arr[1].name()))
                .collect(Collectors.toSet());
        String allowedTransitions = String.join(" ",
                trnasitions);
        System.out.println(forbiddenOption.defaultValue);


        train(il, null, null, ordersOption.value, defaultOption.value,
                forbiddenOption.defaultValue,
                allowedTransitions,
                connectedOption.value, iterationsOption.value,
                gaussianVarianceOption.value, crf, numThreads.value);

        saveToFile(crf, writeCrfToFile);
    }


    private static void setWeightsOptions(CRFTrainerByThreadedLabelLikelihood crft) {
        //noinspection Duplicates
        switch (weightsOption.value) {
            case "dense":
                crft.setUseSparseWeights(false);
                crft.setUseSomeUnsupportedTrick(false);
                break;
            case "some-dense":
                crft.setUseSparseWeights(true);
                crft.setUseSomeUnsupportedTrick(true);
                break;
            case "sparse":
                crft.setUseSparseWeights(true);
                crft.setUseSomeUnsupportedTrick(false);
                break;
            default:
                throw new RuntimeException("Unknown weights option: " + weightsOption.value);
        }
    }

    public static void setWeightsOptions(CRFTrainerByLabelLikelihood crft) {
        //noinspection Duplicates
        switch (weightsOption.value) {
            case "dense":
                crft.setUseSparseWeights(false);
                crft.setUseSomeUnsupportedTrick(false);
                break;
            case "some-dense":
                crft.setUseSparseWeights(true);
                crft.setUseSomeUnsupportedTrick(true);
                break;
            case "sparse":
                crft.setUseSparseWeights(true);
                crft.setUseSomeUnsupportedTrick(false);
                break;
            default:
                throw new RuntimeException("Unknown weights option: " + weightsOption.value);
        }
    }

    public static void trainWhileNotConverged(InstanceList training,
                                              InstanceList testing,
                                              TransducerEvaluator eval,
                                              int iterations,
                                              TransducerTrainer crft) {
        boolean converged;
        for (int i = 1; i <= iterations; i++) {
            converged = crft.train(training, 1);
            if (i % 10 == 0 && eval != null) { // Change the 1 to higher integer to evaluate less often
                eval.evaluate(crft);
            }
            if (viterbiOutputOption.value && i % 10 == 0) {
                new ViterbiWriter("", new InstanceList[]{training, testing}, new String[]{"training", "testing"}).evaluate(crft);
            }
            if (converged) break;
        }
    }

    public static Instance getInstance(List<TokenTreeLeaf> doc, List<Label> labels, boolean preserveInfo) {
        TokenSequence ts = getTokenSequence(doc, preserveInfo);
        TokenSequence ls = getLabelSequence(labels);
        return new Instance(ts, ls, null, null);
    }

    public static Instance getInstance(List<TokenTreeLeaf> doc, boolean preserveInfo) {
        TokenSequence ts = getTokenSequence(doc, preserveInfo);
        return new Instance(ts, null, null, null);
    }

    public static TokenSequence getTokenSequence(List<TokenTreeLeaf> doc, boolean preserveInfo) {
        TokenSequence ts = new TokenSequence(doc.size());
        for (int i = 0; i < doc.size(); i++) {
            TokenTreeLeaf token = doc.get(i);
            Token t = new Token(null);

            Features.setAllFeatures(t, doc, i);

            if (preserveInfo) {
                String txt = Strings.isNullOrEmpty(token.getTextContent()) ? "" : token.getTextContent().trim();
                t.setText(txt);
                t.setProperty(Const.RECHTSPRAAK_TOKEN, token);
            }
            ts.add(t);
        }
        return ts;
    }

    public static TokenSequence getLabelSequence(List<Label> doc) {
        TokenSequence ls = new TokenSequence();
        doc.forEach(l -> ls.add(new Token(l.name())));
        return ls;
    }

    private static CRF constructCrf(Pipe inputPipe) {
        CRF crf = new CRF(inputPipe, null);

        // Add states
        String[] destinations = Arrays.stream(Label.values())
                .map(Enum::name)
                .collect(Collectors.toSet())
                .toArray(new String[Label.values().length]);

        for (Label l2 : Label.values())
            crf.addState(l2.name(), destinations);


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


    public static class RsInstanceIterator implements Iterator<Instance> {
        private final TokenList.FileIterator it;
        private int i = 0;
        final private boolean preserveInfo;
        private final boolean useNewlines;

        public RsInstanceIterator(List<File> xmlFiles, boolean preserveInfo, boolean useNewlines) throws ParserConfigurationException {
            it = new TokenList.FileIterable(
                    new TokenList.FileIterator(xmlFiles.toArray(new File[xmlFiles.size()]))).iterator();
            this.preserveInfo = preserveInfo;
            this.useNewlines = useNewlines;
        }
//        public RsInstanceIterator(List<File> xmlFiles) throws ParserConfigurationException {
//            this(xmlFiles,false,true);
//        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Instance next() {
            TokenList doc = it.next();
            List<Label> labels = TokenTree.labelFromAnnotation(doc);

            if (!useNewlines) {
                final TokenList finalDoc = doc;
                List<Integer> useIndexes = IntStream.range(0, doc.size())
                        .filter((i) -> !(finalDoc.get(i) instanceof Newline)).mapToObj(Integer::new).collect(Collectors.toList());
                doc = doc.getFromIndexes(useIndexes);
                labels = useIndexes.stream().map(labels::get).collect(Collectors.toList());
            }

            Instance instance = getInstance(doc, labels, preserveInfo);
            i++;
            if (i % 50 == 0) {
                System.out.println(i);
            }
            return instance;
        }
    }
}
