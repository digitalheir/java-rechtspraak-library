package deprecated.org.crf.postagging.postaggers.crf;

import deprecated.org.crf.crf.run.CrfTrainer;
import deprecated.org.crf.crf.run.CrfTrainerFactory;
import deprecated.org.crf.postagging.postaggers.crf.features.StandardFeatureGenerator;
import deprecated.org.crf.postagging.postaggers.crf.features.StandardFilterFactory;
import deprecated.org.crf.utilities.TaggedToken;

import java.util.List;
import java.util.Set;


/**
 * Creates a {@link CrfPosTaggerTrainer} for a given corpus.
 * 
 * @author Asher Stern
 * Date: Nov 23, 2014
 *
 */
public class CrfPosTaggerTrainerFactory
{
	/**
	 * Creates a {@link CrfPosTaggerTrainer} for a given corpus.
	 * 
	 * @param corpus the corpus is an list of sentences, where each sentence is a list of tokens, each tagged with a POS-tag.
	 * @return
	 */
	public CrfPosTaggerTrainer createTrainer(List<List<? extends TaggedToken<String, String>>> corpus)
	{
		CrfTrainerFactory<String, String> factory = new CrfTrainerFactory<String, String>();
		CrfTrainer<String, String> crfTrainer = factory.createTrainer(corpus,
				(Iterable<? extends List<? extends TaggedToken<String, String>>> theCorpus, Set<String> tags) -> new StandardFeatureGenerator(theCorpus, tags),
				new StandardFilterFactory());
		CrfPosTaggerTrainer trainer = new CrfPosTaggerTrainer(crfTrainer);

		return trainer;
	}

}
