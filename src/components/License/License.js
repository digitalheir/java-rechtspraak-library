//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import title from  '../../../title';

export default class License extends Component {
    render() {
        const innerHtml =
            '<a hreflang="en" rel="license" href="http://creativecommons.org/licenses/by/4.0/">' +
            '<img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" />' +
            '</a>' +
            '<br/>' +
            '<span property="http://purl.org/dc/terms/title">' + title + '</span>' +
            ' by ' +
            '<a hreflang="en" ' +
            'href="https://github.com/digitalheir/" ' +
            'property="http://creativecommons.org/ns#attributionName" ' +
            'rel="http://creativecommons.org/ns#attributionURL">Maarten Trompper</a>' +
            ' is licensed under a ' +
            '<a hreflang="en" ' +
            'rel="license" ' +
            'href="http://creativecommons.org/licenses/by/4.0/">' +
            'Creative Commons Attribution 4.0 International License' +
            '</a>' +
            '.<br />' +
            'Based on a work at ' +
            '<a hreflang="en" ' +
            'href="https://digitalheir.github.io/java-rechtspraak-library" ' +
            'rel="http://purl.org/dc/terms/source">https://digitalheir.github.io/java-rechtspraak-library</a>' +
            '.';
        return <div style={{textAlign: 'center'}} dangerouslySetInnerHTML={{__html:innerHtml}}></div>;
    }
}
