const data = require('./get-title-patterns-data');
const React = require('react');
const PureRenderMixin = require('react-addons-pure-render-mixin');

const figs = require('../../../../../figures/figs.jsx');
const TreeMap = require('../../../../../charts/tree-map/TreeMap.jsx');

var Fgiure = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        return <figure id={figs.figTitleTreemap.id}>
            <TreeMap data={data.download()} sourceHref={data.href}/>
            <figcaption>
                <span className="figure-number">Fig {figs.figTitleTreemap.num}.</span> Absolute frequency of title patterns
                that occur more than 5 times in the corpus, for the three types of
                section that Rechtspraak.nl divides documents in.
            </figcaption>
        </figure>;
    }
});

module.exports = Fgiure;