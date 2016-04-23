package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 11/03/16.
 */
public class CveAnalyser extends HeaderAnalyser {
    private List<String> mSearchTerms;

    public CveAnalyser(Header header, MainWindow mainWindow, List<String> searchTerms) {
        super(header, mainWindow);
        mSearchTerms = searchTerms;
    }
    public void run () {
        ArrayList<String> command = new ArrayList<String>();
    }
}
