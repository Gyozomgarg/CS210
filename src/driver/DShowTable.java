package driver;

import adt.Response;
import adt.Row;
import adt.Table;
import core.Server;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DShowTable implements Driver{

    private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"(?: +)?show(?: +)tables(?: +)?",
			Pattern.CASE_INSENSITIVE
		);
	}    
    
    @Override
    public Response execute(Server server, String query) {
    	Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
    	Set names = server.database().keySet();
        Object[] tablesnames = names.toArray();
        Row toShow = new Row();
        Table showTables = new Table();
        Row schema = new Row();
        List<Integer> Rows = new ArrayList();
        List<String> tableNames = new ArrayList();
        List<String> types = new ArrayList();
        List<String> columnNames = new ArrayList();
        
        schema.put("table_name", null);
        schema.put("primary_column_name", "table_name");
        columnNames.add("table_name");
        columnNames.add("row_count");
        schema.put("column_names", columnNames);
//        for(int i = 0; i < tablesnames.length; i++){  
//            String tempName = tablesnames[i].toString();
//            Rows.add(server.database().get(tempName).size());
//            types.add("string");
//        }
        types.add("string"); types.add("integer");
        schema.put("column_types", types);
        showTables.put(null,schema);
        
        String toBePrintedMyDuuuudes = "Number of Tables: " + server.database().size() + "\n" + 
                toShow.toString();
        
        return new Response(true, "There are " + tableNames.size() + "tables in the database"+":"+query, showTables);
    }
    
}