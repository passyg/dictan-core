/*
 *  Dictan Open Dictionary Java Library presents the core interface and functionality for dictionaries. 
 *	
 *  Copyright (C) 2010 - 2015  Dmitry Viktorov <dmitry.viktorov@softex.info> <http://www.softex.info>
 *	
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License (LGPL) as 
 *  published by the Free Software Foundation, either version 3 of the License, 
 *  or any later version.
 *	
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *	
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package info.softex.dictionary.core.formats.dsl.utils;

import info.softex.dictionary.core.utils.StringUtils;

/**
 * 
 * @since version 4.6,		02/06/2015
 * 
 * @modified version 4.8,	04/28/2015
 * 
 * @author Dmitry Viktorov
 * 
 */
public class DSLWriteFormatUtils {
	
	public static String convertAdaptedHtmlToDSLDesignTags(String s) {
		
		// Remove all artificial line breaks
		s = s.replaceAll("<br>", "");
		s = s.replaceAll("<br/>", "");
		
		// Italic
		s = s.replaceAll("<i>", "[i]");
		s = s.replaceAll("</i>", "[/i]");
		
		// Bold
		s = s.replaceAll("<b>", "[b]");
		s = s.replaceAll("</b>", "[/b]");

		// Underlined
		s = s.replaceAll("<u>", "[u]");
		s = s.replaceAll("</u>", "[/u]");
		
		// Superscript
		s = s.replaceAll("<sup>", "[sup]");
		s = s.replaceAll("</sup>", "[/sup]");

		// Subcript
		s = s.replaceAll("<sub>", "[sub]");
		s = s.replaceAll("</sub>", "[/sub]");
		
		// Abbreviations 
		s = s.replaceAll("<w>", "[p]");
		s = s.replaceAll("</w>", "[/p]");
		
		// Stressed vowels in a word. They are usually highlighted.
		s = s.replaceAll("<v>", "[']");
		s = s.replaceAll("</v>", "[/']");
		
		// Colored text
		s = s.replaceAll("<span class=\"ca\" style=\"color:(.+?)\">", "[c $1]");
		s = s.replaceAll("<span class=\"cd\">", "[c]");
		s = s.replaceAll("</span>", "[/c]");
		
		// Comments zone
		s = s.replaceAll("<c(.*?)>", "[com$1]");
		s = s.replaceAll("</c>", "[/com]");
		
		// The language of the word or phrase.
		s = s.replaceAll("<l(.*?)>", "[lang$1]");
		s = s.replaceAll("</l>", "[/lang]");
		
		// Specific conversion for Dictan. It helps wrap and style braces (\[ and \]) around transcriptions
		s = s.replaceAll("<o>(.*?)</o>", "{{t}}$1{{/t}}");
		
		// Examples
		s = s.replaceAll("<e(.*?)>", "[ex$1]");
		s = s.replaceAll("</e>", "[/ex]");
		
		// Transcription zone
		s = s.replaceAll("<t (.*?)>", "[t $1]"); // Space is needed to differ it from [trn]
		s = s.replaceAll("<t>", "[t]");
		s = s.replaceAll("</t>", "[/t]");
		
		// Full translation zone
		s = s.replaceAll("<f>", "[*]");
		s = s.replaceAll("</f>", "[/*]");
		
		// No text index zone.
		s = s.replaceAll("<m>", "[!trs\\]");
		s = s.replaceAll("</m>", "\\[/!trs\\]");
		
		// Translation zone
		s = s.replaceAll("<n>", "[trn]");
		s = s.replaceAll("</n>", "[/trn]");
		
		return s;
		
	}
	
	/**
	 * The method does the opposite to <code>DSLReadFormatUtils.convertDSLToAdaptedHtml</code>
	 */
	public static String convertAdaptedHtmlToDSL(String s) {
		
		// Don't process blank string
		if (StringUtils.isBlank(s)) {
			return s;
		}
		
		s = convertAdaptedHtmlToDSLDesignTags(s);
		
		// Invisible comments
		s = s.replaceAll("<!--(.*?)-->", "{{$1}}");
		
		// Left paragraph margins
		s = s.replaceAll("<div class=\"m(.*?)\">", "[m$1]");
		s = s.replaceAll("</div>", "[/m]");
		
		// References
		s = s.replaceAll("<a href=\"(.*?)\"(.*?)>(.*?)</a>", "\\[ref$2\\]$1\\[/ref\\]");
		s = s.replaceAll("<a class=\"v2\" href=\"(.*?)\"(.*?)>(.*?)</a>", "<<$1>>");
		s = s.replaceAll("<a class=\"v3\" href=\"(.*?)\"(.*?)>(.*?)</a>", "\\[url$2\\]$1\\[/url\\]"); // external links
		
		// Media resources. Can't reuse s because it's strike in html.
		s = s.replaceAll("<r>", "[s]");
		s = s.replaceAll("</r>", "[/s]");
		
		// Special characters, first order in back conversion
		s = s.replaceAll("&#160;", "\\\\ ");     // "\ ", non-breaking space, aka &nbsp;
		s = s.replaceAll("&#47;",  "\\\\\\\\");  // "\\"
		s = s.replaceAll("&#91;",  "\\\\[");     // "\["
		s = s.replaceAll("&#93;",  "\\\\]");     // "\]"
		s = s.replaceAll("&#123;", "\\\\{");     // "\{"
		s = s.replaceAll("&#125;", "\\\\}");     // "\}"
		s = s.replaceAll("&#40;",  "\\\\(");     // "\("
		s = s.replaceAll("&#41;",  "\\\\)");     // "\)"
		s = s.replaceAll("&#64;",  "\\\\@");     // "\@"
		s = s.replaceAll("&#126;", "\\\\~");     // "\~"
		
		// Special characters, unescaped, last order in back conversion
		s = s.replaceAll("&#60;",        "<"); // "<"
		s = s.replaceAll("&#62;",        ">"); // ">"
		s = s.replaceAll("&#38;",       "&"); // "&", always last
		
		return s;
	}

}
