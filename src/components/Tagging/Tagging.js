//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
// import Introduction from './Introduction/Introduction';
import Chapter from '../Chapter/Chapter';
import Methods from './Methods/Methods';
import Evaluation from './Evaluation/Evaluation';
import DeterministicTagger from './Methods/DeterministicTagger/DeterministicTagger';
import CRF from './Methods/CRF/CRF';
import FeatureSelection from './Methods/FeatureSelection/FeatureSelection';
import chapters from '../../../chapters';
import taggingSections from './sections'

export default class Tagging extends Component {
    static chapterInfo() {
        return chapters.tagging;
    }

    static title() {
        return Tagging.chapterInfo().title;
    }

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return taggingSections;
    }

    render() {
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");
        return <Chapter
            path={this.props.path}
            title={Tagging.title()}
            sections={taggingSections.inOrder}>
        </Chapter>;
    }
}
