package org.leibnizcenter.rechtspraak.enricher;

import com.google.common.collect.Lists;
import org.leibnizcenter.rechtspraak.cfg.Grammar;
import org.leibnizcenter.rechtspraak.cfg.rule.RightHandSide;
import org.leibnizcenter.rechtspraak.cfg.rule.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Type;
import org.leibnizcenter.rechtspraak.leibnizannotations.Label;

import java.util.Collection;

/**
 * Created by maarten on 19-4-16.
 */
public class DocumentGrammar extends Grammar {
    public static final NonTerminal DOCUMENT = new NonTerminal("#root");
    public static final NonTerminal SECTIONS = new NonTerminal("Sections");
    public static final NonTerminal DOCUMENT_CONTENT = new NonTerminal("Content");
    public static final NonTerminal HEADER = new NonTerminal("Header");
    public static final NonTerminal SECTION = new NonTerminal("Section");
    public static final NonTerminal SECTION_TITLE = new NonTerminal("SectionTitle");
    public static final NonTerminal SECTION_CONTENT = new NonTerminal("SectionContent");
    public static final NonTerminal TEXT_BLOB = new NonTerminal("Text");
    public static final NonTerminal SECTION_TITLE_TEXT = new NonTerminal("SECTION_TITLE_TEXT");
    public static final NonTerminal SINGLE_TITLE_TEXT = new NonTerminal("SINGLE_TITLE_TEXT");
    public static final NonTerminal SINGLE_NUMBERING = new NonTerminal("SINGLE_NUMBERING");
//    private static final NonTerminal SECTIONS_POSSIBLY_FOLLOWED_BY_TEXT = new NonTerminal("SECTIONS_POSSIBLY_FOLLOWED_BY_TEXT");


    public static final Terminal TERMINAL_NUMBERING = new Terminal(Label.NR);
    public static final Terminal TERMINAL_SECTION_TITLE = new Terminal(Label.SECTION_TITLE);
    public static final Terminal TERMINAL_TEXT = new Terminal(Label.TEXT_BLOCK);
    public static final Terminal TERMINAL_NEWLINE = new Terminal(Label.NEWLINE);

    private static final Collection<Rule> RULES = Lists.newArrayList(
            //TODO newlines are possible anywhere

            new Rule(DOCUMENT, rhs(HEADER, DOCUMENT_CONTENT), 0.9),
            new Rule(DOCUMENT, rhs(DOCUMENT_CONTENT), 0.1),

            new Rule(DOCUMENT_CONTENT, rhs(TEXT_BLOB), 0.1),
            new Rule(DOCUMENT_CONTENT, rhs(SECTIONS), 0.1),
            new Rule(DOCUMENT_CONTENT, rhs(DOCUMENT_CONTENT, DOCUMENT_CONTENT), 0.2),

            new Rule(SECTIONS, rhs(SECTION), 0.5),
            new Rule(SECTIONS, rhs(SECTION, SECTIONS), 0.5),

            new Rule(SECTION, rhs(SECTION_CONTENT), 0.4),
            new Rule(SECTION, rhs(SECTION_TITLE, SECTION_CONTENT), 0.6),

            new Rule(SECTION_TITLE, rhs(TERMINAL_NUMBERING), 0.5),
            new Rule(SECTION_TITLE, rhs(SECTION_TITLE_TEXT), 0.5),
            new Rule(SECTION_TITLE, rhs(SINGLE_NUMBERING, SECTION_TITLE_TEXT), 0.5),

            new Rule(SINGLE_NUMBERING, rhs(TERMINAL_NUMBERING), 0.1),

            new Rule(SECTION_TITLE_TEXT, rhs(SINGLE_TITLE_TEXT), 0.1),
            new Rule(SECTION_TITLE_TEXT, rhs(SINGLE_TITLE_TEXT, SINGLE_TITLE_TEXT), 0.1),

            new Rule(SINGLE_TITLE_TEXT, rhs(TERMINAL_SECTION_TITLE), 0.1),

            new Rule(TEXT_BLOB, rhs(TERMINAL_TEXT), 0.5),
            new Rule(TEXT_BLOB, rhs(TERMINAL_NEWLINE), 0.5),
            new Rule(TEXT_BLOB, rhs(TEXT_BLOB, TEXT_BLOB), 0.5),

            new Rule(SECTION_CONTENT, rhs(TEXT_BLOB), 0.5),
            new Rule(SECTION_CONTENT, rhs(SECTIONS), 0.5),
            new Rule(SECTION_CONTENT, rhs(SECTION_CONTENT, SECTION_CONTENT), 0.5)

    );

    private static RightHandSide rhs(Type... types) {
        return new RightHandSide(types);
    }

    public DocumentGrammar() {
        super(DOCUMENT, RULES);
    }
}
