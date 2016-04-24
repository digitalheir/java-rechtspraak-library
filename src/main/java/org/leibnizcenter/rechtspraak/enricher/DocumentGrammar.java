package org.leibnizcenter.rechtspraak.enricher;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.cfg.CYK;
import org.leibnizcenter.rechtspraak.cfg.Grammar;
import org.leibnizcenter.rechtspraak.cfg.rule.RightHandSide;
import org.leibnizcenter.rechtspraak.cfg.rule.StandardRule;
import org.leibnizcenter.rechtspraak.cfg.rule.StochasticRule;
import org.leibnizcenter.rechtspraak.cfg.rule.TypeContainer;
import org.leibnizcenter.rechtspraak.cfg.rule.interfaces.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminalImpl;
import org.leibnizcenter.rechtspraak.cfg.rule.type.interfaces.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.interfaces.Type;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;
import org.leibnizcenter.rechtspraak.tokens.numbering.Numbering;
import org.leibnizcenter.rechtspraak.tokens.numbering.SubSectionNumber;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;
import org.leibnizcenter.rechtspraak.tokens.text.TokenTreeLeaf;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.leibnizcenter.rechtspraak.enricher.DocumentGrammar.RechtspraakElementContainer.*;

/**
 * CFG for making a Rechtspraak.nl XML document hierarchy
 * Created by maarten on 19-4-16.
 */
@SuppressWarnings("WeakerAccess")
public class DocumentGrammar extends Grammar {
    public static final NonTerminal DOCUMENT = new NonTerminalImpl("#root");
    public static final NonTerminal HEADER = new NonTerminalImpl("Header");
    public static final NonTerminal DOCUMENT_CONTENT = new NonTerminalImpl("Content");

    /**
     * 1 or more sections in sequence
     */
    public static final NonTerminal SECTION_BLOB = new NonTerminalImpl("Sections");

    public static final NonTerminal SECTION = new NonTerminalImpl("Section");
    public static final NonTerminal SECTION_TITLE = new NonTerminalImpl("SectionTitle");
    public static final NonTerminal SECTION_CONTENT = new NonTerminalImpl("SectionContent");
    public static final NonTerminal COMPLETE_SECTION_BLOB = new NonTerminalImpl("COMPLETE_SECTION_BLOB");

    public static final NonTerminal TEXT_BLOB = new NonTerminalImpl("Text");
    public static final NonTerminal SECTION_TITLE_TEXT = new NonTerminalImpl("SECTION_TITLE_TEXT");
    public static final NonTerminal SINGLE_TITLE_TEXT = new NonTerminalImpl("SINGLE_TITLE_TEXT");
    public static final NonTerminal SINGLE_NUMBERING = new NonTerminalImpl("SINGLE_NUMBERING");
    public static final NonTerminal COMPLETE_SECTION_CONTENT = new NonTerminalImpl("COMPLETE_SECTION_CONTENT");


    public static final Terminal TERMINAL_NUMBERING = new Terminal(Label.NR);
    public static final Terminal TERMINAL_SECTION_TITLE = new Terminal(Label.SECTION_TITLE);
    public static final Terminal TERMINAL_TEXT = new Terminal(Label.TEXT_BLOCK);
    public static final Terminal TERMINAL_NEWLINE = new Terminal(Label.NEWLINE);

    private static final Collection<Rule> RULES = Lists.newArrayList(
            //TODO newlines are possible anywhere

            new StandardRule(DOCUMENT, rhs(HEADER, DOCUMENT_CONTENT), 1.0),
            new StandardRule(DOCUMENT, rhs(DOCUMENT_CONTENT), 1.0),

            new StandardRule(DOCUMENT_CONTENT, rhs(TEXT_BLOB), 1.0),
            new StandardRule(DOCUMENT_CONTENT, rhs(COMPLETE_SECTION_BLOB), 1.0),
            new StandardRule(DOCUMENT_CONTENT, rhs(DOCUMENT_CONTENT, DOCUMENT_CONTENT), 1.0),

            new CompleteSectionBlob(COMPLETE_SECTION_BLOB, rhs(SECTION_BLOB)),

            new TwoSectionSequences(SECTION_BLOB, rhs(SECTION_BLOB, SECTION_BLOB)),
            new StandardRule(SECTION_BLOB, rhs(SECTION), 1.0),


            new StandardRule(SECTION, rhs(SECTION_TITLE, COMPLETE_SECTION_CONTENT), 1.0),

            // TODO allow newlines in title?
            new StandardRule(SECTION_TITLE, rhs(SINGLE_NUMBERING), 1.0),
            new StandardRule(SECTION_TITLE, rhs(SECTION_TITLE_TEXT), 1.0),
            new StandardRule(SECTION_TITLE, rhs(SINGLE_NUMBERING, SECTION_TITLE_TEXT), 1.0),


            new StandardRule(SECTION_TITLE_TEXT, rhs(SINGLE_TITLE_TEXT), 1.0),
            new StandardRule(SECTION_TITLE_TEXT, rhs(SINGLE_TITLE_TEXT, SINGLE_TITLE_TEXT), 1.0),

            new StandardRule(SINGLE_TITLE_TEXT, rhs(TERMINAL_SECTION_TITLE), 1.0),

            new StandardRule(TEXT_BLOB, rhs(TERMINAL_TEXT), 1.0),
            new StandardRule(TEXT_BLOB, rhs(TERMINAL_NEWLINE), 1.0),
            new StandardRule(TEXT_BLOB, rhs(TEXT_BLOB, TEXT_BLOB), 1.0),

            new MyStochasticRule(COMPLETE_SECTION_CONTENT, rhs(SECTION_CONTENT)),

            new StandardRule(SINGLE_NUMBERING, rhs(TERMINAL_NUMBERING), 1.0),

            new StandardRule(SECTION_CONTENT, rhs(TEXT_BLOB), 1.0),
            new Subsections(SECTION_CONTENT, rhs(COMPLETE_SECTION_BLOB)),
            new StandardRule(SECTION_CONTENT, rhs(SECTION_CONTENT, SECTION_CONTENT), 1.0)
    );

    private static RightHandSide rhs(Type... types) {
        return new RightHandSide(types);
    }

    public static class RechtspraakElementContainer {
        public static boolean hasSameMarkup(CYK.ParseTreeContainer section1, CYK.ParseTreeContainer section2) {
            Set<String> title1 = getEmphases(section1);
            Set<String> title2 = getEmphases(section2);
            return title1.equals(title2);
        }

        private static Set<String> getEmphases(CYK.ParseTreeContainer section1) {
            return getSectionTitleFromSection(section1)
                    .map(TokenTreeLeaf::getEmphasis)
                    .filter(e -> e != null)
                    .reduce(Sets::union)
                    .orElse(Collections.emptySet());
        }

        public static boolean inDirectSequence(CYK.ParseTreeContainer section1, CYK.ParseTreeContainer section2) {
            Optional<Numbering> n1 = getNumbering(section1);
            Optional<Numbering> n2 = getNumbering(section2);

            return (!n1.isPresent() && !n2.isPresent())
                    ||
                    (n1.isPresent()
                            && n2.isPresent()
                            && n2.get().isSuccedentOf(n1.get()) // n2 is plausible succ of n1
                            && !isPlausibleFirstSubsection(n2.get()) // ... but not a subsection
                    );
        }

        private static boolean isPlausibleFirstSubsection(Numbering numberingForSection) {
            return numberingForSection.getNumbering() instanceof SubSectionNumber
                    && ((SubSectionNumber) numberingForSection.getNumbering()).firstSubsection();
        }

        static Stream<TokenTreeLeaf> getSectionTitleFromSection(CYK.ParseTreeContainer sectionContainer) {
            return sectionContainer
                    .getInputs().stream()
                    .filter(title -> title.getType().equals(SECTION_TITLE))
                    .limit(1)// Maximum 1 title per section
                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title
                    .filter(title -> title.getType().equals(SECTION_TITLE_TEXT)) // Title text
                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title text
                    .filter(title -> title.getType().equals(SINGLE_TITLE_TEXT)) // Single title text
                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title text
                    .filter(title -> title.getType().equals(TERMINAL_SECTION_TITLE)) // Title text terminal
                    .map(t -> (TokenTreeLeaf) ((Terminal) t).getData());
        }

        static Optional<Numbering> getNumbering(CYK.ParseTreeContainer sectionContainer) {
            return sectionContainer
                    .getInputs().stream()
                    .filter(title -> title.getType().equals(SECTION_TITLE))
                    .limit(1) // Maximum 1 title per section
                    .flatMap(title -> ((CYK.ParseTreeContainer) title).getInputs().stream()) // Get inputs for title
                    .filter(numbering -> numbering.getType().equals(SINGLE_NUMBERING))
                    .limit(1) // Maximum 1 numbering per title
                    .flatMap(num -> ((CYK.ParseTreeContainer) num).getInputs().stream()) // Get numbering for numbering container
                    .limit(1) // Maximum 1 numbering terminal per numbering container
                    .map(t -> ((Numbering) ((Terminal) t).getData()))
                    .findAny();
        }

        /**
         * @param input SECTION_BLOB container
         * @return last section
         */
        public static CYK.ParseTreeContainer getLastSection(CYK.ParseTreeContainer input) {
            for (int i = input.getInputs().size() - 1; i >= 0; i--) {
                CYK.ParseTreeContainer container = (CYK.ParseTreeContainer) input.getInputs().get(i);
                if (container.getType().equals(SECTION)) return container;
                if (container.getType().equals(SECTION_BLOB)) return getLastSection(container);
            }
            throw new InvalidParameterException("Input SECTION_BLOB should have at least one SECTION...");
        }

        /**
         * @param input SECTION_BLOB container
         * @return first section
         */
        public static CYK.ParseTreeContainer getFirstSection(CYK.ParseTreeContainer input) {
            CYK.ParseTreeContainer container = (CYK.ParseTreeContainer) input.getInputs().get(0);
            if (container.getType().equals(SECTION)) return container;
            if (container.getType().equals(SECTION_BLOB)) return getFirstSection(container);
            throw new InvalidParameterException("Input SECTION_BLOB should have at least one SECTION...");
        }

        /**
         * @param inputs arguments for SECTION_BLOB container
         * @return all sections
         */
        public static List<CYK.ParseTreeContainer> getSections(CYK.ParseTreeContainer... inputs) {
            ArrayList<CYK.ParseTreeContainer> l = new ArrayList<>();
            getSections(l, Arrays.asList(inputs));
            return l;
        }

        static void getSections(List<CYK.ParseTreeContainer> acc, List<TypeContainer> inputs) {
            for (TypeContainer input : inputs) {
                CYK.ParseTreeContainer container = (CYK.ParseTreeContainer) input;
                if (container.getType().equals(SECTION)) acc.add(container);
                else if (container.getType().equals(SECTION_BLOB)) getSections(acc, container.getInputs());
                else if (container.getType().equals(COMPLETE_SECTION_BLOB)) getSections(acc, container.getInputs());
                else if (container.getType().equals(SECTION_CONTENT)) getSections(acc, container.getInputs());
                else if (container.getType().equals(TEXT_BLOB)) break;
                else
                    throw new InvalidParameterException("Input SECTION_BLOB should consist of only {SECTION, SECTION_BLOB}, not " + input.getType());
            }
        }
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
    private static class TwoSectionSequences extends StochasticRule {
        public TwoSectionSequences(NonTerminal lhs, RightHandSide rhs) {
            super(lhs, rhs, 1.0);
        }

        @SuppressWarnings("unused")
        @Override
        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
            // Adding at least two sections... See whether they match
            CYK.ParseTreeContainer section1 = getLastSection(inputs[0]); // B
            CYK.ParseTreeContainer section2 = getFirstSection(inputs[1]); // C

            List<TokenTreeLeaf> title1 = getSectionTitleFromSection(section1).collect(Collectors.toList());
            List<TokenTreeLeaf> title2 = getSectionTitleFromSection(section2).collect(Collectors.toList());
            Numbering nr1 = getNumbering(section1).orElse(null);
            Numbering nr2 = getNumbering(section2).orElse(null);

            double prior = 0.5
                    + (hasSameMarkup(section1, section2) ? 0.1 : 0.0)
                    + (inDirectSequence(section1, section2) ? 0.4 : 0.0);
            return StandardRule.getLogProbability(prior, inputs);
        }

    }

    private static class Subsections extends StochasticRule {
        public Subsections(NonTerminal lhs, RightHandSide rhs) {
            super(lhs, rhs, 0.5);
        }

        @Override
        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
            ArrayList<CYK.ParseTreeContainer> subSequences = new ArrayList<>();
            getSections(subSequences, inputs[0].getInputs());


            double plausibleSubSequences = subSequences.stream()
                    .map(RechtspraakElementContainer::getNumbering)
                    .filter(Optional::isPresent).map(Optional::get)
                    .filter(n -> n != null)
                    .count();

            double likelySubSequences = subSequences.stream()
                    .map(RechtspraakElementContainer::getNumbering)
                    .filter(Optional::isPresent).map(Optional::get)
                    .filter(n -> n != null && n.getNumbering() instanceof SubSectionNumber)
                    .count();

            double priorProbability = 0.1
                    + 0.3 * (plausibleSubSequences / subSequences.size())
                    + 0.6 * (likelySubSequences / subSequences.size());
            return StandardRule.getLogProbability(priorProbability, inputs);
        }
    }

    private static class MyStochasticRule extends StochasticRule {
        public MyStochasticRule(NonTerminal LHS, RightHandSide RHS) {
            super(LHS, RHS, 1.0);
        }

        @Override
        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
            return StandardRule.getLogProbability(getPriorProbability(), inputs);
        }


    }

    private static class CompleteSectionBlob extends StochasticRule {
        public CompleteSectionBlob(NonTerminal lhs, RightHandSide rhs) {
            super(lhs, rhs, 1.0);
        }

        public static int countInSequence(List<CYK.ParseTreeContainer> subSections) {
            int count = 0;
            if (subSections.size() > 0) {
                Optional<Numbering> firstNum = getNumbering(subSections.get(0));
                if (firstNum.isPresent() && firstNum.get().getNumbering().isFirstNumbering()) {
                    count++;
                    NumberingNumber lastNum = firstNum.get().getNumbering();
                    for (int i = 1; i < subSections.size(); i++) {
                        Optional<Numbering> num = getNumbering(subSections.get(i));
                        if (num.isPresent()) {
                            if (firstNum.isPresent()
                                    && num.get().getNumbering().isSuccedentOf(lastNum)
                                    && !(num.get().getNumbering() instanceof SubSectionNumber
                                    && num.get().getNumbering().isFirstNumbering())
                                    ) {
                                lastNum = num.get().getNumbering();
                                count++;
                            }
                        }
                    }
                }
            }
            return count;
        }

        @Override
        public double getLogProbability(CYK.ParseTreeContainer... inputs) {
            // See if we have subsections...
            List<CYK.ParseTreeContainer> subSections = getSections(inputs[0]);

            //check if subsections are IN >S E Q U E N C E<?????
            int inSequence = countInSequence(subSections);

            if (subSections.size() <= 0) throw new IllegalStateException();
            double priorProbability = 0.1 +
                    (subSections.size() > 1 ? 0.1 : 0.0) +
                    (subSections.size() > 2 ? 0.1 : 0.0) +
                    (subSections.size() > 3 ? 0.1 : 0.0) +
                    (subSections.size() > 4 ? 0.1 : 0.0) +
                    ((((double) inSequence) / ((double) subSections.size())) * 0.5);
            return StandardRule.getLogProbability(priorProbability, inputs);
        }
    }
}
