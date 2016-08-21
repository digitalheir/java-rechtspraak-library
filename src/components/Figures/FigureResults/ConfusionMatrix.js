import data from './data';
import React from 'react';
import _ from 'underscore';
import figs from '../figs';

function getTable(d, key) {
    var labels = _.map(d.confusionMatrix, ((val, k) => k));
    const labelsNum = labels.length;
    return <div key={key} class="tbl-confusion-matrix" style={{    border: "1px solid #eee", margin: '0 auto 20px auto'}}>
        <table style={{border: 'none', margin: '0 auto 20px auto'}} key={key}>
            <caption style={{fontWeight: 'bold'}}>{key}</caption>
            <tbody>
            <tr>
                <td style={{border: 'none'}}>
                    <table>
                        <caption style={{fontWeight: 'bold'}}>Confusion Matrix</caption>
                        <thead>
                        <tr>
                            <td style={{}}/>
                            <td style={{}}/>
                            <th style={{borderBottom: '1px solid #eee', textAlign:'center',fontWeight:'normal'}}
                                colSpan={labels.length}>Predicted
                            </th>
                        </tr>
                        <tr>
                            <td style={{}}/>
                            <td style={{}}/>
                            {labels.map((l,i)=><th key={"th-"+i+l}>{l}</th>)}
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
                                style={{border: 'none'}}
                                key={i+'-'+l+'-'+l2}>{d.confusionMatrix[l].m[l2] ? d.confusionMatrix[l].m[l2] : 0}</td>));
                            return <tr key={'row'+i+'-'+l}>
                                {children}
                            </tr>
                        })
                        }
                        </tbody>

                    </table>
                </td>
            </tr>
            <tr>
                <td style={{}}>
                    <table style={{margin: '0 auto '+(false?'2':'2')+'0px auto'}}>
                        <caption style={{fontWeight: 'bold'}}>F-scores</caption>
                        <thead><tr>
                        <th>Type</th>
                        <th>Precision</th>
                        <th>Recall</th>
                        <th>F<sub>1</sub>-score</th>
                        <th>F<sub>0.5</sub>-score</th>
                        </tr></thead>
                        <tbody>
                        {
                            labels.map((label, i)=> {
                                return <tr key={"f1-"+i}>
                                    <td>{label}</td>
                                    <td>{d.scores[label].precision.toFixed(2)}</td>
                                    <td>{d.scores[label].recall.toFixed(2)}</td>
                                    <td>{d.scores[label].f1.toFixed(2)}</td>
                                    <td>{d.scores[label].f0_5.toFixed(2)}</td>
                                </tr>
                            })
                        }
                        </tbody>
                    </table>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
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