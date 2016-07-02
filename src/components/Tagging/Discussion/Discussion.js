//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Discussion extends Component {
    //noinspection JSUnusedGlobalSymbols


    render() {
        return <p>
            Taking a closer look at faulty labels,
            we observe that most errors are snippets of text that
            contain only a noun phrase, and it is easy to imagine how
            the CRF assigns such a text block as a title or vice versa.
            Furthermore, due to the sometimes very staccato paragraphs in case law,
            it can be hard even for humans to distinguish section titles and running text.
            Still, the CRF is not currently tuned to target
            problematic cases. This is likely a fruitful venue
            to improve classifier performance.
        </p>;
    }
}

