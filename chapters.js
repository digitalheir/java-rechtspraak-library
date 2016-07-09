const chapters = {
    introduction: {
        id: "chapter-introduction",
        page: 6,
        title: "Introduction",
        route: '/introduction/'
    },
    importing: {
        id: "chapter-importing-and-tokenizing",
        title: "Importing & Tokenizing Data",
        route: '/importing-and-tokenizing/'
    },
    rechtspraakNl: {
        id: "chapter-rechtspraak-nl",
        title: "Rechtspraak.nl Data Set",
        route: '/rechtspraak-nl/'
    },
    tagging: {
        id: "tagging",
        title: "Tagging Elements",
        route: '/tagging/'
    },
    documentStructure: {
        id: "parsing",
        title: "Inferring a Section Hierarchy",
        route: '/document-structure/'
    },
    conclusion: {
        id: "conclusion",
        title: "Conclusion",
        route: '/conclusion/'
    },
    // presentation: {
    //     title: "Dissemination",
    //     route: '/dissemination/'
    // }
    
    pathTo: function(fromPath,toChapter){
        const relativeToRoot = fromPath.match(/\//g).slice(1).map(_ => "../").join("");
        return relativeToRoot+toChapter.route.replace('/','');
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

module.exports = chapters;