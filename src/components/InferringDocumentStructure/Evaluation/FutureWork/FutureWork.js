//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../../Bibliography/References/references'
import bib from  '../../../Bibliography/bib';
import F from  '../../../Math/Math';

export default class FutureWork extends Component {
    //noinspection JSUnusedGlobalSymbols

    render() {
        return <div>
            <p>
                We might improve both
                running time and parsing quality
                by using a less naive parsing algorithm, such as
                the a shift-reduce parser, which runs linearly for any CFG
                ({ref.cite(bib.zhu2013fast)}).
                In terms of parsing quality, we might benefit from using
                a Conditional Probabilistic Context Free Grammar,
                as described in {ref.cite(bib.sutton2004conditional)}.
            </p>
            <p>
                Conditional Probabilistic Context Free Grammars are similar to
                Conditional Random Random Fields in
                that we describe a conditional
                model, instead of a generative one
                (so the probability distribution <F l="P(\mathbf y|\mathbf x)"/> instead
                of <F l="P(\mathbf x,\mathbf y)"/>). We aren't interested in
                generating examples, so we lose no features. We gain the possibility to use
                a large feature set, however.
            </p>
        </div>;
    }
}