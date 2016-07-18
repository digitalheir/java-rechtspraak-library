/*
 * $Id: ParseEvent.java 1807 2010-02-05 22:20:02Z scott $ 
 * Copyright (C) 2007 Scott Martin
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version. The GNU Lesser General Public License is
 * distributed with this software in the file COPYING.
 */
package edu.osu.ling.pep;


import edu.osu.ling.pep.earley.EarleyParser;

/**
 * An event generated by an {@link EarleyParser Earley parser} having to do
 * with a completed {@link Parse parse}.
 * @author <a href="http://www.ling.osu.edu/~scott/">Scott Martin</a>
 * @version $LastChangedRevision: 1807 $
 */
public class ParseEvent extends ParserEvent {
	private static final long serialVersionUID = 1L;

	Parse parse;
	Integer index;
	
	/**
	 * Creates a new parse event for the given parse. The string index used
	 * will be the size of the {@link Parse#getTokens() parse's tokens}. 
	 * @param parse The parse in question.
	 */
	public ParseEvent(EarleyParser earleyParser, Parse parse) {
		this(earleyParser, parse.tokens.size(), parse);
	}
	
	/**
	 * Creates a new event concerning the specified parse.
	 * @param parse The parse in question.
	 */
	protected ParseEvent(EarleyParser earleyParser, Integer index,
			Parse parse) {
		super(earleyParser);
		this.parse = parse;
		this.index = index;
	}

	/**
	 * Gets the parse associated with this event.
	 * @return The parse that was specified when this event was created.
	 */
	public Parse getParse() {
		return parse;
	}
	
	/**
	 * Gets the string index where this event occurred.
	 */
	public Integer getIndex() {
		return index;
	}
}
