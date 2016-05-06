import data from './data';
import React from 'react';
import _ from 'underscore';
import figs from '../figs';
import PercentageBar from '../../PercentageBar/PercentageBar';
//const LineChart = require('../../../../../charts/bar-chart/BarChart.jsx');

export default class FigureWordCount extends React.Component {
    render() {
        //<LineChart labelY='²log frequency' sourceHref={data.url} data={data.data}/>
        return <figure className="chart" id={figs.tfidf.id}>
            <table className=" table">
                <thead>
                <tr>
                    <th>Word #</th>
                    <th>Relative frequency</th>
                    <th>Absolute frequency</th>
                </tr>
                </thead>
                <tbody>
                {_.map(data.data, function (obj) {
                    var percentageTxt = (100 * obj.titleCount / data.totalCount).toFixed(2) + "%";
                    return <tr key={obj.wordCount}>
                        <th className="nr">{obj.wordCount}</th>
                        <td>
                            <PercentageBar percentage={percentageTxt} text={percentageTxt}/>
                            <span className="perc-text">{percentageTxt}</span>
                        </td>
                        <td><span className='perc-text'>{obj.titleCount}</span></td>
                    </tr>;
                })
                }
                </tbody>
            </table>
            <figcaption>
                <span className="figure-number">Fig {figs.tfidf.num}.</span> Word count in
                titles.
            </figcaption>
        </figure>;
    }
}