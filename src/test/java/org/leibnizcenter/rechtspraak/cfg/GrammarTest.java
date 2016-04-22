package org.leibnizcenter.rechtspraak.cfg;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.leibnizcenter.rechtspraak.cfg.rule.Rule;
import org.leibnizcenter.rechtspraak.cfg.rule.type.NonTerminal;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Tests for grammar methods
 * Created by maarten on 21-4-16.
 */
public class GrammarTest {
    @Test
    public void hasVariable() throws Exception {

    }

    @Test
    public void testToString() throws Exception {
    }

    @Test
    public void hasEpsilons() throws Exception {

    }

    @Test
    public void hasNoEpsilonsExceptForStart() throws Exception {

    }

    @Test
    public void isInChomskyNormalFormWithUnaries() throws Exception {

    }

    @Test
    public void convertToChomskyNormalFormWithUnaries() throws Exception {

    }

    @Test
    public void CNF_BIN() throws Exception {

    }

    @Test
    public void getStartSymbol() throws Exception {

    }

    /**
     * For example, in the following grammar, with start symbol S0,
     * <p>
     * S0 → AbB | C
     * B → AA | AC
     * C → b | c
     * A → a | ε
     * the nonterminal A, and hence also B, is nullable, while neither C nor S0 is.
     * Hence the following intermediate grammar is obtained:
     * <p>
     * S0 → AbB | AbB | AbB | AbB   |   C
     * B → AA | AA | AA | AεA   |   AC | AC
     * C → b | c
     * A → a | ε
     * In this grammar, all ε-rules have been "inlined at the call site".
     * In the next step, they can hence be deleted, yielding the grammar:
     * <p>
     * S0 → AbB | Ab | bB | b   |   C
     * B → AA | A   |   AC | C
     * C → b | c
     * A → a
     * This grammar produces the same language as the original example grammar, viz.
     * {ab,aba,abaa,abab,abac,abb,abc,b,bab,bac,bb,bc,c}, but apparently has no ε-rules.
     *
     * @throws Exception
     */
    @Test
    public void CNF_DEL() throws Exception {
        Collection<Rule> rules = Rule.parseRules(
                String.join("\n",
                        "S0 → A b B | C",
                        "B → A A | A C",
                        "C → b | c",
                        "A → a | ε"
                )
        );
        Grammar g = new Grammar(new NonTerminal("S0"), rules);

        Set<String> words = Sets.newHashSet(g).stream().map(Object::toString).collect(Collectors.toSet());
        assertEquals(words, Sets.newHashSet(
                "b", "c", "baa", "ba",
                "ba", "b", "bab", "bac",
                "bc", "bb",
                "abaa",
                "aba", "ab",
                "aba", "abac",
                "abc", "abab", "abb"
        ));

        Set<String> words2 = Sets.newHashSet(g.CNF_DEL()).stream().map(Object::toString).collect(Collectors.toSet());
        assertEquals(words, words2);
    }

    @Test
    public void CNF_START() throws Exception {

    }

    @Test
    public void CNF_TERM() throws Exception {

    }

}