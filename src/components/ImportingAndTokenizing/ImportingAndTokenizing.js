//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Chapter from '../Chapter/Chapter'
import chapters from '../../../chapters'


import dissSections from './sections';


export default class ImportingAndTokenizing extends Component {
    static title() {
        return chapters.importing.title;
    }


    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return dissSections;
    }

    render() {
        var chapterObject = chapters.importing;
        return <Chapter path={this.props.path}
                        title={chapterObject.title}
                        chapter={true}
                        chapterObject={chapterObject}
                        inline={!!this.props.inline}
                        id={this.props.id}
                        sections={dissSections.inOrder}>
            <p>
                In this chapter, we provide an introduction to the Rechtspraak.nl data set that we
                use in our experiments. We then make a number of
                remarks on the necessary steps of importing and
                tokenization that we perform on documents in the corpus.
                Importing and tokenization are necessary pre-processing steps which
                result in a collection of token sequences, where each token corresponds 
                to an XML node in a document. 
                These token sequences then serve as input
                for the labeling process. Eventually, after the tokens
                are assigned a type, they serve as leaf nodes for the section
                hierarchy by analyzing their order according to some formal grammar.
            </p>
        </Chapter>
    }
}
