

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HelperFunctions {
	/** 
	 * Returns the number of lines in the file specified.
	 * @param string The location of the file.
	 * @return The number of lines in the file.
	 * @throws FileNotFoundException
	 */
	public static int countLines(String fileName) {
		int countRec=0;
		try {
			RandomAccessFile randFile = new RandomAccessFile(fileName,"r");
			long lastRec=randFile.length();
			randFile.close(); 
			FileReader fileRead = new FileReader(fileName);
			LineNumberReader lineRead = new LineNumberReader(fileRead);
			lineRead.skip(lastRec);
			countRec=lineRead.getLineNumber();
			fileRead.close();
			lineRead.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return -1;
		} 
		return countRec;
	}
	
	/**
	 * Returns the number of tokens in the file specified.
	 * @param file the file.
	 * @return the number of tokens in the file specified.
	 */
	public static int countTokens(String file) {
		try {
			int count = 0;
			FileInputStream fis = new FileInputStream(new File(file));
			Reader r = new BufferedReader(new InputStreamReader(fis));
			StreamTokenizer stok = new StreamTokenizer(r);
			stok.eolIsSignificant(false);
			int stokret = stok.nextToken();
			while(stokret != StreamTokenizer.TT_EOF) {
				count++;
				stokret = stok.nextToken();
			}
			return count;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Recursive delete file structure.  If file is passed, deletes the file.  If directory passed, it deletes the directory and its constants.
	 * @param dir the directory or file.
	 * @return
	 */
	public static boolean recursiveDelete(File dir) {
		if(dir.isDirectory()) {
			String[] children = dir.list();
			for(String s : children) {
				boolean success = recursiveDelete(new File(dir,s));
				if(!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * Sanitises a string representation of a file path.  Two differnet paths pointing to the same file wil lreturn the same path.
	 * This is used to convert relative to absolute, and to place instances of "/+" with "/".
	 * @param path A absolute or relative file path.
	 * @return An absolute file path.
	 */
	public static String sanetizePath(String path) {
		return new File(path).getAbsolutePath();
	}
	
	
	/**
	 * Returns the % similarity (by line) between two files.
	 * @param fileName1 The first file.
	 * @param fileName2 The second file.
	 * @return the similarity ratio, or a negative number if there was a file error.
	 * @throws FileNotFoundException 
	 */
	public static double getSimilarity(String fileName1, String fileName2) {
		int fileSize1 = HelperFunctions.countLines(fileName1);
		if(fileSize1 == -1) {
			return -100.0;
		}
		int fileSize2 = HelperFunctions.countLines(fileName2);
		if(fileSize2 == -1) {
			return -100.0;
		}
		String diffOutput=null; 
		double simValue=0.0;
		try {
			Process p = Runtime.getRuntime().exec("diff -bi " + fileName1 + " " + fileName2);
			int leftDistinct=0; 
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line = null;
			
			while ((line = in.readLine()) != null) {
				diffOutput = "\t\t" + diffOutput + line + "\n";
				if (line.substring(0,1).contains("<")) {
					leftDistinct++;
				}
			}          
			int sequenceLength = fileSize1 - leftDistinct;  
			simValue = (double) ((double)sequenceLength/((double)fileSize1 + (double)fileSize2 - (double) sequenceLength));
			p.getInputStream().close();
			p.getOutputStream().close();
			p.getErrorStream().close();
			p.destroy();
			in.close();
		}  
		catch (IOException e) {
			e.printStackTrace() ;
		}
		return simValue;
	}
	
	public static List<String> getDifferentLines(String original, String mutant) {
		List<String> retval = new ArrayList<String>();
		String changedLines = "";
		String addedLines = "";
		String deletedLines = "";
		
		Runtime runtime = Runtime.getRuntime();
		String stmt = "diff " + original + " " + mutant;
		Process process;
		try {
			process = runtime.exec(stmt);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = br.readLine();
			while(line != null) {
				if(!(line.startsWith("<") || line.startsWith(">") || line.startsWith("-"))) {
					
					if(line.contains("a")) {
						//System.out.println("add:" + line);
						Scanner s = new Scanner(line);
						s.useDelimiter("a");
						s.next();
						String data = s.next();
						if(data.contains(",")) {
							s = new Scanner(data);
							s.useDelimiter(",");
							int i = s.nextInt();
							int j = s.nextInt();
							for(int k = i; k <= j; k++) {
								addedLines = addedLines + k + ",";
							}
						} else {
							int i = Integer.parseInt(data);
							addedLines = addedLines + i + ",";
						}
					} else if (line.contains("c")) {
						Scanner s = new Scanner(line);
						s.useDelimiter("c");
						s.next();
						String data = s.next();
						if(data.contains(",")) {
							s = new Scanner(data);
							s.useDelimiter(",");
							int i = s.nextInt();
							int j = s.nextInt();
							for(int k = i; k <= j; k++) {
								changedLines = changedLines + k + ",";
							}
						} else {
							int i = Integer.parseInt(data);
							changedLines = changedLines + i + ",";
						}
					} else if (line.contains("d")) {
						Scanner s = new Scanner(line);
						s.useDelimiter("d");
						String data = s.next();
						if(data.contains(",")) {
							s = new Scanner(data);
							s.useDelimiter(",");
							int i = s.nextInt();
							int j = s.nextInt();
							for(int k = i; k <= j; k++) {
								deletedLines = deletedLines + k + ",";
							}
						} else {
							int i = Integer.parseInt(data);
							deletedLines = deletedLines + i + ",";
						}
						
					}
				}
				line = br.readLine();
			}
			process.destroy();
			retval.add(changedLines);
			retval.add(deletedLines);
			retval.add(addedLines);
			return retval;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Integer> getDifferentLinesInts(String infile, String outfile) {
		List<String> lines = getDifferentLines(infile,outfile);
		List<Integer> linesi = new ArrayList<Integer>();
		for(int i = 0; i < 3; i++) {
			String s = lines.get(i);
			Scanner scan = new Scanner(s);
			scan.useDelimiter(",");
			while(scan.hasNextInt()) {
				linesi.add(scan.nextInt());
			}
			scan.close();
		}
		return linesi;
	}
}
