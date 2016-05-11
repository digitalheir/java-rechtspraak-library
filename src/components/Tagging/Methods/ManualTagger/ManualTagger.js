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
            Along with automatic taggers, we have created GUI which makes it possible
            to tag tokens by hand. This is useful for creating a gold standard data set or
            manually validating an algorithmic tagging algorithm.
        </div>;
    }
}
