//noinspection JSUnresolvedVariable
import React, {Component} from 'react';

export default class extends Component {
    static defaultProps() {
        return {
            fig: undefined,
            width: "100%"
        };
    }

    render() {
        const data = this.props.fig ? this.props.fig : this.props;

        const caption = this.props.children && React.Children.count(this.props.children) > 0 ? this.props.children : data.alt;

        return (
            
            <figure id={data.id}>
                <div className="figure-img-container">
                    <img
                        style={{
                            width: this.props.width,
                            display: "inline-block"
                         }}
                        src={this.props.relativeToRoot+data.src}
                        alt={data.alt}/>
                </div>
                <figcaption>
                    <span className="figure-number">Fig {data.num}.</span> {caption}</figcaption>
            </figure>
        );
    }
}
