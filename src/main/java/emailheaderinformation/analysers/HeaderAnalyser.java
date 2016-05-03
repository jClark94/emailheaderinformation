/**
 *
 */
package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

/**
 * @author joshua
 */
abstract public class HeaderAnalyser implements Runnable {
    final Header mHeader;
    final MainWindow mMainWindow;

    public HeaderAnalyser (Header header, MainWindow mainWindow) {
        mHeader = header;
        mMainWindow = mainWindow;
    }
}
