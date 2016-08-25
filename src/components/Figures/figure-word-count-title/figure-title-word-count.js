import data from './data';
import React from 'react';
import _ from 'underscore';
import figs from '../figs';
import PercentageBar from '../../PercentageBar/PercentageBar';
//const LineChart = require('../../../../../charts/bar-chart/BarChart.jsx');

export default class FigureWordCount extends React.Component {
    render() {
        //<LineChart labelY='²log frequency' sourceHref={data.url} data={data.data}/>
        return <figure itemProp="hasPart"
                       itemScope={true}
                       itemType="https://schema.org/Dataset"
                       className="chart"
                       id={figs.figTitleWordCount.id}>
            <span itemProp="distribution" itemScope={true} itemType="https://schema.org/DataDownload">
                <a itemProp="contentUrl" href={data.url}>Data source</a>
            </span>
            <table className=" table">
                <thead>
                <tr>
                    <th>Number of words</th>
                    <th>Relative frequency</th>
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
                    </tr>;
                })// <td><span className='perc-text'>{obj.titleCount}</span></td>
                }
                </tbody>
            </table>
            <figcaption>
                <span itemProp="alternateName" className="figure-number">Fig {figs.figTitleWordCount.num}</span>. <span
                itemProp="description">Word count in
               section titles, excluding numbering.</span>
            </figcaption>
        </figure>;
    }
}