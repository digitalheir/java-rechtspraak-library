package org.crf.function;

/**
 * A multivariate function. A function f(x), where x is a vector, and the function returns a scalar.
 *
 * @author Asher Stern
 * Date: Nov 6, 2014
 *
 */
public interface Function
{
	/**
	 * Returns the f(x) -- the value of the function in the given x.
	 * @param point the "point" is x -- the input for the function.
	 * @return the value of f(x)
	 */
    double value(double... point);

	/**
	 * The size (dimension) of the input vector (x).
	 * @return the size (dimension) of the input vector (x).
	 */
    int size();
}
