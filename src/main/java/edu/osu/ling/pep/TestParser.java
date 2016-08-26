package edu.osu.ling.pep;

import com.google.common.collect.Lists;
import edu.osu.ling.pep.earley.ParserOption;
import edu.osu.ling.pep.grammar.Category;
import edu.osu.ling.pep.grammar.Grammar;
import edu.osu.ling.pep.grammar.Rule;
import edu.osu.ling.pep.grammar.Token;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Maarten on 2016-06-06.
 */
public class TestParser {
    private static final Category S = Category.nonTerminal("S");
    private static final Category ac = Category.terminal("a");
    private static final Category bc = Category.terminal("b");
    private static final Token a = new Token("a");
    private static final Token b = new Token("b");

    public static void main(String[] args) throws PepException {
        Map<ParserOption, Boolean> options = new EnumMap<>(
                ParserOption.class);
        Pep pep = new Pep(options);
        Grammar grammar = new Grammar.Builder()
                .addRule(new Rule(S, ac, S, ac))
                .addRule(new Rule(S, bc))
                .build();
        List<Token> tokens = Lists.newArrayList(a,a,a,a,b,a,a,a,a);
        pep.parse(grammar, tokens, S);
    }


}
