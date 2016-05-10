//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import ChapterSectionContent from '../../Chapter/ChapterSectionContent';
import chapters from '../../../../chapters';
import sectionz from './sections';


export default class Methods extends Component {
    static title() {
        return chapters.documentStructure.title;
    }

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return sectionz;
    }

    render() {
        return <ChapterSectionContent {...this.props} sections={sectionz.inOrder}/>;
    }
}