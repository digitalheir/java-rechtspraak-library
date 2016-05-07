import IntroRechtspraakNl from './RechtspraakNl/IntroRechtspraakNl/IntroRechtspraakNl'
import RechtspraakNlMarkup from './RechtspraakNl/RechtspraakNlMarkup/RechtspraakNlMarkup'
import Importing from './RechtspraakNl/Importing/Importing'
import sectionsIntro from './Introduction/sections'
import sectionsTagging from './Tagging/sections'
import sectionsInferringDocumentStructure from './InferringDocumentStructure/sections'
import Methods from './Tagging/Methods/Methods';
import TaggingEvaluation from './Tagging/Evaluation/Evaluation';
import DeterministicTagger from './Tagging/Methods/DeterministicTagger/DeterministicTagger';
import CRF from './Tagging/Methods/CRF/CRF';
import FeatureSelection from './Tagging/Methods/FeatureSelection/FeatureSelection';
import StructureIntroduction from './InferringDocumentStructure/Introduction/Introduction';
import StructureEvaluation from './InferringDocumentStructure/Evaluation/Evaluation';
import ContextFreeGrammar from './InferringDocumentStructure/ContextFreeGrammars/ContextFreeGrammars';
import CYK from './InferringDocumentStructure/CYK/CYK';

export default function(route) {
    switch (route) {
        //Introduction
        case sectionsIntro.rechtspraakNl.id:
            return IntroRechtspraakNl;
        case sectionsIntro.rechtspraakNlMarkup.id:
            return RechtspraakNlMarkup;
        case sectionsIntro.importing.id:
            return Importing;

        // Tagging
        case sectionsTagging.methods.id:
            return CRF;
        case sectionsTagging.featureSelection.id:
            return FeatureSelection;
        case sectionsTagging.manual.id:
            return DeterministicTagger;
        case sectionsTagging.evaluation.id:
            return TaggingEvaluation;

        // InferringDocumentStructure
        case sectionsInferringDocumentStructure.intro.id:
            return StructureIntroduction;
        case sectionsInferringDocumentStructure.scfg.id:
            return ContextFreeGrammar;
        case sectionsInferringDocumentStructure.cyk.id:
            return CYK;
        case sectionsInferringDocumentStructure.evaluation.id:
            return StructureEvaluation;
        default:
            throw Error("No handler for route " + route);
    }
}