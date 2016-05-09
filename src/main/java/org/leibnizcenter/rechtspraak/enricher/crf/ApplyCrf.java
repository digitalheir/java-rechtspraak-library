package org.leibnizcenter.rechtspraak.enricher.crf;

import cc.mallet.fst.CRF;
import cc.mallet.fst.SimpleTagger;
import cc.mallet.types.Instance;
import cc.mallet.types.Sequence;
import org.leibnizcenter.rechtspraak.tokens.TokenList;
import org.leibnizcenter.util.Const;
import org.leibnizcenter.util.Xml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by Maarten on 11-03-2013.
 */
public class ApplyCrf {

    public static void main(String[] args) throws IOException, ClassNotFoundException, ParserConfigurationException {
        Xml.setFolders(args);

        System.out.println(Xml.IN_FOLDER);

        CRF crf = loadCrf(new File(Const.RECHTSPRAAK_MARKUP_TAGGER_CRF));
        List<File> files = Xml.listXmlFiles(new File(Xml.IN_FOLDER)).subList(0, 3);
        for (TokenList doc : new TokenList.FileIterable(
                new TokenList.FileIterator(files.toArray(new File[files.size()]))
        )) {
            Instance instance = TrainCrf.getInstance(doc, true);
            Sequence data = (Sequence) instance.getData();
            Sequence[] labels = SimpleTagger.apply(crf, (Sequence) TrainCrf.pipe.pipe(instance).getData(), 3);
            for (Sequence s : labels) {
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
            }
            System.out.println("------------------------");
        }

        //TODO write stuff to xml
    }

    public static CRF loadCrf(File file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (CRF) ois.readObject();
    }

}
