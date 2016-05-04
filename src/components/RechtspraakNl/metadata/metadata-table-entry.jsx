//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default class extends Component {
    static defaultProps() {
        return {}
    }

    render() {
        var p = this.props;
        return (
            <tr>
                <td><code>{p.name}</code></td>
                <td>{p.type}</td>
                <td>{p.description}</td>
            </tr>
        );
    }
}

module.exports = Component;
