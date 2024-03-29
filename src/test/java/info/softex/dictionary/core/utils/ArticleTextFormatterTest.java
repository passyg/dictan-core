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

package info.softex.dictionary.core.utils;

import static org.junit.Assert.assertEquals;
import info.softex.dictionary.core.testutils.MavenUtils;
import info.softex.dictionary.core.utils.ArticleTextFormatter.XmlProcessResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

/**
 * 
 * @since version 4.8,		05/02/2015
 * 
 * @author Dmitry Viktorov
 * 
 */
public class ArticleTextFormatterTest {
	
	protected final static String UTF8 = "UTF-8";
	protected final static String FULL_ARTICLE_HTML = "/info/softex/dictionary/core/utils/article_full_html.txt";
	protected final static String FULL_ARTICLE_NO_HTML = "/info/softex/dictionary/core/utils/article_full_no_html.txt";
	protected final static String FULL_ARTICLE_NO_HTML_CUT = "/info/softex/dictionary/core/utils/article_full_no_html_cut.txt";
	
	protected final static int TEST_LENGTH = 7;
	
	@SuppressWarnings("serial")
	protected final static Map<String, String> ARTICLES = new LinkedHashMap<String, String>() {{
		put(null, null);
		put("", "");
		put(" ", " ");
		put("sim", "sim");
		put("simple text", "simple text");
		put("simple text <markup> test</markup>", "simple text  test");
		put("<div>simple \r\ntext <markup> test</markup><div>", "simple \r\ntext  test"); // 6
		put("simple \r\ntext <script> test</script>", "simple \r\ntext ");
		put("simple \r\ntext <style> test</style>", "simple \r\ntext ");
		put("<!-- simple \r\ntext <style> test -->", ""); // 9
		put("text <style> error1 \r\n<style> error2 </style> text", "text  text");
		put("<script> error1 </div>\r\n<style> error2 </style> text 4", "");
		put("<script> error1 </div>\r\n<style> error2 </script> text 4", " text 4");
		put("special &#40; character", "special ( character");
		put("special \r\n&#109 character", "special \r\nm character");
		put("sp &amp ecial character &copy;", "sp & ecial character \u00A9");
		put("<div class=\"header\">butter</div><div class=\"m1\"><o>&#91;</o> <t>'bʌtə</t> <o>&#93;</o>", "butter[ 'bʌtə ]");
	}};
	
	protected final static int[] PROCESSED_LENGTHS = new int[] {
		0, 0, 1, 3, 7, 7, 12, 7, 7, 36,
		47, 55, 55, 7, 7, 10, 56
	};

	@Test
	public void testRemoveXmlHtml() {
		for (Map.Entry<String, String> entry : ARTICLES.entrySet()) {
			XmlProcessResult result = ArticleTextFormatter.removeXmlHtml(entry.getKey());
			if (result != null) {
				assertEquals(entry.getValue(), result.getOutput());
			}
		}
	}
	
	@Test
	public void testRemoveXmlHtmlMaxLength() {
		int count = 0;
		for (Map.Entry<String, String> entry : ARTICLES.entrySet()) {
			String entryValue = entry.getValue();
			if (entryValue != null) {
				if (entryValue.length() >= TEST_LENGTH) {
					entryValue = entryValue.substring(0, TEST_LENGTH);
				}
				XmlProcessResult result = ArticleTextFormatter.removeXmlHtml(entry.getKey(), TEST_LENGTH);
				System.out.println(result.getOutput());
				assertEquals(entryValue.length(), result.getOutput().length());
				assertEquals("Processed lengths at #" + count + " '" + entryValue + "' don't match", PROCESSED_LENGTHS[count], result.getProcessedLength());
				assertEquals("Lines at #" + count + " don't match", entryValue, result.getOutput());
			}
			count++;
		}
	}
	
	@Test
	public void testRemoveXmlHtmlFromFullArticle() throws UnsupportedEncodingException, IOException {		
		String articleOrig = new String(FileUtils.toByteArray(MavenUtils.getCodeSourceRelevantFile(FULL_ARTICLE_HTML)), UTF8);
		String articleNoXml = new String(FileUtils.toByteArray(MavenUtils.getCodeSourceRelevantFile(FULL_ARTICLE_NO_HTML)), UTF8);
		assertEquals(articleNoXml, ArticleTextFormatter.removeXmlHtml(articleOrig).getOutput());
	}
	
	@Test
	public void testRemoveXmlHtmlMaxLengthFromFullArticle() throws UnsupportedEncodingException, IOException {		
		String articleOrig = new String(FileUtils.toByteArray(MavenUtils.getCodeSourceRelevantFile(FULL_ARTICLE_HTML)), UTF8);
		String articleNoXmlCut = new String(FileUtils.toByteArray(MavenUtils.getCodeSourceRelevantFile(FULL_ARTICLE_NO_HTML_CUT)), UTF8);
		assertEquals(articleNoXmlCut, ArticleTextFormatter.removeXmlHtml(articleOrig, 400).getOutput());
	}
	
}
