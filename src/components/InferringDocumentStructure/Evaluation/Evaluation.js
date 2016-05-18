//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Evaluation extends Component {
    render() {
        return <div>
            <p>
                Like in the previous chapter, we evaluate our grammar using an F-score, except
                with a subtle change in the meaning of precision and recall, because now we're comparing
                dealing trees. This definition is due to {ref.cite(bib.abney1991procedure)}.
            </p>
            <ul>
                <li>Precision is the fraction of correct constituents out of the total number of constituents in the
                    candidate parse
                </li>
                <li>Recall is the fraction of correct constituents out of the total number of constituents in the
                    gold standard
                </li>
            </ul>
            <p>
            </p>
        </div>;
    }
}
