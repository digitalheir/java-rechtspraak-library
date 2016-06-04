//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Conclusion extends Component {
    render() {
        return <div>
            <p>
                The problem of automatically assigning semantic
                markup to plain-text documents has existed since
                the rise of hypertext in the late 1980s.
                See {ref.cite(bib.furuta1989automatically)} for
                one historic example that pre-dates XML.
                More recently, the problem has been addressed
                in legal informatics as well.
            </p>

            <p>
                {ref.cite(bib.bacci2009automatic)} have a similar set-up
                to ours, applied to Italian law text.
                They apply
                Hidden Markov Models to distinguish header and footers
                from body. Interestingly, they train a separate HMM
                per law type. For parsing the section hierarchy
                in the body, they use a non-deterministic finite state,
                which correspond to the set of regular expressions,
                although they report some intolerance of their system to
                minor syntactical errors in the input. Legislative texts
                texts tend to be more complex than court judgments, but
                also tend to have a stricter structure.
            </p>

            <p>
                
            </p>
        </div>
    }
}
