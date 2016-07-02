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
    if(!fig) throw new Error("Define fig");
    const href = "#" + fig.id;

    return (
        <a href={href}>Figure {fig.num}</a>
    );
  }
};
