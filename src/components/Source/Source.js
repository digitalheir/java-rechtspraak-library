//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
class Source extends Component {
  render() {
    return (
      <sup className="data-source" style={this.props.styles}><a
        href={this.props.href}>[source]</a>
      </sup>
    );
  }
}
Source.defaultProps = {
  href: undefined,
  styles: {}
};

export default Source;
