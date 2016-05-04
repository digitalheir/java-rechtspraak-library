//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default class BrowserCheck extends Component {
    render() {
        return (
            <div dangerouslySetInnerHTML={{__html:
             "<!--[if lt IE 8]>"+
                '<p class="browserupgrade">You are using an ' +
                 '<strong>outdated</strong>' +
                  ' browser. ' +
                   'Please ' +
                    '<a href="http://browsehappy.com/">upgrade your browser</a>' +
                     ' to improve your experience.</p>'+
                "<![endif]-->"}}>
            </div>
        );
    }
}