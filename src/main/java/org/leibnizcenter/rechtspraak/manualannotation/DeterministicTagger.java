package org.leibnizcenter.rechtspraak.manualannotation;

import org.leibnizcenter.rechtspraak.features.title.TitlePatterns;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledToken;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;
import org.leibnizcenter.rechtspraak.markup.docs.features.Patterns;
import org.w3c.dom.Document;

import java.util.List;

/**
 * Created by maarten on 29-3-16.
 */
public class DeterministicTagger {
    // TODO make feature functions

    public static LabeledTokenList protoTag(String ecli, Document doc, List<RechtspraakElement> untagged) {
        LabeledTokenList tagged = new LabeledTokenList(ecli, untagged.size(), doc);
        int firstTitle = -1;
        for (int i = 0; i < untagged.size(); i++) {
            RechtspraakElement token = untagged.get(i);
            Label tag = protoTag(untagged, i, tagged);
            if (firstTitle < 0 && Label.SECTION_TITLE.equals(tag)) {
                firstTitle = setToPrevIfPrecededByNumber(tagged, i);
            }
            //////////////////////////////////////////////////////

            tagged.add(i, new LabeledToken(token, tag));
        }
        firstTitle = getLikelyFirstTitleIndex(tagged, firstTitle);

        // make everything up to the first title 'INFO'
        for (int i = 0; i < firstTitle; i++) tagged.get(i).tag = Label.OUT;//INFO;

        for (int ix = 0; ix < tagged.size(); ix++) {
            RechtspraakElement token = tagged.get(ix).getToken();
            // This may yet be a title if it is not inline (or follows a NR), does not end with punctuation, and is marked up
            if (Label.OUT.equals(tagged.get(ix).getTag())
                    && hasEmphasis(token)
                    && token.endsWithNonLetter
                    && !token.precedesNonEmptyText
                    && (!token.followsNonEmptyText || (ix > 0 && tagged.get(ix - 1).getTag().equals(Label.SECTION_NR)))
                    ) tagged.get(ix).tag = Label.SECTION_TITLE;
        }
        return tagged;
    }

    private static LabeledToken setBestGuess(LabeledToken t) {
        RechtspraakElement token = t.getToken();
        if (token.getTextContent().startsWith("de gronden van het ")) {
            t.tag = Label.OUT;
        }

        if (Label.OUT.equals(t.tag)) {
            if (hasEmphasis(token) && !token.endsWithNonLetter && token.followsLineBreak) {
                t.tag = Label.SECTION_NR;
            }
        }
        return t;
    }

    public static boolean hasEmphasis(RechtspraakElement token) {
        if (token.getEmphasisSingletonChild() != null) {
            String role = token.getEmphasisSingletonChild().getAttribute("role");
            return ("italic".equals(role)
                    || "bold".equals(role)
                    || "underline".equals(role));
        } else return false;
    }

    private static int getLikelyFirstTitleIndex(List<LabeledToken> tagged, int firstTitle) {
        boolean proceedingsFound = false;
        for (int i = 0; i < (firstTitle > 0 ? firstTitle : tagged.size()); i++) {
            LabeledToken labeledToken = tagged.get(i);
            if (Patterns.matches(TitlePatterns.TitlesNormalizedMatchesHighConf.PROCEEDINGS, labeledToken.getToken())) {
                proceedingsFound = true;
                firstTitle = setToPrevIfPrecededByNumber(tagged, i);
                break;
            }
        }
        if (!proceedingsFound) {
            for (int i = 0; i < (firstTitle > 0 ? firstTitle : tagged.size()); i++) {
                if (i < tagged.size() - 1
                        && tagged.get(i).getTag().equals(Label.SECTION_TITLE)) {
                    firstTitle = setToPrevIfPrecededByNumber(tagged, i);
                    break;
                }
            }
        }
        return firstTitle;
    }

    private static int setToPrevIfPrecededByNumber(List<LabeledToken> tagged, int i) {
        int firstTitle = i;
        if (i > 0) {
            LabeledToken prev = tagged.get(i - 1);
            if (prev.getToken().numbering != null && prev.getToken().numbering.isFirstNumbering()) {
                prev.tag = Label.SECTION_NR;
            }
            if (prev.getTag().equals(Label.SECTION_NR)) firstTitle = i - 1;
        }
        return firstTitle;
    }

    private static Label protoTag(List<RechtspraakElement> untagged, int ix, List<LabeledToken> tagged) {
        RechtspraakElement token = untagged.get(ix);
        if (isDefOut(token)) {
            return Label.OUT;
        } else if (matchesHighConfTitle(token) || matchesLowConfTitleButHasMarkup(token)) {
            if (probablyInfoTag(untagged, ix, tagged)) return Label.OUT;
            return Label.SECTION_TITLE;
        } else if (token.numbering != null) {
            if (!token.isPlausibleNumbering) {
                return Label.OUT;
            } else return Label.SECTION_NR;
        } else {
            return Label.OUT;
        }
    }

    public static boolean matchesHighConfTitle(RechtspraakElement token) {
        return TitlePatterns.TitlesNormalizedMatchesHighConf.matchesAny(token);
    }

    public static boolean matchesLowConfTitleButHasMarkup(RechtspraakElement token) {
        return (hasEmphasis(token) || !token.endsWithNonLetter) && matchesLowConfTitle(token);
    }

    public static boolean matchesLowConfTitle(RechtspraakElement token) {
        return TitlePatterns.TitlesNormalizedMatchesLowConf.matchesAny(token);
    }

    private static boolean probablyInfoTag(List<RechtspraakElement> untagged, int ix, List<LabeledToken> tagged) {
        return false;
//        ix < 20
//                && !(ix > 0 && tagged.get(ix - 1).getTag().equals(Label.SECTION_NR))
//                &&
//                ((untagged.size() > ix + 1 && untagged.get(ix + 1).startsWithLowerCaseLetter)
//                        || (untagged.get(ix).endsWithNonLetter)
//                );
    }

    public static boolean isDefOut(RechtspraakElement token) {
        return token.getNodeName().matches("footnote" +
                "|listitem" +
                "|link" +
                "|section" +
                "|bridgehead" +
                "|.*list" +
                "|(uitspraak|conclusie)\\.info" +
                "|title" +
                "|(informal)?table" +
                "|mediaobject");
    }
}
