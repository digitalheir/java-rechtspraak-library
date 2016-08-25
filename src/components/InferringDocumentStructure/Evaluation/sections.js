const disseminationSections = {
    parseval: {
        page: 26,
        id: 'parseval',
        title: 'PARSEVAL',
    },
    results: {
        page: 26,
        id: 'parsing-results',
        title: 'Results',
    },
    futureWork: {
        page: -666,
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