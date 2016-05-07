const inferringSections = {
    intro: {
        id: 'parse-tree-introduction',
        title: "Introduction",
    },
    scfg: {
        id: 'scfg',
        title: 'Stochastic Context Free Grammars',
    },
    cyk: {
        id: 'cyk',
        title: 'CYK Algorithm',
    },
    evaluation: {
        id: 'parse-tree-evaluation',
        title: 'Evaluation',
    }
};

inferringSections.inOrder = [
    inferringSections.intro,
    inferringSections.scfg,
    inferringSections.cyk,
    inferringSections.evaluation
];
export default inferringSections;
