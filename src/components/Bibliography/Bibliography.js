//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import _ from 'underscore';
import references from './bib';

function getNameElement(citation) {
    // TODO schema author prop
    var nameStr = citation.author.full;
    if (!citation.author.full) {
        nameStr = citation.author.lastName + (citation.author.firstName ? ", " + citation.author.firstName : "");
    }
    if (nameStr) {
        nameStr = <span itemScope={true} itemType="https://schema.org/author" className="ref-author">{nameStr}</span>
    }
    return nameStr;
}


function createCitation(citation, i) {
    if (!citation.id) throw new Error("Citation has no id: " + citation.title);
    
    const name = getNameElement(citation);
    const publication = citation.journal ?
        <span><cite style={{fontStyle: 'normal'}}
                    className="ref-journal">{citation.journal}</cite>.</span>
        : "";
    return <li itemProp="citation"
               itemScope={true}
               itemType="https://schema.org/CreativeWork"
               key={i}
               id={citation.id.toString()}
               className="ref">
        {name} (
        <time dateTime={citation.year} className="ref-year">{citation.year}</time>
        ). <cite>
        <a href={citation.href}><span itemProp="name">{citation.title}</span></a>
    </cite>. {publication}
    </li>;
}

export default class Bibl extends Component {
    render() {
        return <section id="bibliography">
            <h2>References</h2>
            <ol id="reference-list">
                {_.map(references, createCitation)}
            </ol>
        </section>;
    }
}
