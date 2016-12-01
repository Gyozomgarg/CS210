package driver;

import adt.Database;
import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

import adt.Response;
import core.Server;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DInsertIntoTable implements Driver{
	
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"(?: *)INSERT(?: +)INTO(?: *)([a-zA-Z][a-zA-Z0-9_]*)(?: *)(\\((?:[a-zA-Z0-9_, )(\"]+)\\))?(?: *)VALUES(?: *)\\(([-+a-zA-Z0-9_, !\\\"]+)\\)(?: *)",
			Pattern.CASE_INSENSITIVE
		);
	}
	
	@Override
	public Response execute(Server server, String query) {
		Matcher matcher = pattern.matcher(query.trim());
        if (!matcher.matches()) return null;
        boolean auto = false;
        int max = 0;
        boolean intExist = false;
        Table tempTable = new Table();
        String columns = "";
        try{
        if(!matcher.group(2).isEmpty() && matcher.group(2).trim().startsWith("(") && matcher.group(2).trim().endsWith(")") && matcher.group(2).trim().length() > 2){
            columns = matcher.group(2).trim().substring(1,matcher.group(2).trim().length()-1);
        }
        }
        catch(NullPointerException e){
                ArrayList temp = new ArrayList((ArrayList) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
                for(int i = 0; i < temp.size(); i++){
                    columns += temp.get(i).toString();
                    if(i+1<temp.size()){
                        columns += ", ";
                    }
                }            
        }    

        if(matcher.group(3).trim().equals("")){
        	return new Response(false,"Must contain at least 1 Value "+":"+query);
        }
        String[] cols = columns.split(",");
        String[] vals = matcher.group(3).split(",");
        
        if(cols.length == 0 || vals.length == 0){
            return new Response(false,"No Columns and/or Values Entered "+":"+query);
        }
        
        if(cols.length != vals.length){
        	return new Response(false,"Number of Columns and Values do not match "+":"+query);
        }
        if(!server.database().containsKey(matcher.group(1))){
        	return new Response(false,"Table does not exist in the database "+":"+query);
        }
        if(
                cols.toString().contains(server.database().get(matcher.group(1)).get(null).get("primary_column_name").toString())){
        	return new Response(false,"Query must contain primary column "+":"+query);
        }
        for(int i=0; i<cols.length; i++){
        	for(int j=0; j<cols.length; j++){
        		if(i!=j && cols[i].trim().equals(cols[j].trim())){
        			return new Response(false,"Query cannot contain repeated column names "+":"+query);
        		}
        	}
        }
        
        Row tempRow = new Row();
        
        //Checking for Invalid Strings
        for(int i = 0; i<vals.length; i++){
            if(vals[i].contains("\"" )){
                if(vals[i].contains(";")){
                    return new Response(false,"Invalid Character \";\" in String "+":"+query);
                }
                if(vals[i].trim().endsWith("\"") && vals[i].trim().startsWith("\"")){
                    if(vals[i].trim().length() >= 3){    
                        String tempString = vals[i].trim().substring(1, vals[i].trim().length()-2);
                        if(tempString.contains("\"")){
                            return new Response(false,vals[i] + "Contains to many \"\'s "+":"+query);
                        }
                    }
                }
            }
        }    
        for(int i = 0; i<cols.length;i++){
            
            List<String> ColNames = null;
            List<String> ColVals = null;
            
            if(server.database().get(matcher.group(1).trim()).get(null).get("column_names") 
                    instanceof ArrayList && server.database().get(matcher.group(1).trim()).get(null).get("column_types") instanceof ArrayList){
            ColNames = new ArrayList((ArrayList) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
            ColVals = new ArrayList((ArrayList) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
            }
                    
            if(server.database().get(matcher.group(1)).get(null).get("primary_column_name").toString().equals(cols[i].trim())){
                if(vals[i].equals("null")){
                    return new Response(false,"Primary Column Value Cannot be null "+":"+query);
                }
            }
            
            
            if(vals[i].trim().toLowerCase().equals("null") && !vals[i].equals("autointeger")){
                continue;
            }
            
            if(vals[i].trim().toLowerCase().equals("null") && vals[i].equals("autointeger")){
		auto = true;                
		continue;
            }
            
            if(!vals[i].trim().contains("\"") && vals[i].contains(".")){
                return new Response(false,"The Values must be an Integer, String or Boolean "+":"+query);
            }
            
            //Check For Boolean
            if(ColNames.contains(cols[i].trim()) && ColVals.get(ColNames.indexOf(cols[i].trim())).equals("boolean") && !vals[i].contains("\"")){
                if(vals[i].trim().toLowerCase().equals("true") || vals[i].trim().toLowerCase().equals("false")) {
                    Boolean tempBooVal = null;
                    if(vals[i].trim().toLowerCase().equals("true"))
                        tempBooVal = true;
                    else if(vals[i].trim().toLowerCase().equals("false"))
                        tempBooVal = false;
                    else if(vals[i].trim().toLowerCase().equals("null"))
                        tempBooVal = null;
                    else
                    	return new Response(false,"The data type of "+vals[i].trim()+ " does not match with " +cols[i]+":"+query);
                    tempRow.put(cols[i].trim(),tempBooVal);
                }
                else
                	return new Response(false,"The data type of "+vals[i].trim()+ " does not match with " +cols[i]+":"+query);
            }
            //Check for Valid Integer or AutoInteger
            else if(ColVals.get(ColNames.indexOf(cols[i].trim())).equals("integer") || ColVals.get(ColNames.indexOf(cols[i].trim())).equals("autointeger")){
            	if(vals[i].trim().startsWith("-")){
            		if(vals[i].trim().substring(1).startsWith("0") && !vals[i].trim().equals("0")){
                    	return new Response(false, "Integers cannot having preceeding zeros "+":"+query);
                    }
            		try{
                        tempRow.put(cols[i].trim(),Integer.parseInt(vals[i].trim()));
                        if(ColVals.get(ColNames.indexOf(cols[i].trim())).equals("autointeger")){
                            server.database().get(matcher.group(1)).setAutoMax((ColNames.get(ColNames.indexOf(cols[i].trim())).toString()), Integer.parseInt(vals[i].trim()));
                        }
                    }
                    catch(NumberFormatException e){
                        return new Response(false, "Invalid Value for "+cols[i].trim()+", "+vals[i].trim()+"is invalid"+":"+query);
                    }
                }
                else{
                	if(vals[i].trim().startsWith("0") && !vals[i].trim().equals("0")){
                    	return new Response(false, "Integers cannot having preceeding zeros "+":"+query);
                    }
                	try{
                        tempRow.put(cols[i].trim(),Integer.parseInt(vals[i].trim()));
                        if(ColVals.get(ColNames.indexOf(cols[i].trim())).equals("autointeger")){
                            server.database().get(matcher.group(1)).setAutoMax((ColNames.get(ColNames.indexOf(cols[i].trim())).toString()), Integer.parseInt(vals[i].trim()));
                        }
                    }
                    catch(NumberFormatException e){
                        return new Response(false, "Invalid Value for "+cols[i].trim()+", "+vals[i].trim()+"is invalid"+":"+query);
                    }
                }
                
            }
            //String
            else if(ColVals.get(ColNames.indexOf(cols[i].trim())).equals("string") && vals[i].trim().startsWith("\"") && vals[i].trim().endsWith("\"")){
            	if(vals[i].trim().length() >= 3)
            		tempRow.put(cols[i].trim(),vals[i].trim().subSequence(1, vals[i].trim().length()-1));
             	if(vals[i].trim().equals("\"\"")){
             		tempRow.put(cols[i].trim(),"");    
             	}
            }
            else{
            	return new Response(false,"The data type of "+vals[i]+ " does not match with " +cols[i]+":"+query);
            }
        }
            String PCName = server.database().get(matcher.group(1)).get(null).get("primary_column_name").toString();
            String PCVal = "";
            if(tempRow.containsKey(PCName)){
                PCVal = tempRow.get(PCName).toString();
            }
            tempTable.put(PCVal , tempRow);
            if(server.database().get(matcher.group(1)).containsKey(PCVal)){
                return new Response(false, "Different rows cannot have the same primary column value"+":"+query);
            }

		//Check for Auto Integers

	    if(auto = true){
		ArrayList<Integer> autoIndex = new ArrayList<Integer>();
                ArrayList<String>ColVals = new ArrayList((ArrayList) server.database().get(matcher.group(1).trim()).get(null).get("column_types"));
                ArrayList<String>ColNames = new ArrayList((ArrayList) server.database().get(matcher.group(1).trim()).get(null).get("column_names"));
                //Find highest integer
                
                //Check for instances of autointegers in column_types
                for(int z = 0; z < ColVals.size(); z++){
                    if(ColVals.get(z).equals("autointeger"))
                        autoIndex.add(z);
                }
                //for loop to assign values to autointegers that are unmapped or null
                for(int z = 0; z < autoIndex.size(); z++){                    
                        max = server.database().get(matcher.group(1)).getAutoMax(ColNames.get(autoIndex.get(z)));
                        tempRow.put(ColNames.get(autoIndex.get(z)), max);
                        max++;
                        server.database().get(matcher.group(1)).setAutoMax(ColNames.get(autoIndex.get(z)),max);
                        intExist=true;
                }
	    }

		//Insert into Table
            server.database().get(matcher.group(1)).put(PCVal, tempRow);
            


            Row schemaRow = new Row();
            //Recreating Schema
            schemaRow.put("table_name", null);
            schemaRow.put("primary_column_name", server.database().get(matcher.group(1)).get(null).get("primary_column_name"));
            schemaRow.put("column_names", server.database().get(matcher.group(1)).get(null).get("column_names"));
            schemaRow.put("column_types", server.database().get(matcher.group(1)).get(null).get("column_types"));
            		
            //Insert Computed Schema		
            tempTable.put(null, schemaRow);
            
        return new Response(true, "" + matcher.group(1) + " modified with " + cols.length + " columns created for row: " + PCVal +":"+query, tempTable);
        
	}
}	
