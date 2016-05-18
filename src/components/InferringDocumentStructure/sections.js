const inferringSections = {
    intro: {
        id: 'parse-tree-introduction',
        title: "Introduction",
    },
    methods: {
        id: 'parse-tree-methods',
        title: "Methods",
    },
    evaluation: {
        id: 'parse-tree-evaluation',
        title: 'Results',
    }
};

inferringSections.inOrder = [
    inferringSections.intro,
    inferringSections.methods,
    inferringSections.evaluation
];
export default inferringSections;
