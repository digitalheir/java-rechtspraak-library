var chapters = {
    introduction: {
        title: "Introduction",
        route: '/introduction/'
    },
    rechtspraakNl: {
        title: "Rechtspraak.nl Data Set",
        route: '/rechtspraak-nl/'
    },
    tagging: {
        title: "Tagging Elements",
        route: '/tagging/'
    },
    documentStructure: {
        title: "Inferring a Document Structure",
        route: '/document-structure/'
    },
    dissemination: {
        title: "Dissemination of Enrich Dataset",
        route: '/dissemination/'
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
    // chapters.rechtspraakNl,
    chapters.tagging,
    chapters.documentStructure
];

chapters.inOrder.forEach(ch=> {
    if (!ch) throw new Error("Chapters object contains null or undefined item.");
});

module.exports = chapters;