var React = require('react');
var _ = require('underscore');
var PureRenderMixin = require('react-addons-pure-render-mixin');
var StackedBarChart = require('../../../../charts/StackedBarChart.jsx');
var Source = require('../../../../source');

var ref = require('../../../../citation/references');
var refs = ref.ref;

var MarkupComponent = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },
    render: function () {
        return <section id={this.props.id}>
            <h2>{this.props.title}</h2>

            <p>
                Software created in the development of this thesis
            </p>

            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                </tr>
                </thead>


                <tbody>
                <tr>
                    <td><a hrefLang="en" href="https://github.com/digitalheir/java-xml-to-json/">
                        {abbrs.xml} to {abbrs.json} java library
                    </a></td>
                    <td>Library for non-destructively converting {abbrs.xml} to {abbrs.json} and back.</td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                </tr>
                </tbody>
            </table>

        </section>
            ;
    }
});
module.exports = MarkupComponent;