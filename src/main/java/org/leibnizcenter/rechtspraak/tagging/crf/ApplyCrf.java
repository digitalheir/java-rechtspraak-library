package org.leibnizcenter.rechtspraak.tagging.crf;

import cc.mallet.fst.CRF;
import cc.mallet.types.Instance;
import cc.mallet.types.Sequence;
import cc.mallet.types.TokenSequence;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Maarten on 11-03-2013.
 */
public class ApplyCrf {
    public static void main(String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException {
        System.out.println(Xml.OUT_FOLDER_AUTOMATIC_TAGGING);

        CRF crf = loadCrf(new File(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF_TRAINED_ON_AUTO_ANNOTATED));
        //Pipe pipe = crf.getInputPipe();
        List<File> files = Xml.listXmlFiles(new File(Xml.OUT_FOLDER_AUTOMATIC_TAGGING)).subList(0, 3);

        Iterator<Instance> instIterator = new TrainCrf.RsInstanceIterator(files,true,true);
        //Iterator<Instance> pipedIterator = pipe.newIteratorFrom(instIterator);
        while (instIterator.hasNext()) {
            Instance next = instIterator.next();
            TokenSequence data = (TokenSequence) next.getData();
            Instance transduced = crf.transduce(next);
            Sequence s = (Sequence) transduced.getTarget();
            for (int i = 0; i < s.size(); i++) {
                Object label = s.get(i);
                //LabeledToken taggedToken = doc.get(i);
                System.out.println(label + ": " + data.get(i));
//                    if (!taggedToken.getTag().toString().equals(label)) {
//                        System.out.println("---------------------------------");
//                        System.out.println(label + ": " + ((Token) data.get(i)));
//                        System.out.println(taggedToken.getTag() + ": " + ((Token) data.get(i)));
//                        System.out.println("---------------------------------");
//                    }
            }
            System.out.println("------------------------");
            System.out.println("------------------------");
        }

        //TODO write stuff to xml
    }

    public static CRF loadCrf(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        return loadCrf(fis);
    }

    public static CRF loadCrf(InputStream fis) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(fis);

        return (CRF) ois.readObject();
    }

}
