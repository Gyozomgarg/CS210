package driver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Response;
import adt.Table;
import core.Server;

public class DImport implements Driver{
    
    String location;
    
    private static final Pattern pattern;
    static {
            pattern = Pattern.compile(
                    "(?: )*IMPORT(?: )+([a-zA-Z][a-zA-z0-9_\\:]+.txt)(?: )*",
                    Pattern.CASE_INSENSITIVE
            );
    }

    @Override
    public Response execute(Server server, String query) {
            Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
        location = matcher.group(1).trim();
        Table temp =  deserialize();
        server.database().put(temp.get(null).get("table_name").toString(),temp);

        return new Response(true,"Table Successfully Imported",temp);
    }

    public Table deserialize(){
        ObjectInputStream stream = null;
        FileInputStream file = null;
        Table table = null;
        try{
            file = new FileInputStream(this.location);
            stream = new ObjectInputStream(file);
            table = (Table) stream.readObject();
        }
        catch(IOException i){
            i.printStackTrace();
        }
        catch(ClassNotFoundException c){
            c.printStackTrace();
        }
        return table;
    }

}
