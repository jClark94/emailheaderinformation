/**
 * 
 */
package emailheaderinformation.analysers;

import java.util.concurrent.Callable;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

/**
 * @author joshua
 * @param <V>
 *
 */
abstract public class HeaderAnalyser implements Runnable {
	final Header mHeader;
	final MainWindow mMainWindow;

	public HeaderAnalyser(Header header, MainWindow mainWindow) {
		mHeader = header;
		mMainWindow = mainWindow;
	}
}
