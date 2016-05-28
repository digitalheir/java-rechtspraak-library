//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import FigRef from './../../Figures/FigRef'
import FigImg from './../../Figures/Image/Image'
import figs from './../../Figures/figs'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import F from  '../../Math/Math';
import sections from  './sections';


export default class Results extends Component {

    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return sections;
    }

    render() {
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(_ => "../").join("");

        return <div>
            <p>
                To compare the performance of CRFs, we also define a deterministic algorithm which serves
                as a baseline performance.
                The tagger uses many of the same features that the CRFs use
                to determine the labels for given tokens. (e.g. 'if it looks like a
                known title, assign it to <code>title</code>'; 'if it looks like a number and is congruent with
                previous numbers, assign it to <code>nr</code>'.)
            </p>
            <p>
                For assessing the performance of our trained CRFs,
                we compare three conditions:
            </p>

            <ol>
                <li>The deterministic tagger as a baseline</li>
                <li>One CRF trained on 100 documents that are randomly selected and manually annotated</li>
                <li>One CRF trained on 100 documents that are randomly selected and manually annotated, but with all
                    newline tokens omitted
                </li>
            </ol>

            <p>
                We include the newline condition, because including newlines could
                either positively or negatively affect
                performance.
                On the one hand, newlines carry semantic information
                information: the author thought it appropriate to
                demarcate something with whitespace. But on the other hand they
                might obscure information about the previous label. Consider a
                numbering, followed by a newline, followed by a section title.
                Our CRFs only consider one previous label, so the relationship
                between the numbering and the title might not be represented well.
                We see in <FigRef fig={figs.taggingResults}/> that
                including newline tokens performs only slightly better than not
                including newlines.
            </p>
        </div>;
    }
}