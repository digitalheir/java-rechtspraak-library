//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

import Chapter from '../Chapter/Chapter';
import chapters from '../../../chapters';
import inferringSections from './sections';


export default class Inferring extends Component {
    static title() {
        return chapters.documentStructure.title;
    }

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return inferringSections;
    }

    render() {
        return <Chapter id={this.props.id}
                        chapter={true}
                        path={this.props.path}
                        inline={!!this.props.inline}
                        title={Inferring.title()}
                        sections={inferringSections.inOrder}/>;
    }
}