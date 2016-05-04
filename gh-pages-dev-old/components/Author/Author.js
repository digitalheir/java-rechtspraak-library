import React, {Component, PropTypes} from 'react';

import PureRenderMixin from 'react-addons-pure-render-mixin';

const ghUrl = "https://github.com/digitalheir";
var AuthorData = React.createClass({
  mixins: [PureRenderMixin],

  getDefaultProps: function () {
    return {}
  },

  render: function () {
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
          href="m.f.a.trompper@uva.nl" itemProp="email" className="author-value">m.f.a.trompper@uva.nl</a>
        </div>
      </div>
    );
  }
});


export default AuthorData;
