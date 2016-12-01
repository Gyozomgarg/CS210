package adt;

/** 
 * This class models a query response including
 * a success flag, message, and result table.
 * 
 * Do not modify this class.
 */
public final class Response {
	private final boolean success;
	private final String message;
	private final Table table;
	
	public Response(boolean success) {
		this.success = success;
		this.message = null;
		this.table = null;
	}
	
	public Response(boolean success, String message) {
		this.success = success;
		this.message = message;
		this.table = null;
	}
	
	public Response(boolean success, Table table) {
		this.success = success;
		this.message = null;
		this.table = table;
	}
	
	public Response(boolean success, String message, Table table) {
		this.success = success;
		this.message = message;
		this.table = table;
	}
	
	final public boolean success() {
		return success;
	}
	
	final public String message() {
		return message;
	}
	
	final public Table table() {
		return table;
	}
}