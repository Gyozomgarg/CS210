package driver;

import adt.Response;
import core.Server;

/** 
 * This interface stipulates the protocols
 * for query drivers.
 * 
 * Do not modify this interface.
 */
public interface Driver {
	public Response execute(Server server, String query);
}
