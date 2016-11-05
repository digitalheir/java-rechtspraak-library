# Rechtspraak.nl library
[![GitHub version](https://badge.fury.io/gh/digitalheir%2Fjava-rechtspraak-library.svg)](http://badge.fury.io/gh/digitalheir%2Fjava-rechtspraak-library)
[![Build Status](https://travis-ci.org/digitalheir/java-rechtspraak-library.svg?branch=master)](https://travis-ci.org/digitalheir/java-rechtspraak-library)

A Java interface to the judgment database of [rechtspraak.nl](http://www.rechtspraak.nl/).

## Documentation
This project is part of my master's thesis in Artificial Intelligence. A HTML copy of my thesis is available 
at [here](https://digitalheir.github.io/java-rechtspraak-library/).

## Usage
Download [the latest JAR](https://github.com/digitalheir/java-rechtspraak-library/releases/latest) or grab from Maven:

```xml
<dependencies>
        <dependency>
            <groupId>org.leibnizcenter</groupId>
            <artifactId>rechtspraak</artifactId>
            <version>2.0.4</version>
        </dependency>
</dependencies>
```

or Gradle:
```groovy
compile 'org.leibnizcenter:rechtspraak:2.0.4'
```

You can access the Rechtspraak.nl search API through `org.leibnizcenter.rechtspraak.SearchRequest.Builder` and `org.leibnizcenter.rechtspraak.SearchResult`. All builder options are optional. For instance:

```java
        SearchResult iterator = new SearchRequest.Builder()
                .max(1000) // Results per page. Cannot be larger than and defaults to 1000
                .returnType(ReturnType.DOC) // Return documents for which Rechtspraak.nl lists at least metadata (META) or metadata and a transcription of the case (DOC)
                .from(0) // Offset in results, used for pagination
                .sort(Sort.ASC) //Sort results on modification date ascending or descending. Default is ascending (oldest first).
                .build().execute();

        Assert.assertTrue(iterator.hasNext());
        iterator = iterator.next();
        Assert.assertTrue(iterator.hasNext());
        iterator = iterator.next();
```

For all options, see the [JavaDoc for SearchRequest.Builder](http://phil.uu.nl/~trompper/rechtspraak-2.0.0-javadoc/org/leibnizcenter/rechtspraak/SearchRequest.Builder.html)

Search results contain judgment metadata (accessed through `SearchResult#getJudgments()`). For fetching and parsing the actual documents, use `org.leibnizcenter.rechtspraak.RechtspraakNlInterface`. For example, given an ECLI:

```java
            OpenRechtspraak doc = parseXml(
                        // For purists that don't trust the parsing scheme (there is no official doctype), you can work with the raw XML stream as well
                        requestXmlForEcli("ECLI:NL:RBNNE:2014:1005").body().byteStream() 
            );
```

`OpenRechtspraak` is a Java object which represents the unmarshalled XML document at http://data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:RBNNE:2014:1005 

## Requirements
* Versions 1.X.X require Java 7
* Versions 2.X.X require Java 8

## More
Inquiries go to maarten.trompper@gmail.com.
