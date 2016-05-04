import React from 'react'
import Router from 'react-router'
import Root from './components/Root'
import Index from './components/Index'
import RechtspraakNl from './components/RechtspraakNl/RechtspraakNl'
import Introduction from './components/Introduction/Introduction'
import Tagging from './components/Tagging/Tagging'
import InferringDocumentStructure from './components/InferringDocumentStructure/InferringDocumentStructure'
import chapters from '../chapters'

let Route = Router.Route;
let DefaultRoute = Router.DefaultRoute;


let Routes = <Route handler={Root} path='/'>
    <DefaultRoute handler={Index}/>
    {chapters.inOrder.map(chapter=> {
        switch (chapter.route){
            // case chapters.documentStructure.route:
            case chapters.introduction.route:
                return <Route path={chapter.route} handler={Introduction}/>;
            case chapters.rechtspraakNl.route:
                return <Route path={chapter.route} handler={RechtspraakNl}/>;
            case chapters.tagging.route:
                return <Route path={chapter.route} handler={Tagging}/>;
            case chapters.documentStructure.route:
                return <Route path={chapter.route} handler={InferringDocumentStructure}/>;
            default:
                return "";
        }
        return ;
    })}
</Route>;

export default Routes;