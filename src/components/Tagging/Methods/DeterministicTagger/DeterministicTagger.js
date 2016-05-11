//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../../Figures/FigRef'
import FigImg from './../../../Figures/Image/Image'
import figs from './../../../Figures/figs'
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import F from  '../../../Math/Math';

export default class DeterministicTagger extends Component {
    render() {
        return <div>
            To compare the performance of CRFs, we also define a deterministic algorithm which
            uses some straightforward rules to determine token tags
            based on the patterns that we have identified above. (e.g. 'if it looks like a
            known title, assign it to <code>title</code>'; 'if it looks like a number and is congruent with
            previous numbers, assign it to <code>nr</code>'.)
        </div>;
    }
}
