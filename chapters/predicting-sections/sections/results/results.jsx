var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var F = require('../../../../math.jsx');
const FigRef = require('./../../../../figures/fig-ref.jsx');
const figs = require('./../../../../figures/figs.jsx');

var RsMetadataComponent = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        return (
            <section id={this.props.id}>
                <h2>{this.props.title}</h2>

                <p>
                    We train a CRF in Mallet with default setting (i.e, linear
                    chain CRF with gradient decent), using random subsets of the
                    available pre-tagged data, of 70% and 30% for training
                    and testing respectively.
                </p>

                <p>
                    Yields an F1-score of 96% (precision: 96%, recall: 96%)
                </p>
            </section>
        );
    }
});
module.exports = RsMetadataComponent;