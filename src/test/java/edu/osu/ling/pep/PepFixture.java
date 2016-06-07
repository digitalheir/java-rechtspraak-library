/*
 * $Id: PepFixture.java 1797 2010-01-29 20:07:16Z scott $ 
 * Copyright (C) 2007 Scott Martin
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. The GNU Lesser General Public License is
 * distributed with this software in the file COPYING.
 */
package edu.osu.ling.pep;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import edu.osu.ling.pep.earley.Edge;
import edu.osu.ling.pep.grammar.*;
import junit.framework.TestCase;


/**
 * @author <a href="http://www.ling.osu.edu/~scott/">Scott Martin</a>
 * @version $LastChangedRevision: 1797 $
 */
public abstract class PepFixture extends TestCase {
    Grammar grammar, mixed;
    Category A, B, C, D, E, X, Y, Z, a, b;
    Category seed, S, NP, VP, Det, N, the, boy, girl, left;
    Rule rule1, rule2, rule3, rule4, rule5, rule6, rule7, rule8;
    Edge edge1, edge2, edge3;
    List<Token> tokens;

    public void testFixture() {
    }

    @Override
    protected void setUp() throws Exception {
        A = new Category("A", false);
        B = new Category("B", false);
        C = new Category("C", false);
        D = new Category("D", false);
        E = new Category("E", false);
        X = new Category("X", false);
        Y = new Category("Y", false);
        Z = new Category("Z", false);
        a = new Category("a", true);
        b = new Category("b", true);

        rule1 = new Rule(A, B, C, D, E);
        rule2 = new Rule(A, a);
        rule3 = new Rule(X, Y, Z);
        rule4 = new Rule(A, X, a);
        rule5 = new Rule(X, a, Z);
        rule6 = new Rule(Z, b);
        rule7 = new Rule(X, a);
        rule8 = new Rule(X, b);

        edge1 = new Edge(new DottedRule(rule1, 2), 3);
        edge2 = new Edge(new DottedRule(rule3, 0), 0);
        edge3 = new Edge(new DottedRule(rule2, 1), 2);

        grammar = new Grammar.Builder("test")
                .addRule(new Rule(S, NP, VP))
                .addRule(new Rule(NP, Det, N))
                .addRule(new Rule(VP, left))
                .addRule(new Rule(Det, a))
                .addRule(new Rule(Det, the))
                .addRule(new Rule(N, boy))
                .addRule(new Rule(N, girl))
                .build();

        S = new Category("S", false);
        seed = S;
        NP = new Category("NP", false);
        VP = new Category("VP", false);
        Det = new Category("Det", false);
        N = new Category("N", false);

        the = new Category("the", true);
        boy = new Category("boy", true);
        girl = new Category("girl", true);
        left = new Category("left", true);


        tokens = Lists.newArrayList(the.name, boy.name, left.name).stream()
                .map(Token::new)
                .collect(Collectors.toList());

        mixed = new Grammar.Builder("mixed")
                .addRule(rule4)
                .addRule(rule5)
                .addRule(rule6)
                .addRule(rule7)
                .addRule(rule8)
                .build();
    }
}
