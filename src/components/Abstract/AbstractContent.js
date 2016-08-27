import React  from 'react'
import abbrs  from '../abbreviations'
export default class AbstractContent extends React.Component {
    render() {
        //var relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");
        return (
            <div itemProp="description">
                <p>
                    A growing amount of Dutch case law is openly distributed
                    on <a hrefLang="nl" href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
                    Currently, many documents are not marked up or marked up only very sparsely,
                    hampering our ability to process these documents automatically.
                </p>

                <p>
                    In this thesis, we explore the problem of automatic assignment of a section
                    structure to the texts of Dutch court judgments. To this end, we develop a database that
                    mirrors the {abbrs.xml} data offering of Rechtspraak.nl. We
                    experiment with Linear-Chain
                    Conditional Random Fields to label text elements with their roles in the document (text,
                    title or numbering). Given a list of labels, we experiment with
                    probabilistic context-free grammars
                    to generate a parse tree which represents the section hierarchy of a document.
                </p>

                <p>
                    We report F<sub>1</sub> scores of around 0.91 for tagging section titles (around 1.0 for 
                    other types) and 0.92 for parsing the tokens into a section hierarchy.
                </p>
            </div>
        )
    }
}
