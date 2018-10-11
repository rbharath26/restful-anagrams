package com.racabe.anagrams.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.racabe.anagrams.exception.DictionaryException;
import com.racabe.anagrams.service.IDictionaryService;
import com.racabe.anagrams.utils.Utils;

/**
 * The class DictionaryService, where can find the methods to load the dictionary and to find the
 * anagrams.
 */
@Service
public class DictionaryService implements IDictionaryService {

  /** Set the minimum length for a valir word into the possible anagram. */
  private static final int MIN_LENGTH_ANAGRAM = 3;

  /** The dictionary parsed by word length and hashcode. */
  private Map<Integer, Map<Long, List<String>>> dictionaryParsed =
      new HashMap<Integer, Map<Long, List<String>>>();

  /**
   * Parses the dictionary to hastable.
   *
   * @param dictionaryFile the dictionary file
   * @return the map
   */
  public void parseDictionaryToHastableByWordLength(MultipartFile dictionaryFile) {
    // Normalize file name
    String dictionaryName = StringUtils.cleanPath(dictionaryFile.getOriginalFilename());

    try {
      // Check if the file's name contains invalid characters
      if (dictionaryName.contains("..")) {
        throw new DictionaryException(
            "Sorry! Dictionary name contains invalid path sequence " + dictionaryName);
      }

      // Using try-with-resources statement contains two declarations that are separated by a
      // semicolon. When the block of code that directly follows it terminates, either normally or
      // because of an exception, the close methods of the fileReader and scanner objects are
      // automatically called in this order.
      dictionaryParsed = new HashMap<Integer, Map<Long, List<String>>>();
      try (ByteArrayInputStream stream = new ByteArrayInputStream(dictionaryFile.getBytes());
          Scanner scanner = new Scanner(stream)) {
        while (scanner.hasNext()) {
          String word = scanner.next();

          // Only accept words of a concrete length
          if (word.length() >= MIN_LENGTH_ANAGRAM) {
            // Create the hash code for this word
            Long numberKey = Utils.wordToHashNumber(word);
            Integer lengthKey = word.length();

            Map<Long, List<String>> candidatesByLength = dictionaryParsed.get(lengthKey);

            if (candidatesByLength == null) {
              candidatesByLength = new HashMap<Long, List<String>>();
            }

            List<String> candidates = candidatesByLength.get(numberKey);
            if (candidates == null) {
              candidates = new ArrayList<String>();
            }

            candidates.add(word);
            candidatesByLength.put(numberKey, candidates);
            dictionaryParsed.put(lengthKey, candidatesByLength);
          }
        }
      }
    } catch (IOException ex) {
      throw new DictionaryException(
          "Could not parse dictionary " + dictionaryName + ". Please try again!", ex);
    }
  }

  /**
   * Gets the anagrams.
   *
   * @param words the word or phrase used to search anagrams
   * @return a lis of the anagrams found
   */
  public Set<String> getAnagrams(String words) {
    // When the processing is finished, it will contain the list of anagrams found
    Set<String> anagrams = new HashSet<String>();

    // Special characters should be ignored
    String inputWithoutSpaces = words.replaceAll("[^A-Za-z]", "");

    // Configure initial state
    int lengthInputWord = inputWithoutSpaces.length();

    // Calculate the hashcode of the input word
    Long inputWordHash = Utils.wordToHashNumber(inputWithoutSpaces);

    // Calculate the maximum word that can have an anagram
    int maxNumAnagramsByLine = lengthInputWord / MIN_LENGTH_ANAGRAM;

    // Search all combinations of anagrams for the input word for each possible number of words
    for (int numAnagrams = 1; numAnagrams <= maxNumAnagramsByLine; numAnagrams++) {
      // Calculate the max length for a word into the anagram
      int maxLength = lengthInputWord - ((numAnagrams - 1) * MIN_LENGTH_ANAGRAM);

      // Create a sequence from the minimun length of a word to maximun length, and generate all
      // possible cominations of this lenghts by number of words into the anagrams (We discard a
      // combination if the sum of the lengths is greater than the size of the input word)
      Integer[] lengths = IntStream.rangeClosed(MIN_LENGTH_ANAGRAM, maxLength).boxed()
          .toArray(size -> new Integer[size]);
      Set<List<Integer>> validLengthCombinations =
          Utils.generateLengths(lengths, numAnagrams, lengthInputWord);

      // For each combination of lengths, search the possible anagrams
      if (validLengthCombinations != null && !validLengthCombinations.isEmpty()) {
        Iterator<List<Integer>> itValidLengthCombinations = validLengthCombinations.iterator();
        while (itValidLengthCombinations.hasNext()) {
          List<Integer> lengthCombinations = itValidLengthCombinations.next();

          if (lengthCombinations != null && !lengthCombinations.isEmpty()) {
            String[][] storeAnagrams = new String[lengthCombinations.size()][];
            this.getAnagramsByLength(lengthCombinations.stream().toArray(size -> new Integer[size]), 0,
                inputWordHash, storeAnagrams, anagrams);
          }
        }
      }
    }

    return anagrams;

  }

  /**
   * Gets the anagrams by length.
   *
   * @param lengthCombinations List of lengths for the different words that will form an anagram of
   *        the given word
   * @param currentLengthPos Current position in the list to get a length
   * @param inputWordHash Current hash code to check if a word into the dictionary is or not valid
   * @param storeAnagrams Store the list of word for each length to finally form the list of
   *        anagrams
   * @param anagramsFound List of anagrams found in the process.
   */
  private void getAnagramsByLength(Integer[] lengthCombinations, int currentLengthPos,
      Long inputWordHash, String[][] storeAnagrams, Set<String> anagramsFound) {

    // Condition to break the recursion and form all combinations of word by length to get each
    // anagram
    if (currentLengthPos == lengthCombinations.length) {
      anagramsFound.addAll(Utils.cartesianProductString(storeAnagrams));
      return;
    }

    // Get the actual length of the possible word to form an anagram
    Integer currentLength = lengthCombinations[currentLengthPos];

    // Get the possible candidates from the dictionary by length
    Map<Long, List<String>> candidatesByLength = dictionaryParsed.get(currentLength);
    if (candidatesByLength != null && !candidatesByLength.isEmpty()) {
      Iterator<Entry<Long, List<String>>> itCandidatesByLength =
          candidatesByLength.entrySet().iterator();
      while (itCandidatesByLength.hasNext()) {
        Entry<Long, List<String>> entryCandidates = itCandidatesByLength.next();
        Long key = entryCandidates.getKey();

        // For each candidates by length, we check if the hash codes with this length module the
        // hash code of the word is zero. If this condition is fulfilled, as each hash code consists
        // of products of prime numbers, it will mean that we have found a list of words of a
        // specific length that can be part of the anagram
        if (inputWordHash % key == 0) {
          storeAnagrams[currentLengthPos] =
              entryCandidates.getValue().stream().toArray(size -> new String[size]);

          // Pass to the next length and the newy hash code will be the rest between the current
          // hash code and the hash code found by length
          this.getAnagramsByLength(lengthCombinations, currentLengthPos + 1, inputWordHash / key,
              storeAnagrams, anagramsFound);
        }
      }
    }
  }

}
