//noinspection JSUnresolvedVariable
import React, {Component} from 'react';
import IntroRechtspraakNl from './RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from './RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from './RechtspraakNl/Importing/Importing'
import sectionsIntro from './Introduction/sections'
import sectionsTagging from './Tagging/sections'

import sectionsParsingEvaluation from './InferringDocumentStructure/Evaluation/sections'
import PARSEVAL from './InferringDocumentStructure/Evaluation/PARSEVAL/PARSEVAL'
import parseResults from './InferringDocumentStructure/Evaluation/Results/Results'
import parseFutureWork from './InferringDocumentStructure/Evaluation/FutureWork/FutureWork'

import sectionsEvaluation from './Tagging/Results/sections'
import FScores from './Tagging/Results/FScores/FScores'
import TaggingResultz from './Tagging/Results/Resultz/Resultz'

import sectionsTaggingMethods from './Tagging/Methods/sections'
import sectionsInferringDocumentStructure from './InferringDocumentStructure/sections'
import sectionsInferringDocumentStructureMethods from './InferringDocumentStructure/Methods/sections'
import sectionsCrf from './Tagging/Methods/CRF/sections'

import GraphicalModels  from './Tagging/Methods/CRF/GraphicalModels/GraphicalModels';
import HMMs from './Tagging/Methods/CRF/HMMs/HMMs';
import LinearChainCRF from './Tagging/Methods/CRF/LinearChainCRF/LinearChainCRF';
import Inference from './Tagging/Methods/CRF/Inference/Inference';
import ParameterEstimation from './Tagging/Methods/CRF/ParameterEstimation/ParameterEstimation';
import Performance from './Tagging/Methods/CRF/Performance/Performance';

import TaggingEvaluation from './Tagging/Results/Results';
//import TaggingDiscussion from './Tagging/Discussion/Discussion';
import TaggingMethods from './Tagging/Methods/Methods';
import TaggingIntroduction from './Tagging/Introduction/Introduction';
// import ManualTagger from './Tagging/Methods/ManualTagger/ManualTagger';
import DeterministicTagger from './Tagging/Methods/DeterministicTagger/DeterministicTagger';
import CRF from './Tagging/Methods/CRF/CRF';
import FeatureSelection from './Tagging/Methods/FeatureSelection/FeatureSelection';
import StructureIntroduction from './InferringDocumentStructure/Introduction/Introduction';
import StructureMethods from './InferringDocumentStructure/Methods/Methods';
import StructureEvaluation from './InferringDocumentStructure/Evaluation/Evaluation';
import ContextFreeGrammar from './InferringDocumentStructure/ContextFreeGrammars/ContextFreeGrammars';
import AdditionalEnrichment from './Dissemination/AdditionalEnrichment/AdditionalEnrichment';
import CYK from './InferringDocumentStructure/CYK/CYK';
import Dissemination from './Dissemination/sections';

export default function getHandler(route) {
    switch (route) {
        //Introduction
        case sectionsIntro.rechtspraakNl.id:
            return IntroRechtspraakNl;
        case sectionsIntro.rechtspraakNlMarkup.id:
            return RechtspraakNlMarkup;
        case sectionsIntro.importing.id:
            return Importing;

        // Tagging
        case sectionsTagging.taggingIntroduction.id:
            return TaggingIntroduction;
        case sectionsTagging.taggingMethods.id:
            return TaggingMethods;
        case sectionsTaggingMethods.featureSelection.id:
            return FeatureSelection;
        case sectionsTaggingMethods.crf.id:
            return CRF;
        case sectionsTaggingMethods.deterministic.id:
            return DeterministicTagger;
        //case sectionsTaggingMethods.manual.id:
        // return ManualTagger;
        // case sectionsTagging.taggingDiscussion.id:
        //     return TaggingDiscussion;
        case sectionsTagging.taggingResults.id:
            return TaggingEvaluation;

        // CRF
        case sectionsCrf.graphicalModels.id:
            return GraphicalModels;
        case sectionsCrf.crfPerformance.id:
            return Performance;
        case sectionsCrf.linearChain.id:
            return LinearChainCRF;
        case sectionsCrf.inference.id:
            return Inference;
        case sectionsCrf.parameterEstimation.id:
            return ParameterEstimation;
        case sectionsCrf.hmm.id:
            return HMMs;

        // evaluation
        case sectionsEvaluation.fScores.id:
            return FScores;
        case sectionsEvaluation.results.id:
            return TaggingResultz;

        // sectionsParsingEvaluation
        case sectionsParsingEvaluation.parseval.id:
            return PARSEVAL;
        case sectionsParsingEvaluation.results.id:
            return parseResults;
        case sectionsParsingEvaluation.futureWork.id:
            return parseFutureWork;

        // InferringDocumentStructure
        case sectionsInferringDocumentStructure.intro.id:
            return StructureIntroduction;
        case sectionsInferringDocumentStructure.methods.id:
            return StructureMethods;
        case sectionsInferringDocumentStructureMethods.scfg.id:
            return ContextFreeGrammar;
        case sectionsInferringDocumentStructureMethods.cyk.id:
            return CYK;
        case sectionsInferringDocumentStructure.evaluation.id:
            return StructureEvaluation;

        //  Dissemination
        case Dissemination.additionalEnrichment.id:
            return AdditionalEnrichment;

        default:
            throw Error("No handler for route " + route + ". You should edit getSectionComponent.js");
    }
}

export function getSubSections(props, headerLevel) {
    return function (section) {
        if (!section || !section.id) throw new Error("Subsection was invalid: " + section);
        const SectionContent = getHandler(section.id);

        const header = React.createElement(
            "h" + headerLevel,
            {className: "title"},
            <a className="link-up" href="#"/>,
            section.title
        );
        let subsubsections = '';
        if (!!SectionContent.getSections) {
            const getSubSubsections = getSubSections(props, headerLevel + 1);
            subsubsections = SectionContent.getSections().inOrder.map(getSubSubsections);
            //console.log(subsubsections);
        }
        return <section key={section.id}
                        id={section.id}>
            {header}
            <SectionContent {...props}/>
            {subsubsections}
        </section>;
    }
}