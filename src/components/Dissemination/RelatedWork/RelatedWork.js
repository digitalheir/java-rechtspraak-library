//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';
import abbrs  from '../../abbreviations'
export default class RelatedWork extends Component {
    render() {
        return <div>
            <p>
                The problem of automatically assigning semantic
                markup to plain-text documents has existed since
                the rise of hypertext in the late 1980s:
                see {ref.cite(bib.furuta1989automatically)} for
                one historic example that predates {abbrs.xml}.
            </p>

            <p>
                {ref.cite(bib.abolhassani2003information)} discuss
                the general problem of automatic markup from digitally scanned
                documents, and define parsing a section structure as a task in macro-level markup.
                This is in contrast to micro-level markup, such as named entity recognition.
                They review some general solutions
                but argue that general automatic markup <q cite={bib.abolhassani2003information.url}>will remain
                a problem for a long time</q>.
            </p>

            <p>
                Indeed, most approaches to automatic markup are domain-specific.
                Somewhat recently, the problem has been addressed
                in legal informatics as well. {ref.cite(bib.bacci2009automatic)} have a similar set-up
                to ours, applied to Italian law texts.
                They successfully apply
                Hidden Markov Models to distinguish headers and footers
                from body elements. Interestingly, they train a
                separate <abbr title="Hidden Markov Model">HMM</abbr> for
                every law type. For parsing the section hierarchy
                in the body, they use non-deterministic finite state machines,
                which corresponds to the class of (non-deterministic) regular
                expressions. The system shows some intolerance to
                syntactical errors in the input but can handle common input
                issues. The system uses much fewer features than ours,
                which may be explained by the fact
                that legislative
                texts tend to have a more strict structure than case law, even
                though it also tends to be more deeply nested.
            </p>
        </div>
    }
}
