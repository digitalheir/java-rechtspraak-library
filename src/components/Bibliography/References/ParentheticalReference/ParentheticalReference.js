//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
const bibliography = require('../../bib');

export default class extends Component {
    render() {
        const refId = this.props.refId;
        var page = this.props.page;
        
        var reference = refId;
        if (typeof refId == 'string') {
            reference = bibliography[refId];
        }

        if (!reference) {
            throw new Error("Reference not found: " + refId);
        }


        // Create string for page
        var strPage = reference.pages ? ", pp. " + reference.pages : "";
        page = page ? page : reference.page;
        if (page) {
            strPage = ", p. " + page;
        }

        //Create string for name
        var name = reference.author.lastName ? reference.author.lastName.toString() : reference.author.abbr;

        //Create element
        return (<a className="inline-citation"
                   href={"#"+reference.id.toString()}
        >{name} (
            <time dateTime={reference.year.toString()} className="ref-year">{reference.year.toString()}</time>
            {strPage.toString()})</a>);
    }
}
