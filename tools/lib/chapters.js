//todo
const urls = require('../urls');

const rs = function () {
    const id = "rechtspraak-nl";
    const sections = require(`./${id}/sections`);
    const title = "Rechtspraak.nl";
    const description = "Description of Rechtspraak.nl data set describing Dutch case law";
    const href = urls.base + id + '/';
    sections.inOrder.forEach(section=> {
        section.href = href + "#" + section.id
    });

    return {
        title: title,
        description: description,
        id: id,
        href: href,
        sections: sections
    };
}();


const crf = function () {
    const id = "conditional-random-fields";
    const href = urls.base + id + '/';
    const sections = require(`./${id}/sections`);

    sections.inOrder.forEach(section=> {
        section.href = href + "#" + section.id
    });
    return {
        title: "Conditional Random Fields",
        description: "Introduction to Conditional Random Fields",
        id: id,
        href: href,
        sections: sections
    };
}();

const titlePrediction = function () {
    const id = "predicting-titles";
    const href = urls.base + id + '/';
    const sections = require(`./${id}/sections`);

    sections.inOrder.forEach(section => {
        section.href = href + "#" + section.id
    });
    return {
        title: "Predicting titles",
        description: "CRF experiment to predict titles in Dutch case law texts",
        id: id,
        href: href,
        sections: sections
    };
}();
const sectionPrediction = function () {
    const id = "predicting-sections";
    const href = urls.base + id + '/';
    const sections = require(`./${id}/sections`);

    sections.inOrder.forEach(section=> {
        section.href = href + "#" + section.id
    });
    return {
        title: "Predicting sections",
        description: "CRF experiment to predict sections in Dutch case law texts",
        id: id,
        href: href,
        sections: sections
    };
}();

const appendixSoftware = function () {
    const id = "software-used";
    const href = urls.base + id;
    const sections = require(`./${id}/sections`);

    sections.inOrder.forEach(section=> {
        section.href = href + "#" + section.id
    });
    return {
        title: "Appendix I",
        subtitle: "Software Used",
        description: "Introduction to Conditional Random Fields",
        id: id,
        href: href,
        sections: sections
    };
}();

const chapters = {
    rechtspraak: rs,
    crf: crf,
    titlePrediction: titlePrediction,
    sectionPrediction: sectionPrediction,
    appendixSoftware: appendixSoftware
};

chapters.inOrder = [
    rs, crf, titlePrediction, appendixSoftware
];

module.exports = chapters;
