import IntroRechtspraakNl from './RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from './RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from './RechtspraakNl/Importing/Importing'
import sectionsIntro from './Introduction/sections'
import sectionsTagging from './Tagging/sections'
import sectionsTaggingMethods from './Tagging/Methods/sections'
import sectionsInferringDocumentStructure from './InferringDocumentStructure/sections'
import sectionsInferringDocumentStructureMethods from './InferringDocumentStructure/Methods/sections'
import TaggingEvaluation from './Tagging/Results/Results';
import TaggingDiscussion from './Tagging/Discussion/Discussion';
import TaggingMethods from './Tagging/Methods/Methods';
import TaggingIntroduction from './Tagging/Introduction/Introduction';
import ManualTagger from './Tagging/Methods/ManualTagger/ManualTagger';
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

export default function (route) {
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
        case sectionsTaggingMethods.manual.id:
            return ManualTagger;
        case sectionsTagging.taggingDiscussion.id:
            return TaggingDiscussion;
        case sectionsTagging.taggingResults.id:
            return TaggingEvaluation;

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
            throw Error("No handler for route " + route+". You should edit getSectionComponent.js");
    }
}