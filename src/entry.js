import React from 'react'
import ReactDOMServer from 'react-dom/server'
import Router from 'react-router'
import Routes from './Routes.jsx'

if (typeof document !== 'undefined') {
    var initialProps = JSON.parse(document.getElementById('initial-props').innerHTML);
    Router.run(Routes, Router.HistoryLocation, function (Handler) {
        React.render(React.createElement(Handler, initialProps), document)
    })
}

let Entry = function render (locals, callback) {
    Router.run(Routes, locals.path, function (Handler) {
        var html = ReactDOMServer.renderToStaticMarkup(React.createElement(Handler, locals));
        callback(null, '<!DOCTYPE html>' + html)
    })
};

export default Entry;