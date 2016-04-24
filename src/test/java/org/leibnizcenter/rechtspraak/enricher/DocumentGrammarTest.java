package org.leibnizcenter.rechtspraak.enricher;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.cfg.CYK;
import org.leibnizcenter.rechtspraak.cfg.rule.Term;
import org.leibnizcenter.rechtspraak.cfg.rule.type.Terminal;

import java.util.Iterator;
import java.util.List;

/**
 * Created by maarten on 21-4-16.
 */
public class DocumentGrammarTest {
    @Test
    public void test() {
        DocumentGrammar grammar = new DocumentGrammar();
        Iterator<Term> iterator = grammar.iterator();
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());
        System.out.println(iterator.next());

        List<Terminal> sentence = Lists.newArrayList(
                DocumentGrammar.TERMINAL_NEWLINE,
                DocumentGrammar.TERMINAL_NEWLINE,
                DocumentGrammar.TERMINAL_NEWLINE,
                DocumentGrammar.TERMINAL_NUMBERING,
                DocumentGrammar.TERMINAL_SECTION_TITLE,
                DocumentGrammar.TERMINAL_NEWLINE,
                DocumentGrammar.TERMINAL_TEXT,
                DocumentGrammar.TERMINAL_TEXT,
                DocumentGrammar.TERMINAL_TEXT,
                DocumentGrammar.TERMINAL_SECTION_TITLE,
                DocumentGrammar.TERMINAL_NEWLINE
        );
        CYK.ParseTreeContainer tree = CYK.getBestParseTree(
                sentence, grammar, grammar.getStartSymbol()
        );

        System.out.println(tree);
    }
}