import React from 'react';
import abbrs  from '../../abbreviations'
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        return <div>
            <p>
                We present an enriched set of
                {abbrs.xml} documents in a CouchDB database, available at <a
                href="https://rechtspraak.cloudant.com/docs/">http://rechtspraak.cloudant.com/docs/</a>. We also provide
                the
                enriched data set as a collection of {abbrs.html} pages, indexed for full text search.
            </p>
            <p>
                The source code for this project is published in two separate Java libraries:
            </p>
            <ul>
                <li>
                    One library for importing and enriching documents from Rechtspraak.nl, <a
                    className='print-url' href="https://github.com/digitalheir/java-rechtspraak-library">on
                    GitHub</a>
                </li>
                <li>One library for mirroring the Rechtspraak.nl corpus
                    to a CouchDB database, <a className='print-url'
                                              href="https://github.com/digitalheir/dutch-case-law-to-couchdb">on
                        GitHub</a>
                </li>
            </ul>
        </div>;
    }
}