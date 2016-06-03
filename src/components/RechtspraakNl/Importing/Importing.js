//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import Source from '../../Source/Source'
import ref from '../../Bibliography/References/references'
import bib from  '../../Bibliography/bib';

export default class Importing extends Component {
    render() {
        return <p>
                Regarding importing, we have created a separate library for
                mirroring the Rechtspraak.nl corpus
                from the web service
                to a CouchDB database. We mirror the data set in order to facilitate the collection
                of statistics of case law documents used in this thesis
                through MapReduce computations.
                Source code for this project is available <a
                href="https://github.com/digitalheir/dutch-case-law-to-couchdb">on GitHub</a>.
            </p>;
    }
}
