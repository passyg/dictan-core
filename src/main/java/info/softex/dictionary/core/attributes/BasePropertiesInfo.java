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

package info.softex.dictionary.core.attributes;

import info.softex.dictionary.core.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 
 * @since version 2.0,		03/12/2011
 * 
 * @modified version 2.6,	08/27/2011
 * @modified version 2.9,	11/13/2011
 * @modified version 3.9,	12/05/2013
 * @modified version 4.0,	02/02/2014
 * @modified version 4.5,	03/29/2014
 * @modified version 4.6,	01/19/2015
 * 
 * @author Dmitry Viktorov
 * 
 */
public class BasePropertiesInfo implements Cloneable {
	
	public static enum PrimaryKey {
		
		FORMAT_NAME("format.name"),
		FORMAT_VERSION("format.version"),
		
		BASE_VERSION("base.version"),
		BASE_TYPE("base.type"),
		BASE_DATE("base.date"),
		BASE_NAME_SHORT("base.name.short"),
		BASE_NAME_FULL("base.name.full"),
		BASE_PARTS_CURRENT_NUMBER("base.parts.current.number"),
		BASE_PARTS_TOTAL_NUMBER("base.parts.total.number"),
		BASE_PARTS_MAIN_SIZE_MIN("base.parts.main.size.min"),
		BASE_PARTS_SECONDARY_SIZE_MIN("base.parts.secondary.size.min"),
		BASE_SECURITY_PROPERTIES_MD5("base.security.properties.md5"),

		@Deprecated
		ARTICLES_NUMBER("articles.number"), // The property is used only at FDB versions 1 and 2
		
		WORDS_NUMBER("words.number"),
		WORDS_MAPPINGS_NUMBER("words.mappings.number"),
		WORDS_RELATIONS_NUMBER("words.relations.number"),
		
		ARTICLES_ACTUAL_NUMBER("articles.actual.number"),
		ARTICLES_FORMATTING_MODE("articles.formatting.mode"),
		ARTICLES_FORMATTING_INJECT_WORD_MODE("articles.formatting.word.inject.mode"),
		ARTICLES_BLOCKS_SIZE_UNCOMPRESSED_MIN("articles.blocks.size.uncompressed.min"),
		
		ABBREVIATIONS_NUMBER("abbreviations.number"),
		ABBREVIATIONS_FORMATTING_MODE("abbreviations.formatting.mode"),

		MEDIA_RESOURCES_NUMBER("media.resources.number"),
		MEDIA_RESOURCES_BLOCKS_SIZE_UNCOMPRESSED_MIN("media.resources.blocks.size.uncompressed.min"),
		
		INFO_COMPILATION_CREATOR_NAME("info.compilation.creator.name"),
		INFO_COMPILATION_PROGRAM_NAME("info.compilation.program.name"),
		INFO_COMPILATION_PROGRAM_VERSION("info.compilation.program.version"),
		INFO_COMPILATION_SDK_NAME("info.compilation.sdk.name"),
		INFO_COMPILATION_SDK_VERSION("info.compilation.sdk.version"),
		INFO_COMPILATION_OS_NAME("info.compilation.os.name"),
		INFO_COMPILATION_OS_VERSION("info.compilation.os.version"),
		INFO_COMPILATION_DATE("info.compilation.date");
		
		private String key = null;
		
		private PrimaryKey(String inKey) {
			this.key = inKey;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public static PrimaryKey valueOfKey(String key) {
			PrimaryKey[] values = PrimaryKey.values();
			for (int i = 0; i < values.length; i++) {
				if (values[i].getKey().equalsIgnoreCase(key)) {
					return values[i];
				}
			}
			return null;
		}
		
		public static String getBasePartsArticlesBlockIdStartKey(int num) {
			return "base.parts." + num + ".articles.blocks.id.start";
		}
		
		public static String getBasePartsMediaResourcesBlockIdStartKey(int num) {
			return "base.parts." + num + ".media.resources.blocks.id.start";
		}

	}
	
	public enum ArticlesFormattingMode {
		DISABLED, 
		FULL,
		BASIC,
		DSL
	}
	
	public enum ArticlesFormattingInjectWordMode { 
		DISABLED,
		ALWAYS,
		AUTO
	}

	public enum AbbreviationsFormattingMode {
		DISABLED,
		FULL,
		BASIC,
		DSL
	}
	
	public enum BaseTypes {
		UNDEFINED,
		DICTIONARY,
		LEXICON,
		ENCYCLOPEDIA		
	}
	
	protected LinkedHashMap<String, Object> primaryParams = new LinkedHashMap<String, Object>();
	
	// Media
	private boolean mediaBaseSeparate = false;
	private String mediaBaseVersion = null;
	private String mediaFormatVersion = null;
	private String mediaFormatName = null;
	
	private long mediaFileSize = 0; // Only if the media base is separate 
	
	private String articlesCodepageName = null;
	private String wordsCodepageName = null;
	
	// Meta base file parameters
	private String baseFilePath;
	private long baseFileSize;	
	
	public String getBaseFilePath() {
		return baseFilePath;
	}
	
	public void setBaseFilePath(String dictionaryFilePath) {
		baseFilePath = dictionaryFilePath;
	}

	public String getBaseFileName() {
		return getBaseFilePath() == null ? null : new File(getBaseFilePath()).getName();
	}
	
	public String getBaseVersion() {
		return (String) primaryParams.get(PrimaryKey.BASE_VERSION.getKey());
	}

	public void setBaseVersion(int major, int minor) {
		String version = major + "." + minor;
		primaryParams.put(PrimaryKey.BASE_VERSION.getKey(), version);
	}

	public void setBaseDate(Date date) {
		primaryParams.put(PrimaryKey.BASE_DATE.getKey(), dateToString(date));
	}
	
	public void setCompilationDate(Date date) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_DATE.getKey(), dateToString(date));
	}
	
	public String getCompilationDate() {
		return (String) primaryParams.get(PrimaryKey.INFO_COMPILATION_DATE.getKey());
	}
	
	public String getBaseDate() {
		return (String) primaryParams.get(PrimaryKey.BASE_DATE.getKey());
	}
	
	public int getFormatVersion() {
		return getIntValue(PrimaryKey.FORMAT_VERSION.getKey());
	}
	
	public void setFormatVersion(int formatVersion) {
		primaryParams.put(PrimaryKey.FORMAT_VERSION.getKey(), formatVersion);
	}

	public String getFormatName() {
		return (String) primaryParams.get(PrimaryKey.FORMAT_NAME.getKey());
	}

	public void setFormatName(String formatName) {
		primaryParams.put(PrimaryKey.FORMAT_NAME.getKey(), formatName);
	}
	
	public String getBaseFullName() {
		return StringUtils.defaultString((String) primaryParams.get(PrimaryKey.BASE_NAME_FULL.getKey()));
	}
	
	public String getBaseShortName() {
		return StringUtils.defaultString((String) primaryParams.get(PrimaryKey.BASE_NAME_SHORT.getKey()));
	}

	public String getBaseType() {
		Object baseType = primaryParams.get(PrimaryKey.BASE_TYPE.getKey());
		if (baseType instanceof String) {
			return BaseTypes.valueOf(baseType.toString()).name();
		}
		return BaseTypes.UNDEFINED.name();
	}

	public void setBaseFullName(String fullName) {
		primaryParams.put(PrimaryKey.BASE_NAME_FULL.getKey(), fullName);
	}
	
	public void setBaseShortName(String shortName) {
		primaryParams.put(PrimaryKey.BASE_NAME_SHORT.getKey(), shortName);
	}

	public void setBaseType(BaseTypes type) {
		primaryParams.put(PrimaryKey.BASE_TYPE.getKey(), type.name());
	}
	
	public void setArticlesFormattingMode(ArticlesFormattingMode mode) {
		primaryParams.put(PrimaryKey.ARTICLES_FORMATTING_MODE.getKey(), mode.name());
	}
	
	public void setArticlesFormattingInjectWordMode(ArticlesFormattingInjectWordMode prependMode) {
		primaryParams.put(PrimaryKey.ARTICLES_FORMATTING_INJECT_WORD_MODE.getKey(), prependMode.name());
	}

	public String getArticlesFormattingMode() {
		return getArticlesFormattingModeResolved().name();
	}
	
	public ArticlesFormattingMode getArticlesFormattingModeResolved() {
		Object mode = primaryParams.get(PrimaryKey.ARTICLES_FORMATTING_MODE.getKey());
		if (mode instanceof String) {
			if ("MEDIA".equalsIgnoreCase((String)mode)) { // Except the obsolete MEDIA formatting
				return ArticlesFormattingMode.BASIC;
			}
			return ArticlesFormattingMode.valueOf(mode.toString());
		}
		return ArticlesFormattingMode.FULL;
	}
	
	public String getArticlesFormattingInjectWordMode() {
		return getArticlesFormattingInjectWordModeResolved().name();
	}
	
	public ArticlesFormattingInjectWordMode getArticlesFormattingInjectWordModeResolved() {
		Object mode = primaryParams.get(PrimaryKey.ARTICLES_FORMATTING_INJECT_WORD_MODE.getKey());
		if (mode instanceof String) {
			return ArticlesFormattingInjectWordMode.valueOf(mode.toString());
		}
		return ArticlesFormattingInjectWordMode.AUTO;
	}

	public String getAbbreviationsFormattingMode() {
		return getAbbreviationsFormattingModeResolved().name();
	}
	
	public AbbreviationsFormattingMode getAbbreviationsFormattingModeResolved() {
		Object mode = primaryParams.get(PrimaryKey.ABBREVIATIONS_FORMATTING_MODE.getKey());
		if (mode instanceof String) {
			return AbbreviationsFormattingMode.valueOf(mode.toString());
		}
		return AbbreviationsFormattingMode.FULL;
	}
	
	public void setAbbreviationsFormattingMode(AbbreviationsFormattingMode mode) {
		primaryParams.put(PrimaryKey.ABBREVIATIONS_FORMATTING_MODE.getKey(), mode.name());
	}
	
	public Map<String, Object> getPrimaryParameters() {
		return primaryParams;
	}
	
	public void setBaseFileSize(long size) {
		baseFileSize = size;
	}
	
	public long getBaseFileSize() {
		return baseFileSize;
	}
	
	public int getBasePartsTotalNumber() {
		int result = getIntValue(PrimaryKey.BASE_PARTS_TOTAL_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	
	public int getBasePartsArticlesBlockIdStart(int num) {
		return getIntValue(PrimaryKey.getBasePartsArticlesBlockIdStartKey(num));
	}
	
	public int getBasePartsMediaResourcesBlockIdStart(int num) {
		return getIntValue(PrimaryKey.getBasePartsMediaResourcesBlockIdStartKey(num));
	}

	
	public void setBasePartsTotalNumber(int number) {
		primaryParams.put(PrimaryKey.BASE_PARTS_TOTAL_NUMBER.getKey(), number);
	}
	
	public void setArticlesCodepageName(String transCodepageName) {
		articlesCodepageName = transCodepageName;
	}
	
	public String getArticleCodepageName() {
		return articlesCodepageName;
	}
	
	public String getWordsCodepageName() {
		return wordsCodepageName;
	}
	
	public void setWordsCodepageName(String inWordsCodepageName) {
		wordsCodepageName = inWordsCodepageName;
	}
	
	public int getWordsNumber() {
		int result = getIntValue(PrimaryKey.WORDS_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	public int getArticlesActualNumber() {
		int result = getIntValue(PrimaryKey.ARTICLES_ACTUAL_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	/**
	 * Only needed for the old FDB bases. Use <code>getWordsNumber()</code>.
	 */
	@Deprecated
	public int getArticlesDeprecatedNumber() {
		int result = getIntValue(PrimaryKey.ARTICLES_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	/**
	 * Only needed for the old FDB viewers. Use <code>setWordsNumber()</code>.
	 */
	@Deprecated
	public void setArticlesDeprecatedNumber(int wordsNumber) {
		primaryParams.put(PrimaryKey.ARTICLES_NUMBER.getKey(), wordsNumber);
	}
	
	public int getWordsMappingsNumber() {
		int result = getIntValue(PrimaryKey.WORDS_MAPPINGS_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	public int getWordsRelationsNumber() {
		int result = getIntValue(PrimaryKey.WORDS_RELATIONS_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	/**
	 * @return - Total number of words, media resources, and abbreviations
	 */
	public int getWmraNumber() {
		return getWordsNumber() + getMediaResourcesNumber() + getAbbreviationsNumber();
	}
	
	public void setWordsNumber(int wordsNumber) {
		primaryParams.put(PrimaryKey.WORDS_NUMBER.getKey(), wordsNumber);
	}
	
	public void setWordsMappingsNumber(int mappingsNumber) {
		primaryParams.put(PrimaryKey.WORDS_MAPPINGS_NUMBER.getKey(), mappingsNumber);
	}
	
	public void setWordsRelationsNumber(int redirectsNumber) {
		primaryParams.put(PrimaryKey.WORDS_RELATIONS_NUMBER.getKey(), redirectsNumber);
	}
	
	public void setArticlesActualNumber(int actualNumber) {
		primaryParams.put(PrimaryKey.ARTICLES_ACTUAL_NUMBER.getKey(), actualNumber);
	}

	public int getAbbreviationsNumber() {
		int result = getIntValue(PrimaryKey.ABBREVIATIONS_NUMBER.getKey());
		return result > 0 ? result : 0;
	}

	public void setAbbreviationsNumber(int abbreviationsNumber) {
		primaryParams.put(PrimaryKey.ABBREVIATIONS_NUMBER.getKey(), abbreviationsNumber);
	}
	
//	public Locale getBaseLocale() {
//		Object obj = this.primaryParams.get(PrimaryKeys.BASE_LOCALE);
//		if (obj instanceof Locale) {
//			return (Locale)obj;
//		} else if (obj instanceof String) {
//			return new Locale((String)obj);
//		}
//		return null;
//	}
//
//	public void setBaseLocale(Locale locale) {
//		this.primaryParams.put(PrimaryKeys.BASE_LOCALE, locale);
//	}
	
	public void setCompilationCreatorName(String name) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_CREATOR_NAME.getKey(), name);
	}
	
	public String getCompilationCreatorName() {
		return (String) primaryParams.get(PrimaryKey.INFO_COMPILATION_CREATOR_NAME.getKey());
	}
	
	public void setCompilationProgramName(String name) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_PROGRAM_NAME.getKey(), name);
	}
	
	public void setCompilationProgramVersion(String version) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_PROGRAM_VERSION.getKey(), version);
	}
	
	public void setCompilationSdkName(String name) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_SDK_NAME.getKey(), name);
	}
	
	public void setCompilationSdkVersion(String version) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_SDK_VERSION.getKey(), version);
	}
	
	public void setCompilationOsName(String name) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_OS_NAME.getKey(), name);
	}
	
	public void setCompilationOsVersion(String version) {
		primaryParams.put(PrimaryKey.INFO_COMPILATION_OS_VERSION.getKey(), version);
	}
	
	@Override
	public String toString() {
		String prim = this.primaryParams != null ? this.primaryParams.toString() : "";
		if (getArticleCodepageName() != null) {
			return "articleCodePage: " + getArticleCodepageName() + " " + prim;
		}
		return prim;
	}
	
	public int getMediaResourcesNumber() {
		int result = getIntValue(PrimaryKey.MEDIA_RESOURCES_NUMBER.getKey());
		return result > 0 ? result : 0;
	}
	
	public void setMediaResourcesNumber(int mediaResourcesNumber) {
		primaryParams.put(PrimaryKey.MEDIA_RESOURCES_NUMBER.getKey(), mediaResourcesNumber);
	}
	
	public String getMediaBaseVersion() {
		return mediaBaseVersion;
	}
	
	public void setMediaBaseVersion(String inMediaBaseVersion) {
		this.mediaBaseVersion = inMediaBaseVersion;
	}
	
	public String getMediaFormatVersion() {
		return mediaFormatVersion;
	}
	
	public void setMediaFormatVersion(String inMediaFormatVersion) {
		this.mediaFormatVersion = inMediaFormatVersion;
	}
	
	public String getMediaFormatName() {
		return mediaFormatName;
	}
	
	public void setMediaFormatName(String inMediaFormatName) {
		this.mediaFormatName = inMediaFormatName;
	}
	
	public long getMediaFileSize() {
		return mediaFileSize;
	}
	
	public void setMediaFileSize(long inMediaFileSize) {
		this.mediaFileSize = inMediaFileSize;
	}
	
	public boolean isMediaBaseSeparate() {
		return mediaBaseSeparate;
	}
	
	public void setMediaBaseSeparate(boolean inMediaBaseSeparate) {
		this.mediaBaseSeparate = inMediaBaseSeparate;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BasePropertiesInfo clone() {
		try {
			BasePropertiesInfo info = (BasePropertiesInfo) super.clone();
			info.primaryParams = (LinkedHashMap) this.primaryParams.clone();
			return info;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
//	
//	public void skipNumbers() {
//		setArticlesNumber(0);
//		setMediaResourcesNumber(0);
//		setAbbreviationsNumber(0);
//	}
	
	// Private methods ------------------------------------------------------------

	private int getIntValue(String keyString) {
		Object obj = primaryParams.get(keyString);
		if (obj instanceof Integer) {
			return (Integer)obj;
		} else if (obj instanceof String) {
			return Integer.parseInt((String)obj);
		} else if (obj != null) {
			throw new IllegalArgumentException("The key " + keyString + " cannot be cast to int");
		}
		return -1;
	}
	
//	private boolean getBooleanValue(String keyString, boolean defValue) {
//		Object obj = primaryParams.get(keyString);
//		if (obj instanceof Boolean) {
//			return (Boolean)obj;
//		} else if (obj instanceof String) {
//			return Boolean.parseBoolean((String)obj);
//		} else if (obj != null) {
//			throw new IllegalArgumentException("The key " + keyString + " cannot be cast to boolean");
//		}
//		return defValue;
//	}
	
	private String dateToString(Date date) {
		//2012-03-06T08:00:00,000Z
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'");
		format.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
		return format.format(date);
	}
 	
}
