var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var AuthorData = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {
            percentage: React.PropTypes.string.isRequired,
            text: React.PropTypes.string.isRequired
        }
    },

    render: function () {
        var txt = this.props.text ? this.props.text : this.props.percentage;
        return (
            <svg width="80" height="10">
                <rect className="perc-bar bg" width="100%" height="100%">
                    <title>{txt}</title>
                </rect>
                <rect className="perc-bar" width={this.props.percentage} height="100%">
                    <title>{txt}</title>
                </rect>
            </svg>
        );
    }
});

module.exports = AuthorData;