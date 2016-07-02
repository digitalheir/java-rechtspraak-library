//noinspection JSUnresolvedVariable
import React, {Component} from 'react';


export default class extends Component {
    static propTypes() {
        return {
            listing: React.PropTypes.shape({
                id: React.PropTypes.string.isRequired,
                num: React.PropTypes.isRequired
            }).isRequired
        };
    }

    render() {
        const fig = this.props.listing;
        if(!fig) throw new Error("Define listing");
        const href = "#" + fig.id;

        return (
            <a href={href}>Listing {fig.num}</a>
        );
    }
};
