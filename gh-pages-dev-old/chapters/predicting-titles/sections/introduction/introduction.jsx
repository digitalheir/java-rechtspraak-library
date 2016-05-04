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
                Once we have analyzed a document's titles, we wish to mark up the text such that
                there is some semantic sectioning ordering of text blocks. The approach we take
                here is similar to []

                It is tempting to assume that all documents that contain a <code>section</code> tag are
                well-structured. On closer inspection, however, many of these documents seem to be erroneously
                marked up. For example, consider <code>
                <a href="data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:RBDHA:2015:554">ECLI:NL:RBDHA:2015:554</a>
            </code>, which contains a section labeled <code>beslissing</code> ('judgment') of which the title is the
                abridged text of an article of a certain law. Later on in the document, the section that contains the
                actual judgment is correctly annotated.
            </section>
        );
    }
});
module.exports = RsMetadataComponent;