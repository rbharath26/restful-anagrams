package com.racabe.anagrams.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.racabe.anagrams.controller.uri.AnagramRestURIConstants;
import com.racabe.anagrams.service.IDictionaryService;

/**
 * The class AnagramController where we find the REST methods.
 */
@Controller
public class AnagramController {

  /** The dictionary service. */
  @Autowired
  private IDictionaryService dictionaryService;

  /**
   * HTTP request to get all possible anagrams for a word into a dictionary given.
   *
   * @param dictionary the dictionary file with all words accepted
   * @param words the word or phrase used to search anagrams
   * @return Return all possible anagrams for the words found into the dictionary and the time used
   *         to resolve it into a JSON object
   */
  @CrossOrigin(origins = "http://localhost:4200")
  @RequestMapping(value = AnagramRestURIConstants.POST_ANAGRAMS, method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody Map<String, Object> getDiagramsUsingDictionary(
      @RequestParam(name = "dictionaryFile", required = true) MultipartFile dictionary,
      @RequestParam(name = "words", required = true) String words) {

    // Register start time
    long startTime = System.currentTimeMillis();

    // We load the dictionary in memory, classifying it according to the length of the words it has
    // and a hashcode that we associate with the words
    dictionaryService.parseDictionaryToHastableByWordLength(dictionary);

    // Search the anagrams
    Set<String> anagrams = dictionaryService.getAnagrams(words);

    // Register finish time
    long endTime = System.currentTimeMillis();
    double totalTime = (endTime - startTime) / 1000d;

    // Put the anagrams and time in a map to return the JSON object
    NumberFormat formatter = new DecimalFormat("#0.00000");
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("word", words);
    result.put("anagrams", anagrams);
    result.put("seconds", formatter.format(totalTime));

    return result;
  }
}
