package com.example.androidcalc;

import java.util.Stack;
import java.util.StringTokenizer;

public class ONP {
    private String wyrazenie;//zawiera wyrażenie w pierwotnej postaci

    private String onp;//wyrażenie w odwrotnej notacji polskiej

    private String nieLiczby = "+-*/^()";

    public String getONP() {
        return onp;
    }

    public ONP(String wyrazenie) {

        System.out.println("Wyrażenie: " + wyrazenie);
        this.wyrazenie = wyrazenie;

        onp = "";

        toONP();//wywołaj konwersję wyrażenia na oonp
        System.out.println("WyrażenieONP: " + onp);

    }

    /**
     * Konwersja wyrażenia na odwrotną notację polską.
     * <p>
     * 1 - dzielimy wyrażenie infiksowe na części na podstawie separatorów
     * <p>
     * 2 - dopóki są elementy w wyrażeniu wejściowym
     * <p>
     * 2.1 - pobieramy element
     * <p>
     * 2.2 - jeżeli element jest operatorem
     * <p>
     * 2.2.1 - sprawdzemy priorytety
     * <p>
     * 2.2.2 - odkładamy operator na stos
     * <p>
     * 2.3 - jeżeli element jest nawiasem otwierającym
     * <p>
     * 2.3.1 - odłóż na stos nawias otwierający
     * <p>
     * 2.4 - jeżeli element jest nawiasem zamykającym
     * <p>
     * 2.4.1 - ściągamy operatory ze stosu, aż do nawiasu otwierajęcego
     * <p>
     * 2.4.2 - ściągnij ze stosu
     * <p>
     * 2.4.3 - ściągnij już niepotrzebny nawias otwierający
     * <p>
     * 2.5 - jeżeli element nie jest operatorem ani nawiasem dodajemy go do wyrażenia postfiksowego
     * <p>
     * 3 - ściągamy ze stosu pozostałe operatory i dodajemy je do wyrażenia postfiksowego
     * <p>
     * 3.1 - ściągnij i dopisz do wyrażenia onp
     */

    private void toONP() {

// eliminateNakedMinus();

        Stack<String> stos = new Stack<String>();

        StringTokenizer st = new StringTokenizer(wyrazenie, "+-*/^()", true);//1

        while (st.hasMoreTokens()) //2

        {

            String s = st.nextToken();//2.1

            if (s.equals("+") || s.equals("*") || s.equals("-") || s.equals("/") || s.equals("^")) //2.2

            {

                while (!stos.empty() && priorytet(stos.peek()) >= priorytet(s))//2.2.1

                {

                    onp += stos.pop() + " ";

                }

                stos.push(s);//2.2.2

            } else if (s.equals("(")) //2.3

            {

                stos.push(s);//2.3.1

            } else if (s.equals(")"))//2.4

            {

                while (!stos.peek().equals("(")) //2.4.1

                {

                    onp += stos.pop() + " ";//2.4.2

                }

                stos.pop();//2.4.3

            } else //2.5

            {
                onp += s + " ";
            }

        }

        while (!stos.empty())//3

        {

            onp += stos.pop() + " ";//3.1

        }

    }

    /**
     * Obliczenie priorytetu operatora:
     * <p>
     * + lub - = 1
     * <p>
     * * lub / = 2
     * <p>
     * ^ = 3
     * <p>
     * pozostałe = 0
     *
     * @return
     */

    private boolean czyNieLiczba(char z) {

        for (int i = 0; i < nieLiczby.length(); i++) {

            if (nieLiczby.charAt(i) == z) {
                return true;
            }

        }

        return false;

    }

    public static int priorytet(String operator) {

        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if (operator.equals("*") || operator.equals("/")) {
            return 2;
        } else if (operator.equals("^")) {
            return 3;
        } else {
            return 0;
        }//pozostałe 0

    }

    /**
     * Zwróć wyrażenie w postaci Odwrotnej Notacji Polskiej
     */

    public @Override
    String toString() //zwróć wyrażenie w postaci onp

    {

        return onp;

    }

    /**
     * Dokonaj obliczenia zapisanego w odwrotnej notacji polskiej i zwróć wynik.
     *
     * @return Zwraca wynik z zapisanego wyrażenia w postaci ONP.
     */

    public double oblicz() {

        String wejscie = onp + "=";
        String[] tab = wejscie.split(" ");
        Stack<Double> stos = new Stack<Double>();//przechowuje wyniki pośrednie

        double a = 0;//przechowuje dane ze stosu

        double b = 0;//przechowuje dane ze stosu

        double w = 0;//wynik operacji arytmetycznej

        StringBuilder buduj = new StringBuilder();

        String spacja = " ";

        String sp = " ";

        int licznik = 0;

        do {//Krok 1: Czytaj el z we

            String czar = tab[licznik];
            //System.out.println(czar);
            if (czar.equals("+") || czar.equals("-") || czar.equals("*") || czar.equals("/") || czar.equals("^"))//Krok 2: Jeśli el nie jest liczbą, to idź do kroku 5

            {

                if (!stos.empty()) {

                    b = stos.pop();//Krok 6: Pobierz ze stosu dwie liczby a i b

                    a = stos.pop();

                    switch (czar) {
                        case "+": {
                            w = a + b;
                            break;
                        }
                        case "-": {
                            w = a - b;
                            break;
                        }
                        case "*": {
                            w = a * b;
                            break;
                        }
                        case "/": {
                            if (b != 0.0) {
                                w = a / b;
                            } else {
                                return Double.NaN;
                            }
                            break;
                        }
                        case "^": {
                            w = Math.pow(a, b);
                            break;
                        }
                    }
                    stos.push(w);//Krok 8: Umieść w na stosie

                }

            } else if (!czar.equals("="))//Krok 3: Umieść el na stosie
            {
                double tmp = Double.parseDouble(czar);

                stos.push(tmp);

                buduj = new StringBuilder();


            } else {

                if (!stos.empty()) {

                    w = stos.pop();//Krok 10: Prześlij na wyjście zawartość wierzchołka stosu

                    break;

                }

            }

            licznik++;

        } while (licznik < tab.length);//Krok 4: Idź do kroku 1

        return w;

    }

}

