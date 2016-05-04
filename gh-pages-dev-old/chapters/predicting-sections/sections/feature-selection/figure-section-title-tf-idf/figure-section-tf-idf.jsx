"use strict";

const data = require('./get-term-frequency-data');
const React = require('react');
const _ = require('underscore');
const PureRenderMixin = require('react-addons-pure-render-mixin');
const figs = require('../../../../../figures/figs.jsx');
const PercentageBar = require('../figure-relative-title-count-for-terms/PercentageBar.jsx');


function getTablesForTfIdfScores(tfIdfScores) {
    const tables = [];
    for (var sectionRole in tfIdfScores) {
        //noinspection JSUnfilteredForInLoop
        let tfIdfScoresForRole = tfIdfScores[sectionRole];
        //noinspection JSUnfilteredForInLoop
        tables.push(
            <table key={sectionRole} className="table inline-table">
                <caption>{sectionRole}</caption>
                <thead>
                <tr>
                    <th/>
                    <th>term</th>
                    <th>tf-idf score</th>
                </tr>
                </thead>
                <tbody>
                {_.map(tfIdfScoresForRole, function (term,o) {
                    const tfIdfScoreStr = term[1].toFixed(2);return <tr key={term[0]}>
                    <th className="nr">{o+1}</th>
                    <td>{term[0]}</td>
                    <td className="tf-idf-score">
                        <PercentageBar percentage={term[2]} text={tfIdfScoreStr+''}/>
                        <span className="perc-text">{tfIdfScoreStr}</span>
                    </td>
                </tr>;
                    })
                    }
                </tbody>
            </table>);
    }
    return tables;
}
var Fgiure = React.createClass({
    mixins: [PureRenderMixin],

    getDefaultProps: function () {
        return {}
    },

    render: function () {
        var nTerms = 10;

        var tfIdfScores = data.tfidfForAllTerms(nTerms);
        for (var sectionRole in tfIdfScores) {
            //noinspection JSUnfilteredForInLoop
            let tfIdfScoresForRole = tfIdfScores[sectionRole];

            let maxVal = tfIdfScoresForRole[0][1];
            _.map(tfIdfScoresForRole, function (arr) {
                arr[2] = (100 * arr[1] / maxVal).toFixed(2) + "%"
            });
        }

        return <figure className="chart" id={figs.tfidf.id}>
            <div >
                {getTablesForTfIdfScores(tfIdfScores)}
            </div>
            <figcaption>
                <span className="figure-number">Fig {figs.tfidf.num}.</span> Top {nTerms} tf-idf scores for stemmed
                words in
                section titles, per section role.
                We take as document here any string block (such as a paragraph, or title). Stemming is performed using
                the <a href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">Snowball algorithm for
                Dutch</a>.
            </figcaption>
        </figure>;
    }
});

module.exports = Fgiure;