//package org.leibnizcenter.rechtspraak.enricher;
//
//import org.leibnizcenter.cfg.category.terminal.Terminal;
//import org.leibnizcenter.cfg.token.Token;
//import org.leibnizcenter.rechtspraak.tagging.Label;
//import org.leibnizcenter.rechtspraak.tokens.LabeledToken;
//
///**
// * Utility functional interface
// * <p>
// * Created by Maarten on 23-8-2016.
// */
//public class HasToken implements Terminal<LabeledToken> {
//    private final Label label;
//
//    @SuppressWarnings("WeakerAccess")
//    public HasToken(Label label) {
//        this.label = label;
//    }
//
//    @Override
//    public boolean hasCategory(Token<LabeledToken> token) {
//        return label.equals(token.obj.getTag());
//    }
//}
