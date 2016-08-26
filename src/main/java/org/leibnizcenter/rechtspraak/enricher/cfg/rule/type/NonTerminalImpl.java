//package org.leibnizcenter.rechtspraak.enricher.cfg.rule.type;
//
//import org.jetbrains.annotations.NotNull;
//import org.leibnizcenter.rechtspraak.enricher.cfg.rule.type.interfaces.NonTerminal;
//
//import java.util.regex.Pattern;
//
///**
// * Created by Maarten on 2016-04-24.
// */
//public class NonTerminalImpl implements NonTerminal{
//    private final String name;
//    @SuppressWarnings("unused")
//    private static final Pattern REGEX = Pattern.compile("\\p{Lu}[\\p{L}0-9'\"]*");
//
//    /**
//     * @param name String to represent this terminal. Must be capitalized.
//     */
//    public NonTerminalImpl(@NotNull String name) {
////        if (!REGEX.matcher(name).matches())
////            throw new InvalidParameterException("Non-terminal must start with uppercase letter");
//        this.name = name;
//    }
//    @Override
//    public String toString() {
//        return getName();
//    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || !(o instanceof NonTerminal)) return false;
//
//        NonTerminal that = (NonTerminal) o;
//
//        return getName().equals(that.getName());
//    }
//
//    @Override
//    public int hashCode() {
//        return getName().hashCode();
//    }
//
//    public String getName() {
//        return name;
//    }
//}
