//package org.leibnizcenter.rechtspraak.util;
//
//import opennlp.tools.tokenize.TokenizerME;
//import opennlp.tools.tokenize.TokenizerModel;
//import opennlp.tools.util.Span;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by maarten on 22-12-15.
// */
//public class Tokenizer implements opennlp.tools.tokenize.Tokenizer {
//    private final TokenizerModel model;
//    private final opennlp.tools.tokenize.Tokenizer utils;
//
//    public Tokenizer(Language lang) throws IOException {
//        this.model = loadTokenizer(lang);
//        utils = new TokenizerME(model);
//    }
//
//    @Override
//    public String[] tokenize(String str) {
//        return utils.tokenize(str);
//    }
//
//    @Override
//    public Span[] tokenizePos(String s) {
//        return utils.tokenizePos(s);
//    }
//
//    private static TokenizerModel loadTokenizer(Language lang) throws IOException {
//        InputStream modelIn = Tokenizer.class.getResourceAsStream("/utils/" + lang.toString() + "-token.bin");
//        TokenizerModel model = new TokenizerModel(modelIn);
//        modelIn.close();
//
//        return model;
//    }
//
//    public enum Language {
//        nl, en
//    }
//}
