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
                the rise of hypertext in the late 1980s:
                see {ref.cite(bib.furuta1989automatically)} for
                one historic example that pre-dates XML.
            </p>

            <p>
                {ref.cite(bib.abolhassani2003information)} discuss
                the general problem of automatic markup from digitally scanned
                documents, and define parsing a section structure as a part of macro-level markup.
                This is in contrast to micro-level markup, such as named entity recognition.
                They review some general solutions,
                but argue that general automatic markup "will remain a problem for a long time".
            </p>

            <p>
                Indeed, most applications are domain-specific.
                Somewhat recently, the problem has been addressed
                in legal informatics as well. {ref.cite(bib.bacci2009automatic)} have a similar set-up
                to ours, applied to Italian law text.
                They successfully apply
                Hidden Markov Models to distinguish header and footers
                from body. Interestingly, they train a separate HMM
                per law type. For parsing the section hierarchy
                in the body, they use a non-deterministic finite state machine,
                which corresponds to using regular expression.
                They report some intolerance of their system to
                minor syntactical errors in the input, but catch common
                issues. Legislative
                texts tend to be more deeply nested than court judgments, but
                also tend to have a much stricter structure, which is probably why
                they use much less features than in this report.
            </p>
        </div>
    }
}
