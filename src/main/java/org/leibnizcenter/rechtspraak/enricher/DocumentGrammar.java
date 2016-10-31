package org.leibnizcenter.rechtspraak.enricher;

import org.leibnizcenter.cfg.Grammar;
import org.leibnizcenter.cfg.algebra.semiring.dbl.LogSemiring;
import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.token.Token;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.LabeledToken;


/**
 * CFG for making a Rechtspraak.nl XML document hierarchy
 * Created by maarten on 19-4-16.
 */
@SuppressWarnings("WeakerAccess")
public final class DocumentGrammar {
    public static final NonTerminal DOCUMENT = new NonTerminal("#root");
    public static final NonTerminal HEADER = new NonTerminal("Header");
    public static final NonTerminal DOCUMENT_BODY = new NonTerminal("DocumentContent");

    /**
     * 1 or more sections in sequence
     */
    public static final NonTerminal SECTION_BLOB = new NonTerminal("Sections");

    public static final NonTerminal SECTION = new NonTerminal("Section");
    public static final NonTerminal SECTION_TITLE = new NonTerminal("SectionTitle");
    public static final NonTerminal NEWLINE_AND_TITLE_TEXT = new NonTerminal("NEWLINE_AND_TITLE_TEXT");
    public static final NonTerminal SECTION_CONTENT = new NonTerminal("SectionContent");
    public static final NonTerminal SECTION_SEQUENCE = new NonTerminal("SECTION_SEQUENCE");
    public static final NonTerminal COMPLETE_SECTION_BLOB_W_TRAILING_TEXT = new NonTerminal("COMPLETE_SECTION_BLOB_W_TRAILING_TEXT");

    public static final NonTerminal TEXT_BLOB = new NonTerminal("Text");
    public static final NonTerminal SECTION_TITLE_TEXT = new NonTerminal("SECTION_TITLE_TEXT");
    public static final NonTerminal SINGLE_TITLE_TEXT = new NonTerminal("SINGLE_TITLE_TEXT");
    public static final NonTerminal SINGLE_NUMBERING = new NonTerminal("SINGLE_NUMBERING");
    public static final NonTerminal NONTERMINAL_NEWLINE = new NonTerminal("NONTERMINAL_NEWLINE");


    public static final Terminal<LabeledToken> TERMINAL_NUMBERING = new LabelRecognizer(Label.NR);
    //TODO make grammar a bit smarter
//    public static final Terminal<LabeledToken> TERMINAL_NUMBERING_IN_SEQUENCE = new Terminal<LabeledToken>() {
//        @Override
//        public boolean hasCategory(Token<LabeledToken> token) {
//            return ;
//        }
//    };
    public static final Terminal<LabeledToken> TERMINAL_SECTION_TITLE = new LabelRecognizer(Label.SECTION_TITLE);
    public static final Terminal<LabeledToken> TERMINAL_TEXT = new LabelRecognizer(Label.TEXT_BLOCK);
    public static final Terminal<LabeledToken> TERMINAL_NEWLINE = new LabelRecognizer(Label.NEWLINE);


    public final static Grammar grammar = new Grammar.Builder()
            .setSemiring(new LogSemiring())
            //.addRule(1.0, DOCUMENT, /* -> */ HEADER, DOCUMENT_BODY) // TODO
            .addRule(1.0, DOCUMENT, /* -> */ DOCUMENT_BODY)

            .addRule(0.7, DOCUMENT_BODY, /* -> */ SECTION_SEQUENCE)
            .addRule(0.1, DOCUMENT_BODY, /* -> */ TEXT_BLOB, SECTION_SEQUENCE)
            .addRule(0.1, DOCUMENT_BODY, /* -> */ SECTION_SEQUENCE, TEXT_BLOB)
            .addRule(0.1, DOCUMENT_BODY, /* -> */ TEXT_BLOB, SECTION_SEQUENCE, TEXT_BLOB)
//
            .addRule(1.0, SECTION_SEQUENCE, SECTION_BLOB)
//
            .addRule(0.3, SECTION_BLOB, /* -> */ SECTION_BLOB, SECTION_BLOB)
            .addRule(0.3, SECTION_BLOB, /* -> */ SECTION)
            .addRule(0.2, SECTION_BLOB, /* -> */ SECTION, TEXT_BLOB)
            .addRule(0.2, SECTION_BLOB, /* -> */ TEXT_BLOB, SECTION)

            .addRule(0.99, SECTION, /* -> */ SECTION_TITLE, SECTION_CONTENT)
            .addRule(1-0.99, SECTION, /* -> */ SECTION_TITLE)

            .addRule(0.4, SECTION_CONTENT, /* -> */ SECTION_CONTENT, SECTION_CONTENT)
            .addRule(0.4, SECTION_CONTENT, /* -> */ TEXT_BLOB)
            .addRule(0.2, SECTION_CONTENT, /* -> */ SECTION_SEQUENCE)

            .addRule(0.5, TEXT_BLOB, /* -> */ TEXT_BLOB, TEXT_BLOB)
            .addRule(0.3, TEXT_BLOB, /* -> */ TERMINAL_TEXT)
            .addRule(0.2, TEXT_BLOB, /* -> */ TERMINAL_NEWLINE)

            //
            // Section Title
            //
            .addRule(0.2, SECTION_TITLE, /* -> */ TERMINAL_NUMBERING)
            .addRule(0.3, SECTION_TITLE, /* -> */ SECTION_TITLE_TEXT)
            .addRule(0.5, SECTION_TITLE, /* -> */ TERMINAL_NUMBERING, SECTION_TITLE_TEXT)

            .addRule(0.9, SECTION_TITLE_TEXT, /* -> */ TERMINAL_SECTION_TITLE)
            .addRule(0.1, SECTION_TITLE_TEXT, /* -> */ TERMINAL_NEWLINE, SECTION_TITLE_TEXT)

            .build();

    private DocumentGrammar() {
    }

//    /**
//     * Stick together two section sequences
//     * <pre>
//     * SECTION_BLOB -> SECTION_BLOB SECTION_BLOB
//     * </pre>
//     */
//    private static class TwoSectionBlobs extends StochasticRule {
//        public TwoSectionBlobs(NonTerminal lhs, RightHandSide rhs) {
//            super(lhs, rhs, 1.0);
//        }
//
//        @SuppressWarnings("unused")
//        @Override
//        public double getLogProbability(ScoreChart.ParseTreeContainer... inputs) {
//            // Adding at least two sections... See whether they match
//            ScoreChart.ParseTreeContainer section1 = getLastSection(inputs[0]); // B
//            ScoreChart.ParseTreeContainer section2 = getFirstSection(inputs[1]); // C
//
////            final TokenTreeLeaf titleA = getSectionTitleFromSection(section1);
////            final TokenTreeLeaf titleB = getSectionTitleFromSection(section2);
////            Numbering nr1 = getNumbering(section1);
////            Numbering nr2 = getNumbering(section2);
//
//            double prior = 0.6
//                    //+ (hasSameMarkup(section1, section2) ? 0.1 : 0.0)
//                    + (inDirectSequence(getNumbering(section1), getNumbering(section2)) ? 0.4 : 0.0);
//            return StandardRule.getLogProbability(prior, inputs);
//        }
//
//        @Override
//        public ScoreChart.ParseTreeContainer apply(double logProb, ScoreChart.ParseTreeContainer... inputs) {
//            if (inputs.length != 2) throw new InvalidParameterException();
//            return new MultipleSectionBlobsContainer(this, inputs[0], inputs[1]);
//        }
//
//    }
//
//    /**
//     * SECTION_CONTENT -> SECTION_SEQUENCE
//     */
//    private static class  extends StochasticRule {
//        public (NonTerminal lhs, RightHandSide rhs) {
//            super(lhs, rhs, 0.5);
//        }
//
//        @Override
//        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
//            Deque<CYK.ParseTreeContainer> subSequences = new ArrayDeque<>(25);
//            getSections(subSequences, inputs);
//
//
//            int plausibleSubSequences = 0;
//            int likelySubSequences = 0;
//            for (CYK.ParseTreeContainer subs : subSequences) {
//                plausibleSubSequences += 1;
//                Numbering n = RechtspraakElementContainer.getNumbering(subs);
//                if (n != null) {
//                    if (n.getNumbering() instanceof SubSectionNumber) {
//                        likelySubSequences += 1;
//                    }
//                }
//            }
//
//            double size = subSequences.size();
//            double priorProbability = 0.1
//                    + 0.4 * (plausibleSubSequences / size)
//                    + 0.5 * (likelySubSequences / size);
//            return StandardRule.getLogProbability(priorProbability, inputs);
//        }
//    }

    //    private static class CompleteSectionBlob extends StochasticRule {
//        public CompleteSectionBlob(NonTerminal lhs, RightHandSide rhs) {
//            super(lhs, rhs, 1.0);
//        }
//
//        public static int countInSequence(Deque<CYK.ParseTreeContainer> subSections) {
//            if (allHaveNoNumbering(subSections)) return subSections.size();
//
//            int count = 0;
//            if (subSections.size() > 0) {
//                Iterator<CYK.ParseTreeContainer> it = subSections.iterator();
//
//                Numbering firstNum = getNumbering(it.next());
//                if (firstNum != null
//                        && (isPlausibleFirstSubsection(firstNum)
//                        || firstNum.getNumbering().isFirstNumbering())) {
//                    count++;
//                    NumberingNumber prevNum = firstNum.getNumbering();
//                    while (it.hasNext()) {
//                        Numbering num = getNumbering(it.next());
//                        if (num != null) {
//                            if (num.getNumbering().isSuccedentOf(prevNum) // is succedent...
//                                    && !(num.getNumbering() instanceof SubSectionNumber && num.getNumbering().isFirstNumbering()) //...but not a subsection
//                                    ) {
//                                prevNum = num.getNumbering();
//                                count++;
//                            }
//                        }
//                    }
//                }
//            }
//            return count;
//        }
//
//        private static boolean allHaveNoNumbering(Deque<CYK.ParseTreeContainer> subSections) {
//            for (CYK.ParseTreeContainer sec : subSections) if (getNumbering(sec) != null) return false;
//            return true;
//        }
//
//        @Override
//        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
//            // See if we have subsections...
//            Deque<CYK.ParseTreeContainer> sections = getSections(inputs);
//            //if (sections.size() <= 0) throw new IllegalStateException();
//
//            //check if subsections are IN >S E Q U E N C E<?????
//            int inSequence = countInSequence(sections);
//
//            boolean flawless = inSequence == sections.size();
//            double priorProbability = 0.4 +
//                    (flawless ? 0.4 : 0.0) +
//                    ((((double) inSequence) / ((double) sections.size())) * 0.2);
//            return StandardRule.getLogProbability(priorProbability, inputs);
//        }
//
    //}
//    private static class CompletedSectionBlob extends StochasticRule {
//        public CompletedSectionBlob(NonTerminal lhs, RightHandSide rhs) {
//            super(lhs, rhs, 1.0);
//        }
//
////        public static int countInSequence(Deque<ScoreChart.ParseTreeContainer> subSections) {
////            if (allHaveNoNumbering(subSections)) return subSections.size();
////
////            int count = 0;
////            if (subSections.size() > 0) {
////                Iterator<ScoreChart.ParseTreeContainer> it = subSections.iterator();
////
////                Numbering firstNum = getNumbering(it.next());
////                if (firstNum != null
////                        && (isPlausibleFirstSubsection(firstNum.getNumbering())
////                        || firstNum.getNumbering().isFirstNumbering())) {
////                    count++;
////                    NumberingNumber prevNum = firstNum.getNumbering();
////                    while (it.hasNext()) {
////                        Numbering num = getNumbering(it.next());
////                        if (num != null) {
////                            if (num.getNumbering().isSuccedentOf(prevNum) // is succedent...
////                                    && !(num.getNumbering() instanceof SubSectionNumber && num.getNumbering().isFirstNumbering()) //...but not a subsection
////                                    ) {
////                                prevNum = num.getNumbering();
////                                count++;
////                            }
////                        }
////                    }
////                }
////            }
////            return count;
////        }
////
////        public static boolean isPlausibleFirstSubsection(NumberingNumber firstNum) {
////            return firstNum instanceof SubSectionNumber && firstNum.isFirstNumbering();
////        }
////
////        private static boolean allHaveNoNumbering(Deque<ScoreChart.ParseTreeContainer> subSections) {
////            for (ScoreChart.ParseTreeContainer sec : subSections) if (getNumbering(sec) != null) return false;
////            return true;
////        }
//
//        @Override
//        public double getLogProbability(ScoreChart.ParseTreeContainer... inputs) {
//            // See if we have subsections...
////            Deque<ScoreChart.ParseTreeContainer> sections = getSections(inputs);
//            //if (sections.size() <= 0) throw new IllegalStateException();
//
//            //check if subsections are IN >S E Q U E N C E<?????
////            int inSequence = countInSequence(sections);
//
////            boolean flawless = inSequence == sections.size();
//
//
//            Numbering numb = getNumbering(getFirstSection((TypeContainer[]) inputs));
//            double priorProbability = 0.4 +
//                    (
//                            (numb == null
//                                    || numb.getNumbering().isFirstNumbering()
//                                    || (numb.getNumbering() instanceof SubSectionNumber && ((SubSectionNumber) numb.getNumbering()).firstSubsection())
//                            ) ? 0.6 : 0.0);
////                    ((((double) inSequence) / ((double) sections.size())) * 0.2);
//            return StandardRule.getLogProbability(priorProbability, inputs);
//        }
//
//        @Override
//        public ScoreChart.ParseTreeContainer apply(double logProb, ScoreChart.ParseTreeContainer... inputs) {
//            return new MultipleSectionBlobsContainer(this, inputs);
//        }
//
//    }
//
//    public static ScoreChart.ParseTreeContainer getFirstSectionContainer(TypeContainer... input) {
//        for (TypeContainer container : input) {
//            if (container.getType().equals(SECTION)) return (ScoreChart.ParseTreeContainer) container;
//            if (container.getType().equals(SECTION_BLOB))
//                return getFirstSection((ScoreChart.ParseTreeContainer) container);
//        }
//        throw new Error();
//    }
//
//    private static class MultipleSectionBlobsContainer extends ScoreChart.ParseTreeContainer {
//        public final ScoreChart.ParseTreeContainer lastSection;
//        public final ScoreChart.ParseTreeContainer firstSection;
//
//        public MultipleSectionBlobsContainer(Rule rule, ScoreChart.ParseTreeContainer... inputs) {
//            super(rule, inputs);
//            lastSection = getLastSection((TypeContainer[]) inputs); // B
//            firstSection = getFirstSection((TypeContainer[]) inputs); // C
//        }
//    }
//
//    /**
//     * COMPLETE_SECTION_CONTENT -> SECTION_CONTENT
//     */
//    private static class RateSectionContent extends StochasticRule {
//        public RateSectionContent(NonTerminal lhs, RightHandSide rhs) {
//            super(lhs, rhs, 1.0);
//        }
//
//
//        @Override
//        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
//            CYK.ParseTreeContainer sectionContent = inputs[0];
//
//            Deque<TypeContainer> contentsAsList = buildContents(new ArrayDeque<>(), sectionContent);
//            boolean hasDanglingTexts = !isUniform(contentsAsList);
//
//            double priorProbability = 0.99 +
//                    (hasDanglingTexts ? 0.0 : 0.01);
//            return StandardRule.getLogProbability(priorProbability, inputs);
//        }
//
//        private boolean isUniform(Deque<TypeContainer> contentsAsList) {
//            Type type = null;
//            for (TypeContainer tc : contentsAsList) {
//                if (type == null) {
//                    type = tc.getType();
//                } else {
//                    if (!tc.getType().equals(type)) return false;
//                }
//            }
//            return true;
//        }
//
//        private Deque<TypeContainer> buildContents(Deque<TypeContainer> acc, CYK.ParseTreeContainer sectionContent) {
//            for (TypeContainer input : sectionContent.getInputs()) {
//                CYK.ParseTreeContainer container = (CYK.ParseTreeContainer) input;
//                if (container.getType().equals(SECTION_CONTENT)) {
//                    buildContents(acc, container);
//                } else if (container.getType().equals(TEXT_BLOB) || container.getType().equals(SECTION_SEQUENCE)) {
//                    acc.push(container);
//                } else
//                    throw new InvalidParameterException("Input SECTION_BLOB should consist of only {SECTION, SECTION_BLOB}, not " + input.getType());
//            }
//            return acc;
//        }
//    }
    //    static class RechtspraakElementContainer {
//        private RechtspraakElementContainer() {
//        }
//        public static boolean hasSameMarkup() {
//            Set<String> title11 = getEmphases(section1);
//            Set<String> title22 = getEmphases(section2);
//            return title11.equals(title22);
//        }
//
//        private static Set<String> getEmphases(ScoreChart.ParseTreeContainer section1) {
//            TokenTreeLeaf sectionTitleFromSection = getSectionTitleFromSection(section1);
//            if (sectionTitleFromSection != null) {
//                Set<String> emphasis = sectionTitleFromSection.getEmphasis();
//                if (emphasis != null) return emphasis;
//            }
//            return Collections.emptySet();
//        }
//
//        public static boolean inDirectSequence(Numbering n1, Numbering n2) {
//            return (n1 == null && n2 == null)
//                    ||
////                    (n1 == null
////                            && n2.getNumbering().isFirstNumbering())
////                    ||
//                    (n1 != null
//                            && n2 != null
//                            // n2 is plausible succ of n1
//                            && n2.isSuccedentOf(n1)
//                            // ... but not a subsection successorship
//                            && isPureSuccessorshop(n1.getNumbering(), n2.getNumbering())
//                    );
//        }
//
//        public static boolean isPureSuccessorshop(NumberingNumber n1, NumberingNumber n2) {
//            if (n1 instanceof SubSectionNumber) {
//                return n2 instanceof SubSectionNumber
//                        && ((SubSectionNumber) n1).size() == ((SubSectionNumber) n2).size();
//            } else {
//                return !(n2 instanceof SubSectionNumber);
//            }
//        }
//
//        static TokenTreeLeaf getSectionTitleFromSection(ScoreChart.ParseTreeContainer sectionContainer) {
////            for (TypeContainer title : sectionContainer.getInputs()) {
//            // Section always starts with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0),
//            TypeContainer title = sectionContainer.getInputs()[0];
//            // All section titles...
//            if (!title.getType().equals(SECTION_TITLE))
//                throw new IllegalStateException("Section must start with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0)");
//
//            for (TypeContainer titleText : ((ScoreChart.ParseTreeContainer) title).getInputs()) { // Get inputs for title
//                if (titleText.getType().equals(SECTION_TITLE_TEXT)) { // Title text
//                    for (TypeContainer singleTitleText : ((ScoreChart.ParseTreeContainer) titleText).getInputs()) {
//                        if (singleTitleText.getType().equals(SINGLE_TITLE_TEXT)) {
//                            TypeContainer[] inputs = ((ScoreChart.ParseTreeContainer) singleTitleText).getInputs();
//                            TypeContainer terminal = inputs[0];
//                            if (inputs.length != 1 || !terminal.getType().equals(TERMINAL_SECTION_TITLE)) {
//                                throw new IllegalStateException("new StandardRule(SINGLE_TITLE_TEXT, rhs(TERMINAL_SECTION_TITLE), 1.0),");
//                            }
//                            // Get inputs for title text
//                            return (TokenTreeLeaf) ((Terminal) terminal).getData();
//                        }
//                    }
//                }
//            }
//
//            return null;
////            return sectionContainer.getInputs().stream()
////                    .filter(title -> title.getType().equals(SECTION_TITLE))
////                    .limit(1)// Maximum 1 title per section
////                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title
////                    .filter(title -> title.getType().equals(SECTION_TITLE_TEXT)) // Title text
////                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title text
////                    .filter(title -> title.getType().equals(SINGLE_TITLE_TEXT)) // Single title text
////                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title text
////                    .filter(title -> title.getType().equals(TERMINAL_SECTION_TITLE)) // Title text terminal
////                    .map(t -> (TokenTreeLeaf) ((Terminal) t).getData());
//        }
//
//        static Numbering getNumbering(ScoreChart.ParseTreeContainer sectionContainer) {
//            //            for (TypeContainer title : sectionContainer.getInputs()) {
//            // Section always starts with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0),
//            TypeContainer title = sectionContainer.getInputs()[0];
//            // All section titles...
//            if (!title.getType().equals(SECTION_TITLE))
//                throw new IllegalStateException("Section must start with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0)");
//
//            for (TypeContainer singleNumb : ((ScoreChart.ParseTreeContainer) title).getInputs()) { // Get inputs for title
//                if (singleNumb.getType().equals(SINGLE_NUMBERING)) { // Title numbering
//                    TypeContainer[] inputs = ((ScoreChart.ParseTreeContainer) singleNumb).getInputs();
//                    TypeContainer terminal = inputs[0];
//                    return ((Numbering) ((Terminal) terminal).getData());
//                }
//            }
//            return null;
//        }
//
//        /**
//         * @param inputs Should recursively contain at least 1 section
//         * @return last section
//         */
//        public static ScoreChart.ParseTreeContainer getLastSection(TypeContainer... inputs) {
//            for (int i = inputs.length - 1; i >= 0; i--) {
//                TypeContainer input = inputs[i];
//                if (input instanceof MultipleSectionBlobsContainer)
//                    return ((MultipleSectionBlobsContainer) input).lastSection;
//
//                if (input.getType().equals(SECTION)) return (ScoreChart.ParseTreeContainer) input;
//                if (input.getType().equals(SECTION_BLOB))
//                    return getLastSection(((ScoreChart.ParseTreeContainer) input).inputs);
//            }
//            throw new InvalidParameterException("Input should have at least one SECTION...");
//        }
//
//        /**
//         * @param inputs Should recursively contain at least 1 section
//         * @return first section
//         */
//        public static ScoreChart.ParseTreeContainer getFirstSection(TypeContainer... inputs) {
//            for (TypeContainer input : inputs) {
//                if (input instanceof MultipleSectionBlobsContainer)
//                    return ((MultipleSectionBlobsContainer) input).firstSection;
//
//                if (input.getType().equals(SECTION)) return (ScoreChart.ParseTreeContainer) input;
//                if (input.getType().equals(SECTION_BLOB))
//                    return getFirstSection(((ScoreChart.ParseTreeContainer) input).inputs);
//            }
//            throw new InvalidParameterException("Input should have at least one SECTION...");
//        }
//
////        /**
////         * @param inputs arguments for SECTION_BLOB container
////         * @return all sections
////         */
////        public static Deque<ScoreChart.ParseTreeContainer> getSections(ScoreChart.ParseTreeContainer... inputs) {
////            Deque<ScoreChart.ParseTreeContainer> l = new ArrayDeque<>();
////            getSections(l, inputs);
////            if (l.size() <= 0) System.err.println("0 sections found. deque better of as null");
////            return l;
////        }
////
////        static void getSections(Deque<ScoreChart.ParseTreeContainer> acc, TypeContainer[] inputs) {
////            for (TypeContainer input : inputs) {
////                ScoreChart.ParseTreeContainer container = (ScoreChart.ParseTreeContainer) input;
////                if (container.getType().equals(SECTION)) acc.add(container);
////                else if (container.getType().equals(SECTION_BLOB)) getSections(acc, container.getInputs());
////                else if (container.getType().equals(SECTION_SEQUENCE))
////                    getSections(acc, container.getInputs());
////                else if (container.getType().equals(SECTION_CONTENT)) getSections(acc, container.getInputs());
////                else //noinspection StatementWithEmptyBody
////                    if (container.getType().equals(TEXT_BLOB)) {
////                        //do nothing
////                    } else
////                        throw new InvalidParameterException("Input SECTION_BLOB should consist of only {SECTION, SECTION_BLOB}, not " + input.getType());
////            }
////        }
//    }
}
