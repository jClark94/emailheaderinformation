package emailheaderinformation.analysers;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

public class OxfordHeaderAnalyser extends HeaderAnalyser {

    public OxfordHeaderAnalyser (Header header, MainWindow mainWindow) {
        super(header, mainWindow);
    }

    @Override public void run () {
        if (mHeader.getFields().containsKey("X-Oxford-Username")) {
            Object[] arr = {
                    "Oxford", "Username", "Yes", mHeader.getFields().get("X-Oxford-Username")
            };
            mMainWindow.addToTable(arr);
            mMainWindow.getFoundInformation().addUsername("Oxford", mHeader.getFields().get(
                    "X-Oxford-Username"));
        }
    }
}
