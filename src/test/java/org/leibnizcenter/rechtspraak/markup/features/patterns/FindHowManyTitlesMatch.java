package org.leibnizcenter.rechtspraak.markup.features.patterns;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import com.google.common.io.Files;
import org.leibnizcenter.rechtspraak.TrainWithMallet;
import org.leibnizcenter.rechtspraak.features.title.TitlePatterns.TitlesNormalizedMatchesHighConf;
import org.leibnizcenter.rechtspraak.markup.Label;
import org.leibnizcenter.rechtspraak.markup.RechtspraakToken;
import org.leibnizcenter.rechtspraak.markup.RechtspraakTokenList;
import org.leibnizcenter.rechtspraak.markup.features.Patterns;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static org.leibnizcenter.rechtspraak.markup.RechtspraakCorpus.listXmlFiles;

/**
 * Creates a F1 performance summary for our title regex patterns
 * Created by maarten on 17-3-16.
 */
public class FindHowManyTitlesMatch {
    private static final Pattern PERSON = Pattern.compile("(naam )?(ge[iï]ntimeerde|belanghebbenden?|vader|moeder|ver(weerder|zoeker)|man|vrouw|naam|eiser(es)?|gedaagden?|app?ell?ante?)( sub)?( ?[0-9])?( ?wonende (te|in) woonplaats)?( ?[0-9])?");
    private static final Pattern INSTANTIE = Pattern.compile("bureau jeugdzorg( \\p{L}+)?");

    private static final int INT = 20;
//    (pattern) ->

    public static class PatternPerformance {
        private final int falsePositives;
        private final int truePositives;
        private final TitlesNormalizedMatchesHighConf pattern;
        public final double perc;

        public PatternPerformance(TitlesNormalizedMatchesHighConf pattern, int truePositives, int falsePositives) {
            this.falsePositives = falsePositives;
            this.truePositives = truePositives;
            this.pattern = pattern;
            perc = 100.0 * (((double) truePositives) /
                    (((double) falsePositives) + ((double) truePositives)));
            //System.out.println(truePositives + " / " + (truePositives + falsePositives) + " = ");

        }

        public String toString() {
            return String.format("%-5s %-10s %-10s %-10s",
                    String.format("%1$.2f", perc), this.pattern.name(), this.truePositives, this.falsePositives);
        }
    }

    public static void main(String[] a) throws IOException {
        Multiset<String> falseNegatives = HashMultiset.create();

        Multiset<String> falsePositives = HashMultiset.create();
        Multiset<TitlesNormalizedMatchesHighConf> falsePositiveMap = EnumMultiset.create(TitlesNormalizedMatchesHighConf.class);
        Multiset<TitlesNormalizedMatchesHighConf> truePositiveMap = EnumMultiset.create(TitlesNormalizedMatchesHighConf.class);

        int truePositives = 0, falsePositive = 0, totalTitles = 0, totalTokens = 0;
        List<File> xmlFiles = listXmlFiles(TrainWithMallet.xmlFiles, -100, false);
        for (RechtspraakTokenList doc : new RechtspraakTokenList.FileIterable(xmlFiles)) {
            for (RechtspraakToken token : doc) {
                String text = token.getToken().normalizedText;
                boolean ignore = Strings.isNullOrEmpty(text)
                        || ":".equals(text)
                        || "a".equals(text)
                        || "b".equals(text)
                        || "c".equals(text)
                        || "d".equals(text)
                        || "e".equals(text)
                        || "f".equals(text)
                        || "g".equals(text)
                        || "h".equals(text)
                        || "x".equals(text)
                        || "y".equals(text)
                        || "z".equals(text)
                        || "hd".equals(text)
                        || "en".equals(text)
                        || "nk".equals(text)
                        || "tm".equals(text)
                        || "ap".equals(text)
                        || "um".equals(text)
                        || PERSON.matcher(text).matches()
                        || INSTANTIE.matcher(text).matches();
                if (!ignore) {
                    boolean matchesAnyPattern = false;
                    // Check if this title matches any of our patterns
                    for (TitlesNormalizedMatchesHighConf pattern : TitlesNormalizedMatchesHighConf.set) {
                        boolean thisPatternMatches = false;
                        if (Patterns.matches(pattern, token.getToken())) {
                            thisPatternMatches = true;
                            matchesAnyPattern = true;
                        }
                        if (thisPatternMatches) {
                            if (token.getTag() == Label.SECTION_TITLE) {
                                truePositiveMap.add(pattern);
                            } else {
                                falsePositiveMap.add(pattern);
                            }
                        }
                    }
                    if (token.getTag() == Label.SECTION_TITLE) {
                        if (matchesAnyPattern) {
                            truePositives++;
                        } else {
                            falseNegatives.add(text);
                        }
                        totalTitles++;
                        if (totalTitles % 5000 == 0) System.out.println(totalTitles);
                    } else {
                        if (matchesAnyPattern) {
                            falsePositive++;
                            falsePositives.add(text);
                        }
                    }
                    totalTokens++;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Token count: ").append(totalTokens).append("\n");
        sb.append("Title count: ").append(totalTitles).append("\n");
        sb.append("True  positive: ").append(truePositives).append("\n");
        sb.append("False positive: ").append(falsePositive).append("\n");
        sb.append("___true positive___% ").append((((double) truePositives) * 10.0) / ((double) totalTitles) * 10.0).append("\n");
        sb.append("___true negative___% ").append(((double) ((totalTokens - totalTitles) - falsePositive) * 10.0) /
                ((double) (totalTokens - totalTitles)) * 10.0).append("\n");
        sb.append("________________________________________________________").append("\n");
        sb.append("Top " + INT + " false negatives: ").append("\n");
        // Should be sorted?
        UnmodifiableIterator<Multiset.Entry<String>> it = Multisets.copyHighestCountFirst(falseNegatives).entrySet().iterator();
        for (int i = 0; i < INT; i++) {
            if (it.hasNext()) {
                Multiset.Entry<String> entry = it.next();
                sb.append(entry.getElement()).append(" (").append(entry.getCount()).append(")").append("\n");
            } else {
                break;
            }
        }
        sb.append("________________________________________________________").append("\n");
        sb.append("Top " + INT + " false positives: ").append("\n");
        it = Multisets.copyHighestCountFirst(falsePositives).entrySet().iterator();
        for (int i = 0; i < INT; i++) {
            if (it.hasNext()) {
                Multiset.Entry<String> entry = it.next();
                sb.append(entry.getElement()).append(" (").append(entry.getCount()).append(")").append("\n");
            } else {
                break;
            }
        }

        sb.append("________________________________________________________").append("\n\n");

        TitlesNormalizedMatchesHighConf.set.stream()
                .map((pattern) -> new PatternPerformance(
                        pattern,
                        truePositiveMap.count(pattern),
                        falsePositiveMap.count(pattern)))
                .sorted((o1, o2) -> Double.compare(o1.perc, o2.perc))
                .forEach((perf) -> sb.append(perf.toString()).append("\n"));

        ///////////

        File file = new File("title_patterns_performance.txt");
        Files.write(sb.toString(), file, Charsets.UTF_8);
    }
}
