package com.in28minutes.rest.webservices.resfulwebservices.first;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {
	
	private static Boolean DEBUG = true;
	Grammar grammar;
	
	
	@GetMapping("/first/file/{file}")
	public String calculateResult(@PathVariable String file) {
		
		// Define File Vars
		
		file = "input/" + file;
		
		String fileName = getInputName(file);

		// Get Raw Token Array
		String[] rawTokens = getInput(file).split("\\s+");

		// Init Token Array
		Token[] tokens = new Token[rawTokens.length];

		// Delete output file if exists
		deleteOutput(fileName);
		deleteOutput(fileName+".DEBUG");

		// DEBUG Section
		if (DEBUG) {
			// INPUT
			System.out.println("== INPUT ==");
			appendToOutput(fileName+".DEBUG","== INPUT ==\n");
			System.out.println(getInput(file));
			appendToOutput(fileName+".DEBUG",getInput(file)+"\n");
			// TOKEN
			System.out.println("== TOKENS ==");
			appendToOutput(fileName+".DEBUG","== TOKENS ==\n");
		}

		//Create Tokens Array (For Each Token in Raw Token Array DO:)
		int count = 0;
		for (String arg : rawTokens){
			// Get Token
			Token token = new Token(arg);
			// Allocate Token Onject to token array
			tokens[count] = token;
			// DEBUG Section
			if (DEBUG) {
				// Console Output
				token.oneline();
				// File Output
				appendToOutput(fileName+".DEBUG",token.token+" "+token.message+"\n");
			}
			// Increment Count
			count++;
		}

		// DEBUG Section
		if (DEBUG) {
			System.out.println("== GRAMMAR ==");
			appendToOutput(fileName+".DEBUG","== GRAMMAR ==\n");
		}

		// Init Grammar Array
		grammar = new Grammar(tokens);
		if (grammar.type == "ERROR") {
			System.out.println(grammar.message);
			appendToOutput(fileName,grammar.message);
			if (DEBUG) {
				appendToOutput(fileName+".DEBUG",grammar.message);
			}
			return "";
		} else {
			System.out.print(grammar.toString());
			// DEBUG Section
			if (DEBUG) {
				appendToOutput(fileName+".DEBUG",grammar.toString());
			}
		}


		return grammar.toString();
	}
	
	@GetMapping("/first/{nonterminal}")
	public String getFirst(@PathVariable String nonterminal) {

		// First Sets
		
		String firstString = "";
		
		for (Token nonTerminal : grammar.nonTerminals) {
			if (nonTerminal != null && nonTerminal.token.contentEquals(nonterminal)) {
				// For each Nonterminal get First Sets
				firstString = "FIRST(" + nonTerminal.token + ") = {";
				for (Token token : grammar.first(nonTerminal)) {
					if (token != null) {
						firstString += token.token + ", ";
					}
				}
				firstString += "}";
				firstString = firstString.replace(", }","}");
				// Output First Set
				System.out.println(firstString);
			}
		}
		
		return firstString;
	}
	
	
	// Delete output file
	protected static void deleteOutput(String file) {
		File f = new File("./output/"+file+".out");
		if(f.exists()){
			f.delete();
		}
	}


	// Append to output file
	protected static void appendToOutput(String file, String str) {
		try{
			File f = new File("./output/"+file+".out");
			if(!f.exists()){
				f.createNewFile();
			}
			FileWriter fileWritter = new FileWriter("./output/"+file+".out",true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(str);
			bufferWritter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	// Get input file name
	protected static String getInputName(String file) {
		File f = new File(file);
		return f.getName();
	}

	// File to String
	protected static String getInput(String file) {
		String result = null;
		DataInputStream in = null;

		try {
			File f = new File(file);
			byte[] buffer = new byte[(int) f.length()];
			in = new DataInputStream(new FileInputStream(f));
			in.readFully(buffer);
			result = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException("IO Error", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {}
		}
		return result;
	}
}
