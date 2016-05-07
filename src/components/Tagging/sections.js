const taggingSections = {
    // intro: {
    //     id: 'introduction',
    //     title: "Introduction",
    //     component: Introduction
    // },
    // methods: {
    //     id: 'methods',
    //     title: "Methods",
    //     component: Methods
    // },
    methods: {
        id: 'crf',
        title: "Conditional Random Fields",
    },
    featureSelection: {
        id: 'feature-selection',
        title: 'Feature Selection',
    },
    manual: {
        id: 'manual-tagger',
        title: 'Deterministic Tagger',
    },
    evaluation: {
        id: 'tagging-evaluation',
        title: 'Evaluation',
    }
};

taggingSections.inOrder = [
    taggingSections.featureSelection,
    taggingSections.methods,
    taggingSections.manual,
    taggingSections.evaluation
];
export default taggingSections;
