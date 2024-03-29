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

package info.softex.dictionary.core.formats.source;

import info.softex.dictionary.core.annotations.BaseFormat;
import info.softex.dictionary.core.attributes.AbbreviationInfo;
import info.softex.dictionary.core.attributes.ArticleInfo;
import info.softex.dictionary.core.attributes.BasePropertiesInfo;
import info.softex.dictionary.core.attributes.BasePropertiesInfo.AbbreviationsFormattingMode;
import info.softex.dictionary.core.attributes.BaseResourceInfo;
import info.softex.dictionary.core.attributes.FormatInfo;
import info.softex.dictionary.core.attributes.LanguageDirectionsInfo;
import info.softex.dictionary.core.attributes.MediaResourceInfo;
import info.softex.dictionary.core.attributes.MediaResourceKey;
import info.softex.dictionary.core.attributes.WordInfo;
import info.softex.dictionary.core.formats.api.BaseFormatException;
import info.softex.dictionary.core.formats.api.BaseReader;
import info.softex.dictionary.core.io.DataAdapter;
import info.softex.dictionary.core.utils.ArticleHtmlFormatter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Source base reader which mainly follows the ZD source syntax.
 * 
 * @since version 3.4,		07/02/2012
 * 
 * @modified version 3.5,	08/06/2012
 * @modified version 4.0,	08/06/2012
 * @modified version 4.2,	03/06/2014
 * @modified version 4.6,	01/26/2015
 * @modified version 4.7,	03/23/2015
 * 
 * @author Dmitry Viktorov
 *
 */
@BaseFormat(name = "BASIC_SOURCE", primaryExtension = "", extensions = {}, sortingExpected = false)
public class SourceBaseReader implements BaseReader {
	
	public final static FormatInfo FORMAT_INFO = FormatInfo.buildFormatInfoFromAnnotation(SourceBaseReader.class);
	
	public final static int BUF_SIZE_ARTICLES = 2097152; // 2.0 MB 
	public final static int BUF_SIZE_ABBREVS = 131072;   // 128 KB
	
	private final static Logger log = LoggerFactory.getLogger(SourceBaseReader.class);

	protected BasePropertiesInfo baseInfo = null;
	protected LanguageDirectionsInfo langDirections = null;
	
	protected SourceFileReader articleReader;
	protected SourceFileReader abbrevReader;
	
	protected List<String> words;
	
	protected Set<String> abbrevKeys;
	protected Set<String> mediaResources;
	
	protected final String mediaDirectoryPath;
	protected final File mediaDirectory;
	protected final File sourceDirectory;

	protected boolean isLoaded = false;
	
	public SourceBaseReader(File inSourceDirectory) throws IOException {
		
		if (inSourceDirectory == null || inSourceDirectory.exists() && !inSourceDirectory.isDirectory()) {
			throw new IOException("The source must be a directory, not a file!");
		}
		
		this.sourceDirectory = inSourceDirectory;
		
		this.mediaDirectoryPath = inSourceDirectory + File.separator + SourceFileNames.DIRECTORY_MEDIA;
		this.mediaDirectory = new File(this.mediaDirectoryPath);
		
	}

	@Override
	public void close() throws IOException {
		if (articleReader != null) {
			articleReader.close();
		}
		if (abbrevReader != null) {
			abbrevReader.close();
		}
	}

	@Override
	public void load() throws IOException, BaseFormatException {
				
		if (baseInfo == null) {
	        log.debug("Loading BasePropertiesInfo");
	        loadBasePropertiesInfo();
	    }
		
		if (langDirections == null) {
	        log.debug("Loading LanguageDirectionsInfo");
	        loadLanguageDirectionsInfo();
	    }
		
		isLoaded = true;

		log.debug("Number of Articles: {}", getBasePropertiesInfo().getWordsNumber());
		
	}

	@Override
	public BasePropertiesInfo loadBasePropertiesInfo() throws IOException, BaseFormatException {

		baseInfo = new BasePropertiesInfo();
		
		// Words & Articles
		words = loadWords();

		// Media Resources
		mediaResources = loadMediaResources();
		
		// Abbreviations
		abbrevKeys = loadAbbreviations();
		baseInfo.setAbbreviationsNumber(abbrevKeys.size());
		
		// Set abbreviations formating to disabled if there are no abbreviations
		if (baseInfo.getAbbreviationsNumber() == 0) {
			baseInfo.setAbbreviationsFormattingMode(AbbreviationsFormattingMode.DISABLED);
		}
		
		baseInfo.setWordsNumber(words.size());
		baseInfo.setArticlesActualNumber(words.size());
		baseInfo.setMediaResourcesNumber(mediaResources.size());
		
		return baseInfo;
	}

	/**
	 * Method is not supported.
	 */
	@Override
	public LanguageDirectionsInfo loadLanguageDirectionsInfo() {
		return null;
	}

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	@Override
	public BasePropertiesInfo getBasePropertiesInfo() {
		return baseInfo;
	}
	
	@Override
	public BaseResourceInfo getBaseResourceInfo(String resourceKey) throws BaseFormatException {
		return null;
	}

	@Override
	public FormatInfo getFormatInfo() {
		return FORMAT_INFO;
	}

	/**
	 * Method is not supported.
	 */
	@Override
	public LanguageDirectionsInfo getLanguageDirectionsInfo() {
		return null;
	}

	@Override
	public Set<String> getMediaResourceKeys() throws BaseFormatException {
		return mediaResources;
	}

	@Override
	public MediaResourceInfo getMediaResourceInfo(MediaResourceKey mediaKey) throws BaseFormatException {
		MediaResourceInfo resourceInfo = null;
		String resKey = mediaKey.getResourceKey();
		if (mediaResources.contains(resKey)) {
			try {
				DataAdapter isAdapter = new DataAdapter(new File(mediaDirectoryPath + File.separator + resKey));
				resourceInfo = new MediaResourceInfo(mediaKey, isAdapter.toByteArray());
			} catch (IOException e) {
				log.error("Error", e);
				throw new BaseFormatException("Couldn't retrieve media resource: " + e.getMessage());
			}
		}
		return resourceInfo;
	}

	@Override
	public Set<String> getAbbreviationKeys() throws BaseFormatException {
		return abbrevKeys;
	}

	@Override
	public AbbreviationInfo getAbbreviationInfo(String abbreviationKey) throws BaseFormatException {
		AbbreviationInfo abbrevInfo = new AbbreviationInfo(abbreviationKey, null);
		if (abbrevReader.readLine(abbrevInfo, abbreviationKey)) {
			return abbrevInfo;
		}
		return null;
	}

	/**
	 * Unsupported method.
	 */
	@Override
	public int searchWordIndex(String word, boolean positive) throws BaseFormatException {
		return -1;
	}

	@Override
	public List<String> getWords() {
		return words;
	}
	
	@Override
	public Map<Integer, String> getWordsLike(String wordRoot, int limit) {
		return null;
	}
	
	@Override
	public Map<Integer, Integer> getWordsRedirects() throws BaseFormatException {
		return null;
	}
	
	@Override
	public Map<Integer, String> getWordsMappings() throws BaseFormatException {
		return null;
	}
	
	@Override
	public Map<Integer, String> getAdaptedWordsMappings() throws BaseFormatException {
		return getWordsMappings();
	}

	@Override
	public ArticleInfo getArticleInfo(WordInfo wordInfo) throws BaseFormatException {
		ArticleInfo articleInfo = getRawArticleInfo(wordInfo);
		if (articleInfo != null) {
			String article = ArticleHtmlFormatter.prepareArticle(
					wordInfo.getArticleWord(),
					articleInfo.getArticle(), getAbbreviationKeys(), 
					baseInfo.getArticlesFormattingMode(), 
					baseInfo.getArticlesFormattingInjectWordMode(),
					baseInfo.getAbbreviationsFormattingMode(),
					baseInfo.getMediaResourcesNumber() != 0
				);
			articleInfo.setArticle(article);
		}
		return articleInfo;
	}

	@Override
	public ArticleInfo getRawArticleInfo(WordInfo wordInfo) throws BaseFormatException {
		ArticleInfo articleInfo = new ArticleInfo(wordInfo, null);
		if (articleReader.readLine(articleInfo, wordInfo.getId())) {
			return articleInfo;
		}
		return null;
	}
	
	@Override
	public ArticleInfo getAdaptedArticleInfo(WordInfo wordInfo) throws BaseFormatException {
		return getRawArticleInfo(wordInfo);
	}
	
	protected List<String> loadWords() throws IOException, BaseFormatException {
		File articleFile = new File(sourceDirectory + File.separator + SourceFileNames.FILE_ARTICLES);
		articleReader = new SourceFileReader(articleFile, BUF_SIZE_ARTICLES);
		articleReader.load(false);
		return articleReader.getLineKeys();
	}
	
	protected Set<String> loadAbbreviations() throws IOException, BaseFormatException {
		Set<String> abbKeys = new LinkedHashSet<String>();
		File abbrevFile = new File(sourceDirectory + File.separator + SourceFileNames.FILE_ABBREVIATIONS);
		if (abbrevFile.exists() && abbrevFile.isFile()) {
			abbrevReader = new SourceFileReader(abbrevFile, BUF_SIZE_ABBREVS);
			abbrevReader.load(true);
			abbKeys = abbrevReader.getLineMapper().keySet();
		}
		return abbKeys;
	}
	
	protected Set<String> loadMediaResources() {
		HashSet<String> resources = new HashSet<String>();
		if (mediaDirectory.exists() && mediaDirectory.isDirectory()) {
            for (File file : mediaDirectory.listFiles()) {
                if (file.isFile()) {
                	resources.add(file.getName());
                }
            }
		}		
		return resources;
	}
	
}
