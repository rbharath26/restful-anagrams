package com.racabe.anagrams.service;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

/**
 * The Interface IDictionaryService.
 */
public interface IDictionaryService {
  /**
   * Parses the dictionary to hastable.
   *
   * @param dictionaryFile the dictionary file
   * @return the map
   */
  void parseDictionaryToHastableByWordLength(MultipartFile dictionaryFile);
  
  /**
   * Gets the anagrams.
   *
   * @param words the word or phrase used to search anagrams
   * @return a lis of the anagrams found
   */
  Set<String> getAnagrams(String words);
}
