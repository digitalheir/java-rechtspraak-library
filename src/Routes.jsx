import React from 'react'
import Router from 'react-router'
import Root from './components/Root'
import Index from './components/Index'
import Introduction from './components/Introduction/Introduction'
import Tagging from './components/Tagging/Tagging'
import InferringDocumentStructure from './components/InferringDocumentStructure/InferringDocumentStructure'
import chapters from '../chapters'

let Route = Router.Route;
let DefaultRoute = Router.DefaultRoute;


let Routes = <Route handler={Root} path='/'>
    <DefaultRoute handler={Index}/>
    {
        chapters.inOrder.map(chapter => <Route path={chapter.route} handler={getHandler(chapter.route)}/>)
    })}
</Route>;

function getHandler(route) {
    switch (route) {
        case chapters.introduction.route:
            return Introduction;
        case chapters.tagging.route:
            return Tagging;
        case chapters.documentStructure.route:
            return InferringDocumentStructure;
        default:
            throw Error("No handler for route " + route);
    }
}

export {getHandler};
export default Routes;