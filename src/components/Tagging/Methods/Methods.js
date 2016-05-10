//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Chapter from '../../Chapter/Chapter';
import CRF from './CRF/CRF';
import DeterministicTagger from './DeterministicTagger/DeterministicTagger';
import chapters from '../../../../chapters';
import ChapterSectionContent from '../../Chapter/ChapterSectionContent';

const methodsSections = {
    crf: {
        id: 'crf',
        title: "Conditional Random Fields",
        component: CRF
    },
    deterministic: {
        id: 'deterministic-tagger',
        title: "Deterministic tagger",
        component: DeterministicTagger
    }
};

methodsSections.inOrder = [
    methodsSections.crf,
    methodsSections.deterministic
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
