package org.leibnizcenter.rechtspraak.tokens.numbering;

import com.google.common.collect.Lists;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.FullNumber;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.NumberingNumber;
import org.leibnizcenter.rechtspraak.tokens.numbering.interfaces.SingleTokenNumbering;
import org.leibnizcenter.rechtspraak.util.Collections3;
import org.leibnizcenter.rechtspraak.util.Pair;
import org.leibnizcenter.rechtspraak.util.Regex;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by maarten on 16-2-16.
 */
public class SubSectionNumber extends ArrayList<SingleTokenNumbering> implements NumberingNumber {
    private final String terminal;

    public SubSectionNumber(String s, String terminal) {
        CharSequence toCheck = s.toLowerCase(Locale.ENGLISH).trim();
        if (!Regex.REGEX_SUBSECTION.matcher(toCheck).matches()) {
            throw new InvalidParameterException("String '" + toCheck + "' did not follow regex pattern " + Regex.REGEX_SUBSECTION.pattern());
        }

        List<String> l = Lists.newArrayList(s.split("\\."));

        if (terminal != null) {
            terminal = terminal.trim();
            if (terminal.length() <= 0) terminal = null;
        }
        this.terminal = terminal;

        addAll(Lists.transform(l, NumberingNumber::parseFullInteger));
    }

    public SubSectionNumber(List<SingleTokenNumbering> nums, String terminal) {
        addAll(nums);
        this.terminal = terminal;
    }

//    public boolean isPredecessorTo(SubSectionNumber n1, SubSectionNumber n2) {
//        for (int s = n2.num.size() - 1; s >= 0; s--) {
//            int subNum = n2[s];
//        }
//    }

    /**
     * @return The (sub)section for which this numbering is a subsection
     */
    public SubSectionNumber firstSubsectionOf() {
        if (size() < 2) {
            throw new IllegalStateException("Numbering should have at least a main section and a subsection");
        }

        SingleTokenNumbering checkFor = (get(size() - 1));
        if (checkFor.couldBeFirstInSequence()) {
            return new SubSectionNumber(subList(0, size() - 1), terminal);
        } else {
            return new SubSectionNumber(Collections.emptyList(), terminal);
        }
    }

    /**
     * E.g., 1.2.3.1 is a sub-subsection of 1.2.3
     *
     * @param predecessor
     * @return
     */
    public boolean isSubSubSectionOf(SubSectionNumber predecessor) {
        if (this.size() == predecessor.size() + 1) {
            for (int i = 0; i < predecessor.size() - 1; i++) {
                if (!predecessor.get(i).equals(get(i))) {
                    return false;
                }
            }
            SingleTokenNumbering trailingNumber = trailingNumber();
            return trailingNumber.couldBeFirstInSequence();
        } else {
            return false;
        }
    }

    @Override
    public boolean isSuccedentOf(NumberingNumber precedent) {
        List<SingleTokenNumbering> succedent = this;

        if (precedent instanceof FullNumber) {
            // E.g., 3 -> 3.1
            return succedent.size() == 2
                    && succedent.get(0).equalsSansTerminal(precedent) // [3].1
                    && succedent.get(1) instanceof FullNumber
                    && FullNumber.isFirstNumberInSequence((FullNumber) succedent.get(1)); // 3.[1]
        } else if (precedent instanceof SingleCharNumbering) {
            // E.g., a -> a.1
            return succedent.size() == 2
                    && succedent.get(0).equalsSansTerminal(precedent) // [3].1
                    && succedent.get(1) instanceof SingleCharNumbering
                    && succedent.get(1).couldBeFirstInSequence(); // a.[1] //
        } else if (precedent instanceof SubSectionNumber) {
            // E.g., 1.2.2 -> 1.2.3:
            SubSectionNumber predecessor = ((SubSectionNumber) precedent);

            //noinspection SimplifiableIfStatement
            // all numbers are the same except the last ...
            int ixLastPred = predecessor.size() - 1;
            int ixLastSucc = succedent.size() - 1;
            if (predecessor.size() == this.size()) {
                for (int i = 0; i < ixLastPred; i++) {
                    if (!predecessor.get(i).equals(this.get(i))) {
                        return false;
                    }
                }
                // ... the last is +1
                return this.trailingNumber().isSuccedentOf(predecessor.trailingNumber());
            } else if (predecessor.size() == this.size() - 1) {
                // 1.2.2 -> 1.2.2.1
                return this.isSubSubSectionOf(predecessor);
            } else if (predecessor.size() == this.size() + 1) {
                // 1.2.2 -> 1.3
                for (int i = 0; i < ixLastSucc; i++) {
                    if (!predecessor.get(i).equals(succedent.get(i))) {
                        return false;
                    }
                }
                SingleTokenNumbering lastSucc = predecessor.get(ixLastSucc);
                return lastSucc instanceof FullNumber && succedent.get(ixLastSucc).equals(((FullNumber) lastSucc).succ());
            } else return false;
        } else {
            throw new InvalidParameterException(precedent.getClass().getSimpleName());
        }
    }

    public SingleTokenNumbering trailingNumber() {
        return get(size() - 1);
    }

    /**
     * A subsection numbering is never the first numbering. By definition it needs to be preceded
     * by {@link FullNumber}
     *
     * @return false
     */
    @Override
    public boolean isFirstNumbering() {
        return false;
    }


    @Override
    public String getTerminal() {
        return terminal;
    }

    @Override
    public String toString() {
        return String.join(".", Lists.transform(this, Object::toString));
    }


    @Override
    public boolean equals(Object obj) {
        // All sub elements equal
        return obj instanceof SubSectionNumber
                && Objects.equals(this.terminal, ((SubSectionNumber) obj).getTerminal())
                && this.size() == ((SubSectionNumber) obj).size()
                && Collections3.zip(this.stream(), ((SubSectionNumber) obj).stream())
                .filter((p) -> !p.getKey().equals(p.getValue()))
                .limit(1)
                .collect(Collectors.toSet())
                .size() <= 0;
    }

    @Override
    public boolean equalsSansTerminal(Object obj) {
        // All sub elements equal sans terminal
        return obj instanceof SubSectionNumber
                && this.size() == ((SubSectionNumber) obj).size()
                && Collections3.zip(this.stream(), ((SubSectionNumber) obj).stream())
                .filter((p) -> !p.getKey().equalsSansTerminal(p.getValue()))
                .limit(1)
                .collect(Collectors.toSet())
                .size() <= 0;
    }

    public boolean firstSubsection() {
        SingleTokenNumbering checkFor = (get(size() - 1));
        return checkFor.couldBeFirstInSequence();
    }
}
