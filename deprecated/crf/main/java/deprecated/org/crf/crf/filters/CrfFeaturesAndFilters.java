package deprecated.org.crf.crf.filters;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Encapsulates all the mostlikelytreefromlist, the {@link FilterFactory}, and data-structures used for filtering mostlikelytreefromlist.
 * 
 * @author Asher Stern
 * Date: Nov 11, 2014
 *
 * @param <K>
 * @param <G>
 */
public class CrfFeaturesAndFilters<K, G> implements Serializable
{
	private static final long serialVersionUID = 5815029007668803039L;
	private final FilterFactory<K, G> filterFactory;
	private final CrfFilteredFeature<K, G>[] filteredFeatures;
	private final Map<Filter<K, G>, Set<Integer>> mapActiveFeatures;
	private final Set<Integer> indexesOfFeaturesWithNoFilter;
	
	public CrfFeaturesAndFilters(FilterFactory<K, G> filterFactory,
			CrfFilteredFeature<K, G>[] filteredFeatures,
			Map<Filter<K, G>, Set<Integer>> mapActiveFeatures,
			Set<Integer> indexesOfFeaturesWithNoFilter)
	{
		super();
		this.filterFactory = filterFactory;
		this.filteredFeatures = filteredFeatures;
		this.mapActiveFeatures = mapActiveFeatures;
		this.indexesOfFeaturesWithNoFilter = indexesOfFeaturesWithNoFilter;
	}

	/**
	 * Returns the {@link FilterFactory} to be used with the mostlikelytreefromlist encapsulated here, for efficient feature-value calculations.
	 */
	public FilterFactory<K, G> getFilterFactory()
	{
		return filterFactory;
	}

	/**
	 * An array of all the mostlikelytreefromlist in the CRF model.
	 */
	public CrfFilteredFeature<K, G>[] getFilteredFeatures()
	{
		return filteredFeatures;
	}

	/**
	 * Returns a map from each filter to a set of indexes of mostlikelytreefromlist, that might be active if their filters are equal to
	 * this filter. "Active" means that they might return non-zero.
	 */
	public Map<Filter<K, G>, Set<Integer>> getMapActiveFeatures()
	{
		return mapActiveFeatures;
	}

	/**
	 * Returns a set of indexes of mostlikelytreefromlist that might be active for any type of input. These mostlikelytreefromlist have no filters.
	 */
	public Set<Integer> getIndexesOfFeaturesWithNoFilter()
	{
		return indexesOfFeaturesWithNoFilter;
	}
}
