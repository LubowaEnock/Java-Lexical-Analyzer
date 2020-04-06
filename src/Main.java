/*
 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String data = "";
		List<String> allLines = new ArrayList();

		Path path = Paths.get("/home/lubowa/eclipse-workspace/Lexical Analyzer/src/data/program.txt");
		try {
			byte[] bytes = Files.readAllBytes(path);
			allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		/*
		try {
		data = new String(Files.readAllBytes(
				Paths.get("/home/lubowa/eclipse-workspace/Lexical Analyzer/src/data/program.txt")));
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("Failed to read file "+e);
	
		} 
		*/
		

		Main main = new Main();
		data = String.join(", ", allLines);

		main.parse(data);

		
	}
	
	//Returns true if the character is a letter
	private boolean isLetter(char c) {
		String letters="qwertyuiopasdfghjklzxcvbnm";
		if(letters.indexOf(c)>=0) {
			return true;
		}
		return false;
	}
	
	
	
	// Returns 'true' if the character is a DELIMITER. 
	private boolean isSpecialCharacter(char ch) {
		if (ch == '+' || ch == '-' || ch == '*' ||  
		        ch == '/' || ch == ',' || ch == ';' || ch == '>' ||  ch == '.' ||
		        ch == '<' || ch == '=' || ch == '(' || ch == ')' ||  
		        ch == '[' || ch == ']' || ch == '{' || ch == '}' || ch == ';' || ch == '%') {
	        return (true); 

		}
		
		return (false); 
		
	}
	
	private boolean isOperator(char ch) { 
	    if (ch == '+' || ch == '-' || ch == '*' ||  
	        ch == '/' || ch == '>' || ch == '<' ||  
	        ch == '=' || ch == '%') {
	        return true; 
	    }
	    return false; 
	} 
	  
	// Returns 'true' if the string is a VALID IDENTIFIER. 
	private boolean isIdentifier(String str) { 
		char first_char = str.charAt(0);
		
		if(isLetter(first_char) || first_char == '_' || first_char == '$') {
			return true;	
		}	    
		return false; 
	} 
	  
	// Returns 'true' if the string is a KEYWORD. (Only some of the keywords are identified) 
	private boolean isKeyword(String str) { 

		if(str.equals("abstract") || str.equals("boolean") || str.equals("byte") || 
				str.equals("break") || str.equals("class") || str.equals("case") || 
				str.equals("catch") || str.equals("char") || str.equals("continue") || 
				str.equals("default") || str.equals("do") || str.equals("double") || 
				str.equals("else") || str.equals("extends") || str.equals("final") || 
				str.equals("finally") || str.equals("float") || str.equals("for") || 
				str.equals("if") || str.equals("implements") || str.equals("import") || 
				str.equals("instanceof") || str.equals("int") || str.equals("interface") || 
				str.equals("native") || str.equals("new") || str.equals("long") || 
				str.equals("package") || str.equals("private") || str.equals("protected") ||
				str.equals("public") || str.equals("return") || str.equals("static") ||
				str.equals("goto") || str.equals("synchronized") || str.equals("super") ||
				str.equals("short") || str.equals("switch") || str.equals("this") ||
				str.equals("throws") || str.equals("throw") || str.equals("transient") || 
				str.equals("try") || str.equals("void") || str.equals("volatile") ||
				str.equals("while") || str.equals("assert") || str.equals("const") ||
				str.equals("enum") || str.equals("strictfp") || str.equals("trim") || 
				str.equals("String") || str.equals("return") || str.equals("false") ||
				str.equals("true")) {
			return true;
		}
		
	    return false; 
	} 
	  
	
	
	
	
	// Parsing the input STRING. 
	private void parse(String str) {
		List<String> words = new ArrayList();
		List<Character> special_characters = new ArrayList();
		List<String> keywords = new ArrayList();
		List<String> identifiers = new ArrayList();
		List<String> literals = new ArrayList();
		List<Character> operators = new ArrayList();
		HashMap<String,String> table = new HashMap<String, String>();
		String strNew = str;
		
		//Remove all operators
	    for(int i = 0; i<strNew.length();i++) {
	    	if(isOperator(strNew.charAt(i))) {
	    		operators.add(strNew.charAt(i));
	    		table.put(String.valueOf(strNew.charAt(i)),"Operator");
	    		strNew = strNew.replace(String.valueOf(strNew.charAt(i)), " ");
	    	}
	    }
	    
	    //Remove all special characters
	    for(int i = 0; i<strNew.length();i++) {
	    	if(isSpecialCharacter(strNew.charAt(i))) {
	    		special_characters.add(strNew.charAt(i));
	    		table.put(String.valueOf(strNew.charAt(i)),"Special character");
	    		strNew = strNew.replace(String.valueOf(strNew.charAt(i)), " ");
	    	}
	    }
	    
	    
	    Pattern pattern = Pattern.compile("\\s");
	    Matcher matcher = pattern.matcher(strNew);
	    boolean found = matcher.find();
	    //Check if string contains more than one token
	    if(found) {
	    	//Split the string into a list 
	    	String[] elements = strNew.split(" ");
			for(String string:elements) {
				
				words.add(string.trim());
			}
			//loop through all words and get their tokens
			for(String string:words) {
				if(isKeyword(string)) {
					keywords.add(string);
					table.put(string, "keyword");
				}
				else {
					if(!(string.trim().equals(""))) {
						if(isIdentifier(string)) {
							identifiers.add(string);
							table.put(string, "identifier");
						}
						else {
							literals.add(string);
							table.put(string, "literal");
						}
					}
				}
			}
	    }
	    else {
	    	//Ensure it's not white space
	    	if(!(strNew.trim().equals(""))) {
	    		//check for its token
		    	if(isKeyword(strNew)) {
					keywords.add(strNew);
					table.put(strNew, "keyword");
				}
				else if(isIdentifier(strNew)) {
					identifiers.add(strNew);
					table.put(strNew, "Identifier");
				}
				else {
					literals.add(strNew);
					table.put(strNew, "Literal");
				}
	    	}
	    	
	    }
	    System.out.println("Operators are "+operators);
	    System.out.println("Special characters are "+special_characters);
	    System.out.println("Keywords are "+keywords);
	    System.out.println("Identifiers are "+identifiers);
	    System.out.println("Literals are "+literals);
	    
	    System.out.println("\n\nSymbol table");
	    System.out.println("Lexeme \t\t\t\t\t\t\t\t\t\tToken\n\n");
	    for (String i : table.keySet()) {
	    	  System.out.print(i+"\t\t\t\t\t\t\t\t\t");
	    	  System.out.println(table.get(i));
	    	}

	    
	} 
	
	
	
}
	
