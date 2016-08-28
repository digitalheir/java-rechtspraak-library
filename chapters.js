const chapters = {
    introduction: {
        page: '4',
        id: "chapter-introduction",
        title: "Introduction",
        route: '/introduction/'
    },
    importing: {
        page: '6',
        title: "Importing & Tokenizing Data",
        ttitle: "Importing & Tokenizing Data From Rechtspraak.nl",
        id: "chapter-importing-and-tokenizing",
        route: '/importing-and-tokenizing/'
    },
    rechtspraakNl: {
        page: 'X',
        id: "chapter-rechtspraak-nl",
        title: "Rechtspraak.nl Data Set",
        route: '/rechtspraak-nl/'
    },
    tagging: {
        page: 12,
        id: "tagging",
        title: "Tagging Elements",
        ttitle: "Tagging Elements with Conditional Random Fields",
        route: '/tagging/'
    },
    documentStructure: {
        page: 23,
        id: "parsing",
        title: "Inferring a Section Hierarchy",
        ttitle: "Inferring a Section Hierarchy with Probabilistic Context-Free Grammars",
        route: '/document-structure/'
    },
    conclusion: {
        page: 28,
        id: "conclusion",
        title: "Conclusion",
        route: '/conclusion/'
    },
    // presentation: {
    //     title: "Dissemination",
    //     route: '/dissemination/'
    // }

    pathTo: function (fromPath, toChapter) {
        const relativeToRoot = fromPath.match(/\//g).slice(1).map(_ => "../").join("");
        return relativeToRoot + toChapter.route.replace('/', '');
    }
};

chapters.inOrder = [
    chapters.introduction,
    chapters.importing,
    chapters.tagging,
    chapters.documentStructure,
    chapters.conclusion
];

chapters.inOrder.forEach(ch=> {
    if (!ch) throw new Error("Chapters object contains null or undefined item.");
});
chapters.inOrder.forEach((ch, i)=>ch.number = i+1);

//console.log(chapters)

module.exports = chapters;