const data = require('./get-data');
const React = require('react');
const PureRenderMixin = require('react-addons-pure-render-mixin');

const figs = require('../../../../../figures/figs.jsx');
const Sequences = require('../../../../../charts/sequences-sunburst/Sequences.jsx');

var Fig = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        var colors = {
            "[Other]": "#7b615c",
            "search": "#de783b",
            "account": "#6ab975",
            "other": "#a173d1"
        };
        colors[data.hasNr] = "#5687d1";
        colors[data.doesntHaveNr] = "#bbbbbb";

        return <figure id={figs.figTitleNumbering.id}>
            <Sequences colors={colors} data={data.download()} sourceHref={data.href}/>
            <figcaption>
                <span className="figure-number">Fig {figs.figTitleNumbering.num}.</span> fffffffff
            </figcaption>
        </figure>;
    }
});

module.exports = Fig;