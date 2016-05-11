//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Importing extends Component {
    render() {
        return <div>
            <p>
                Importing and tokenizing documents on Rechtspraak.nl is
                theoretically straightforward, so it will suffice to make only a couple
                of remarks on the subject.
            </p>

            <strike>
                We have developed two
                separate Java libraries: one for importing
                (and enriching) documents from Rechtspraak.nl (
                <a href="https://github.com/digitalheir/java-rechtspraak-library">source
                    code on GitHub</a>), and one for
            </strike>

            <p>
                As described above, we have a separate library for importing
                mirroring the Rechtspraak.nl corpus
                as a CouchDB database.

                This was done in order to facilitate the collection
                of statistics of case law documents used in this document. The project
                includes a module to convert XML to JSON and a number of MapReduce functions
                to generate statistics.
                The source code for this projects is available <a
                href="https://github.com/digitalheir/dutch-case-law-to-couchdb">on GitHub</a>.
            </p>

            <p>
                Regardings tokenization, we need to so some forward thinking to determine
                how to split Rechtspraak.nl XML texts.
                We will use four terminal nodes in our section hierarchy:
            </p>

            <ol>
                <li><code>numbering</code>, for numbering in a section heading</li>
                <li><code>title text</code>, for text in a section heading</li>
                <li><code>text block</code>, for running text outside of a section heading</li>
                <li><code>newline</code>, for newlines</li>
            </ol>

            <p>
                So we tokenize a document for portions of the document that are could be labeled any of the above.
                In this regard, newlines are trivial to detect, and we assume
                that Rechtspraak.nl has already done a job of splitting text blocks in <code>para</code> tags,
                but still there are a number of arbitrary decisions we need to make about how granular a token must be.
                These detailed can be
                consulted in the <a
                href="https://github.com/digitalheir/java-rechtspraak-library/blob/master/src/main/java/org/leibnizcenter/rechtspraak/tokens/tokentree/TokenTree.java">
                tokenization source code
            </a>.
            </p>

            <p>
                One complication is that Rechtspraak.nl delivers an XML tree, which is more
                rich than a linear array of words. Indeed, it often happens that multiple <code>para</code> tags
                are wrapped in
                a <code>paragroup</code>, which sometimes represents a coherent set of paragraphs. On the other hand,
                sometimes the paragraph grouping makes no sense. Although it is possible to efficiently tag tree
                structures instead of a linear list of tokens ({ref.cite(bib.bradley2010learning)}), this requires more
                somewhat more complicated machinery. So the tagging algorithm that we use in the following supposes
                a linear sequence of tokens. This means that it is possible
                that we lose some information in the process, because we coerce a tree structure into a linear
                sequence, and then build a tree structure which may clash with the original tree structure.
                Merging the two resulting tree structures is a possibility, but we do not currently consider this.
            </p>

            <p>
                Another noteworthy issue is that numberings are often not annotated. In our parsing algorithm, we
                assume that
                a potential numbering occurs as the beginning of a text block and represents Arabic or Roman
                numerals, alphabetic numbering or a list marking (i.e. some symbol such
                as '-' or '*').
            </p>
        </div>;
    }
}
