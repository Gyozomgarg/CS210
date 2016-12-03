package driver;

import adt.Response;
import adt.Table;
import core.Server;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DDropTable implements Driver{
    
    private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"(?: +)?drop(?: +)table(?: )(?: +)?((?:[a-zA-Z])|(?:[a-zA-Z][a-zA-z0-9_]+))(?: +)?",
			Pattern.CASE_INSENSITIVE
		);
	}
    
    @Override
    public Response execute(Server server, String query) {
        Table removed = new Table();
        int rows;
        Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
        if (server.database().containsKey(matcher.group(1))){
            removed = new Table(server.database().get(matcher.group(1)));
            server.database().remove(matcher.group(1));
            rows = removed.size()-1;
            String message = "Table Name: " + matcher.group(1) + " " + "Rows: " + rows + " has been dropped";
            return new Response(true,message+" -> "+query,removed);
        }
        else{
            return new Response(false, "" + matcher.group(1) + " does not exist within the database"+":"+query);
        } 
    }
    
}


