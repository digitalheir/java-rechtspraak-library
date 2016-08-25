import tfidfForAllTerms from './get-term-frequency-data';
import React from 'react';
import _ from 'underscore';
import figs from '../figs';
import PercentageBar from '../../PercentageBar/PercentageBar';


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
                {_.map(tfIdfScoresForRole, function (term, o) {
                    const tfIdfScoreStr = term[1].toFixed(2);
                    return <tr key={term[0]}>
                        <th className="nr">{o + 1}</th>
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
export default class FigureSectionTitleTfIdf extends React.Component {
    render() {
        var nTerms = 10;

        var tfIdfScores = tfidfForAllTerms(nTerms);
        for (var sectionRole in tfIdfScores) {
            //noinspection JSUnfilteredForInLoop
            let tfIdfScoresForRole = tfIdfScores[sectionRole];

            let maxVal = tfIdfScoresForRole[0][1];
            _.map(tfIdfScoresForRole, function (arr) {
                arr[2] = (100 * arr[1] / maxVal).toFixed(2) + "%"
            });
        }

        return <figure className="chart" id={figs.sectionsTfidf.id}>
            <div >
                {getTablesForTfIdfScores(tfIdfScores)}
            </div>
            <figcaption>
                <span className="figure-number">Fig {figs.sectionsTfidf.num}.</span> Top {nTerms} tf-idf scores for stemmed
                words in
                section titles, per section role.
                Stemming is performed using
                the <a hrefLang="en" href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">Snowball algorithm for
                Dutch</a>.
                '[NUMBER]', '[PUNCTUATION]' and '[de|het|een]' are special classes for numbers, punctation, and
                grammatical articles (the Dutch equivalent of the articles 'the' and 'a').
            </figcaption>
        </figure>;
    }
}