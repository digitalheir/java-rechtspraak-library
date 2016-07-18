package org.leibnizcenter.rechtspraak.tokens;

import org.leibnizcenter.util.Pair;

/**
 * Encapsulates a token and its tag.
 * <p>
 * In the task of part-of-speech tagging, the token is usually a word, and the tag is a part-of-speech tag like verb, noun, etc.
 *
 * @param <K>
 * @param <G>
 * @author Asher Stern
 *         Date: Nov 8, 2014
 */
public class TaggedToken<K, G> {
    public final K token;
    public G tag;

    public TaggedToken(K token, G tag) {
        super();
        this.token = token;
        this.tag = tag;
    }

    public TaggedToken(Pair<K, G> pair) {
        this(pair.getKey(),pair.getValue());
    }

    public K getToken() {
        return token;
    }

    public G getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "CrfTaggedToken [getToken()=" + getToken() + ", getTag()="
                + getTag() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaggedToken<?, ?> other = (TaggedToken<?, ?>) obj;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        if (token == null) {
            if (other.token != null)
                return false;
        } else if (!token.equals(other.token))
            return false;
        return true;
    }
}
