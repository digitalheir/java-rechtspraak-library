import React, {PropTypes} from 'react';
import Router from 'react-router'
import Header from './Header'
import GoogleAnalytics from './GoogleAnalytics/GoogleAnalytics';
// import config from '../../config';
import Bibliography from './Bibliography/Bibliography'
import License from './License/License'
import chapters from '../../chapters'

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

        // <link rel="apple-touch-icon" href="apple-touch-icon.png"/>
        //if (!this.props.path) throw new Error("Define path");
        const relativeToRoot = this.props.path.match(/\//g).slice(1).map(a=>"../").join("");
        return (
            <html lang="en">
            <head>
                <meta charSet="utf-8"/>
                <meta httpEquiv="X-UA-Compatible" content="IE=edge"/>
                <title>{this.getTitle(this.props.path)}</title>
                <meta name="description" content={this.getDescription(this.props.path)}/>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
                <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Noto+Sans"/>
                <link rel="stylesheet"
                      href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css"/>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/KaTeX/0.6.0/katex.min.css"/>
                <link rel="stylesheet" href={relativeToRoot+"style.css"}/>
            </head>
            <body itemScope={true}
                  itemType="http://schema.org/WebPage"
                  className='p2'>
            {this.props.path == '/full/' ? '' : <Header {...this.props} />}
            <meta content="en" itemProp="inLanguage"/>
            <meta content="true" itemProp="isFamilyFriendly"/>
            <meta content="2016" itemProp="copyrightYear"/>
            <meta content="expositive" itemProp="interactivityType"/>
            <RouteHandler {...this.props} />
            <Bibliography/>
            {this.props.path == '/full/' ? '' : <License {...this.props} />}
            <script
                id='initial-props'
                type='application/json'
                dangerouslySetInnerHTML={initialProps}></script>
            <script async={true} src={relativeToRoot+'app.js'}></script>
            </body>
            </html>
        );
        // <GoogleAnalytics />
    }

    getDescription(path) {
        switch (path) {
            case chapters.introduction.route:
                return 'Introduction to the problem of creating a document structure for documents of Dutch case law using Conditional Random Fields and Probabilistic Context-Free Grammars';
            case chapters.importing.route:
                return 'Chapter on importing and tokenizing Dutch case law documents';
            case chapters.rechtspraakNl.route:
                return 'Chapter on the Rechtspraak.nl open data set';
            case chapters.tagging.route:
                return 'Chapter on macro tagging document elements with a Conditonal Random Field';
            case chapters.documentStructure.route:
                return 'Chapter on creating a section hierarchy for a document with Probabilistic Context-Free Grammars';
            case chapters.conclusion.route:
                return 'Review of results and information on dissemination';
            case '/full/':
            default:
                return 'MSc thesis about creating structure documents of Dutch case law using Conditional Random Fields and Probabilistic Context-Free Grammars';
        }
    }

    getTitle(path) {
        for (let chapter in chapters)
            if (chapters.hasOwnProperty(chapter))
                if (chapters[chapter].route == path)
                    return chapters[chapter].ttitle ? chapters[chapter].ttitle : chapters[chapter].title;

        return this.props.title;
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

