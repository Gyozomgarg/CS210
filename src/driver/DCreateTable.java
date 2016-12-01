package driver;

import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

import java.util.Scanner;

public class DCreateTable implements Driver {
    
    private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"(?: +)?(?:Create(?: +)Table)(?: +)((?:[a-zA-Z])|(?:[a-zA-Z][a-zA-z0-9_]+))(?: +)?\\(([a-zA-Z0-9_ ,]+)\\)(?: +)?",
			Pattern.CASE_INSENSITIVE
		);
	}

    private String text;

    @Override
    public Response execute(Server server, String query) {
        boolean exists = false;
        Table dropped = null;
        Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
        text = matcher.group(2);
        
        if(server.database().containsKey(matcher.group(1))){
        	exists = true;
        }
        
        String[] param = text.split(",");
        Table table = new Table();
        Row schema = new Row();
        Row tempRow = new Row();
        Row colVal = new Row();
        //Store Table Name in schema
        tempRow.put("table_name", matcher.group(1));
        schema.put("table_name", matcher.group(1));
        List columnNames = new ArrayList();
        List columnTypes = new ArrayList();        
        // Going Through the query
        for(int i = 0; i < param.length; i++){
            // Locating and storying primary information in schema and table
            if(param[i].trim().toLowerCase().startsWith("primary")){
                tempRow = new Row();
                String[] strTemp = param[i].trim().split(" ");
                String columnName = "";
                //Switch Statement to determine data type and/or valid
                String dataType = strTemp[1];
                dataType = dataType.toLowerCase();
                if(!dataType.toLowerCase().equals("string") && !dataType.toLowerCase().equals("integer") && !dataType.toLowerCase().equals("boolean")){
                    return new Response(false,"Unsupported Data Type for primary column"+":"+query);
                }
                for(int j = 2; j < strTemp.length; j++){
                    if(j==strTemp.length-1){
                        columnName += strTemp[j];
                    }
                    else{
                        columnName += strTemp[j] + "_";
                    } 
                }
                tempRow = new Row();
                tempRow.put("primary_column_name", columnName);
                if(schema.containsKey("primary_column_name")){
                    return new Response(false,"There can only be one primary column"+":"+query);
                }
                schema.put("primary_column_name", columnName);
                columnNames.add(columnName);
                columnTypes.add(dataType);
                colVal.put(columnName, dataType);
                continue;
            }
            
            
            // Create Column Name and Column Types for Schema
            param[i] = param[i].trim();
            String[] strTemp = param[i].split(" ");
            
            if(!strTemp[0].toLowerCase().equals("string") && !strTemp[0].toLowerCase().equals("integer") && !strTemp[0].toLowerCase().equals("boolean") && !strTemp[0].toLowerCase().equals("autointeger")){
                return new Response(false,"Unsupported Data Type"+":"+query);
            }
            
            if(strTemp[0].toLowerCase().equals("autointeger")){
            	table.newAutoMax(strTemp[1]);
            }
            
            columnTypes.add(strTemp[0].toLowerCase());
            String tempString = "";
            for(int j = 1; j<strTemp.length; j++){               
                if(j < strTemp.length-1)
                    tempString += strTemp[j] + " ";
                else
                    tempString += strTemp[j];
            }
            columnNames.add(tempString);
            colVal.put(tempString, strTemp[0].toLowerCase());
        }
        
        if(!schema.containsKey("primary_column_name")){
        	return new Response(false, "A primary key is required"+":"+query);
        }
        
        tempRow = new Row();
        schema.put("table_name", matcher.group(1));
        tempRow.put("table_name", matcher.group(1));
        tempRow.put("column_names", new ArrayList(columnNames));
        schema.put("column_names", new ArrayList(columnNames));
        tempRow = new Row();
        tempRow.put("column_types", new ArrayList(columnTypes));
        schema.put("column_types", new ArrayList(columnTypes));
        table.put(null, new Row(schema));
        Row autoMax = new Row(table.getAutoMaxCon());
        server.database().put(matcher.group(1), table);
        server.database().get(matcher.group(1)).setAutoMaxCon(autoMax);
        if(exists==false)
            return new Response(true,"Created Table: " + matcher.group(1) + " with " + columnNames.size() + " columns"+":"+query , table);
        if(exists==true)
            return new Response(false,"A table with the name " + matcher.group(1) + " already exists"+":"+query);
        
        return new Response(true,"Created Table: " + matcher.group(1) + ""+":"+query,table);
    }
    
}
