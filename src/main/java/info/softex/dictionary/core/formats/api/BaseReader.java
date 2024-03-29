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

package info.softex.dictionary.core.formats.api;

import info.softex.dictionary.core.attributes.AbbreviationInfo;
import info.softex.dictionary.core.attributes.ArticleInfo;
import info.softex.dictionary.core.attributes.BasePropertiesInfo;
import info.softex.dictionary.core.attributes.BaseResourceInfo;
import info.softex.dictionary.core.attributes.FormatInfo;
import info.softex.dictionary.core.attributes.LanguageDirectionsInfo;
import info.softex.dictionary.core.attributes.MediaResourceInfo;
import info.softex.dictionary.core.attributes.MediaResourceKey;
import info.softex.dictionary.core.attributes.WordInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @since version 1.0,		09/23/2010
 * 
 * @modified version 2.0,	03/10/2011
 * @modified version 2.5,	07/13/2011
 * @modified version 2.6,	08/21/2011
 * @modified version 4.2,	03/06/2014
 * @modified version 4.6,	02/01/2015
 * @modified version 4.7,	03/26/2015
 *  
 * @author Dmitry Viktorov
 * 
 */
public interface BaseReader {
	
	public static final String UTF8 = "UTF-8";
	
	public void close() throws Exception;
	
	/**
	 * Loads the whole base. 
	 * loadDictionaryInfo() and loadLanguageDirections() should be called within this method.
	 * 
	 * @throws BaseFormatException
	 * @throws Exception
	 */
	public void load() throws BaseFormatException, Exception;
	
	/**
	 * Loads only the dictionary info.
	 * 
	 * @return
	 * @throws BaseFormatException
	 * @throws Exception
	 */
	public BasePropertiesInfo loadBasePropertiesInfo() throws BaseFormatException, Exception;
	
	/**
	 * Loads only the supported language directions.
	 * 
	 * @return
	 * @throws BaseFormatException
	 * @throws Exception
	 */
	public LanguageDirectionsInfo loadLanguageDirectionsInfo() throws BaseFormatException, Exception;
	
	public boolean isLoaded();
	
	public BasePropertiesInfo getBasePropertiesInfo();
	public FormatInfo getFormatInfo();
	public LanguageDirectionsInfo getLanguageDirectionsInfo();
	
	public BaseResourceInfo getBaseResourceInfo(String resourceKey) throws BaseFormatException;
	
	public Set<String> getMediaResourceKeys() throws BaseFormatException;
	public MediaResourceInfo getMediaResourceInfo(MediaResourceKey mediaKey) throws BaseFormatException;
	
	public Set<String> getAbbreviationKeys() throws BaseFormatException;
	public AbbreviationInfo getAbbreviationInfo(String abbreviationKey) throws BaseFormatException;

	/**
	 * Search the word index. The result must be returned even if the word is not found.
	 * In this case the index denotes the item where the word would be placed.
	 * 
	 * @param word - word to be searched
	 * @param positive - If true, only positive index is returned regardless if the word is found or not. 
	 * 					 If false, a negative one is returned if the word is not found.
	 * @return -
	 * 			The word id if the word is found, or the id where the word would be placed if is not.
	 * 
	 * @throws BaseFormatException
	 */
	public int searchWordIndex(String word, boolean positive) throws BaseFormatException;
	
	public List<String> getWords();

    /**
     * Returns words based on SQL 'like' query.
     */
	public Map<Integer, String> getWordsLike(String rootWord, int limit) throws BaseFormatException;
	
	public Map<Integer, String> getWordsMappings() throws BaseFormatException;
	public Map<Integer, String> getAdaptedWordsMappings() throws BaseFormatException;	
	public Map<Integer, Integer> getWordsRedirects() throws BaseFormatException;	
	
	/**
	 * Returns the full formatted article
	 */
	public ArticleInfo getArticleInfo(WordInfo wordInfo) throws BaseFormatException;
	
	/**
	 * Returns the article with the basic set of rules applied. The method is mainly 
	 * needed to speed up rendering, e.g. it can transfer a specific formatting to HTML.
	 * If no adaptation should be done, the method should simply delegate to 
	 * <code>getRawArticleInfo</code>. 
	 * 
	 * If the method is defined, the writer is supposed to have the rules to convert 
	 * the article back to its raw view. Otherwise the original markup can be lost.
	 */
	public ArticleInfo getAdaptedArticleInfo(WordInfo wordInfo) throws BaseFormatException;
	
	/**
	 * Returns the raw article as is, w/o any explicit forrmatting
	 */
	public ArticleInfo getRawArticleInfo(WordInfo wordInfo) throws BaseFormatException;
	
}
