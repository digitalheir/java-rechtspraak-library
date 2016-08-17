import React, {Component, PropTypes} from 'react';

const ghUrl = "https://github.com/digitalheir";

export default class AuthorData extends Component {
    render() {
        var ghUrl = "https://github.com/digitalheir";
        //<meta content={ghUrl}/>
        return (
            <div itemProp="author copyrightHolder" itemScope={true}
                 itemType="https://schema.org/Person" className="author-data">
                <div id="muh-name" itemProp="name" className="author-line">
                    <span itemProp="givenName">Maarten</span> <span itemProp="familyName">Trompper</span>
                </div>
                <div id="github-link" className="author-line"><a href={ghUrl}
                                                                 className="icon no-decoration"><i
                    className="fa fa-github"> </i></a><a
                    itemProp="url"
                    href={ghUrl}
                    className="author-value">digitalheir</a></div>
                <div id="mail-link" className="author-line"><a href={ghUrl}
                                                               className="icon no-decoration"><i
                    className="fa fa-envelope"> </i></a><a
                    href="mailto:maarten.trompper@gmail.com" itemProp="email" className="author-value">maarten.trompper@gmail.com</a>
                </div>
            </div>
        );
    }
}

