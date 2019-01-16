import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class DriverClass {

	public static void main(String[] args) {
//		System.out.println("File name ? ");
//		Scanner scanner = new Scanner(System.in);
//		String fileName = scanner.nextLine();
//		scanner.close();
		String fileName = "input.txt"; 

		try {
			List<String> inputLines = readTextFile(fileName);
			List<String> outputLines = Conversion.convertToHTML(inputLines);
			for (String line : outputLines) {
				System.out.println(line);
			}
			writeTextFile(outputLines, "output.txt");
		} catch (IOException e) {
			System.err.println("File Path : "+new File("input.txt").getAbsolutePath());
			System.err.println(e.getMessage() + "\nError : File not found !");
		}
	}

	static List<String> readTextFile(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllLines(path, StandardCharsets.UTF_8);
	}

	static void writeTextFile(List<String> strLines, String fileName) throws IOException {
		Path path = Paths.get(fileName);
		Files.write(path, strLines, StandardCharsets.UTF_8);
	}
}
