package org.leibnizcenter.rechtspraak.tagging.experiments;

import cc.mallet.fst.CRF;
import cc.mallet.types.Instance;
import cc.mallet.types.Sequence;
import cc.mallet.types.TokenSequence;
import com.google.gson.Gson;
import org.leibnizcenter.rechtspraak.tagging.DeterministicTagger;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tagging.crf.ApplyCrf;
import org.leibnizcenter.rechtspraak.tagging.crf.TrainCrf;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.rechtspraak.tokens.text.Newline;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.rechtspraak.tokens.tokentree.TokenTree;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by maarten on 13-5-16.
 */
public class EvaluateResultingCRFs {
    final public ConfusionMatrix scores = new ConfusionMatrix();

    public static void main(String[] args) throws ParserConfigurationException {
        new EvaluateResultingCRFs().evaluatePerformanceDeterminsticTagger();
        Arrays.stream(ExperimentConditions.values())
                .parallel()
                .forEach(experimentConditions ->
                        new EvaluateResultingCRFs().evaluatePerformance(experimentConditions));
    }

    private void evaluatePerformanceDeterminsticTagger() throws ParserConfigurationException {
        List<File> xmlFiles = Xml.listXmlFiles(new File(Const.IN_FOLDER_TESTING));
        TokenList.FileIterable it = new TokenList.FileIterable(new TokenList.FileIterator(xmlFiles.toArray(new File[xmlFiles.size()])));
        Collection<Wrong> wrongs = new HashSet<>();
        for (TokenList doc : it) {
            List<Label> manualLabels = TokenTree.labelFromAnnotation(doc);
            List<Label> transducedLabels = DeterministicTagger.tag(doc);

            for (int i = 0; i < transducedLabels.size(); i++) {
                Label manualLabel = manualLabels.get(i);
                Label transducedLabel = Label.fromString.get(transducedLabels.get(i).toString());

                scores.addObservation(manualLabel, transducedLabel);
                if (!manualLabel.equals(transducedLabel)) {
                    String txt = doc.get(i).getNormalizedText();
                    wrongs.add(new Wrong(doc.getEcli(), manualLabel, transducedLabel, txt));
                }
            }

        }
        Writer sw = new StringWriter();
        Performance perf = new Performance(scores, wrongs);
        new Gson().toJson(perf, sw);
        System.out.println("\"deterministicTagger\": " + sw.toString() + ",");
    }

    private void evaluatePerformance(ExperimentConditions condition) {
        try {
            Set<Wrong> wrongs = new HashSet<>();
            CRF crf = ApplyCrf.loadCrf(condition.writeCrfToFile);
            List<File> xmlFiles = Xml.listXmlFiles(new File(Const.IN_FOLDER_TESTING));

            TokenList.FileIterable itr = new TokenList.FileIterable(new TokenList.FileIterator(xmlFiles.toArray(new File[xmlFiles.size()])));
            for (TokenList doc : itr) {
                if (!condition.useNewlines) doc = new TokenList(
                        doc.getEcli(),
                        doc.getSource(),
                        doc.stream().filter(ttl -> !(ttl instanceof Newline)).collect(Collectors.toList())
                );
                List<Label> actualLabels = TokenTree.labelFromAnnotation(doc);
                Instance instance = TrainCrf.getInstance(doc, actualLabels, true);
                final TokenSequence inputData = (TokenSequence) instance.getData();

                Instance result = crf.transduce(instance);
                Sequence transducedLabels = (Sequence) result.getData();

                for (int i = 0; i < transducedLabels.size(); i++) {
                    Label manualLabel = actualLabels.get(i);
                    Label transducedLabel = Label.fromString.get(transducedLabels.get(i).toString());

                    scores.addObservation(manualLabel, transducedLabel);
                    if (!manualLabel.equals(transducedLabel)) {
                        TokenTreeLeaf ttl = (TokenTreeLeaf) inputData.get(i).getProperty(Const.RECHTSPRAAK_TOKEN);
                        wrongs.add(new Wrong(doc.getEcli(), manualLabel, transducedLabel, ttl.getNormalizedText()));
                    }
                }
            }
            Writer sw = new StringWriter();
//            Files.newWriter(
//                    new File(condition.writeCrfToFile + ".performance.json"), Charsets.UTF_8
//            );
            Performance perf = new Performance(scores, wrongs);
            new Gson().toJson(perf, sw);
            System.out.println("\"" + condition.writeCrfToFile.getName() + "\": " + sw.toString() + ",");
        } catch (ClassNotFoundException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static class Performance {
        final ConfusionMatrix confusionMatrix;
        final Map<Label, LabelPerformance> scores = new EnumMap<>(Label.class);
        private final Collection<Wrong> wrongs;

        public Performance(ConfusionMatrix matrix, Collection<Wrong> wrongs) {
            confusionMatrix = matrix;
            this.wrongs = wrongs;
            for (Label l : matrix.keySet()) {
                LabelPerformance labelPerformance = new LabelPerformance(matrix, l);

                if (!(labelPerformance.truePositive == 0 && labelPerformance.falseNegative == 0))
                    scores.put(l, labelPerformance);
            }
        }

    }

    public static class LabelPerformance {
        final public int truePositive;
        final public int trueNegative;
        final public int falseNegative;
        final public int falsePositive;

        final public double precision;
        final public double recall;

        final public double f1;
        public final double f0_5;

        public LabelPerformance(ConfusionMatrix matrix, Label l) {
            truePositive = matrix.getTruePositives(l);
            trueNegative = matrix.getTrueNegatives(l);
            falseNegative = matrix.getFalseNegatives(l);
            falsePositive = matrix.getFalsePositives(l);


            precision = (1.0 * truePositive) / (1.0 * (truePositive + falsePositive));
            recall = (1.0 * truePositive) / (1.0 * (truePositive + falseNegative));

            f1 = (1 + 1.0) * (precision * recall) / ((1.0 * precision) + recall);
            f0_5 = (1 + Math.pow(0.5, 2)) * (precision * recall) / ((Math.pow(0.5, 2) * precision) + recall);

            if (
                    Double.isNaN(truePositive) ||
                            Double.isNaN(falseNegative) ||
                            Double.isNaN(trueNegative) ||
                            Double.isNaN(falsePositive) ||
                            Double.isNaN(recall) ||
                            Double.isNaN(precision) ||
                            Double.isNaN(f1) ||
                            Double.isNaN(f0_5)
                    )
                throw new Error();
        }
    }

    private class Wrong {
        public final String text;
        public final Label actual;
        public final String ecli;
        public final Label predicted;

        public Wrong(String ecli, Label actual, Label predicted, String text) {
            this.ecli = ecli;
            this.actual = actual;
            this.predicted = predicted;
            this.text = text;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Wrong wrong = (Wrong) o;

            if (!text.equals(wrong.text)) return false;
            if (actual != wrong.actual) return false;
            if (!ecli.equals(wrong.ecli)) return false;
            return predicted == wrong.predicted;

        }

        @Override
        public int hashCode() {
            int result = text.hashCode();
            result = 31 * result + actual.hashCode();
            result = 31 * result + ecli.hashCode();
            result = 31 * result + predicted.hashCode();
            return result;
        }
    }
}
