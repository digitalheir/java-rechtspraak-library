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
        return (
            <figure id={figs.markupStats.id}>
                <div className="figure-container">
                    <StackedBarChart
                        data={figs.markupStats.data}
                        sourceHref={figs.markupStats.url}
                        />
                </div>
                <figcaption>
                    <span className="figure-number">Fig {figs.markupStats.num}.</span> Chart
                    showing the number of richly marked up versus non-richly marked up data. We define
                    'richly marked up' here as containing at least one <code>&lt;section&gt;</code> tag.
                </figcaption>
            </figure>
        );
    }
});

module.exports = Component;