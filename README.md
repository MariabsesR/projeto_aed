O Wordle é um jogo de palavras que se tornou muito conhecido em 2022. Se não conhecem, experimentem aqui para aprender as regras.

Pretende-se que implementem uma classe que jogue o Wordle.

Para tal têm disponíveis algumas classes auxiliares (cf. código abaixo):

    Report - um enumerado com as respostas possíveis de comparação de letras
    Attempt - uma classe para guardar o resultado de propor uma palavra
    Oracle - uma classe que tem a palavra secreta e devolve um objeto Attempt após propormos uma palavra

Existem dois ficheiros disponíveis

    words.txt, um dicionário de palavras (uma palavra por linha)
    frequencies.txt, uma lista das frequências de cada letra do alfabeto. O início do ficheiro é algo como

e 0,124168  (ie, cada texto é composto por ~12.4% de e's)
t 0,096921
a 0,082003
i 0,076806

O valor de uma palavra é calculada da seguinte forma: somar as frequências de cada letra e multiplicar pelo número de letras únicas da palavra. Por exemplo, o valor de 'crane' é 1.919150 e de 'banana' é 1.228437.

Podem aceder aos ficheiros usando a classe Scanner, por exemplo,

     Scanner sc = new Scanner(new File("words.txt"));

 

O funcionamento da classe Wordle deve seguir os seguintes requisitos:

    O construtor recebe um objeto Oracle (com a palavra escondida), e deve ainda ler as frequências do ficheiro bem como as palavras do ficheiro dicionário mas apenas aquelas que tenham o mesmo tamanho da palavra escondida.
    O dicionário deve ser guardado numa lista de strings
    As frequências devem ser guardadas num HashMap<Character, Double>
    A próxima palavra a sugerir deve ser aquela com o maior valor possível, e que seja compatível com todas as tentativas feitas até ao momento. Usem uma PriorityQueue<String> para guardar as palavras compatíveis por ordem de valor.
    Isto significa que devem guardar as tentativas feitas até ao momento, por exemplo, numa lista de objetos Attempt.
    O método play() deverá seguir este algoritmo: ir sugerindo palavras até que haja uma sugestão que acerte na palavra escondida. O método então deve devolver uma lista com todas as palavras sugeridas desde o início.

For example:
Test 	Result

Wordle wordle = new Wordle(new Oracle("--"));
System.out.println(String.format("%.6f", wordle.value("crane")));
System.out.println(String.format("%.6f", wordle.value("banana")));

	

1.919150
1.228437

Wordle wordle = new Wordle(new Oracle("strong"));
System.out.println(wordle.play());

	

[sainte, strond, strong]

Wordle wordle = new Wordle(new Oracle("reality"));
System.out.println(wordle.play());

	

[aeonist, meatier, reality]

Wordle wordle = new Wordle(new Oracle("--"));
System.out.println(String.format("%.6f", wordle.value("crane")));
System.out.println(String.format("%.6f", wordle.value("banana")));

	

1.919150
1.228437

	

1.919150
1.228437

	
	

Wordle wordle = new Wordle(new Oracle("strong"));
System.out.println(wordle.play());

	

[sainte, strond, strong]

	

[sainte, strond, strong]

	
	

Wordle wordle = new Wordle(new Oracle("reality"));
System.out.println(wordle.play());

	

[aeonist, meatier, reality]

	

[aeonist, meatier, reality]

	
	

Wordle wordle = new Wordle(new Oracle("consistent"));
System.out.println(wordle.play());

	

[arsenolite, impotences, coexistent, consistent]

	

[arsenolite, impotences, coexistent, consistent]

	
	

Wordle wordle = new Wordle(new Oracle("of"));
System.out.println(wordle.play());

	

[et, ai, no, os, or, ol, od, oh, oc, ou, om, of]

	

[et, ai, no, os, or, ol, od, oh, oc, ou, om, of]

	
	

Wordle wordle = new Wordle(new Oracle("--"));
Oracle o = new Oracle("test");
Attempt a = o.checkGuess("test", "test");
System.out.println(wordle.isSolution(a));

	

true

	

true

	
	

Wordle wordle = new Wordle(new Oracle("--"));
Oracle o = new Oracle("test");
Attempt a = o.checkGuess("tset", "test");
System.out.println(wordle.isSolution(a));

	

false

	

false
