package core;

import driver.*;
import adt.Response;
import adt.Database;
import adt.Table;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.List;
import java.util.ArrayList;

/** 
 * This class implements a server with an active
 * connection to a backing database.
 * 
 * Finishing implementing the required features
 * but do not modify the protocols.
 */
public class Server {
	
	Database db = new Database();
	
	public Server() {
		// TODO: Instantiate a non-static database field.
	}
	
	public Database database() {	
		return db;
	}
	
	public List<Response> interpret(String script) throws FileNotFoundException, IOException {
				
                String[] commands = script.split(";");
                List<Response> responses = new ArrayList<Response>();
                String saves = "";
                ObjectOutputStream stream = null;
                FileOutputStream file = null;
                file = new FileOutputStream("saves.txt");
                stream = new ObjectOutputStream(file);
                for(int i = 0; i < commands.length; i++){
                    String query = commands[i];
                    
                    if(query.trim().equals(""))
                        continue;
                    
                    Driver CreateTable = new DCreateTable();
                    Response CreateTable_response = CreateTable.execute(this, query);
                    if (CreateTable_response != null) {                            
                            responses.add(CreateTable_response);
                            stream.writeObject(script);
                            continue;
                    }
                    
                    Driver DropTable = new DDropTable();
                    Response DropTable_response = DropTable.execute(this, query);
                    if (DropTable_response != null) {
                            responses.add(DropTable_response);
                            stream.writeObject(script);
                            continue;
                    }
                    
                    Driver ShowTable = new DShowTable();
                    Response ShowTable_response = ShowTable.execute(this, query);
                    if (ShowTable_response != null) {
                            responses.add(ShowTable_response);
                            continue;
                    }
                    
                    Driver InsertTable = new DInsertIntoTable();
                    Response InsertTable_Response = InsertTable.execute(this, query);
                    if(InsertTable_Response != null){
                        responses.add(InsertTable_Response);
                        stream.writeObject(script);
                        continue;
                    }
                    
                    Driver DumpTable = new DDumpTable();
                    Response DumpTable_Response = DumpTable.execute(this, query);
                    if(DumpTable_Response != null){
                        responses.add(DumpTable_Response);
                        continue;
                    }
                    
                    Driver SelectTable = new DSelect();
                    Response SelectTable_Response = SelectTable.execute(this, query);
                    if(SelectTable_Response != null){
                        responses.add(SelectTable_Response);
                        continue;
                    }
                    
                    Driver ExportTable = new DExport();
                    Response ExportTable_Response = ExportTable.execute(this, query);
                    if(ExportTable_Response != null){
                        responses.add(ExportTable_Response);
                        continue;
                    }
                    
                    Driver ImportTable = new DImport();
                    Response ImportTable_Response = ImportTable.execute(this, query);
                    if(ImportTable_Response != null){
                        responses.add(ImportTable_Response);
                        continue;
                    }
                    
                    Driver AlterTable = new DAlterTable();
                    Response AlterTable_Response = AlterTable.execute(this, query);
                    if(AlterTable_Response != null){
                        responses.add(AlterTable_Response);
                        continue;
                    }                    
                    
                    if(commands[i].trim().toLowerCase().equals("exit")){
                    	responses.add(new Response(true ,"Exiting Now"));
                        serialize(db);
                        return responses;
                    }
                    
                    responses.add(new Response(false, "Typographical Error"));
                    
                }
                
                
		
		/*
		 * TODO: This manually passes the single query
		 * to each driver until it finds one that works.
		 * It would be much better to encapsulate all
		 * drivers into a single collection, then pass
		 * each query in sequence into each driver in
		 * sequence to find a working driver.
		 */
		
		
		return responses;
	}

    private void serialize(Database db) throws FileNotFoundException, IOException {
        ObjectOutputStream stream = null;
        FileOutputStream file = null;
        try {
                file = new FileOutputStream("database.txt");
                stream = new ObjectOutputStream(file);

                stream.writeObject(db);

                stream.close();
                file.close();
        }
	catch (IOException i) {
            i.printStackTrace();
        }
    }
}
