/**
 * 
 */
package logic;

import helper.ProcGenException;

/**
 * @author DINESH THANGAVEL
 *
 */
public interface Command {
  public String execute(String arguments) throws ProcGenException;
}
