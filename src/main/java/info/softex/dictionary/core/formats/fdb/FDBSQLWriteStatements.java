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

package info.softex.dictionary.core.formats.fdb;

/**
 * 
 * @since version 2.6, 08/26/2011
 * 
 * @modified version 3.9, 01/29/2014
 * @modified version 4.0, 02/06/2014
 * @modified version 4.6, 01/28/2015
 * 
 * @author Dmitry Viktorov
 * 
 */
public class FDBSQLWriteStatements {

	public static final String CREATE_TABLE_WORDS =
		"CREATE TABLE " + FDBTables.words +
		" (word_id INTEGER PRIMARY KEY, word TEXT UNIQUE NOT NULL)";
	
	// Words mappings
	public static final String CREATE_TABLE_WORDS_MAPPINGS =
		"CREATE TABLE " + FDBTables.words_mappings + " (word_id INTEGER PRIMARY KEY, word_mapping_1 TEXT DEFAULT NULL, word_mapping_2 TEXT DEFAULT NULL)";
	
	public static final String CREATE_INDEX_WORDS_MAPPINGS_WORD_MAPPINGS_1 =
		"CREATE INDEX words_mappings_word_mapping_1_idx ON " + FDBTables.words_mappings + "(word_mapping_1)";

	public static final String CREATE_INDEX_WORDS_MAPPINGS_WORD_MAPPINGS_2 =
		"CREATE INDEX words_mappings_word_mapping_2_idx ON " + FDBTables.words_mappings + "(word_mapping_2)";
	
	
	// Words relations
	public static final String CREATE_TABLE_WORDS_RELATIONS =
		"CREATE TABLE " + FDBTables.words_relations +
		" (relation_id INTEGER PRIMARY KEY, word_id INTEGER NOT NULL, to_word_id INTEGER NOT NULL, relation_type INTEGER NOT NULL)";
	
	public static final String CREATE_INDEX_WORDS_RELATIONS_WORD_ID =
		"CREATE INDEX words_relations_word_id_idx ON " + FDBTables.words_relations + "(word_id)";
	
	public static final String CREATE_INDEX_WORDS_RELATIONS_TO_WORD_ID =
		"CREATE INDEX words_relations_to_word_id_idx ON " + FDBTables.words_relations + "(to_word_id)";
	
	public static final String CREATE_INDEX_WORDS_RELATIONS_RELATION_TYPE =
		"CREATE INDEX words_relations_relation_type_idx ON " + FDBTables.words_relations + "(relation_type)";
	
	
	public static final String CREATE_TABLE_ARTICLE_BLOCKS =
		"CREATE TABLE " + FDBTables.article_blocks +
		" (article_block_id INTEGER PRIMARY KEY, article_block BLOB NOT NULL)";
	
	public static final String CREATE_TABLE_LANGUAGE_DIRECTIONS =
		"CREATE TABLE " + FDBTables.language_directions +
		" (language_direction_id INTEGER PRIMARY KEY, from_locale TEXT NOT NULL, to_locale TEXT NOT NULL)";

	public static final String CREATE_TABLE_ABBREVIATIONS =
		"CREATE TABLE " + FDBTables.abbreviations +
		" (abbreviation_id INTEGER PRIMARY KEY, abbreviation TEXT UNIQUE NOT NULL, definition BLOB NOT NULL)";
	
	public static final String CREATE_TABLE_MEDIA_RESOURCE_KEYS =
		"CREATE TABLE " + FDBTables.media_resource_keys +
		" (media_resource_id INTEGER PRIMARY KEY, media_resource_key TEXT UNIQUE NOT NULL)";
	
	public static final String CREATE_TABLE_MEDIA_RESOURCE_BLOCKS =
		"CREATE TABLE " + FDBTables.media_resource_blocks +
		" (media_resource_block_id INTEGER PRIMARY KEY, media_resource_block BLOB NOT NULL)";

	public static final String CREATE_TABLE_BASE_PROPERTIES =
		"CREATE TABLE " + FDBTables.base_properties +
		" (base_property_id INTEGER PRIMARY KEY, base_property_key TEXT UNIQUE NOT NULL, base_property TEXT)";

	public static final String CREATE_TABLE_BASE_RESOURCES =
		"CREATE TABLE " + FDBTables.base_resources +
		" (base_resource_id INTEGER PRIMARY KEY, base_resource_key TEXT UNIQUE NOT NULL, base_resource BLOB NOT NULL, data_1 BLOB NOT NULL, data_2 BLOB NOT NULL, data_3 BLOB NOT NULL, info_1 TEXT NOT NULL, info_2 TEXT NOT NULL, info_3 TEXT NOT NULL, info_4 TEXT NOT NULL, info_5 TEXT NOT NULL, info_6 TEXT NOT NULL)";
	
	public static final String CREATE_INDEX_LANGUAGE_DIRECTIONS_FL =
		"CREATE INDEX index_language_directions_from_locale ON language_directions(from_locale)";

	public static final String CREATE_INDEX_LANGUAGE_DIRECTIONS_TL =
		"CREATE INDEX index_language_directions_to_locale ON language_directions(to_locale)";
	
	// INSERT --------------------------------------------------------------------------------------------
	public static final String INSERT_BASE_PROPERTY =
		"INSERT INTO " + FDBTables.base_properties + " (base_property_id, base_property_key, base_property) VALUES(?, ?, ?)";
	
	public static final String INSERT_WORD =
		"INSERT INTO " + FDBTables.words + " (word_id, word) VALUES(?, ?)";

	public static final String INSERT_WORD_MAPPING =
		"INSERT INTO " + FDBTables.words_mappings + " (word_id, word_mapping_1, word_mapping_2) VALUES(?, ?, ?)";
	
	public static final String INSERT_WORD_RELATION =
		"INSERT INTO " + FDBTables.words_relations + " (relation_id, word_id, to_word_id, relation_type) VALUES(?, ?, ?, ?)";
	
	public static final String INSERT_ARTICLE =
		"INSERT INTO " + FDBTables.article_blocks + 
		" (article_block_id, article_block) VALUES(?, ?)";
	
	public static final String INSERT_MEDIA_RESOURCE_KEY =
		"INSERT INTO " + FDBTables.media_resource_keys + " (media_resource_id, media_resource_key) VALUES(?, ?)";

	public static final String INSERT_MEDIA_RESOURCE =
		"INSERT INTO " + FDBTables.media_resource_blocks + " (media_resource_block_id, media_resource_block) VALUES(?, ?)";
	
	public static final String INSERT_ABBREVIATION =
		"INSERT INTO " + FDBTables.abbreviations + "(abbreviation_id, abbreviation, definition) VALUES(?, ?, ?)";
	
	public static final String INSERT_LANGUAGE_DIRECTION =
		"INSERT INTO " + FDBTables.language_directions + " (language_direction_id, from_locale, to_locale) VALUES(?, ?, ?)";
		
	public static final String INSERT_BASE_RESOURCE =
		"INSERT INTO " + FDBTables.base_resources + 
		" (base_resource_id,base_resource_key,base_resource,data_1,data_2,data_3,info_1,info_2,info_3,info_4,info_5,info_6) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";		

	
	// UPDATE ---------------------------------------------------------
	public static final String UDATE_BASE_PROPERTY =
		"UPDATE " + FDBTables.base_properties + " SET base_property=? WHERE base_property_key=?";
	
	// DROP -----------------------------------------------------------
	public static final String DROP_TABLE_UNI = "DROP TABLE IF EXISTS";

}
