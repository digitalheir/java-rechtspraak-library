package org.leibnizcenter.rechtspraak.enricher;

import com.google.common.collect.Lists;
import org.leibnizcenter.rechtspraak.enricher.cfg.Grammar;
import org.leibnizcenter.rechtspraak.enricher.cfg.ScoreChart;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.RightHandSide;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.StandardRule;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.StochasticRule;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.TypeContainer;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.interfaces.Rule;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.NonTerminalImpl;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.NonTerminal;
import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.Type;
import org.leibnizcenter.rechtspraak.tagging.Label;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.SubSectionNumber;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.security.InvalidParameterException;
import java.util.*;

import static org.leibnizcenter.rechtspraak.enricher.DocumentGrammar.RechtspraakElementContainer.*;

/**
 * CFG for making a Rechtspraak.nl XML document hierarchy
 * Created by maarten on 19-4-16.
 */
@SuppressWarnings("WeakerAccess")
public class DocumentGrammar extends Grammar {
    public static final NonTerminal DOCUMENT = new NonTerminalImpl("#root");
    public static final NonTerminal HEADER = new NonTerminalImpl("Header");
    public static final NonTerminal DOCUMENT_CONTENT = new NonTerminalImpl("DocumentContent");

    /**
     * 1 or more sections in sequence
     */
    public static final NonTerminal SECTION_BLOB = new NonTerminalImpl("Sections");

    public static final NonTerminal SECTION = new NonTerminalImpl("Section");
    public static final NonTerminal SECTION_TITLE = new NonTerminalImpl("SectionTitle");
    public static final NonTerminal NEWLINE_AND_TITLE_TEXT = new NonTerminalImpl("NEWLINE_AND_TITLE_TEXT");
    public static final NonTerminal SECTION_CONTENT = new NonTerminalImpl("SectionContent");
    public static final NonTerminal COMPLETE_SECTION_BLOB = new NonTerminalImpl("COMPLETE_SECTION_BLOB");
    public static final NonTerminal COMPLETE_SECTION_BLOB_W_TRAILING_TEXT = new NonTerminalImpl("COMPLETE_SECTION_BLOB_W_TRAILING_TEXT");

    public static final NonTerminal TEXT_BLOB = new NonTerminalImpl("Text");
    public static final NonTerminal SECTION_TITLE_TEXT = new NonTerminalImpl("SECTION_TITLE_TEXT");
    public static final NonTerminal SINGLE_TITLE_TEXT = new NonTerminalImpl("SINGLE_TITLE_TEXT");
    public static final NonTerminal SINGLE_NUMBERING = new NonTerminalImpl("SINGLE_NUMBERING");
    public static final NonTerminal COMPLETE_SECTION_CONTENT = new NonTerminalImpl("COMPLETE_SECTION_CONTENT");
    public static final NonTerminal NONTERMINAL_NEWLINE = new NonTerminalImpl("NONTERMINAL_NEWLINE");


    public static final Terminal TERMINAL_NUMBERING = new Terminal(Label.NR);
    public static final Terminal TERMINAL_SECTION_TITLE = new Terminal(Label.SECTION_TITLE);
    public static final Terminal TERMINAL_TEXT = new Terminal(Label.TEXT_BLOCK);
    public static final Terminal TERMINAL_NEWLINE = new Terminal(Label.NEWLINE);

    private static final Collection<Rule> RULES = Lists.newArrayList(
            //TODO newlines are possible anywhere

            new StandardRule(DOCUMENT, rhs(HEADER, DOCUMENT_CONTENT), 1.0),
            new StandardRule(DOCUMENT, rhs(DOCUMENT_CONTENT), 1.0),

            new StandardRule(DOCUMENT_CONTENT, rhs(COMPLETE_SECTION_BLOB), 1.0),
            new StandardRule(DOCUMENT_CONTENT, rhs(TEXT_BLOB, COMPLETE_SECTION_BLOB), 0.8),
            new StandardRule(DOCUMENT_CONTENT, rhs(COMPLETE_SECTION_BLOB, TEXT_BLOB), 0.8),
            new StandardRule(DOCUMENT_CONTENT, rhs(TEXT_BLOB, COMPLETE_SECTION_BLOB_W_TRAILING_TEXT), 0.8),

            new CompletedSectionBlob(COMPLETE_SECTION_BLOB, rhs(SECTION_BLOB)),
            new StandardRule(COMPLETE_SECTION_BLOB_W_TRAILING_TEXT, rhs(COMPLETE_SECTION_BLOB, TEXT_BLOB), 1.0),

            new TwoSectionBlobs(SECTION_BLOB, rhs(SECTION_BLOB, SECTION_BLOB)),
            new StandardRule(SECTION_BLOB, rhs(SECTION), 1.0),
            new StandardRule(SECTION_BLOB, rhs(SECTION, TEXT_BLOB), 0.8),
            new StandardRule(SECTION_BLOB, rhs(TEXT_BLOB, SECTION), 0.8),


            new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0),

            // TODO allow newlines in title?

            new StandardRule(COMPLETE_SECTION_CONTENT, rhs(SECTION_CONTENT), 1.0),

            new StandardRule(SECTION_CONTENT, rhs(TEXT_BLOB), 1.0),
            new StandardRule(SECTION_CONTENT, rhs(COMPLETE_SECTION_BLOB), 1.0),
            new StandardRule(SECTION_CONTENT, rhs(SECTION_CONTENT, SECTION_CONTENT), 1.0),

            //
            // Section Title
            //

            new StandardRule(SECTION_TITLE, rhs(SINGLE_NUMBERING), 1.0),
            new StandardRule(SECTION_TITLE, rhs(SECTION_TITLE_TEXT), 1.0),
            new StandardRule(SECTION_TITLE, rhs(SINGLE_NUMBERING, SECTION_TITLE_TEXT), 1.0),

            new StandardRule(NONTERMINAL_NEWLINE, rhs(TERMINAL_NEWLINE), 1.0),
            new StandardRule(SECTION_TITLE_TEXT, rhs(SINGLE_TITLE_TEXT), 1.0),
            new StandardRule(SECTION_TITLE_TEXT, rhs(NONTERMINAL_NEWLINE,SECTION_TITLE_TEXT), 0.99),

            new StandardRule(SINGLE_TITLE_TEXT, rhs(TERMINAL_SECTION_TITLE), 1.0),

            new StandardRule(TEXT_BLOB, rhs(TERMINAL_TEXT), 1.0),
            new StandardRule(TEXT_BLOB, rhs(TERMINAL_NEWLINE), 1.0),
            new StandardRule(TEXT_BLOB, rhs(TEXT_BLOB, TEXT_BLOB), 1.0),


            new StandardRule(SINGLE_NUMBERING, rhs(TERMINAL_NUMBERING), 1.0)

    );

    private static RightHandSide rhs(Type... types) {
        return new RightHandSide(types);
    }

    static class RechtspraakElementContainer {
        private RechtspraakElementContainer() {
        }

        public static boolean hasSameMarkup(ScoreChart.ParseTreeContainer section1, ScoreChart.ParseTreeContainer section2) {
            Set<String> title11 = getEmphases(section1);
            Set<String> title22 = getEmphases(section2);
            return title11.equals(title22);
        }

        private static Set<String> getEmphases(ScoreChart.ParseTreeContainer section1) {
            TokenTreeLeaf sectionTitleFromSection = getSectionTitleFromSection(section1);
            if (sectionTitleFromSection != null) {
                Set<String> emphasis = sectionTitleFromSection.getEmphasis();
                if (emphasis != null) return emphasis;
            }
            return Collections.emptySet();
        }

        public static boolean inDirectSequence(Numbering n1, Numbering n2) {
            return (n1 == null && n2 == null)
                    ||
//                    (n1 == null
//                            && n2.getNumbering().isFirstNumbering())
//                    ||
                    (n1 != null
                            && n2 != null
                            // n2 is plausible succ of n1
                            && n2.isSuccedentOf(n1)
                            // ... but not a subsection successorship
                            && isPureSuccessorshop(n1.getNumbering(), n2.getNumbering())
                    );
        }

        public static boolean isPureSuccessorshop(NumberingNumber n1, NumberingNumber n2) {
            if (n1 instanceof SubSectionNumber) {
                return n2 instanceof SubSectionNumber
                        && ((SubSectionNumber) n1).size() == ((SubSectionNumber) n2).size();
            } else {
                return !(n2 instanceof SubSectionNumber);
            }
        }

        static TokenTreeLeaf getSectionTitleFromSection(ScoreChart.ParseTreeContainer sectionContainer) {
//            for (TypeContainer title : sectionContainer.getInputs()) {
            // Section always starts with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0),
            TypeContainer title = sectionContainer.getInputs()[0];
            // All section titles...
            if (!title.getType().equals(SECTION_TITLE))
                throw new IllegalStateException("Section must start with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0)");

            for (TypeContainer titleText : ((ScoreChart.ParseTreeContainer) title).getInputs()) { // Get inputs for title
                if (titleText.getType().equals(SECTION_TITLE_TEXT)) { // Title text
                    for (TypeContainer singleTitleText : ((ScoreChart.ParseTreeContainer) titleText).getInputs()) {
                        if (singleTitleText.getType().equals(SINGLE_TITLE_TEXT)) {
                            TypeContainer[] inputs = ((ScoreChart.ParseTreeContainer) singleTitleText).getInputs();
                            TypeContainer terminal = inputs[0];
                            if (inputs.length != 1 || !terminal.getType().equals(TERMINAL_SECTION_TITLE)) {
                                throw new IllegalStateException("new StandardRule(SINGLE_TITLE_TEXT, rhs(TERMINAL_SECTION_TITLE), 1.0),");
                            }
                            // Get inputs for title text
                            return (TokenTreeLeaf) ((Terminal) terminal).getData();
                        }
                    }
                }
            }

            return null;
//            return sectionContainer.getInputs().stream()
//                    .filter(title -> title.getType().equals(SECTION_TITLE))
//                    .limit(1)// Maximum 1 title per section
//                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title
//                    .filter(title -> title.getType().equals(SECTION_TITLE_TEXT)) // Title text
//                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title text
//                    .filter(title -> title.getType().equals(SINGLE_TITLE_TEXT)) // Single title text
//                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title text
//                    .filter(title -> title.getType().equals(TERMINAL_SECTION_TITLE)) // Title text terminal
//                    .map(t -> (TokenTreeLeaf) ((Terminal) t).getData());
        }

        static Numbering getNumbering(ScoreChart.ParseTreeContainer sectionContainer) {
            //            for (TypeContainer title : sectionContainer.getInputs()) {
            // Section always starts with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0),
            TypeContainer title = sectionContainer.getInputs()[0];
            // All section titles...
            if (!title.getType().equals(SECTION_TITLE))
                throw new IllegalStateException("Section must start with a section title, see rule new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0)");

            for (TypeContainer singleNumb : ((ScoreChart.ParseTreeContainer) title).getInputs()) { // Get inputs for title
                if (singleNumb.getType().equals(SINGLE_NUMBERING)) { // Title numbering
                    TypeContainer[] inputs = ((ScoreChart.ParseTreeContainer) singleNumb).getInputs();
                    TypeContainer terminal = inputs[0];
                    return ((Numbering) ((Terminal) terminal).getData());
                }
            }
            return null;
        }

        /**
         * @param inputs Should recursively contain at least 1 section
         * @return last section
         */
        public static ScoreChart.ParseTreeContainer getLastSection(TypeContainer... inputs) {
            for (int i = inputs.length - 1; i >= 0; i--) {
                TypeContainer input = inputs[i];
                if (input instanceof MultipleSectionBlobsContainer)
                    return ((MultipleSectionBlobsContainer) input).lastSection;

                if (input.getType().equals(SECTION)) return (ScoreChart.ParseTreeContainer) input;
                if (input.getType().equals(SECTION_BLOB))
                    return getLastSection(((ScoreChart.ParseTreeContainer) input).inputs);
            }
            throw new InvalidParameterException("Input should have at least one SECTION...");
        }

        /**
         * @param inputs Should recursively contain at least 1 section
         * @return first section
         */
        public static ScoreChart.ParseTreeContainer getFirstSection(TypeContainer... inputs) {
            for (TypeContainer input : inputs) {
                if (input instanceof MultipleSectionBlobsContainer)
                    return ((MultipleSectionBlobsContainer) input).firstSection;

                if (input.getType().equals(SECTION)) return (ScoreChart.ParseTreeContainer) input;
                if (input.getType().equals(SECTION_BLOB))
                    return getFirstSection(((ScoreChart.ParseTreeContainer) input).inputs);
            }
            throw new InvalidParameterException("Input should have at least one SECTION...");
        }

//        /**
//         * @param inputs arguments for SECTION_BLOB container
//         * @return all sections
//         */
//        public static Deque<ScoreChart.ParseTreeContainer> getSections(ScoreChart.ParseTreeContainer... inputs) {
//            Deque<ScoreChart.ParseTreeContainer> l = new ArrayDeque<>();
//            getSections(l, inputs);
//            if (l.size() <= 0) System.err.println("0 sections found. deque better of as null");
//            return l;
//        }
//
//        static void getSections(Deque<ScoreChart.ParseTreeContainer> acc, TypeContainer[] inputs) {
//            for (TypeContainer input : inputs) {
//                ScoreChart.ParseTreeContainer container = (ScoreChart.ParseTreeContainer) input;
//                if (container.getType().equals(SECTION)) acc.add(container);
//                else if (container.getType().equals(SECTION_BLOB)) getSections(acc, container.getInputs());
//                else if (container.getType().equals(COMPLETE_SECTION_BLOB))
//                    getSections(acc, container.getInputs());
//                else if (container.getType().equals(SECTION_CONTENT)) getSections(acc, container.getInputs());
//                else //noinspection StatementWithEmptyBody
//                    if (container.getType().equals(TEXT_BLOB)) {
//                        //do nothing
//                    } else
//                        throw new InvalidParameterException("Input SECTION_BLOB should consist of only {SECTION, SECTION_BLOB}, not " + input.getType());
//            }
//        }
    }

    public DocumentGrammar() {
        super(DOCUMENT, RULES);
    }

    /**
     * Stick together two section sequences
     * <pre>
     * SECTION_BLOB -> SECTION_BLOB SECTION_BLOB
     * </pre>
     */
    private static class TwoSectionBlobs extends StochasticRule {
        public TwoSectionBlobs(NonTerminal lhs, RightHandSide rhs) {
            super(lhs, rhs, 1.0);
        }

        @SuppressWarnings("unused")
        @Override
        public double getLogProbability(ScoreChart.ParseTreeContainer... inputs) {
            // Adding at least two sections... See whether they match
            ScoreChart.ParseTreeContainer section1 = getLastSection(inputs[0]); // B
            ScoreChart.ParseTreeContainer section2 = getFirstSection(inputs[1]); // C

//            final TokenTreeLeaf titleA = getSectionTitleFromSection(section1);
//            final TokenTreeLeaf titleB = getSectionTitleFromSection(section2);
//            Numbering nr1 = getNumbering(section1);
//            Numbering nr2 = getNumbering(section2);

            double prior = 0.6
                    //+ (hasSameMarkup(section1, section2) ? 0.1 : 0.0)
                    + (inDirectSequence(getNumbering(section1), getNumbering(section2)) ? 0.4 : 0.0);
            return StandardRule.getLogProbability(prior, inputs);
        }

        @Override
        public ScoreChart.ParseTreeContainer apply(double logProb, ScoreChart.ParseTreeContainer... inputs) {
            if (inputs.length != 2) throw new InvalidParameterException();
            return new MultipleSectionBlobsContainer(this, inputs[0], inputs[1]);
        }

    }

//    /**
//     * SECTION_CONTENT -> COMPLETE_SECTION_BLOB
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
    private static class CompletedSectionBlob extends StochasticRule {
        public CompletedSectionBlob(NonTerminal lhs, RightHandSide rhs) {
            super(lhs, rhs, 1.0);
        }

//        public static int countInSequence(Deque<ScoreChart.ParseTreeContainer> subSections) {
//            if (allHaveNoNumbering(subSections)) return subSections.size();
//
//            int count = 0;
//            if (subSections.size() > 0) {
//                Iterator<ScoreChart.ParseTreeContainer> it = subSections.iterator();
//
//                Numbering firstNum = getNumbering(it.next());
//                if (firstNum != null
//                        && (isPlausibleFirstSubsection(firstNum.getNumbering())
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
//        public static boolean isPlausibleFirstSubsection(NumberingNumber firstNum) {
//            return firstNum instanceof SubSectionNumber && firstNum.isFirstNumbering();
//        }
//
//        private static boolean allHaveNoNumbering(Deque<ScoreChart.ParseTreeContainer> subSections) {
//            for (ScoreChart.ParseTreeContainer sec : subSections) if (getNumbering(sec) != null) return false;
//            return true;
//        }

        @Override
        public double getLogProbability(ScoreChart.ParseTreeContainer... inputs) {
            // See if we have subsections...
//            Deque<ScoreChart.ParseTreeContainer> sections = getSections(inputs);
            //if (sections.size() <= 0) throw new IllegalStateException();

            //check if subsections are IN >S E Q U E N C E<?????
//            int inSequence = countInSequence(sections);

//            boolean flawless = inSequence == sections.size();


            Numbering numb = getNumbering(getFirstSection((TypeContainer[]) inputs));
            double priorProbability = 0.4 +
                    (
                            (numb == null
                                    || numb.getNumbering().isFirstNumbering()
                                    || (numb.getNumbering() instanceof SubSectionNumber && ((SubSectionNumber) numb.getNumbering()).firstSubsection())
                            ) ? 0.6 : 0.0);
//                    ((((double) inSequence) / ((double) sections.size())) * 0.2);
            return StandardRule.getLogProbability(priorProbability, inputs);
        }

        @Override
        public ScoreChart.ParseTreeContainer apply(double logProb, ScoreChart.ParseTreeContainer... inputs) {
            return new MultipleSectionBlobsContainer(this, inputs);
        }

    }

//    public static ScoreChart.ParseTreeContainer getFirstSectionContainer(TypeContainer... input) {
//        for (TypeContainer container : input) {
//            if (container.getType().equals(SECTION)) return (ScoreChart.ParseTreeContainer) container;
//            if (container.getType().equals(SECTION_BLOB))
//                return getFirstSection((ScoreChart.ParseTreeContainer) container);
//        }
//        throw new Error();
//    }

    private static class MultipleSectionBlobsContainer extends ScoreChart.ParseTreeContainer {
        public final ScoreChart.ParseTreeContainer lastSection;
        public final ScoreChart.ParseTreeContainer firstSection;

        public MultipleSectionBlobsContainer(Rule rule, ScoreChart.ParseTreeContainer... inputs) {
            super(rule, inputs);
            lastSection = getLastSection((TypeContainer[]) inputs); // B
            firstSection = getFirstSection((TypeContainer[]) inputs); // C
        }
    }

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
//                } else if (container.getType().equals(TEXT_BLOB) || container.getType().equals(COMPLETE_SECTION_BLOB)) {
//                    acc.push(container);
//                } else
//                    throw new InvalidParameterException("Input SECTION_BLOB should consist of only {SECTION, SECTION_BLOB}, not " + input.getType());
//            }
//            return acc;
//        }
//    }
}
