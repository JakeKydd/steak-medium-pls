package spinfo.minisearch.classes;

import java.io.File;
import java.util.Map;

import spinfo.minisearch.exeption.SearchEngineException;
import spinfo.minisearch.interfaces.IResult;

public class ResultClass implements IResult{

	@Override
	public int compareTo(IResult arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfOccurrences() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getContent() throws SearchEngineException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Integer> getHighlightedPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAbsoluteDocumentPath() {
		// This will change to the file object given by the IResult Object
		File file = new File("c:\\HaxLogs.txt");

		System.out.println("Absoluter Pfad : " + file.getAbsolutePath());
		return file.getAbsolutePath();
		
	}

	@Override
	public long getLastModified() {
		// TODO Auto-generated method stub
		// This will change to the file object given by the IResult Object
		File file = new File("c:\\HaxLogs.txt");

		System.out.println("Zuletzt geändert : " + file.lastModified());
		return file.lastModified();
	}

	@Override
	public long getSize() {
		// TODO Auto-generated method stub
		// This will change to the file object given by the IResult Object
		File file = new File("c:\\HaxLogs.txt");

		System.out.println("Datengröße in bytes : " + file.length());
		return file.length();
	}

}
