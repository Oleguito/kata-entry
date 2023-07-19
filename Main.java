import java.io.IOException;
import java.util.Scanner;

class Main {

	static String arabicNumerals[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String romanNumerals[] = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
	static int equivalents[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	public static void main(String args[]) throws IOException {
		System.out.println("Welcome to ARESOME CAWCULATOR!");
		System.out.println("Enter operands and operation ( + - * / ) SPACE-SEPARATED e.g. '2 + 3' or 'X - IV' (no quotes)");
		System.out.println("Limitations:");
		System.out.println("1) Only numbers 1 - 10 (I - X) are permitted (10 included, no zeros)");
		System.out.println("2) Only 1 of 4 operators, only 2 operands");
		System.out.println("3) Integer operands and  results only");
		System.out.println("4) No mixing Arabic and Roman numbers together, keep it consistent!");
		System.out.println();
		
		System.out.print("Input: ");
		Scanner in = new Scanner(System.in);
		String input = in.nextLine();
		System.out.println("Output: "+calc(input));
		in.close();
	}

	public static String calc(String input) throws IOException{
		/*
		 * Протриммить и разделить по пробелам
		 */
		String parsed[] = input.trim().split(" ");

		/* Если количество элементов не три, значит ошибка */
		if(parsed.length!=3){
			throw new IOException("Only two operands and one operator permitted");
		}

		/*
		 * Проверим оператор
		 */
		if(!isValidOperator(parsed[1])){
			throw new IOException("Operator must be one of ( + - * / )");
		}
		
		/* Если первый арабский то если второй арабский то считаем по арабски ЕСЛИ второй неверный ошибка
		 * ИНАЧЕ если первый римский то если второй римский то считаем по римски ЕСЛИ второй неверный ошибка
		 * иначе первый операнд неверный
		 */
		if(isValidInputArabic(parsed[0])){
			if(isValidInputArabic(parsed[2])){
				// System.out.println("Do Arabic stuff");
				/* 
				 *	rand от слова operand 
				 */ 
				int rand1 = equivalents[indexOf(arabicNumerals, parsed[0])];
				// System.out.println("rand1: "+rand1);
				int rand2 = equivalents[indexOf(arabicNumerals, parsed[2])];
				// System.out.println("rand2: "+rand2);
				int res=0;
				switch (parsed[1]) {
					case "+": {res = rand1 + rand2; break;}
					case "-": {res = rand1 - rand2; break;}
					case "*": {res = rand1 * rand2; break;}
					case "/": {res = rand1 / rand2; break;}
				}
				// System.out.println("Output: "+res);
				return String.valueOf(res);
			} else {
				throw new IOException("Second operand must also be a valid Arabic numeral, see Limitations for reference");
			}
		} else {
			if(isValidInputRoman(parsed[0])){
				if(isValidInputRoman(parsed[2])){
					// System.out.println("Do Roman stuff");
					int rand1 = equivalents[indexOf(romanNumerals, parsed[0])];
					// System.out.println("rand1: "+rand1);
					int rand2 = equivalents[indexOf(romanNumerals, parsed[2])];
					// System.out.println("rand2: "+rand2);
					int res=0;
					switch (parsed[1]) {
						case "+": {res = rand1 + rand2; break;}
						case "-": {res = rand1 - rand2; break;}
						case "*": {res = rand1 * rand2; break;}
						case "/": {res = rand1 / rand2; break;}
					}
					// System.out.println("ROMAN Output: "+checkConvert(res));
					return checkConvert(res);
				}else{
					throw new IOException("Second operand must also be a valid Roman numeral, see Limitations for reference");
				}
			} else {
				throw new IOException("First operand must be a valid Arabic or Roman numeral, see Limitations for reference");
			}
		}
	}

	static boolean isValidInputArabic(String value) {
		for(String s:arabicNumerals){
			if(value.equals(s)){
				return true;
			}
		}
		return false;
	}

	static boolean isValidInputRoman(String value) {
		for(String s:romanNumerals){
			if(value.equals(s)){
				return true;
			}
		}
		return false;
	}

	static int indexOf(String array[], String item){
		for (int i=0; i<array.length; i++){
			if(array[i].equals(item)) {
				return i;
			}
		}
		return -1;
	}

	static boolean isValidOperator(String value){
		switch (value) {
			case ("+"): {return true;}
			case ("-"): {return true;}
			case ("*"): {return true;}
			case ("/"): {return true;}
			default: {
				return false;
			}
		}
	}

	/*
	 * Если ту функцию вложить в эту получится как-то слишком громоздко,
	 * И так оставлять вроде тоже не комильфо
	 * я не знаю как поступать в таком случае
	 */
	static String checkConvert(int num){
		if (num<=0){
			throw new ArithmeticException("Roman number cannot be negative or zero");
		} else {
			if (num>0 && num<=10) {
				return romanNumerals[num-1];
			} else {
				return convert(num);
			}
		}
	}

	/* Собственно преобразование
	 * Я делю аргумент на 100 и вычитаю из аргумента 100 умноженное то что получилось
	 * Потом делю то что осталось от аргумента на 10 и вычитаю из него 10 * на результат деления
	 * В аргументе num остаются единицы
	 */
	static String convert(int num){
		String result="";
		int temps[] = {0, 0};
		int romanVals[] = {100, 10};
		for(int i=0; i<romanVals.length; i++){
			temps[i] = num / romanVals[i];
			if(num >= romanVals[i]) {
				num -= romanVals[i]*temps[i];
			}
			// System.out.println(romanVals[i]+": "+temps[i]+" num: "+num);
		}

		/* Мы не получим число больше 100 */
		if(temps[0]>0){
			return "C";
		}

		/* А здесь все остальные числа 
		 * Логика простая, если от 1 до 3 десятков то пишем Х, ХХ или ХХХ
		 * Если четыре то пишем XL
		 * Если пять, шесть семь или восемь то пишем V с ноль, один, два или три икса
		 * Если девять десяток, то пишем XC
		*/
		if(temps[1]>0 && temps[1]<4){
			for(int i=0; i<temps[1]; i++){
				result += "X";
			}
		} else if (temps[1]==4) {
			result += "XL";
		} else if (temps[1]>4 && temps[1]<9) {
			result += "L";
			for(int i=0; i<temps[1]-5; i++){
				result += "X";
			}
		} else {
			result += "XC";
		}

		/* Добавляем единицы если они есть */
		if(num!=0){
			result += romanNumerals[num-1];
		}
		
		return result;
	}
}// end class Main;
