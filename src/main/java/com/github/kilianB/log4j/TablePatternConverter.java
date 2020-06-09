package com.github.kilianB.log4j;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

/**
 * Registers the %td keyword to be replaced by <tr> tags in a pattern useful to
 * create table rows
 * 
 * @author Kilian
 *
 */
@Plugin(name = "TestPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "td"})
public class TablePatternConverter extends LogEventPatternConverter {

	protected TablePatternConverter(String[] options) {
		super("TablePatternConverter", "td");
	}

	public static TablePatternConverter newInstance(final String[] options) {
		return new TablePatternConverter(options);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void format(final LogEvent event, final StringBuilder toAppendTo) {
		toAppendTo.append("</td><td>");
	}

}
