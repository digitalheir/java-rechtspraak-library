
public class LeibnizTags{
    public static final String LEIBNIZ_NAMESPACE = "http://www.leibnizcenter.org/";
    
    public static final String TAG_TEXT = "text";
    public static final String TAG_POTENTIAL_NR = "potentialnr";
    public static final String TAG_TEXTGROUP = "textgroup";
    public static final String TAG_LIST_MARKING = "listmarking";
    public static final String TAG_QUOTE = "quote";
    
    public static final Set<String> all = Sets.newHashSet(
        TAG_TEXT,
        TAG_POTENTIAL_NR,
        TAG_TEXTGROUP,
        TAG_LIST_MARKING,
        TAG_QUOTE
    );
    
    public static final boolean hasLeibnizNameSpace(Node n){
        boolean y= n.getNamespace.equals(LEIBNIZ_NAMESPACE);
        
        if(y && !all.contains(n.getNodeName())) 
            throw new IllegalStateException("Unknown tag "+n.getNodeName+" with Leibniz.org namespace");
        
        return y;
    }
}