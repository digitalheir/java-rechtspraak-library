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
            contain only a noun phrase.
            Because of the sometimes very staccato paragraphs in case law, it is easy to imagine how
            the <abbr title="Conditional Random Field">CRF</abbr> might confuse text blocks and titles.
            it can be hard even for humans to distinguish section titles and running text.
            Still, the <abbr title="Conditional Random Field">CRF</abbr> is not currently tuned to target
            problematic cases, and doing so is likely to be a fruitful way to improve classifier performance.
        </p>;
    }
}

