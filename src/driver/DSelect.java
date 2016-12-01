/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;

import adt.Response;
import adt.Row;
import adt.Table;
import core.Server;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//add support for *

public class DSelect implements Driver{
    private static final Pattern pattern;
        static {
                    pattern = Pattern.compile(
                            "(?: *)SELECT(?: *)([ a-zA-Z0-9_,*]*)(?: *)FROM(?: *)([a-zA-Z][a-zA-Z0-9_]*)(?: *)",
                            Pattern.CASE_INSENSITIVE
                    );
        } 
    @Override
    public Response execute(Server server, String query) {
        Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
        if(!server.database().containsKey(matcher.group(2))){
            return new Response(false,"Table does not exist in database");
        }
        String[] cols;
        ArrayList<String> columns = new ArrayList();
        ArrayList<String> alias = new ArrayList();
        
        if(matcher.group(1).trim().equals("*")){
            columns = new ArrayList((ArrayList) server.database().get(matcher.group(2)).get(null).get("column_names"));
            cols = new String[columns.size()];
            alias = new ArrayList(columns);
        }
        else{
            cols = matcher.group(1).split(",");
            for(int i=0; i < cols.length;i++){
                cols[i] = cols[i].trim();
                //If * is also in this fail
                for(int j =0; j<cols.length; j++){
                    if(cols[j].trim().equals("*")){
                        return new Response(false,"You cannot include * when using column names");
                    }
                } 
                if(cols[i].contains(" ")){
                    String temp[] = cols[i].split(" ");

                    if(temp.length<3 || !temp[1].toLowerCase().equals("as")){
                        return new Response(false,"as <alias> is required if using alias");
                    }
                    ArrayList<String> aThing = new ArrayList();
                    for(int j = 0;j<temp.length;j++){
                        if(!temp[j].equals("")){
                            aThing.add(temp[j].toString());
                        }
                    }
                    Object[] finalarray = new String[aThing.size()];
                    if(aThing.size() ==3){
                        finalarray = aThing.toArray();
                    }
                    else{
                        return new Response(false,"Error with alias");
                    }
                    if(finalarray[1].toString().toLowerCase().equals("as")){
                        columns.add(finalarray[0].toString());
                        alias.add(finalarray[2].toString());
                    }
                }
                else{
                    columns.add(cols[i].trim());
                    alias.add(cols[i].trim());
                }
            }
        }
        //Check for duplicate col names with for loop
        
        for(int i = 0; i<cols.length; i++){
            for(int j = 0; j<cols.length; j++){
                if(!matcher.group(1).trim().equals("*") && i!=j && cols[i].equals(cols[j])){
                    if(alias.get(i).equals(alias.get(j))){
                        return new Response(false, "Repeated Columns must have unambiguous alias");
                    }    
                }        
            }
        }
        //Check for primary column
        
        for(int i = 0; i<cols.length; i++){
        	if(!columns.contains(server.database().get(matcher.group(2)).get(null).get("primary_column_name")))
        		return new Response(false,"Primary Column is required for selection");
        }
        
        
        //columns and alias should share indexes
        //do the rest of the modules here
        String primaryColumnName = server.database().get(matcher.group(2)).get(null).get("primary_column_name").toString();
        
        
        Table toReturn = new Table();
        //make dat dere schema
        Row newSchema = new Row();
        newSchema.put("column_names", alias);
    	ArrayList<String> column_types = new ArrayList();
        for(int i = 0; i < cols.length; i++){
        	ArrayList<String> temp = new ArrayList((ArrayList) server.database().get(matcher.group(2)).get(null).get("column_names"));
        	ArrayList<String> tempTypes = new ArrayList((ArrayList) server.database().get(matcher.group(2)).get(null).get("column_types"));
        	if(temp.contains(columns.get(i)))
        		column_types.add(tempTypes.get(temp.indexOf(columns.get(i))));
        }
        newSchema.put("column_types", column_types);
        newSchema.put("table_name", null);
        newSchema.put("primary_column_name", alias.get(columns.indexOf(primaryColumnName)));
        
        toReturn.put(null, newSchema);
        Object[] rows = server.database().get(matcher.group(2)).keySet().toArray();
        
        for(int i=0; i<rows.length; i++){
            if(rows[i] != null){
                Row sourceRow = new Row(server.database().get(matcher.group(2)).get(rows[i].toString()));
                Row tempRow = new Row();
                for(int j=0; j<columns.size(); j++){
                	if(sourceRow.containsKey(columns.get(j)) && !sourceRow.get(columns.get(j)).equals(null))
                        tempRow.put(alias.get(j), sourceRow.get(columns.get(j)));
                }
                toReturn.put(rows[i].toString(),new Row(tempRow));
            }    
        }
        String successMessage = "" + toReturn.size() + " Rows selected from " + matcher.group(2) + "";
        return new Response(true,successMessage,toReturn);
    }
    
}
