package org.leibnizcenter.rechtspraak.enricher;

/**
 * Helpers functions enriching Rechtspraak XML
 * <p>
 * Created by Maarten on 2016-04-01.
 */
public class Enrich {
    private final CRF crf;
    public Enricher(){
        this(f);
    }
    
    public Enricher(File crfModelFile){
        if(!crfModelFile.exists()) throw new InvalidParameterException("CRF model not found at "+crfModelFile.getAbcolutePath())
        this.crf = loadCrf(f);
    }
    
    public Document enrich(String ecli, Document doc) {
        // Tokenize 
        tokenList = TokenList.parse( ecli, Document doc, Xml.getRootElement(doc));
        
        // Apply tagging
        DeterministicTagger.tag(tokenList);
        
        // Dissolve intermediate tags
        cleanUp(doc.getRootElement());
        
        // Return enriched doc
        return ;
    }
    
    public static void cleanUp(Node node){
        Node[] children = Xml.getChildren(node);
        // If this element has a leibniz namespace, either dissolve the tag or make it an 'official' tag
        if(LeibnizTags.hasLeibnizNameSpace(node)){
            etc...
        }

        //
        for(Node child:children){
            cleanUp(child);
        }
    }

    public static Set<NamedElementFeatureFunction> setFeatures(Token t, List<TokenTreeLeaf> tokens, int ix,
                                                               NamedElementFeatureFunction... values) {
        Set<NamedElementFeatureFunction> matches = new HashSet<>();
        for (NamedElementFeatureFunction f : values)
            if (f.apply(tokens, ix)) {
                t.setFeatureValue(f.name(), 1.0);
                matches.add(f);
            }
//            else t.setFeatureValue("NOT_"+f.name(), 1.0);
        return matches;
    }

    public static boolean matchesAny(List<TokenTreeLeaf> tokens, int ix, ElementFeatureFunction... patterns) {
        for (ElementFeatureFunction p : patterns)
            if (p.apply(tokens, ix))
                return true;
        return false;
    }
}
