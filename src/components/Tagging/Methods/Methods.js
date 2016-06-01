//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import chapters from '../../../../chapters';
import ChapterSectionContent from '../../Chapter/ChapterSectionContent';

const methodsSections = {
    featureSelection: {
        id: 'feature-selection',
        title: "Feature Selection"
    },
    crf: {
        id: 'crf',
        title: "Conditional Random Fields"
    },
    deterministic: {
        id: 'deterministic-tagger',
        title: "Deterministic tagger"
    }
};

methodsSections.inOrder = [
    methodsSections.featureSelection,
    methodsSections.crf
    // methodsSections.deterministic
];


export default class Tagging extends Component {
    static title() {
        return chapters.tagging.title;
    }

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return methodsSections;
    }

    render() {
        return <ChapterSectionContent {...this.props} sections={methodsSections.inOrder}>
            For the purpose of tagging, we use a class of statistical classifiers called
            Conditional Random Fields (CRFs). We use this technique because CRFs
            tend to have state-of-the-art performance
            in pattern recognition tasks. For our purpose, we circumvent the
            usual downside of CRFs that it overfits to a single corpus, because
            we expressly train for one particular corpus.
        </ChapterSectionContent>;
    }
}
