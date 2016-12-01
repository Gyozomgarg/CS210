package driver;

import adt.Response;
import core.Server;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DAlterTable implements Driver{
    private static final Pattern insertPattern;
            static {
                    insertPattern = Pattern.compile(
                            "(?: )*ALTER(?: )+TABLE(?: )+([a-zA-Z0-9_]+)(?: )+INSERT(?: )+COLUMN(?: )+((?:string|boolean|integer))(?: )([a-zA-Z0-9_]+)(?: )+(FIRST|LAST|(?:AFTER(?: )+[a-zA-Z0-9_]+))(?: )*",
                            Pattern.CASE_INSENSITIVE
                            // 1-TABLE NAME 2-<Data Type> Column Name 3-FIRST|LAST|AFTER CN
                    );
            }
    private static final Pattern renamePattern;
            static {
                    renamePattern = Pattern.compile(
                            "(?: )*ALTER(?: )+TABLE(?: )+([a-zA-Z0-9_]+)(?: )+RENAME(?: )+COLUMN(?: )+([a-zA-Z0-9_]+)(?: )+([a-zA-Z0-9_]+)(?: )*",
                            Pattern.CASE_INSENSITIVE
                            // 1-TABLE NAME 2-OLD COLUMN NAME 3-NEW COLUMN NAME
                    );
            }
    private static final Pattern dropPattern;
            static {
                    dropPattern = Pattern.compile(
                            "(?: )*ALTER(?: )+TABLE(?: )+([a-zA-Z0-9_]+)(?: )+DROP(?: )+COLUMN(?: )+([a-zA-Z0-9_]+)(?: )*",
                            Pattern.CASE_INSENSITIVE
                            // 1-TABLE NAME 2-COLUMN NAME
                    );
            }
    private static final Pattern tablePattern;
            static {
                    tablePattern = Pattern.compile(
                            "(?: )*ALTER(?: )+TABLE(?: )+([a-zA-Z0-9_]+)(?: )+RENAME(?: )+TO(?: )+([a-zA-Z0-9_]+)(?: )*",
                            Pattern.CASE_INSENSITIVE
                            // 1-TABLE NAME 2- NEW TABLE NAME
                    );
            }        
    @Override
    public Response execute(Server server, String query) {
        int check = 0;
        Matcher matcher = null;
        while(true){
            matcher = insertPattern.matcher(query.trim());
            if(matcher.matches()){
                check = 1;
                break;
            }
            matcher = renamePattern.matcher(query.trim());
            if(matcher.matches()){
                check = 2;
                break;
            }
            matcher = dropPattern.matcher(query.trim());
            if(matcher.matches()){
                check = 3;
                break;
            }
            matcher = tablePattern.matcher(query.trim());
            if(matcher.matches()){
                check = 4;
                break;
            }
            return null;
        }
        String tableName = matcher.group(1).trim();
        switch(check){
            case 1: insertAlter(server, matcher);
                    break;
            case 2: renameAlter(server, matcher);
                    break;
            case 3: dropAlter(server, matcher);
                    break;
            case 4: tableName = tableAlter(server, matcher);
                    break;
        }
        
        return new Response(true,"Table "+matcher.group(1).trim()+" has been altered"+":"+query, server.database().get(tableName));
    }
    
    public void insertAlter(Server server, Matcher matcher){
        String tableName = matcher.group(1).trim();
	String dataType = matcher.group(2).toLowerCase().trim();
	String colName = matcher.group(3).trim();
	String[] location = matcher.group(4).trim().split(" ");
	if(location.length == 1){
		if(location[0].toLowerCase().equals("first")){
			ArrayList<String> names = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
        		ArrayList<String> types = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
			names.add(0,colName);
			types.add(0,dataType);
			server.database().get(matcher.group(1).trim()).get(null).put("column_names", names);
        		server.database().get(matcher.group(1).trim()).get(null).put("column_types", types);
		}
		if(location[0].toLowerCase().equals("last")){
			ArrayList<String> names = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
        		ArrayList<String> types = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
			names.add(names.size(),colName);
			types.add(types.size(),dataType);
			server.database().get(matcher.group(1).trim()).get(null).put("column_names", names);
        		server.database().get(matcher.group(1).trim()).get(null).put("column_types", types);
		}
	}
	if(location.length > 1){
		ArrayList<String> fixedLoc = new ArrayList<String>();
		for(int i = 0; i<location.length; i++){
			if(!location[i].equals("")){
				fixedLoc.add(location[i]);
			}
		}
		String predCol = fixedLoc.get(1);
		ArrayList<String> names = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
        	ArrayList<String> types = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
		int predLocation = names.indexOf(predCol);
		names.add(predLocation+1,colName);
		types.add(predLocation+1,dataType);
		server.database().get(matcher.group(1).trim()).get(null).put("column_names", names);
        	server.database().get(matcher.group(1).trim()).get(null).put("column_types", types);
	}

    }
    
    public void renameAlter(Server server, Matcher matcher){
        ArrayList<String> names = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
        ArrayList<String> types = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
        int index = names.indexOf(matcher.group(2).trim());
        names.remove(index);
        names.add(index, matcher.group(3).trim());
        if(types.get(index).equals("autointeger")){
            int value = server.database().get(matcher.group(1)).getAutoMax(matcher.group(2).trim());
            server.database().get(matcher.group(1)).setAutoMax(matcher.group(2).trim(), value);
        }
        server.database().get(matcher.group(1).trim()).get(null).put("column_names", names);
        //Change Column Names in All Rows in Table
        try{
            if(server.database().get(matcher.group(1)).entrySet().toArray().length > 0){
                Object[] keyset = server.database().get(matcher.group(1).trim()).keySet().toArray();
                for(int i = 0; i<keyset.length; i++){
                    server.database().get(matcher.group(1)).get(keyset[i]).put(matcher.group(3), 
                            server.database().get(matcher.group(1)).get(keyset[i]).get(matcher.group(2)));
                    server.database().get(matcher.group(1)).get(keyset[i]).remove(matcher.group(2));
                }

            }
        }
        catch(Exception e){
            
        }
    }
    public void dropAlter(Server server, Matcher matcher){
        ArrayList<String> names = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
        ArrayList<String> types = new ArrayList<String>((ArrayList<String>) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
        int index = names.indexOf(matcher.group(2).trim());
        names.remove(index);
        types.remove(index);
        server.database().get(matcher.group(1).trim()).get(null).put("column_names", names);
        server.database().get(matcher.group(1).trim()).get(null).put("column_types", types);                
    }
    public String tableAlter (Server server, Matcher matcher){
        server.database().put(matcher.group(2).trim(), server.database().get(matcher.group(1).trim()));
        server.database().get(matcher.group(2).trim()).get(null).put("table_name", matcher.group(2).trim());
        server.database().remove(matcher.group(1).trim());
        return matcher.group(2).trim();
    }
    
}
