//noinspection JSUnresolvedVariable
import React, {Component} from 'react';


export default class extends Component {
    static propTypes() {
        return {
            fig: React.PropTypes.shape({
                id: React.PropTypes.string.isRequired,
                num: React.PropTypes.isRequired
            }).isRequired
        };
    }

    render() {
        const fig = this.props.fig;
        if (!fig) throw new Error("No fig set to refer to...");
        const href = "#" + fig.id;

        // Note creating a dummy scope is an ugly solution, but valid
        // http://webmasters.stackexchange.com/questions/55489/microdata-with-nested-product-and-reviews/55494#55494
        return (
            <span itemScope={true}>
                <a itemRef={fig.id}
                   id={fig.id+'-ref'}
                   itemScope={true}
                   href={href}><span itemProp="name">Figure {fig.num}</span></a>
            </span>
        );
    }
};
