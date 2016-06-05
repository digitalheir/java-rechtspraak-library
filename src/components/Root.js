import React, {PropTypes} from 'react';
import Router from 'react-router'
import Header from './Header'
import GoogleAnalytics from './GoogleAnalytics/GoogleAnalytics';
// import config from '../../config';
import Bibliography from './Bibliography/Bibliography'
import License from './License/License'

let RouteHandler = Router.RouteHandler;


class Root extends React.Component {

    constructor(props) {
        super(props);
        //noinspection UnnecessaryLabelJS
        title: //noinspection BadExpressionStatementJS
            props.string;
    }

    render() {
        let initialProps = {
            __html: safeStringify(this.props)
        };

        //if (!this.props.path) throw new Error("Define path");
        var relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");

        return (
            <html>
            <head>
                <meta charSet="utf-8"/>
                <meta httpEquiv="X-UA-Compatible" content="IE=edge"/>
                <title>{this.props.title}</title>
                <meta name="description" content={this.props.description}/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <link rel="apple-touch-icon" href="apple-touch-icon.png"/>
                <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Noto+Sans"/>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.6.0/katex.min.css"/>
                <link rel="stylesheet" href={relativeToRoot+"style.css"}/>
            </head>
            <body className='p2'>
            {this.props.path == '/full/' ? '' : <Header {...this.props} />}
            <RouteHandler {...this.props} />
            <Bibliography/>
            <License/>
            <script
                id='initial-props'
                type='application/json'
                dangerouslySetInnerHTML={initialProps}></script>
            </body>
            </html>
        );
        // <GoogleAnalytics />
        // <script async src={relativeToRoot+'bundle.js'}></script>
    }
}
// <Footer/> //TODO

// Copy all properties except webpackStats (added in by Webpack), which is huge,
// has circular references and will not be used by the React components anyways.
// Make regex replacements to not break the HTML as well.
function safeStringify(obj) {
    let objNoStats = {};
    for (var key in obj) {
        if (obj.hasOwnProperty(key) && key != "webpackStats") {
            objNoStats[key] = obj[key];
        }
    }
    return JSON.stringify(objNoStats).replace(/<\/script/g, '<\\/script').replace(/<!--/g, '<\\!--')
}
Root.propTypes = {
    path: React.PropTypes.string.isRequired
};
export default Root;
// Html.propTypes = {
//     title: PropTypes.string,
//     description: PropTypes.string,
//     body: PropTypes.string.isRequired,
//     debug: PropTypes.bool.isRequired,
// };
//
// export default Html;

