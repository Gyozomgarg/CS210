package core;

import adt.Database;
import adt.Response;
import adt.Table;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

/** 
 * This class implements an interactive console
 * for a database server.
 * 
 * Finishing implementing the required features
 * but do not modify the protocols.
 */

/** 
 Test Insert for Module 7
    create table test_table (primary string ps, autointeger a1, autointeger a2); 
    insert into test_table (ps, a1) values ("p1", 7);
    alter table test_table insert column boolean b1 first;
    alter table test_table insert column boolean b2 last;
    alter table test_table insert column boolean b3 after ps;
    alter table test_table rename column a1 a2;
    alter table test_table drop column b3;
    alter table test_table rename to table1;
    dump table test_table;
    dump table table1;
    insert into table1 (ps) values ("p2");
    insert into table1 (ps,a2) values ("p3",17);
    insert into table1 (ps) values ("p4");
    dump table table1;
 **/

public class Console {
	private static final Exception FileNotFoundException = null;

	public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException, Exception{
            prompt(new Server(), System.in, System.out);
            
	}
	
	public static void prompt (Server server, InputStream input, OutputStream output) throws Exception {
            Scanner in = new Scanner(input);
            PrintStream out = new PrintStream(output);
            boolean tabularView = false;
/**
         out.print("Was the Server Close unexpectedly?\nYes or No?\n>> ");
            String temp = in.nextLine();
            if(temp.toLowerCase().trim().equals("yes")){
            ObjectInputStream stream = null;
            FileInputStream file = null;
            try{            	
                file = new FileInputStream("saves.txt");
                stream = new ObjectInputStream(file);
                if(stream.available()>0){
                	server.interpret(stream.readObject().toString());
                	out.print("QuickSave Loaded\n");
                }
                else{
                	out.print("No QuickSave Loaded\n");
                }
             }
             catch(FileNotFoundException e){
            	out.print("No QuickSave Loaded\n"); 
             }

             try{
                stream = null;
                file = null;
                file = new FileInputStream("database.txt");
                stream = new ObjectInputStream(file);
                if(stream.available()>0){
                	server.db = (Database) stream.readObject();
                	out.print("Save Loaded\n");
                }
                else{
                	out.print("No Save Loaded\n");
                }
             }
             catch(FileNotFoundException e){
            	 out.print("No Save Loaded\n");
             }
**/
            boolean running = true;
            while(running){
	            out.print("Please Enter Query\n>> ");
	            String line = in.nextLine();
                    if(line.equals(""))
                        line = in.nextLine();
	
	            List<Response> responses = server.interpret(line);
	            for(int i = 0; i < responses.size(); i++){
	                    out.println("Success: " + responses.get(i).success());
	                    out.println("Message: " + responses.get(i).message());
	                    if(tabularView == false)
	                        out.println("Table:   " + responses.get(i).table());
	                    else
	                        out.println("Table: " + tabularView(responses.get(i).table()));
	                    out.println("---------------------------------------------------------------------------");
	            }
	            Response exit = new Response(true, "Exiting Now");
	            if(responses.size() != 0 && responses.get(responses.size()-1).message().equals("Exiting Now") && responses.get(responses.size()-1).success() == true){
	                    break;
	            }	
            }            
        }
        
        public static String tabularView(Table table){
            if(table == null){
                return null;
            }
            String toPrint = new String();
            toPrint += "\n";
            String fullWidth = new String();
            Object[] keySet = table.keySet().toArray();
            if(table.entrySet().toArray().length ==1 || table.equals(null))
                return "The table "+table.get(null).get("table_name")+" is empty";
            try{
                if(!table.get(null).containsValue(null)){
                String temp = table.get(null).get("table_name").toString();
                if(temp.length()>15){
                    temp = temp.substring(0,11);
                    temp += "...";
                }
                toPrint += "____________\n";
                toPrint += ("|"+temp+"|\n");
            }
            }
            catch(NullPointerException e){
                
            }
            ArrayList ColNames = new ArrayList((ArrayList) table.get(null).get("column_names"));
            ArrayList ColTypes = new ArrayList((ArrayList) table.get(null).get("column_types"));
            for(int i =0; i<((ColNames.size()+1)*15+(ColNames.size()+1));i++){
                fullWidth +="_";
            }
            toPrint += fullWidth+"\n";
            //Creating Header Row which I named Legend but don't want to change right now
            String legend = new String();
            legend += "|Key Value     |";
            String columnthing = new String();
            for(int i =0;i<ColNames.size();i++){
                columnthing = new String();
                if(ColNames.get(i).equals(table.get(null).get("primary_column_name"))){
                    columnthing = "*"+table.get(null).get("primary_column_name").toString();
                }
                else
                    columnthing += ColNames.get(i);
                if(columnthing.length()>15){
                    columnthing = columnthing.substring(0,11);
                    columnthing += "...";
                }
                if(columnthing.length()<15){
                    do{
                        columnthing += " ";
                    }while(columnthing.length()<15);
                }
                columnthing += "|";
                legend += columnthing;
            }
            toPrint += legend +"\n";
            toPrint += fullWidth+"\n";
            //Now to put them juicy rows into the table
            for(int i = 0; i<keySet.length; i++){
                try{
                    keySet[i].toString();
                }
                catch(NullPointerException e){
                    continue;
                }
                
                String rowToPrint = new String();
                rowToPrint += "|";
                    rowToPrint += keySet[i].toString();
                if(rowToPrint.length()>15){
                    rowToPrint = rowToPrint.substring(0,11);
                    rowToPrint += "...\"";
                }
                if(rowToPrint.length()>15){
                    rowToPrint = rowToPrint.substring(0,12);
                    rowToPrint += "...";
                }
                if(rowToPrint.length()<15){
                    do{
                        rowToPrint += " ";
                    }while(rowToPrint.length()<15);
                }
                rowToPrint += "|";
                for(int j = 0; j<ColTypes.size(); j++){
                    String row = new String();
                    if(table.get(keySet[i].toString()).get(ColNames.get(j)) != null){
                        row += table.get(keySet[i].toString()).get(ColNames.get(j));
                        if(ColTypes.get(j).equals("string")){
                            row = "\""+row+"\"";
                        }
                    }else
                        row += "          ";
                    if(row.length()>15){
                        row = row.substring(0,11);
                        row += "...";
                    }
                    if(row.length()<15){
                        do{
                            row += " ";
                        }while(row.length()<15);
                    }
                    row += "|";
                    rowToPrint += row;
                }
                rowToPrint += "\n";
                toPrint += rowToPrint;
            }
            toPrint += fullWidth;
            
            return toPrint;
        } 
}	
