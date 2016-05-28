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
        return <ChapterSectionContent {...this.props} sections={methodsSections.inOrder}/>;
    }
}
