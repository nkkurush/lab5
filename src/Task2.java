/*
Написать программу, определяющую, является ли данное расположение «решаемым»,
        то есть можно ли из него за конечное число шагов перейти к правильному.
        Если это возможно, то необходимо найти хотя бы одно решение - последовательность движений,
        после которой числа будут расположены в правильном порядке.

        Входные данные: массив чисел, представляющий собой расстановку в
        Порядке «слева направо, сверху вниз». Число 0 обозначает пустое поле. Например,
        массив [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0] представляет собой
        «решенную» позицию элементов.

        Выходные данные: если решения нет, то функция должна вернуть
        Пустой массив []. Если решение есть, то необходимо представить решение —
        для каждого шага записывается номер передвигаемого на данном шаге элемента.*/

import java.util.*;

public class Task2 {
    public static void main(String[] args) {
        int[] puzzle = {1,2,3,4,5,6,7,8,9,0};
        List<Integer> solution = solvePuzzle(puzzle);
        if (solution.isEmpty()) {
            System.out.println("Решения нет.");
        } else {
            System.out.println("Решение: " + solution);
        }
    }

    public static List<Integer> solvePuzzle(int[] puzzle) {
        if (!isSolvable(puzzle)) {
            return new ArrayList<>();
        }

        int[] goal = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
        Map<String, String> moves = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.add(puzzle);
        moves.put(Arrays.toString(puzzle), "");

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (Arrays.equals(current, goal)) {
                return parseMoves(moves, Arrays.toString(current));
            }

            int zeroIndex = findZeroIndex(current);
            List<Integer> possibleMoves = findPossibleMoves(zeroIndex);
            for (int move : possibleMoves) {
                int[] next = Arrays.copyOf(current, current.length);
                int temp = next[zeroIndex + move];
                next[zeroIndex + move] = 0;
                next[zeroIndex] = temp;
                String nextStr = Arrays.toString(next);
                if (!moves.containsKey(nextStr)) {
                    queue.add(next);
                    moves.put(nextStr, moves.get(Arrays.toString(current)) + move);
                }
            }
        }

        return new ArrayList<>();
    }

    public static boolean isSolvable(int[] puzzle) {
        int inversions = 0;
        for (int i = 0; i < puzzle.length - 1; i++) {
            for (int j = i + 1; j < puzzle.length; j++) {
                if (puzzle[i] > puzzle[j] && puzzle[i] != 0 && puzzle[j] != 0) {
                    inversions++;
                }
            }
        }

        int zeroIndex = findZeroIndex(puzzle);
        int zeroRowFromBottom = 4 - zeroIndex / 4; // Zero-based counting

        return (inversions % 2 == 0 && zeroRowFromBottom % 2 == 1) || (inversions % 2 == 1 && zeroRowFromBottom % 2 == 0);
    }

    public static int findZeroIndex(int[] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    public static List<Integer> findPossibleMoves(int zeroIndex) {
        List<Integer> possibleMoves = new ArrayList<>();
        if (zeroIndex % 4 > 0) {
            possibleMoves.add(-1); // Move left
        }
        if (zeroIndex % 4 < 3) {
            possibleMoves.add(1); // Move right
        }
        if (zeroIndex / 4 > 0) {
            possibleMoves.add(-4); // Move up
        }
        if (zeroIndex / 4 < 3) {
            possibleMoves.add(4); // Move down
        }
        return possibleMoves;
    }

    public static List<Integer> parseMoves(Map<String, String> moves, String finalPosition) {
        List<Integer> solution = new ArrayList<>();
        String curPosition = finalPosition;
        while (!moves.get(curPosition).equals("")) {
            int move = Integer.parseInt(moves.get(curPosition).substring(0, 1));
            solution.add(move);
            int zeroIndex = curPosition.indexOf("0");
            int newZeroIndex = zeroIndex + move;
            curPosition = curPosition.substring(0, zeroIndex) + curPosition.charAt(newZeroIndex) + "0" + curPosition.substring(zeroIndex + 1, newZeroIndex) + curPosition.charAt(zeroIndex) + curPosition.substring(newZeroIndex + 1);
        }
        return solution;
    }
}
