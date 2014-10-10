package vhdlOutputTests;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

/**
 * 
 */

/**
 * @author DINESH THANGAVEL
 *
 */
public class TestUtils {
	public static String OutputFolderDirectory = "D:\\Processor_Creator\\VhdlHelper\\vhdl_output";
	public static String BaselineFolderDirectory = "D:\\Semester7\\FYP\\SourceCode\\src\\test\\baselineResources";

	public static void clearFilesInOutputFolder() throws IOException{
		FileUtils.cleanDirectory(new File(OutputFolderDirectory));
	}
}
