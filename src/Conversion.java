
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Conversion {

	private static String temp = new String();
	private static boolean isEmptyLine = false;
	private static boolean isParagraphCreated = false;
	private static boolean isListCreated = false;
	private static boolean isListItemCreated = false;
	private static String header = "<html>\n<head>\n<title>";
	private static String middle = "</title>\n</head>\n<body>";
	private static String footer = "</body>\n</html>";
	private static String TITLE_SEPARATOR = ":";
	private static String BOLD_1 = "\\*"; // <b>...</b>
	private static String BOLD_2 = "\\!"; // <b>...</b>
	private static String ITALICS = "\\_"; // <i>...</i>
	private static String UNDERLINE = "\\%"; // <u>...</u>
	private static String LIST = "-"; //

	public static List<String> convertToHTML(List<String> inputLines) {
		List<String> outputLines = new ArrayList<String>();
		String[] arrayOfWords;
		int lastIndex;

		String titleLine = inputLines.get(0);
		outputLines.add(header + titleLine.substring(titleLine.indexOf(TITLE_SEPARATOR) + 1).trim() + middle);
		inputLines.remove(0);
		if (!inputLines.get(0).startsWith(LIST)) {
			outputLines.add("<p>");
			isParagraphCreated = true;
		}
		for (int i = 0; i < inputLines.size(); i++) {
			String line = inputLines.get(i);
			if (line != null && line.trim().equals("")) { // this checks for empty white line
				if (isListCreated) {
					outputLines.add("</ul>");
					isListCreated = false;
				}
				if (isParagraphCreated && !outputLines.get(outputLines.size() - 1).startsWith("<p>")) {
					outputLines.add("</p>");
					isParagraphCreated = false;
				}
				if (!isParagraphCreated && !isEmptyLine) {
					outputLines.add("<p>");
					isParagraphCreated = true;
				}
				isEmptyLine = true;
				continue;
			} else if (line.startsWith(LIST)) {
				if (isListCreated && isListItemCreated) {
					lastIndex = outputLines.size() - 1;
					outputLines.set(lastIndex, outputLines.get(lastIndex).concat("</li>"));
					isListItemCreated = false;
				} else {
					outputLines.add("<ul>");
					isListCreated = true;
				}
			} else if (isListCreated && isListItemCreated)  {
					lastIndex = outputLines.size() - 1;
					outputLines.set(lastIndex, outputLines.get(lastIndex).concat("</li>"));
					isListItemCreated = false;
					outputLines.add("</ul>");
					isListCreated = false;
			}
			isEmptyLine = false;
			arrayOfWords = line.trim().split(" ");
			for (int j = 0; j < arrayOfWords.length; j++) {
				temp = arrayOfWords[j];
				arrayOfWords[j] = convertWord(arrayOfWords[j], 0);
			}
			outputLines.add(String.join(" ", arrayOfWords));
		}
		
		
		lastIndex = outputLines.size() - 1;
		if (isListItemCreated) {
			outputLines.set(lastIndex, outputLines.get(lastIndex).concat("</li>"));
		}
		if (isListCreated) {
			outputLines.add("</ul>");
		}
		if (isParagraphCreated && outputLines.get(outputLines.size() - 1).startsWith("<p>")) {
			outputLines.remove(lastIndex);
		}else if(isParagraphCreated) {
			outputLines.add("</p>");
		}
		outputLines.add(footer);
		return outputLines;
	}

	public static String convertWord(String word, int index) {
		switch (temp.charAt(index)) {
		case '*':
			word = word.replaceFirst(BOLD_1, "<b>");
			break;
		case '!':
			word = word.replaceFirst(BOLD_2, "<b>").concat("</b>");
			break;
		case '_':
			word = word.replaceFirst(ITALICS, "<i>");
			break;
		case '%':
			word = word.replaceFirst(UNDERLINE, "<u>");
			break;
		case '-':
			word = word.replaceFirst(LIST, "<li>");
			isListItemCreated = true;
		}

		switch (temp.charAt(temp.length() - (index + 1))) {
		case '*':
			word = word.replaceFirst(BOLD_1, "</b>");
			break;
		case '_':
			word = word.replaceFirst(ITALICS, "</i>");
			break;
		case '%':
			word = word.replaceFirst(UNDERLINE, "</u>");
			break;
		default:
			break;
		}
		
		if (Pattern.matches(".*[\\*\\_\\!%].*", word)) {
			word = convertWord(word, ++index);
		}
		return word;
	}
}
