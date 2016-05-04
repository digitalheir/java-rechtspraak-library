var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var F = require('../../../../math.jsx');
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
                    Once we have analyzed a document's titles, we wish to mark up the text such that
                    there is some semantic sectioning ordering of text blocks. The approach we take
                    for achieving is similar to [TODO: link to Predicting titles], in that we
                    experiment with a linear-chain CRF to automatically mark up the text. The difference
                    here is the labeling granularity: in this chapter, we use text blocks as
                    observations, as opposed to word tokens.
                    The reasoning is that section elements should span paragraphs, i.e.,
                    can be seen as <a href="https://www.w3.org/TR/html4/struct/global.html#h-7.5.3">block
                    elements</a>, and so it is appropriate to label entire text blocks.
                </p>
                <p>
                    [TODO result summary]
                </p>
            </section>
        );
    }
});
module.exports = RsMetadataComponent;