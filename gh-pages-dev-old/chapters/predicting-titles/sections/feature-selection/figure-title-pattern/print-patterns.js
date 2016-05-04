const downloadString = require('../../../../../download-string-sync');
var encodeUri = require("querystring").escape;
var fs = require("fs");
var _ = require("underscore");

var OTHER = '_OTHER';

var URL = "https://rechtspraak.cloudant.com/docs/" +
        "_design/stats/" +
        "_list/value_large_enough/" +
        "section_roles?" +
        "group_level=2"
//+ "&stale=ok"
//+"&startkey="+encodeUri("[\"\"]")
//+"&endkey="+encodeUri("[\"z\"]")
    ;

function download() {
    var result = JSON.parse(downloadString(URL));

// Transform results
    var aggr = {};
    result.forEach(function (row) {
        aggr[row.key[0]] = aggr[row.key[0]] ? aggr[row.key[0]] : {};
//                    if (row.value < 10) {
//                        aggr[row.key[0]][OTHER] = aggr[row.key[0]][OTHER] ?
//                        aggr[row.key[0]][OTHER] + row.value : row.value;
//                    } else {
        var pattern = row.key[1].trim().length <= 0 ? '_EMPTY' : row.key[1];
        aggr[row.key[0]][pattern] = row.value;
//                    }
    });

    var rootChildren = [];
    for (var name in aggr) {
        if (aggr.hasOwnProperty(name)) {
            var patternChildren = [];
            var aggrPatterns = aggr[name];
            for (var pattern in aggrPatterns) {
                if (aggrPatterns.hasOwnProperty(pattern)) {
                    patternChildren.push({
                        name: pattern,
                        size: aggrPatterns[pattern]
                    });
                }
            }

            rootChildren.push({
                name: name,
                children: patternChildren
            });
        }
    }
    return {
        name: "Title patterns",
        children: rootChildren
    };
}

var lines = [];
lines.push("package org.leibnizcenter.rechtspraak.markup.features;");
lines.push("import org.crf.crf.filters.CrfFilteredFeature;");
lines.push("import org.leibnizcenter.rechtspraak.markup.Label;");
lines.push("import com.google.common.collect.Sets;");
lines.push("import org.leibnizcenter.rechtspraak.markup.RechtspraakElement;");
lines.push("import org.leibnizcenter.rechtspraak.markup.features.TokenMatchFeature;");
lines.push("import org.leibnizcenter.rechtspraak.markup.features.TokenMatchFilter;");
lines.push("import java.util.HashSet;");
lines.push("import java.util.Set;");

lines.push("/**");
lines.push(" * This code was generated automatically");
lines.push(" */");
lines.push("public class TitlesEncountered {");
lines.push("public static final Set<CrfFilteredFeature<RechtspraakElement, Label>> features = new HashSet<>(2000);");
lines.push("public static final Set<String> titles = Sets.newHashSet(");
const data = download();
data.children.forEach(function (child) {
    lines.push("//");
    lines.push("// " + child.name);
    lines.push("//");
    lines.push(_.map(_.sortBy(child.children, function (title) {
        return -title.size
    }), function (title) {
        if (title.name != "_EMPTY") {
            //var varName = title.name.split(" ").join("_").toUpperCase();
            return ((title.size < 20 ? "//" : "") + "\"" + title.name + "\" /*" + title.size + "*/");
        } else {
            return "//";
        }
    }).join(",\n"));
});
lines.push("\n");
lines.push(");");
lines.push("}");
fs.writeFile("titles.java", lines.join("\n"));