package org.leibnizcenter.rechtspraak.tagging.experiments;

import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;

import java.io.File;

/**
 * Created by maarten on 13-5-16.
 */
public enum ExperimentConditions {
//    AutomaticallyAnnotated(
//            Xml.OUT_FOLDER_AUTOMATIC_TAGGING,
//            true,
//            Const.RECHTSPRAAK_MARKUP_TAGGER_CRF_TRAINED_ON_AUTO_ANNOTATED
//    ),
//    AutomaticallyAnnotatedNoNewlines(
//            Xml.OUT_FOLDER_AUTOMATIC_TAGGING,
//            false,
//            Const.RECHTSPRAAK_MARKUP_TAGGER_CRF_TRAINED_ON_AUTO_ANNOTATED_NO_NEWLINES
//    ),
    ManuallyAnnotatedNoNewlines(
            Xml.OUT_FOLDER_MANUAL_TAGGING,
            false,
            Const.RECHTSPRAAK_MARKUP_TAGGER_CRF_TRAINED_ON_MANUALLY_ANNOTATED_NO_NEWLINES
    ),
    ManuallyAnnotated(
            Xml.OUT_FOLDER_MANUAL_TAGGING,
            true,
            Const.RECHTSPRAAK_MARKUP_TAGGER_CRF_TRAINED_ON_MANUALLY_ANNOTATED
    );

    public final File xmlFolder;
    public final boolean useNewlines;
    public final File writeCrfToFile;

    ExperimentConditions(String filePath, boolean useNewlines, String writeCrfToFile) {
        this.useNewlines = useNewlines;
        xmlFolder = new File(filePath);
        this.writeCrfToFile = new File(writeCrfToFile);
    }
}
