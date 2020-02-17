/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2020, Thomas Kellerer
 *
 * Licensed under a modified Apache License, Version 2.0
 * that restricts the use for certain governments.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *     https://www.sql-workbench.eu/manual/license.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.eu
 *
 */
package workbench.db;

/**
 * Enum for foreign key match types according to the SQL standard.
 *
 * @author Thomas Kellerer
 */
public enum FKMatchType
{
	SIMPLE,
	FULL,
	PARTIAL,
	UNKNOWN;

	@Override
	public String toString()
	{
		if (this == UNKNOWN) return "";
		return super.toString();
	}

	public static FKMatchType fromString(String input)
	{
		if (input == null || input.isEmpty()) return UNKNOWN;
		try
		{
			return valueOf(input.toUpperCase());
		}
		catch (Throwable th)
		{
			return UNKNOWN;
		}
	}
}