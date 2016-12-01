/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

import adt.Response;
import core.Server;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Gyozomgarg
 */
public class DDumpTable implements Driver{
	
    private static final Pattern pattern;
    static {
            pattern = Pattern.compile(
                    "(?: *)DUMP(?: +)TABLE(?: +)((?:[a-zA-Z][a-zA-z0-9_]+))(?: *)",
                    Pattern.CASE_INSENSITIVE
            );
    }

    @Override
    public Response execute(Server server, String query) {
        Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
        if(server.database().containsKey(matcher.group(1))){
            return new Response(true,"Table Found"+":"+query,server.database().get(matcher.group(1)));
        }
        else{
            return new Response(false,"Table not found"+":"+query);
        }
    }
}    
