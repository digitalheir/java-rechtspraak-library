"use strict";

const React = require('react');
const _ = require('underscore');
const PureRenderMixin = require('react-addons-pure-render-mixin');

const PercentageBar = require('./PercentageBar.jsx');
const figs = require('../../../../../figures/figs.jsx');



const numberOfTerms = 10;
const data =require('./raw-data');
data.splice(numberOfTerms);


var Fgiure = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        return <figure id={figs.titleTf.id}>
            <table className="chart table">
                <thead>
                <tr>
                    <th/>
                    <th>term</th>
                    <th>relative title frequency</th>
                </tr>
                </thead>
                <tbody>
                {_.map(data, function (term,o) {
                    let percentageTxt = term[1].toFixed(2)+"%";
                    return <tr key={term[0]}>
                        <th className="nr">{o+1}</th>
                        <td>{term[0]}</td>
                        <td>
                            <PercentageBar percentage={percentageTxt} text={percentageTxt}/>
                            <span className="perc-text">{percentageTxt}</span>
                        </td>
                    </tr>;
                    })
                    }
                </tbody>
            </table>
            <figcaption>
                <span className="figure-number">Fig {figs.titleTf.num}.</span> Top {numberOfTerms}
                word stems in occurring
                section titles. Percentages are the percentage of titles texts that the word stem occurs in.
                Stemming is performed using
                the <a href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">Snowball algorithm for
                Dutch</a>.
            </figcaption>
        </figure>;
    }
});

module.exports = Fgiure;