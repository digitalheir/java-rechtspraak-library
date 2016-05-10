//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Chapter from '../Chapter/Chapter'
import chapters from '../../../chapters'


import dissSections from './sections';


export default class Dissemination extends Component {
    static title() {
        return chapters.dissemination.title;
    }


    //noinspection JSUnusedGlobalSymbols
    static getSections() {
        return dissSections;
    }

    render() {
        return <Chapter path={this.props.path} title={Dissemination.title()} sections={dissSections.inOrder}>
            In addition to a document transformation pipeline, we present the enriched set of
            XML documents in a CouchDB database, available at <a
            href="https://rechtspraak.cloudant.com/docs/">rechtspraak.cloudant.com/docs/</a>. We also provide the
            enriched dataset as a collection of HTML pages, indexed for full text search. The web site is available
            at <a href="https://rechtspraak.lawreader.nl/"/>
        </Chapter>
    }
}