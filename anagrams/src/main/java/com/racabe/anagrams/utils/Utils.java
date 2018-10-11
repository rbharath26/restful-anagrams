package com.racabe.anagrams.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Class Utils.
 */
public final class Utils {

  // Private constructor to prevent instantiation
  private Utils() {
      throw new UnsupportedOperationException();
  }

  /**
   * The Constant CONSTANT_PRIMES. This list will be used to transform a letter by a prime number.
   * To improve the performance and memory use, we will see the frequency English letters to assign
   * the number: more frequency -> less number
   * 
   * Source for frequency letters: http://norvig.com/mayzner.html
   */
  private static final int[] CONSTANT_PRIMES = {5, 71, 37, 31, 2, 47, 59, 23, 11, 89, 79, 29, 43, 13, 7,
      53, 97, 19, 17, 3, 41, 73, 61, 83, 67, 101};

  /**
   * Method to know the prime number of a letter.
   *
   * @param character the character to transform in prime number
   * @return the prime number for the letter
   */
  public static Integer charToNumber(char character) {
    // Using "mod" operation we can use letters in upper or lower case, because the prime number
    // will be the same.
    return CONSTANT_PRIMES[((int) character % 32) - 1];
  }

  /**
   * Method to transform a word in a hash code. This hash code will be used to identify anagrams. If
   * we multiply each prime number of each letter of the word, we obtain a unique number for any
   * word with the same letters/characters.
   *
   * @param word the word to covert in a hash code using prime numbers
   * @return the hash code for a word
   */
  public static Long wordToHashNumber(String word) {
    char[] wordArray = word.toCharArray();
    Long numberKey = 1L;
    for (char c : wordArray) {
      numberKey = numberKey * Utils.charToNumber(c);
    }

    return numberKey;
  }

  /**
   * Generate the list of combinations of lengths for an anagram according to the maximum number of
   * words for the anagram.
   *
   * @param lengths List with the sequence of lengths, used to get the combinations
   * @param maxNumColumns The max number of words (group in list of maxNumColumns elements)
   * @param totalWordLength Original length of the word to discard a combination if the sum of its
   *        lengths is greater than totalWordLength
   * @return The sets of the lengths
   */
  public static Set<List<Integer>> generateLengths(Integer[] lengths, int maxNumColumns,
      int totalWordLength) {
    Set<List<Integer>> validCombinations = new HashSet<List<Integer>>();
    Integer[] combinations = new Integer[maxNumColumns];
    combineLengths(lengths, combinations, 0, maxNumColumns, totalWordLength, validCombinations);

    return validCombinations;
  }

  /**
   * Recursive method to generate all possible combinations of lengths.
   *
   * @param lengths List with the sequence of lengths, used to get the combinations
   * @param combinations Temporal combination of lengths
   * @param currentCol Current position in the list
   * @param maxNumColumns The maximum number of lengths
   * @param totalWordLength Original length of the word to discard a combination if the sum of its
   *        lengths is greater than totalWordLength
   * @param validCombinations The sets of valid combinations of lengths
   */
  private static void combineLengths(Integer[] lengths, Integer[] combinations, int currentCol,
      int maxNumColumns, int totalWordLength, Set<List<Integer>> validCombinations) {
    if (currentCol == maxNumColumns) {
      int lengthCombination = Arrays.stream(combinations).reduce(0, Integer::sum);
      if (totalWordLength == lengthCombination) {
        List<Integer> orderedLengths = Arrays.asList(combinations);
        Collections.sort(orderedLengths);
        validCombinations.add(new ArrayList<Integer>(orderedLengths));
      }
      return;
    }
    for (int length : lengths) {
      combinations[currentCol] = length;
      combineLengths(lengths, combinations, currentCol + 1, maxNumColumns, totalWordLength,
          validCombinations);
    }
  }

  /**
   * Method to get the Cartesian product of the words by lengths found to form the anagrams.
   *
   * @param elements The words group by length to form the combinations
   * @return The sets of anagrams found
   */
  public static Set<String> cartesianProductString(String[][] elements) {
    Set<String> anagramsCombinations = new HashSet<String>();

    String[] anagram = new String[elements.length];
    getCombinatedAnagram(elements, 0, anagram, anagramsCombinations);

    return anagramsCombinations;
  }

  /**
   * Gets the combinated anagram.
   *
   * @param elements the elements
   * @param level the level
   * @param anagram the anagram
   * @param anagramsCombinations the anagrams combinations
   * @return the combinated anagram
   */
  private static void getCombinatedAnagram(String[][] elements, int level, String[] anagram,
      Set<String> anagramsCombinations) {
    if (level == elements.length) {
      List<String> combination = new ArrayList<String>(Arrays.asList(anagram));
      Collections.sort(combination);
      anagramsCombinations.add(String.join(" ", combination));
      return;
    }

    for (int pos = 0; pos < elements[level].length; pos++) {
      anagram[level] = elements[level][pos];
      getCombinatedAnagram(elements, level + 1, anagram, anagramsCombinations);
    }
  }
}
