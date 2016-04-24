package deprecated.org.crf.postagging.postaggers.crf;

import deprecated.org.crf.crf.run.CrfInferencePerformer;
import deprecated.org.crf.postagging.postaggers.PosTagger;
import deprecated.org.crf.utilities.TaggedToken;

import java.util.List;

/**
 * A part-of-speech tagger which assigns the tags using CRF inference. CRF is an acronym of Conditional Random Fields.
 * 
 * @author Asher Stern
 * Date: Nov 10, 2014
 *
 */
public class CrfPosTagger implements PosTagger
{
	private final CrfInferencePerformer<String, String> inferencePerformer;

	public CrfPosTagger(CrfInferencePerformer<String, String> inferencePerformer)
	{
		this.inferencePerformer = inferencePerformer;
	}

	@Override
	public List<TaggedToken<String,String>> tagSentence(List<String> sentence)
	{
		return inferencePerformer.tagSequence(sentence);
	}

}
