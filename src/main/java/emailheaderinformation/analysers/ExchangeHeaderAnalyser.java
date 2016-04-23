package emailheaderinformation.analysers;

import java.util.Set;

import emailheaderinformation.MainWindow;
import emailheaderinformation.model.Header;

public class ExchangeHeaderAnalyser extends HeaderAnalyser {

	public ExchangeHeaderAnalyser(Header header, MainWindow mainWindow) {
		super(header, mainWindow);
	}

	@Override
	public void run() {
		Set<String> keySet = mHeader.getFields().keySet();
		boolean hasExchangeEntry = false;
		for (String key : keySet) {
			if(key.startsWith("X-MS-Exchange")) {
				Object[] arr = {"Exchange", key, "Yes", mHeader.getFields().get(key) };
				mMainWindow.addToTable(arr);
			}
		}
	}
}
