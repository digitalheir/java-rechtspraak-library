package org.leibnizcenter.rechtspraak.tagging.experiments;

import cc.mallet.fst.CRF;
import cc.mallet.types.Instance;
import cc.mallet.types.Sequence;
import cc.mallet.types.TokenSequence;
import com.google.common.base.Charsets;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.io.Files;
import com.google.gson.Gson;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tagging.crf.ApplyCrf;
import org.leibnizcenter.rechtspraak.tagging.crf.TrainCrf;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by maarten on 13-5-16.
 */
public class EvaluateResultingCRFs {
    final static Performance performanceCounter = new Performance();

    public static void main(String[] args) {
        Arrays.stream(ExperimentConditions.values())
                .parallel()
                .forEach(EvaluateResultingCRFs::evaluatePerformance);
    }

    private static void evaluatePerformance(ExperimentConditions condition) {
        try {
            CRF crf = ApplyCrf.loadCrf(condition.writeCrfToFile);
            TrainCrf.RsInstanceIterator instanceIterator = new TrainCrf.RsInstanceIterator(
                    Xml.listXmlFiles(new File(Const.IN_FOLDER_TESTING)),
                    true,
                    condition.useNewlines
            );
            while (instanceIterator.hasNext()) {
                Instance instance = instanceIterator.next();

                final TokenSequence inputData = (TokenSequence) instance.getData();
                final Sequence manualLabels = (Sequence) instance.getTarget();


                Instance result = crf.transduce(instance);
                Sequence transducedLabels = (Sequence) result.getTarget();

                for (int i = 0; i < transducedLabels.size(); i++) {
                    Label manualLabel = Label.fromString.get(manualLabels.get(i).toString());
                    Label transducedLabel = Label.fromString.get(manualLabels.get(i).toString());

                    performanceCounter.totalCount++;
                    if (manualLabel.equals(transducedLabel)) {
                        performanceCounter.addMatch(transducedLabel);
                    } else {
                        TokenTreeLeaf ttl = (TokenTreeLeaf) inputData.get(i).getProperty(Const.RECHTSPRAAK_TOKEN);
                        performanceCounter.wrongs.add(ttl.getNormalizedText());
                    }
                }
            }

            BufferedWriter sw = Files.newWriter(
                    new File(condition.writeCrfToFile+".performance.json"), Charsets.UTF_8
            );
            new Gson().toJson(performanceCounter, sw);
        } catch (ClassNotFoundException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static class Performance {
        private final Multiset<Label> scores;
        private final Multiset<String> wrongs;
        public int totalCount = 0;
        public int totalMatches;

        public Performance() {
            this.scores = HashMultiset.create();
            this.wrongs = HashMultiset.create();
        }

        public void addMatch(Label label) {
            totalMatches++;
            scores.add(label);
        }
    }
}
