package com.ryanliang.knockknock;

import java.util.List;

/**
 * KKModellable interface defines the interface(s) involving retrieving data from a source or saving data to a source. 
 * @author Ryan L.
 * @version $Revision$
 * @since 1.7
 */
public interface KKModellable {
	//public void setView(Viewable view);

	public List<KKJoke> getListOfKKJokes();

	
}
