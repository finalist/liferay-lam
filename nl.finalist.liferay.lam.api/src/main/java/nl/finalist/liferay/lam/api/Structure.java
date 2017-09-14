package nl.finalist.liferay.lam.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

public interface Structure {

	void createStructure(File structureJson) throws FileNotFoundException, IOException, ParseException;
}
