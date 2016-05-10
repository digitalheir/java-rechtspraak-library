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
    crf: {
        id: 'crf',
        title: "Conditional Random Fields",
    },
    featureSelection: {
        id: 'feature-selection',
        title: 'Feature Selection',
    },
    deterministic: {
        id: 'deterministic-tagger',
        title: 'Deterministic Tagger',
    }, 
    manual: {
        id: 'manual-tagger',
        title: 'Manual Tagger',
    }
};

taggingSections.inOrder = [
    taggingSections.featureSelection,
    taggingSections.crf,
    taggingSections.deterministic,
    taggingSections.manual
];
export default taggingSections;
