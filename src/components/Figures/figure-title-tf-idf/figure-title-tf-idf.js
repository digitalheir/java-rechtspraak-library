import tfidfForAllTerms  from './get-term-frequency-data'
import React  from 'react'
import _  from 'underscore'
import figs  from '../figs'
import PercentageBar  from '../../PercentageBar/PercentageBar'

export default class FigureTitleTfIdf extends React.Component {
    render() {
        var nTerms = 20;
        var tfIdfScores = tfidfForAllTerms(nTerms);
        var maxVal = tfIdfScores[0][1];
        _.map(tfIdfScores,function(arr){
           arr[2] = (100*arr[1]/maxVal).toFixed(2)+"%"
        });

        return <figure className="chart" id={figs.tfidf.id}>
            <table className="chart table">
                <thead>
                <tr>
                    <th/>
                    <th>term</th>
                    <th>tf-idf score</th>
                </tr>
                </thead>
                <tbody>
                {_.map(tfIdfScores, function (term,o) {
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
            </table>
            <figcaption>
                <span className="figure-number">Fig {figs.tfidf.num}.</span> Top {nTerms} tf-idf
                scores for stemmed words in
                section titles.
                We take as document here any string block (such as a paragraph, or title). Stemming is performed using
                the <a href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">Snowball algorithm for
                Dutch</a>.
            </figcaption>
        </figure>;
    }
}