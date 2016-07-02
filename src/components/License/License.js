//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import title from  '../../../title';

export default class License extends Component {
    render() {
        const innerHtml =
            '<a rel="license" href="http://creativecommons.org/licenses/by/4.0/">' +
            '<img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" />' +
            '</a>' +
            '<br/>' +
            '<span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">' + title + '</span>' +
            ' by ' +
            '<a ' +
            'xmlns:cc="http://creativecommons.org/ns#" ' +
            'href="https://github.com/digitalheir/" ' +
            'property="cc:attributionName" ' +
            'rel="cc:attributionURL">Maarten Trompper</a>' +
            ' is licensed under a ' +
            '<a ' +
            'rel="license" ' +
            'href="http://creativecommons.org/licenses/by/4.0/">' +
            'Creative Commons Attribution 4.0 International License' +
            '</a>' +
            '.<br />' +
            'Based on a work at ' +
            '<a ' +
            'xmlns:dct="http://purl.org/dc/terms/" ' +
            'href="https://digitalheir.github.io/java-rechtspraak-library" ' +
            'rel="dct:source">https://digitalheir.github.io/java-rechtspraak-library</a>' +
            '.';
        return <div style={{textAlign: 'center'}} dangerouslySetInnerHTML={{__html:innerHtml}}></div>;
    }
}
