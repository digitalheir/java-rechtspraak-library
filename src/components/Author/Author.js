import React, {Component, PropTypes} from 'react';

const ghUrl = "https://github.com/digitalheir";

export default class AuthorData extends Component {
  render() {
    var ghUrl = "https://github.com/digitalheir";
    return (
      <div itemProp="author copyrightHolder" itemScope={true}
           itemType="http://schema.org/Person" className="author-data">
        <meta itemProp="url" content={ghUrl}/>
        <div id="github-link" className="author-line"><a href={ghUrl}
                                                         className="icon no-decoration"><i
          className="fa fa-github"> </i></a><a
          href={ghUrl} className="author-value">digitalheir</a></div>
        <div id="mail-link" className="author-line"><a href={ghUrl}
                                                       className="icon no-decoration"><i
          className="fa fa-envelope"> </i></a><a
          href="maarten.trompper@gmail.com" itemProp="email" className="author-value">maarten.trompper@gmail.com</a>
        </div>
      </div>
    );
  }
}

