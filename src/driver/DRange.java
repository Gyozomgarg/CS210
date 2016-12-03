package driver;

import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * This is an example driver only.
 * 
 * It is not required for submission and can
 * be deleted if desired.
 * 
 * Examples:
 * 	RANGE 5
 * 	RANGE 3 AS x
 * 
 * Response 1:
 * 	success flag
 * 	no message
 * 	result table
 * 		primary integer column "number"
 *		rows [0]; [1]; [2]; [3]; [4]
 * 
 * Response 2:
 * 	success flag
 * 	no message
 * 	result table
 * 		primary integer column "x"
 *		rows [0]; [1]; [2]
 */
public class DRange implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"RANGE ([0-9]+) (?:AS\\s+(\\w+))?".replace(" ", "\\s*"),
			Pattern.CASE_INSENSITIVE
		);
	}

	private int upper;
	private String name;

	@Override
	public Response execute(Server server, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if (!matcher.matches()) return null;

		upper = Integer.parseInt(matcher.group(1));
		name = matcher.group(2) != null ? matcher.group(2) : "number";
		
		Table table = new Table();
		
		Row schema = new Row();
		
		schema.put("table_name", null);
		
		schema.put("primary_column_name", name);
		
		List<String> names = new ArrayList<>();
		names.add(name);
		schema.put("column_names", names);
		
		List<String> types = new ArrayList<>();
		types.add("integer");
		schema.put("column_types", types);
		
		table.put(null, schema);
		
		for (int i = 0; i < upper; i++) {
			Row row = new Row();
			row.put(name, i);
			table.put(i, row);
		}
		
		return new Response(true, table);
	}
}
