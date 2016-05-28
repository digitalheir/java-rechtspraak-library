import data from './data';
import React from 'react';
import _ from 'underscore';
import figs from '../figs';

function getTable(d, key) {
    var labels = _.map(d.confusionMatrix, ((val, k) => k));

    return <table style={{margin: '0 0 '+(false?'2':'8')+'0px 0'}} key={key}>
        <caption>{key}</caption>
        <thead>
        <tr>
            <td/>
            <td/>
            <th style={{textAlign:'center',fontWeight:'normal'}} colSpan={labels.length}>Predicted</th>
        </tr>
        <tr>
            <td/>
            <td/>
            {labels.map(l=><th>{l}</th>)}
        </tr>
        </thead>
        <tbody>
        {labels.map((l, i)=> {
            let children = [<th key={i+'-'+l}>{l}</th>];
            if (i == 0) {
                children.push(<th style={{fontWeight:'normal'}}
                                  key={"head-90"}
                                  rowSpan={labels.length}>
                    <div
                        className="minus90">
                        Actual
                    </div>
                </th>);
            }
            children = children.concat(labels.map(l2 => <td
                key={i+'-'+l+'-'+l2}>{d.confusionMatrix[l].m[l2] ? d.confusionMatrix[l].m[l2] : 0}</td>));
            return <tr key={'row'+i+'-'+l}>
                {children}
            </tr>
        })
        }
        </tbody>
    </table>
}
export default class ConfusionMatrix extends React.Component {
    render() {
        // class="wikitable" style="border:none; float:left; margin-top:0;"
        return <figure className="chart" id={figs.confusionMatrix.id}>
            {_.map(data, getTable)}
            <figcaption>
                <span className="figure-number">Fig {figs.confusionMatrix.num}.</span>
                Confusion matrices for the three test conditions.
            </figcaption>
        </figure>
            ;
    }
}