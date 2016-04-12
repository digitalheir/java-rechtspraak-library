import React from 'react';
import PureRenderMixin from 'react-addons-pure-render-mixin';

var BrowserCheck = React.createClass({
  mixins: [PureRenderMixin],

  getDefaultProps: function () {
    return {}
  },

  render: function () {
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
});

export default BrowserCheck;
