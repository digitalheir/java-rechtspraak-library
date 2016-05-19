package org.leibnizcenter.rechtspraak.tokens.numbering;

import com.google.common.collect.Sets;
import org.leibnizcenter.rechtspraak.tokens.RechtspraakElement;
import org.leibnizcenter.util.Strings2;
import org.w3c.dom.Element;

import java.util.Set;

/**
 * <p>
 * Helps to parse list items
 * </p>
 * Created by maarten on 31-3-16.
 */
public class ListMarking extends RechtspraakElement {

    /**
     * Sticks
     */
    public static Set<Character> VERTICAL_LIST_MARKINGS = Sets.newHashSet(
            '︱',    // U+FE31	&#65073;	presentation form for vertical em dash	vertical variant of em dash
            '︲',    // U+FE32	&#65074	presentation form for vertical en dash	vertical variant of en dash
            '‖',    //8214	2016	 	DOUBLE VERTICAL LINE
            '†',    //8224	2020	&dagger;	DAGGER
            '‡',    //8225	2021	&Dagger;	DOUBLE DAGGER
            '|'     //8225	2021	&Dagger;	DOUBLE DAGGER
    );

    public static Set<Character> CLASSIC_ROUND_LIST_MARKINGS = Sets.newHashSet(
            '*',
            '⁕',    //8277	2055	 	FLOWER PUNCTUATION MARK
            '‧',    //8231	2027	 	HYPHENATION POINT
            '·',    // middle dot
            '•',    //8226	2022	&bull;	BULLET
            '°', '◦', 'º', '○', '◯', '◉', '◌', '◎', '●', '⬤',
            '\u23FA',
            '⬬', '⬭',
            '⬮', '⬯',
            '▪', '▫', '■', '□',
            '▪', '▫', '◻', '◼', '◽', '◾',
            '▪', '▫', '◽',
            '▪', '▫', '◽'
    );
    public static Set<Character> FANCY_LIST_MARKINGS = Sets.newHashSet(
            '‣',    //8227	2023	 	TRIANGULAR BULLET
            '․',    //8228	2024	 	ONE DOT LEADER
            '⁌',    //8268	204C	 	BLACK LEFTWARDS BULLET
            '⁍',    //8269	204D	 	BLACK RIGHTWARDS BULLET
            '¤', '▢', '▣', '▤', '▥', '▦', '▧', '▨', '▩', '▬', '▭', '▮', '▯',
            '◐', '◑', '◒', '◓', '◔', '◕', '◖', '◗', '◘', '◙', '◚', '◛', '◜', '◝', '◞', '◟',
            '▰', '▱', '▲', '△', '▴', '▵', '▶', '▷', '▸', '▹', '►', '▻', '▼', '▽', '▾', '▿',
            '◀', '◁', '◂', '◃', '◄', '◅', '◆', '◇', '◈', '◉', '◊', '○', '◌', '◍', '◎', '▶',
            '◀', '◠', '◡', '◢', '◣', '◤', '◥', '◧', '◨', '◩', '◪', '◫', '◬', '▶',
            '▶', '◭', '◮',
            '◰', '◱', '◲', '◳', '◴', '◵', '◶', '◷', '◸', '◹', '◺', '◻', '◼', '◽', '◾', '◿'
    );
    public static Set<Character> QUESTIONABLE_LIST_MARKINGS = Sets.newHashSet(
            '#', // round-ish
            '=',
            '゠',    // U+30A0	&#12448;	katakana-hiragana double hyphen	in Japasene kana writing
            '᐀',    // U+1400	&#5120;	canadian syllabics hyphen	used in Canadian Aboriginal Syllabics
            '±',
            '÷',
            '?' // Probably a unicode fail; see http://uitspraken.rechtspraak.nl/inziendocument?id=ECLI:NL:RBALM:2005:AU7924
    );
    public static Set<Character> ETC_LIST_MARKINGS = Sets.newHashSet(
            '›',    //8250	203A	&rsaquo;	SINGLE RIGHT-POINTING ANGLE QUOTATION MARK
            '※',    //8251	203B	 	REFERENCE MARK
            '⁂',    //8258	2042	 	ASTERISM
            '⁎',    //8270	204E	 	LOW ASTERISK
            '⁑',    //8273	2051	 	TWO ASTERISKS ALIGNED VERTICALLY
            '⁜',    //8284	205C	 	DOTTED CROSS
            '⁘',    //8280	2058	 	FOUR DOT PUNCTUATION
            '⁙'    //8281	2059	 	FIVE DOT PUNCTUATION
    );

    /**
     * Hyphens
     */
    public static Set<Character> HORIZONTAL_LIST_MARKING = Sets.newHashSet(
            '¯',
            '‾', //	8254	203E	&oline;	OVERLINE
            '־',    // U+05BE	&#1470;	hebrew punctuation maqaf	word hyphen in Hebrew
            '-',    // U+002D	&#45;	hyphen-minus	the Ascii hyphen, with multiple usage, or “ambiguous semantic value”; the width should be “average”
            '~',    // U+007E	&#126;	tilde	the Ascii tilde, with multiple usage; “swung dash”
            '֊',    // U+058A	&#1418;	armenian hyphen	as soft hyphen, but different in shape
            '᠆',    // U+1806	&#6150;	mongolian t odo soft hyphen	as soft hyphen, but displayed at the beginning of the second line
            '‐',    // U+2010	&#8208;	hyphen	unambiguously a hyphen character, as in “left-to-right”; narrow width
            '‑',    // U+2011	&#8209;	non-breaking hyphen	as hyphen (U+2010), but not an allowed line break point
            '‒',    // U+2012	&#8210;	figure dash	as hyphen-minus, but has the same width as digits
            '–',    // U+2013	&#8211;	en dash	used e.g. to indicate a range of values
            '—',    // U+2014	&#8212;	em dash	used e.g. to make a break in the flow of a sentence
            '―',    // U+2015	&#8213;	horizontal bar	used to introduce quoted text in some typographic styles; “quotation dash”; often (e.g., in the representative glyph in the Unicode standard) longer than em dash
            '⁓',    // U+2053	&#8275;	swung dash	like a large tilde
            '⁻',    // U+207B	&#8315;	superscript minus	a compatibility character which is equivalent to minus sign U+2212 in superscript style
            '₋',    // U+208B	&#8331;	subscript minus	a compatibility character which is equivalent to minus sign U+2212 in subscript style
            '−',    // U+2212	&#8722;	minus sign	an arithmetic operator; the glyph may look the same as the glyph for a hyphen-minus, or may be longer ;
            '⸗',    // U+2E17	&#11799;	double oblique hyphen	used in ancient Near-Eastern linguistics; not in Fraktur, but the glyph of Ascii hyphen or hyphen is similar to this character in Fraktur fonts
            '⸺',    // U+2E3A	&#11834;	two-em dash	omission dash<(a>, 2 em units wide
            '⸻',    // U+2E3B	&#11835;	three-em dash	used in bibliographies, 3 em units wide
            '〜',    // U+301C	&#12316;	wave dash	a Chinese/Japanese/Korean character
            '〰',    // U+3030	&#12336;	wavy dash	a Chinese/Japanese/Korean character
            '﹘',    // U+FE58	&#65112;	small em dash	small variant of em dash
            '﹣',    // U+FE63	&#65123;	small hyphen-minus	small variant of Ascii hyphen
            '－'    // U+FF0D	&#65293;	full width hyphen-minus	variant of Ascii hyphen for use with CJK characters
    );

    public static Set<Character> all =
            Sets.union(VERTICAL_LIST_MARKINGS,
                    Sets.union(CLASSIC_ROUND_LIST_MARKINGS,
                            Sets.union(FANCY_LIST_MARKINGS,
                                    Sets.union(QUESTIONABLE_LIST_MARKINGS,
                                            Sets.union(HORIZONTAL_LIST_MARKING, ETC_LIST_MARKINGS)
                                    )
                            )
                    )
            );

    public ListMarking(Element element) {
        super(element);
    }

    public static boolean startsWithListMarking(String s) {
        int at = startsWithListMarkingAtChar(s);
        return at > -1;
    }

    public static int startsWithListMarkingAtChar(String s) {
        int at = Strings2.firstNonWhitespaceCharIsAny(s, all);
        if (at > -1
                && !(s.length() >= at + 1 && Strings2.firstNonWhitespaceCharIsAny(s, all, at + 1) > -1)) return at;
        else return -1;
    }
}
