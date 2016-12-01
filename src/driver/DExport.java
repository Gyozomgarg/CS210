package driver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Response;
import adt.Table;
import core.Server;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DExport implements Driver{

    private static final Pattern pattern;
        static {
            pattern = Pattern.compile(
                    "(?: )*EXPORT(?: )+([a-zA-Z][a-zA-z0-9_]+)(?: )+([a-zA-Z][a-zA-z0-9_\\:]+.txt)(?: )*",
                    Pattern.CASE_INSENSITIVE
            );
        }          
    
    String destination;    
    @Override
    public Response execute(Server server, String query){
            Matcher matcher = pattern.matcher(query.trim());
    if (!matcher.matches()) return null;
    destination = matcher.group(2).trim();
    ObjectOutputStream stream = null;
    FileOutputStream file = null;
    Table target = new Table(server.database().get(matcher.group(1).trim()));
        try {
            serialize(target);
        } catch (IOException ex) {
        }
    return new Response(true,"Table "+matcher.group(1)+" was saved to "+matcher.group(2));
    }

    public void serialize(Table table) throws FileNotFoundException, IOException{
        ObjectOutputStream stream = null;
        FileOutputStream file = null;
        file = new FileOutputStream(destination);
        stream = new ObjectOutputStream(file);
        stream.writeObject(table);
        stream.close();
        file.close();
    }

}
