var React = require('react');
var PureRenderMixin = require('react-addons-pure-render-mixin');

var F = require('../../../../math');

var LogResComponent = React.createClass({
    mixins: [PureRenderMixin],
    getDefaultProps: function () {
        return {}
    },


    render: function () {
        var o = this.props;

        return <section>
            <h2>Logistic regression</h2>

            <p>

            </p>


            <section>
                <h3>Logit function</h3>

                <p>
                    We assume that the natural logarithm of the odds ratio
                    is equivalent to a linear function of the independent variables. Id est, <F
                    l="logit(p) = \ln \left(\frac{p}{1-p} \right) = \beta_0 + \beta_1 x_1 + ..."/>
                </p>

                <p>

                </p>
            </section>
        </section>;
    }
});

module.exports = LogResComponent;