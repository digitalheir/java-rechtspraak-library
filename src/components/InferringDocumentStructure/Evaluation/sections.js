const disseminationSections = {
    parseval: {
        id: 'parseval',
        title: 'PARSEVAL',
    },
    results: {
        id: 'parsing-results',
        title: 'Results & Discussion',
    },
    futureWork: {
        id: 'future-work',
        title: 'Future Work',
    }
};

disseminationSections.inOrder = [
    disseminationSections.parseval,
    disseminationSections.results,
    // disseminationSections.futureWork,
];

export default disseminationSections;