var React = require('react');
var figs = require('../../../../figures/figs');
var StackedBarChart = require('../../../../charts/StackedBarChart.jsx');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var Component = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        var p = this.props;
        return (
            <tr>
                <td><code>{p.name}</code></td>
                <td>{p.type}</td>
                <td>{p.description}</td>
            </tr>
        );
    }
});

module.exports = Component;