const React = require('react');
const _ = require('underscore');
const PercentageBar = require('../../PercentageBar/PercentageBar');
const figs = require('../figs');


const data =require('./data');


export default class FigureRelativeTitleCountForTerms extends React.Component {
    //noinspection JSMethodCanBeStatic
    render() {
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
                {_.map(data.data, function (term,o) {
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
                <span className="figure-number">Fig {figs.titleTf.num}.</span> Top {data.numberOfTerms}
                word stems in occurring
                section titles. Percentages are the percentage of titles texts that the word stem occurs in.
                Stemming is performed using
                the <a href="http://snowball.tartarus.org/algorithms/dutch/stemmer.html">Snowball algorithm for
                Dutch</a>.
            </figcaption>
        </figure>;
    }
}