import React  from 'react'

export default class Header extends React.Component {
    render() {
        //var relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");
        return (
            <div>
                <p>
                    A growing amount of Dutch case law is openly distributed
                    on <a href="http://www.rechtspraak.nl/">Rechtspraak.nl</a>.
                    Currently, many documents are not marked up or marked up only very sparsely,
                    hampering our ability to process these documents automatically.
                </p>

                <p>
                    In this thesis, we explore the problem of automatically assigning a section
                    structure to the texts of Dutch court judgments. To this end, we develop a database that
                    mirrors the XML data offering of Rechtspraak.nl. We
                    experiment with Linear-Chain
                    Conditional Random Fields to label text elements with their role in the document (text,
                    title or numbering). Afterwards, we experiment with stochastic context free grammars to generate a
                    parse tree to model section nesting.
                </p>

                <p>
                    TODO: results
                </p>
            </div>
        )
    }
}
