import React from 'react'
import Router from 'react-router'
import Root from './components/Root'
import Index from './components/Index'
import Introduction from './components/Introduction/Introduction'
import FullThesis from './components/FullThesis/FullThesis'
import Tagging from './components/Tagging/Tagging'
import InferringDocumentStructure from './components/InferringDocumentStructure/InferringDocumentStructure'
import Dissemination from './components/Dissemination/Dissemination'
import ImportingAndTokenizing from './components/ImportingAndTokenizing/ImportingAndTokenizing'
import chapters from '../chapters'

let Route = Router.Route;
let DefaultRoute = Router.DefaultRoute;


let Routes = <Route handler={Root} path='/'>
    <DefaultRoute handler={Index}/>
    {
        chapters.inOrder.map(
            (chapter,i) => 
            <Route 
                key={"route-"+i}
                path={chapter.route} 
                chapterObject={chapter} 
                handler={getHandler(chapter.route)}/>
        )
    }
    <Route key={"route-full"} path={'/full/'} handler={FullThesis}/>
    )}
</Route>;

function getHandler(route) {
    switch (route) {
        case chapters.introduction.route:
            return Introduction;
        case chapters.tagging.route:
            return Tagging;
        case chapters.documentStructure.route:
            return InferringDocumentStructure;
        case chapters.conclusion.route:
            return Dissemination;
        case chapters.importing.route:
            return ImportingAndTokenizing;
        default:
            throw Error("No handler for route " + route+". You should edit Routes.jsx.");
    }
}

export {getHandler};
export default Routes;