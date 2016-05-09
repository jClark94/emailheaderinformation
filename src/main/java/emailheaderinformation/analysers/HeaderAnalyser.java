/**
 *
 */
package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.util.concurrent.Callable;

/**
 * @author joshua
 */
abstract public class HeaderAnalyser<V> implements Callable<V> {
    final Header mHeader;
    final MainWindow mMainWindow;

    public HeaderAnalyser (Header header, MainWindow mainWindow) {
        mHeader = header;
        mMainWindow = mainWindow;
    }
}
