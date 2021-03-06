package deprecated.org.crf.postagging.demo;

import deprecated.org.crf.postagging.postaggers.crf.CrfPosTagger;
import deprecated.org.crf.postagging.postaggers.crf.CrfPosTaggerLoader;
import deprecated.org.crf.utilities.CrfException;
import deprecated.org.crf.utilities.ExceptionUtil;
import deprecated.org.crf.utilities.TaggedToken;
import deprecated.org.crf.utilities.log4j.Log4jInit;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Run the CrfPosTagger on a given text file.
 * This program loads the CRF pos tagging model from the given directory, processes the given file, and write the tagged
 * sentences into the output file.
 * The input file should contain a single <B>tokenized</B> sentence in each line.
 * 
 * @author Asher Stern
 * Date: Nov 20, 2014
 *
 */
public class UsePosTagger
{

	private static final Logger logger = Logger.getLogger(UsePosTagger.class);
	private final File crfPosTaggerModelDirectory;
	private final File sentencesToTag;
	private final File outputFile;


	public UsePosTagger(File crfPosTaggerModelDirectory, File sentencesToTag, File outputFile) {
		super();
		this.crfPosTaggerModelDirectory = crfPosTaggerModelDirectory;
		this.sentencesToTag = sentencesToTag;
		this.outputFile = outputFile;
	}

	/**
	 * Entry point
	 * @param args 1. directory that contains the learned model file. 2. input file name. 3. output file name.
	 */
	public static void main(String[] args)
	{
		try
		{
			Log4jInit.init(Level.DEBUG);
			try
			{
				int index=0;
				UsePosTagger application = new UsePosTagger(new File(args[index++]),new File(args[index++]),new File(args[index++]));
				application.go();
			}
			catch(Throwable t)
			{
				ExceptionUtil.logException(t, logger);
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace(System.out);
		}
	}

	public void go()
	{
		logger.info("Loading pos tagger ...");
		CrfPosTaggerLoader loader = new CrfPosTaggerLoader();
		CrfPosTagger posTagger = loader.load(crfPosTaggerModelDirectory);
		logger.info("Loading pos tagger - done.");
		logger.info("Processing ...");
		try(BufferedReader reader = new BufferedReader(new FileReader(sentencesToTag)))
		{
			try(PrintWriter writer = new PrintWriter(outputFile))
			{
				String line = reader.readLine();
				while (line!=null)
				{
					line = line.trim();
					if (line.length()<=0)
					{
						break;
					}

					String[] tokens = line.split("\\s+");
					ArrayList<String> listTokens = new ArrayList<String>(tokens.length);
					for (String token : tokens) {listTokens.add(token);}
					List<TaggedToken<String,String>> taggedTokens = posTagger.tagSentence(listTokens);
					writer.println(representTaggedTokens(taggedTokens));

					line = reader.readLine();
				}
			}
		}
		catch (IOException e)
		{
			throw new CrfException("IO failuer.",e);
		}
		logger.info("Processing - done.");
	}

	private String representTaggedTokens(List<TaggedToken<String,String>> taggedTokens)
	{
		StringBuilder sb = new StringBuilder();
		boolean firstIteration = true;
		for (TaggedToken<String,String> taggedToken : taggedTokens)
		{
			if (firstIteration) {firstIteration=false;}
			else {sb.append(" ");}
			sb.append(taggedToken.getToken()).append("/").append(taggedToken.getTag());
		}
		return sb.toString();
	}
}
