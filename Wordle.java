import java.io.*;
import java.util.*;

public class Wordle {

	private Oracle solution;
	
	private HashMap<Character, Double> frequencies;
	private PriorityQueue<String> words;
	public Attempt saveLastAttempt;
	private List<String> usedGuess;
	
	

	public Wordle(Oracle oracle)  {

		solution = oracle;
		
		frequencies = new HashMap<Character, Double>();
	
		words=  new PriorityQueue<String>(1, new comparatorFrequencies());
		usedGuess=  new ArrayList<String>();
	    saveLastAttempt=null;
	  
      
     try {
    	  Scanner sc = new Scanner(new File("frequencies.txt"));
    	
  		Character saveLetter = sc.next().charAt(0);
  		Double key = sc.nextDouble();
  		frequencies.put(saveLetter, key);
  		while (sc.hasNextLine()) {
  			
  			sc.nextLine();
  			saveLetter = sc.next().charAt(0);
  			key = sc.nextDouble();
  			frequencies.put(saveLetter, key);

  		}
  		 sc = new Scanner(new File("words.txt"));
  		while (sc.hasNextLine()) {
  			String testWord = sc.nextLine();
  			if (testWord.length() == solution.length())
  				words.add(testWord);
  		

  		}
     }catch (Exception e) {
		System.out.println("ficheirto nao encontrado");
	}
		
	
	}

	/**
	 * Predicado que valida se uma palavra guess é compatível com as palavras já
	 * experimentadas
	 */
	    public boolean isCompatible(String guess) { 
	    	 if(saveLastAttempt==null)return true;
	    
	    	for(int i =0; i<usedGuess.size();i++) {
	    		
	    	Report[]  test = solution.checkGuess(usedGuess.get(i)).getReport();
	       int currentChar=0;
	    	for(Report type:test) {
	    		if(type==Report.OK && guess.charAt(currentChar)!=usedGuess.get(i).charAt(currentChar) )
	    			return false;
	    		if(type==Report.SWAP && (guess.indexOf(usedGuess.get(i).charAt(currentChar))==-1 ||
	    				 guess.indexOf(usedGuess.get(i).charAt(currentChar))==currentChar ||
	    				 guess.lastIndexOf(usedGuess.get(i).charAt(currentChar))==currentChar))
	    			return false	;
	    		if(type==Report.NO &&  guess.indexOf(usedGuess.get(i).charAt(currentChar))!=-1)
	    			return false;
	    		currentChar++;
	    	}
	    	}
	    	return true;
		  
	   }

	/**
	 * Predicado que valida se a tentativa é a resposta certa (ie, tudo OKs)
	 */
	 public boolean isSolution(Attempt attempt) {
	
		Report[]  test =attempt.getReport();
		for(int i=0; i<test.length;i++) {
			if(test[i]!=Report.OK)return false;
		}
		return true;
	 }

	/**
	 * Devolve o valor de uma dada palavra. O valor é dado pela soma das frequências
	 * das suas letras vezes o número de letras únicas.
	 * 
	 */
	public double value(String word) {
		int numberOfUniqueChar=0;
		double totalValue = 0;
		for (int i = 0; i < word.length(); i++) {
			char test = word.charAt(i);
			int testForDuplicates = word.lastIndexOf(test);
			if ( testForDuplicates==i)
				numberOfUniqueChar++;
			
				totalValue += frequencies.get(word.charAt(i));
		}
		return totalValue*numberOfUniqueChar;
	}

	/**
	 * retorna a palavra com maior valor e que seja compatível com o historial de
	 * tentativas
	 */
	   public String getGuess() {
		  
		   
		   for(int i =0;i<words.size();i++) {
			   String saveGuess =this.words.poll();
				
		
			   if(isCompatible(saveGuess)) {
				  
				  usedGuess.add(saveGuess);
			      return saveGuess;
			   }
		   }
		   return null;
	   }

	/**
	 * Joga uma partida de Wordle, devolvendo a lista de tentativas
	 */
	 public List<String> play() {
		 Attempt test = solution.checkGuess(getGuess());
		 saveLastAttempt= test;
		 while(isSolution(saveLastAttempt)==false) {
			 saveLastAttempt=solution.checkGuess(getGuess());
			 
			 
			 
		 }
		return usedGuess;
	 }


private class comparatorFrequencies implements Comparator<String>{

		@Override
		public int compare(String o1, String o2) {
	    if(Math.abs(  value(o1)-value(o2))>  0.000001) {
		if(value(o1)<value(o2))
			return 1 ;    //troquei os valoeres do que pede para dar do maior ao menor
		if(value(o1)>value(o2))
			return -1;
	    }
		
		return 0;
		
		}
	
}

}



/**
 * Enumerado com as três possíveis respostas quando se pergunta se uma letra
 * pertence à palavra que pretendemos adivinhar
 */
enum Report {
	OK, SWAP, NO
};

/**
 * Classe que guarda uma comparação entre palavras de igual tamanho. O array
 * report irá guardar no índice i o resultado de comparar as respetivas letras
 * do índice: OK se a letra está na posição correta, SWAP se a letra está noutra
 * posição, NO se a letra não existe.
 */
class Attempt {

	private String guess;
	private Report[] report;

	public Attempt(String guess, Report[] report) {
		this.guess = guess;
		this.report = report;
	}

	public String getGuess() {
		return guess;
	}

	public void setGuess(String guess) {
		this.guess = guess;
	}

	public Report[] getReport() {
		return report;
	}

	public void setReport(Report[] report) {
		this.report = report;
	}

	public boolean equals(Attempt other) {
		return guess.equals(other.guess) && Arrays.equals(report, other.report);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < guess.length(); i++) {
			sb.append(guess.charAt(i)).append(":");
			sb.append(report[i]).append("; ");
		}
		return sb.replace(sb.length() - 2, sb.length(), ".").toString();
	}
}

/**
 * Classe auxilar que é usada para o Wordle fazer perguntas sobre a palavra a
 * adivinhar
 */
class Oracle {
	private String secretWord;

	public Oracle(String secretWord) {
		this.secretWord = secretWord;
	}

	public int length() {
		return secretWord.length();
	}

	public Attempt checkGuess(String guess) {
		return checkGuess(secretWord, guess);
	}

	public Attempt checkGuess(String word, String guess) {
		Report[] report = new Report[guess.length()];

		for (int i = 0; i < guess.length(); i++) {
			if (guess.charAt(i) == word.charAt(i))
				report[i] = Report.OK;
			else if (word.indexOf(guess.charAt(i)) >= 0)
				report[i] = Report.SWAP;
			else
				report[i] = Report.NO;
		}

		return new Attempt(guess, report);
	}
}
