/*
 *  Dictan Open Dictionary Java Library presents the core interface and functionality for dictionaries. 
 *	
 *  Copyright (C) 2010 - 2014  Dmitry Viktorov <dmitry.viktorov@softex.info> <http://www.softex.info>
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

import java.util.Observer;

/**
 * 
 * @since version 2.6, 08/21/2011
 * 
 * @since modified 4.4, 03/17/2014
 * @since modified 4.5, 03/30/2014
 *  
 * @author Dmitry Viktorov
 * 
 */
public interface BaseWriter {
	
	public static final String UTF8 = "UTF-8";
	
	public void createBase(String... params) throws Exception;
	
	public BasePropertiesInfo saveBasePropertiesInfo(BasePropertiesInfo basePropertiesInfo) throws Exception;
	public BasePropertiesInfo getBasePropertiesInfo();

	public LanguageDirectionsInfo saveLanguageDirectionsInfo(LanguageDirectionsInfo languageDirectionsInfo) throws Exception;
	public LanguageDirectionsInfo getLanguageDirectionsInfo();
	
	public void saveRawArticleInfo(ArticleInfo articleInfo) throws Exception;
	public void saveAdaptedArticleInfo(ArticleInfo articleInfo) throws Exception;
	
	public void saveAbbreviationInfo(AbbreviationInfo abbreviationInfo) throws Exception;
	public void saveBaseResourceInfo(BaseResourceInfo baseResourceInfo) throws Exception;
	public void saveMediaResourceInfo(MediaResourceInfo mediaResourceInfo) throws Exception;
	
	public void flush() throws Exception;
	public void close() throws Exception;
	
	public FormatInfo getFormatInfo();
	
	public void addObserver(Observer observer);

}
