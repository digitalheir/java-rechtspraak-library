const disseminationSections = {
    parseval: {
        page: 25,
        id: 'parseval',
        title: 'PARSEVAL',
    },
    results: {
        page: 25,
        id: 'parsing-results',
        title: 'Results',
    },
    futureWork: {
        page: 25,
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