import React from 'react';
/**
 * A component for a chapter page on our website.
 */
export default class Chapter extends React.Component {
    render() {
        return <div><p>
                We present an enriched set of
                XML documents in a CouchDB database, available at <a
                href="https://rechtspraak.cloudant.com/docs/">http://rechtspraak.cloudant.com/docs/</a>. We also provide
                the
                enriched dataset as a collection of HTML pages, indexed for full text search.
            </p>
            <p>
            The source code for this project is published in two separate Java libraries:
            <ul>
                <li>
                    One library for importing and enriching documents from Rechtspraak.nl (
                    <a href="https://github.com/digitalheir/java-rechtspraak-library">on GitHub</a>)
                </li>
                <li>One library for mirroring the Rechtspraak.nl corpus
                    to a CouchDB database (<a href="https://github.com/digitalheir/dutch-case-law-to-couchdb">on
                        GitHub</a>)
                </li>
            </ul>
            </p></div>;
    }
}