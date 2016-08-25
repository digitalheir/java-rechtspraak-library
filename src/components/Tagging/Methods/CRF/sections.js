const crfSections = {
    undirectedGraphicalModels: {
        page: 14,
        id: 'undirected-graphical-models',
        title: 'Undirected Graphical Models'
    },
    hmm: {
        page: 14,
        id: 'hmm',
        title: 'Directed Graphical Models'
    }, 
    generativeDiscriminative: {
        page: 16,
        id: 'generative-discriminative',
        title: 'Generative-Discriminative Pairs'
    },
    linearChain: {
        page: 16,
        id: 'linear-chain-crf',
        title: 'Linear-Chain Conditional Random Fields'
    },
    crfPerformance: {
        page: 'x',
        id: 'crf-performance',
        title: 'Performance'
    },
    inference: {
        page: 18,
        id: 'crf-inference',
        title: 'Inference'
    },
    parameterEstimation: {
        page: 17,
        id: 'crf-parameter-estimation',
        title: 'Parameter Estimation'
    }
};

crfSections.inOrder = [
    crfSections.hmm,
    crfSections.undirectedGraphicalModels,
    crfSections.generativeDiscriminative,
    crfSections.linearChain,
    crfSections.parameterEstimation,
    crfSections.inference,
    // crfSections.crfPerformance,
];


export default crfSections;
