package org.leibnizcenter.rechtspraak.enricher.mostlikelytreefromlist.interfaces;

import org.leibnizcenter.rechtspraak.util.immutabletree.ImmutableTree;

import java.util.Deque;

/**
 * Created by Maarten on 2016-04-11.
 */
public interface PenaltyCalculator {
    int getPenalty(Deque<ImmutableTree> addToPath, ImmutableTree nodeToAdd);
}
