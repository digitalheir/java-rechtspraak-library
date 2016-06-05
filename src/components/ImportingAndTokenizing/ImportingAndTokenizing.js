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
        return <Chapter path={this.props.path}
                        title={ImportingAndTokenizing.title()}
                        inline={!!this.props.inline}
                        id={this.props.id}
                        sections={dissSections.inOrder}>
            <p>
                In this chapter, we provide an introduction to the Rechtspraak.nl data set that we
                experiment on. We then make a number of
                remarks on the necessary steps of importing and
                tokenization that we perform on documents in the corpus
                in order to create a collection of token instances that we
                use in our experiments with labeling the document elements
                and creating
                a section hierarchy.
            </p>
        </Chapter>
    }
}
