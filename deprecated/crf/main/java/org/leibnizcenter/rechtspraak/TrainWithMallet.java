package org.leibnizcenter.rechtspraak;

import cc.mallet.fst.*;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.TokenSequence2FeatureVectorSequence;
import cc.mallet.types.*;
import cc.mallet.util.CommandOption;
import deprecated.org.crf.utilities.TaggedToken;
import org.leibnizcenter.rechtspraak.tagging.crf.features.Features;
import org.leibnizcenter.rechtspraak.leibnizannotations.Annotator;
import org.leibnizcenter.rechtspraak.markup.docs.Const;
import org.leibnizcenter.rechtspraak.markup.docs.Label;
import org.leibnizcenter.rechtspraak.markup.docs.LabeledTokenList;
import org.leibnizcenter.rechtspraak.markup.docs.RechtspraakElement;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.leibnizcenter.rechtspraak.markup.docs.RechtspraakCorpus.listXmlFiles;

/**
 * Created by maarten on 11-3-16.
 */
public class TrainWithMallet {


}
