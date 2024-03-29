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
 * Enum of all DB tables at FDB format. 
 * 
 * @since version 2.6, 		08/27/2011
 * 
 * @modified version 4.6,	01/28/2015
 * 
 * @author Dmitry Viktorov
 * 
 */
public enum FDBTables {
	
	words,
	words_mappings,
	words_relations,
	article_blocks,
	abbreviations,
	language_directions,
	
	media_resource_keys,
	media_resource_blocks,
	
	base_properties,
	base_resources;
	
}

