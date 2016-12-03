package adt;

import java.io.Serializable;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import adt.HashMap;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import adt.Row;

/** 
 * This class is a hash map alias providing
 * a Primary Key Value -> Row mapping.
 * 
 * Additional non-map features can be implemented.
 * Schema requirements are defined in the project.
 */
public class Table extends HashMap<Object, Row> implements Serializable{

	public Row temp = new Row();

	public Table() {
		super();	
	}

	public Table(Table table) {
		super(table);

	}
	
	//Create ArrayList for maximum values of auto integer in node form <ColName, Max>
	
	public void newAutoMax(String colName){
		temp.put(colName, 0);
		
	}

	
	public void setAutoMax(String colName, int value){
		if(value > (int) temp.get(colName))
            temp.put(colName, value);
	}
	
	public int getAutoMax(String colName){
		return (int) temp.get(colName);
	}
        
        public Row getAutoMaxCon(){
            return temp;
        }
        
        public void setAutoMaxCon(Row temp2){
            temp = new Row (temp2);
        }


	// stream.writeObject
        public void writeObject(ObjectOutputStream stream) throws IOException{
            stream.writeObject("nbailey_table ");
            stream.writeObject(this.get(null).get("table_name")+" ");
            stream.writeObject(this.get(null).get("primary_column_name")+" ");    
            //Store column_name & column_type size
            ArrayList<String> names = (ArrayList) this.get(null).get("column_names");
            ArrayList<String> types = (ArrayList) this.get(null).get("column_types");
            int size = names.size();
            stream.writeInt(size);
            //Store Column Names
            for(int i = 0; i<names.size(); i++){
                stream.writeObject(names.get(i));
            }
            //Store Column Types
            for(int i = 0; i<types.size(); i++){
                stream.writeObject(types.get(i));
            }
            //Get KeySet to get all row names
            Object[] keySet = this.keySet().toArray();
            //Store Amount of Rows
            stream.writeInt(keySet.length);
            //Store Key Values
            for(int i = 0; i<keySet.length; i++){
                Object[] values = this.get(keySet[i]).values().toArray();
                for(int j=0; j<names.size(); j++){
                    if(values[j] instanceof Integer){
                        stream.writeInt((Integer) values[j]);
                        continue;
                    }
                    if(values[j] instanceof Boolean){
                        stream.writeBoolean((Boolean) values[j]);
                        continue;
                    }
                    stream.writeObject(values[j]);
                }
            }
	}
	public Table readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
            Table toReturn = new Table();
            Row schema = new Row();
            String temp = stream.readObject().toString();
            if(!temp.equals("nbailey_table")){
                System.out.println("The File does not contain the correct table");
                return null;
            }    
            schema.put("table_name", stream.readObject().toString().trim());
            schema.put("primary_column_name", stream.readObject().toString().trim());
            String pcn = schema.get("primary_column_name").toString();
            //Create ArrayList of Names
            int size = Integer.parseInt(stream.readObject().toString().trim());
            ArrayList<String> names = new ArrayList();
            for(int i = 0; i<size; i++){
                names.add(stream.readObject().toString().trim());
            }
            schema.put("column_names", names);
            //Create ArrayList of Types
            ArrayList<String> types = new ArrayList();
            for(int i = 0; i<size; i++){
                types.add(stream.readObject().toString().trim());
            }
            schema.put("column_types", types);
            //Store schema in computed table
            toReturn.put(null, schema);
            //Store Values of Rows
            int rows = Integer.parseInt(stream.readObject().toString().trim());
            for(int i = 0; i<rows; i++){
                Row create = new Row();
                for(int j = 0; j<size;j++){
                    if(types.get(j).equals("integer")){
                        create.put(names.get(j), stream.readInt());
                        continue;
                    }
                    if(types.get(j).equals("boolean")){
                        create.put(names.get(j),stream.readBoolean());
                        continue;
                    }
                    if(types.get(j).equals("string")){
                        create.put(names.get(j), stream.readObject().toString().trim());
                    }
                }
                toReturn.put(create.get(pcn).toString().trim(), create);
            }
            
            return toReturn;
	}
}
