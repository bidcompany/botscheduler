package com.github.kilianB.log4j;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.util.Transform;
import org.apache.logging.log4j.util.Strings;

/**
 * Outputs log messages in an HTML table while granting the entire flexibility
 * of using pattern layouts.
 * 
 * @author Kilian
 *
 */
@Plugin(name = "PatternHtmlLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class PatternHtmlLayout extends AbstractStringLayout {

	/**
	 * Default font family: {@value}.
	 */
	public static final String DEFAULT_FONT_FAMILY = "arial,sans-serif";
	private static final String REGEXP = Strings.LINE_SEPARATOR.equals("\n") ? "\n" : Strings.LINE_SEPARATOR + "|\n";
	private static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
	private static final String DEFAULT_TITLE = "Log4j Log Messages";
	private static final String DEFAULT_CONTENT_TYPE = "text/html";

	public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";

	private final String title;
	private final String contentType;
	private final String font;
	private final String headerSize;
	private final String[] header; // table headers

	private final boolean omitStyle; // Ignore all styling options and return plain html;
	private final String tableCSSClass; // Append class name to table tag
	private final String theadCSSClass;
	
	/**
	 * Shall just the table be printed to file without <html markup?>
	 */
	private final boolean justTable; 
	
	private PatternParser patternParser;
	private final PatternFormatter[] formatters;
	private final boolean[] lineBreakFormater;

	/** Possible font sizes */
	public static enum FontSize {
		SMALLER("smaller"), XXSMALL("xx-small"), XSMALL("x-small"), SMALL("small"), MEDIUM("medium"), LARGE(
				"large"), XLARGE("x-large"), XXLARGE("xx-large"), LARGER("larger");

		private final String size;

		private FontSize(final String size) {
			this.size = size;
		}

		public String getFontSize() {
			return size;
		}

		public static FontSize getFontSize(final String size) {
			for (final FontSize fontSize : values()) {
				if (fontSize.size.equals(size)) {
					return fontSize;
				}
			}
			return SMALL;
		}

		public FontSize larger() {
			return this.ordinal() < XXLARGE.ordinal() ? FontSize.values()[this.ordinal() + 1] : this;
		}
	}

	private PatternHtmlLayout(final String title, final String contentType, final Charset charset, final String font,
			final String fontSize, final String headerSize, final String header,
			// Pattern layout
			final Configuration config, final String eventPattern, final boolean alwaysWriteExceptions,
			final boolean disableAnsi, final boolean noConsoleNoAnsi, boolean omitStyle, String tableCSSClass, boolean justTable, String theadCSSClass) {
		super(charset);
		this.title = title;
		this.contentType = addCharsetToContentType(contentType);
		this.font = font;
		this.headerSize = headerSize;
		this.header = header.split(",");
		this.omitStyle = omitStyle;
		this.tableCSSClass = tableCSSClass == null ? "" : tableCSSClass;
		this.justTable = justTable;
		this.theadCSSClass = theadCSSClass == null ? "" : theadCSSClass;

		patternParser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
		if (patternParser == null) {
			patternParser = new PatternParser(config, "Converter", LogEventPatternConverter.class);
			config.addComponent("Converter", patternParser);
			patternParser = config.getComponent("Converter");
		}

		List<PatternFormatter> patternFormatter = patternParser.parse(eventPattern, alwaysWriteExceptions, disableAnsi,
				noConsoleNoAnsi);
		formatters = patternFormatter.toArray(new PatternFormatter[0]);
		lineBreakFormater = new boolean[formatters.length];

		for (int i = 0; i < formatters.length; i++) {
			if (formatters[i].getConverter().getName().equals("TablePatternConverter")) {
				lineBreakFormater[i] = true;
			}
		}

	}

	private String addCharsetToContentType(final String contentType) {
		if (contentType == null) {
			return DEFAULT_CONTENT_TYPE + "; charset=" + getCharset();
		}
		return contentType.contains("charset") ? contentType : contentType + "; charset=" + getCharset();
	}

	/**
	 * Formats as a String.
	 *
	 * @param event
	 *            The Logging Event.
	 * @return A String containing the LogEvent as HTML.
	 */
	@Override
	public String toSerializable(final LogEvent event) {

		// Omit empty messages. No reason to log.
		if (event.getMessage().getFormattedMessage().isEmpty())
			return "";

		final StringBuilder sbuf = getStringBuilder();

		StringBuilder newSb = new StringBuilder();

		sbuf.append(Strings.LINE_SEPARATOR).append("<tr>");
		sbuf.append(Strings.LINE_SEPARATOR).append("<td>");

		// Escape all non tr tags
		for (int i = 0; i < formatters.length; i++) {
			formatters[i].format(event, newSb);

			if (!lineBreakFormater[i]) {
				sbuf.append(Transform.escapeHtmlTags(newSb.toString()));
			} else {
				sbuf.append(newSb.toString());
			}
			newSb.setLength(0);
		}

		sbuf.append(newSb.toString().replaceAll(REGEXP, "</br>"));
		sbuf.append("</td>").append(Strings.LINE_SEPARATOR);

		final Throwable throwable = event.getThrown();
		if (throwable != null) {
			sbuf.append("<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : ").append(12);
			sbuf.append(";\" colspan=\"6\">");
			appendThrowableAsHtml(throwable, sbuf);
			sbuf.append("</td></tr>").append(Strings.LINE_SEPARATOR);
		}

		sbuf.append("</tr>");

		return sbuf.toString();
	}

	@Override
	/**
	 * @return The content type.
	 */
	public String getContentType() {
		return contentType;
	}

	private void appendThrowableAsHtml(final Throwable throwable, final StringBuilder sbuf) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		try {
			throwable.printStackTrace(pw);
		} catch (final RuntimeException ex) {
			// Ignore the exception.
		}
		pw.flush();
		final LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
		final ArrayList<String> lines = new ArrayList<>();
		try {
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		} catch (final IOException ex) {
			if (ex instanceof InterruptedIOException) {
				Thread.currentThread().interrupt();
			}
			lines.add(ex.toString());
		}
		boolean first = true;
		for (final String line : lines) {
			if (!first) {
				sbuf.append(TRACE_PREFIX);
			} else {
				first = false;
			}
			sbuf.append(Transform.escapeHtmlTags(line));
			sbuf.append(Strings.LINE_SEPARATOR);
		}
	}

	private StringBuilder appendLs(final StringBuilder sbuilder, final String s) {
		sbuilder.append(s).append(Strings.LINE_SEPARATOR);
		return sbuilder;
	}

	private StringBuilder append(final StringBuilder sbuilder, final String s) {
		sbuilder.append(s);
		return sbuilder;
	}

	/**
	 * Returns appropriate HTML headers.
	 * 
	 * @return The header as a byte array.
	 */
	@Override
	public byte[] getHeader() {
		final StringBuilder sbuf = new StringBuilder();
		
		if(!justTable) {
			append(sbuf, "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ");
			appendLs(sbuf, "\"http://www.w3.org/TR/html4/loose.dtd\">");
			appendLs(sbuf, "<html>");
			appendLs(sbuf, "<head>");
			append(sbuf, "<meta charset=\"");
			append(sbuf, getCharset().toString());
			appendLs(sbuf, "\"/>");
			append(sbuf, "<title>").append(title);
			appendLs(sbuf, "</title>");
		}
		
		if (!justTable && !omitStyle) {
			appendLs(sbuf, "<style type=\"text/css\">");
			appendLs(sbuf, "<!--");
			append(sbuf, "body, table {font-family:").append(font).append("; font-size: ");
			appendLs(sbuf, headerSize).append(";}");
			appendLs(sbuf, "th {background: #336699; color: #FFFFFF; text-align: left;}");
			appendLs(sbuf, "-->");
			appendLs(sbuf, "</style>");
			appendLs(sbuf, "</head>");
			appendLs(sbuf, "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">");
			appendLs(sbuf, "<table " + (tableCSSClass.isEmpty() ? "" : "class='" + tableCSSClass + "'")
					+ " cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">");
		} else {
			if(!justTable) {
				appendLs(sbuf, "</head>");
			}
			appendLs(sbuf, "<body>");
			appendLs(sbuf, "<table " + (tableCSSClass.isEmpty() ? "" : "class='" + tableCSSClass + "'") + ">");
		}

		appendLs(sbuf, "<thead " + (theadCSSClass.isEmpty() ? "" : "class='" + theadCSSClass + "'") + ">");
		appendLs(sbuf,"<tr>");
		
		
		for (String s : header) {
			appendLs(sbuf, "<th>" + s + "</th>");
		}
		appendLs(sbuf, "</tr>");
		appendLs(sbuf, "</thead>");
		return sbuf.toString().getBytes(getCharset());
	}

	/**
	 * Returns the appropriate HTML footers.
	 * 
	 * @return the footer as a byte array.
	 */
	@Override
	public byte[] getFooter() {
		final StringBuilder sbuf = new StringBuilder();
		appendLs(sbuf, "</table>");
		appendLs(sbuf, "<br>");
		if(!justTable) {
			appendLs(sbuf, "</body></html>");
		}
		return getBytes(sbuf.toString());
	}

	/**
	 * Creates an HTML Layout.
	 * 
	 * @param locationInfo
	 *            If "true", location information will be included. The default is
	 *            false.
	 * @param title
	 *            The title to include in the file header. If none is specified the
	 *            default title will be used.
	 * @param contentType
	 *            The content type. Defaults to "text/html".
	 * @param charset
	 *            The character set to use. If not specified, the default will be
	 *            used.
	 * @param fontSize
	 *            The font size of the text.
	 * @param font
	 *            The font to use for the text.
	 * @return An HTML Layout.
	 */
	@PluginFactory
	public static PatternHtmlLayout createLayout(
			@PluginAttribute(value = "title", defaultString = DEFAULT_TITLE) final String title,
			@PluginAttribute(value = "omitStyle", defaultBoolean = false) final boolean omitStyle,
			@PluginAttribute(value = "justTable", defaultBoolean = false) final boolean justTable,
			@PluginAttribute(value = "tableCSSClass") final String tableCSSClass,
			@PluginAttribute(value = "theadCSSClass") final String theadCSSClass,
			
			@PluginAttribute("contentType") String contentType,
			@PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset,
			@PluginAttribute("fontSize") String fontSize,
			@PluginAttribute(value = "fontName", defaultString = DEFAULT_FONT_FAMILY) final String font,
			@PluginAttribute(value = "header") String header,
			// Pattern Layout
			@PluginAttribute(value = "pattern", defaultString = DEFAULT_CONVERSION_PATTERN) final String pattern,
			@PluginConfiguration final Configuration config,
			// LOG4J2-783 use platform default by default, so do not specify defaultString
			// for charset
			@PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = false) final boolean alwaysWriteExceptions,
			@PluginAttribute(value = "noConsoleNoAnsi") final boolean noConsoleNoAnsi) {
		final FontSize fs = FontSize.getFontSize(fontSize);
		fontSize = fs.getFontSize();
		final String headerSize = fs.larger().getFontSize();
		if (contentType == null) {
			contentType = DEFAULT_CONTENT_TYPE + "; charset=" + charset;
		}
		return new PatternHtmlLayout(title, contentType, charset, font, fontSize, headerSize, header, config, pattern,
				alwaysWriteExceptions, false, noConsoleNoAnsi, omitStyle, tableCSSClass,justTable,theadCSSClass);
	}

}
