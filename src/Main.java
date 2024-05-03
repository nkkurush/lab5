import java.util.Scanner;

/*Реализовать методы поиска подстроки в строке.
        Добавить возможность ввода строки и подстроки с клавиатуры.
        Предусмотреть возможность существования пробела.
        Реализовать возможность выбора опции чувствительности или нечувствительности к регистру.
                Оценить время работы каждого алгоритма поиска и сравнить его со временем
        работы стандартной функции поиска, используемой в выбранном языке программирования.*/
public class Main {
    public static void main(String[] args) {
        long startTime1 = System.currentTimeMillis(); // Засекаем начальное время
// Ваш код, время выполнения которого нужно измерить
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String str2 = sc.nextLine();

        KMPAlgorithm kmpAlgorithm = new KMPAlgorithm();
        kmpAlgorithm.KMPSearch(str,str2);
        long endTime1 = System.currentTimeMillis(); // Засекаем конечное время
        long elapsedTime1 = endTime1 - startTime1; // Вычисляем затраченное время
        System.out.println(elapsedTime1);
        long startTime2 = System.currentTimeMillis(); // Засекаем начальное время
        BoyerMooreAlgorithm boyerMooreAlgorithm = new BoyerMooreAlgorithm();
        boyerMooreAlgorithm.search(str.toCharArray(),str2.toCharArray());
        long endTime2 = System.currentTimeMillis(); // Засекаем конечное время
        long elapsedTime2 = endTime2 - startTime2; // Вычисляем затраченное время
        System.out.println(elapsedTime2);
    }
    static class BoyerMooreAlgorithm {
        static int NO_OF_CHARS = 256;

        static int max(int a, int b) {
            return (a > b) ? a : b;
        }

        static void badCharHeuristic(char[] str, int size, int[] badchar) {
            int i;

            for (i = 0; i < NO_OF_CHARS; i++) {
                badchar[i] = -1;
            }

            for (i = 0; i < size; i++) {
                badchar[(int) str[i]] = i;
            }
        }

        static void search(char[] txt, char[] pat) {
            int m = pat.length;
            int n = txt.length;

            int[] badchar = new int[NO_OF_CHARS];

            badCharHeuristic(pat, m, badchar);

            int s = 0;
            while (s <= (n - m)) {
                int j = m - 1;

                while (j >= 0 && pat[j] == txt[s + j]) {
                    j--;
                }

                if (j < 0) {
                    System.out.println("Найдено вхождение подстроки на позиции " + s);
                    s += (s + m < n) ? m - badchar[txt[s + m]] : 1;
                } else {
                    s += max(1, j - badchar[txt[s + j]]);
                }
            }
        }
    }
        static class KMPAlgorithm {
        int[] computeLPSArray(String pattern) {
            int patternLength = pattern.length();
            int[] lps = new int[patternLength];
            int len = 0;
            int i = 1;
            lps[0] = 0;

            while (i < patternLength) {
                if (pattern.charAt(i) == pattern.charAt(len)) {
                    len++;
                    lps[i] = len;
                    i++;
                } else {
                    if (len != 0) {
                        len = lps[len - 1];
                    } else {
                        lps[i] = 0;
                        i++;
                    }
                }
            }
            return lps;
        }

        void KMPSearch(String text, String pattern) {
            int patternLength = pattern.length();
            int textLength = text.length();
            int[] lps = computeLPSArray(pattern);
            int i = 0;
            int j = 0;
            while (i < textLength) {
                if (pattern.charAt(j) == text.charAt(i)) {
                    i++;
                    j++;
                }
                if (j == patternLength) {
                    System.out.println("Найдено вхождение подстроки на позиции " + (i - j));
                    j = lps[j - 1];
                } else if (i < textLength && pattern.charAt(j) != text.charAt(i)) {
                    if (j != 0) {
                        j = lps[j - 1];
                    } else {
                        i = i + 1;
                    }
                }
            }
        }
    }
}