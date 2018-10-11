package com.racabe.anagrams.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.racabe.anagrams.service.IDictionaryService;

/**
 * The Class AnagramControllerUnitTest Unit Test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AnagramControllerUnitTest {

  /**
   * MockMvc is the main entry point for server-side Spring MVC test support. Perform a request and
   * return a type that allows chaining further actions, such as asserting expectations, on the
   * result.
   */
  @Autowired
  private MockMvc mvc;

  /** The dictionary service. */
  @MockBean
  private IDictionaryService dictionaryService;

  /**
   * Given dictionary and words when post anagrams then return json object.
   *
   * @throws Exception the exception
   */
  @Test
  public void givenDictionaryAndWords_whenPostAnagrams_thenReturnJsonObject()
    throws Exception {
       
      String words = "arona";
      MockMultipartFile dictionaryFile = new MockMultipartFile("dictionaryFile", "anagramDic.txt", "text/plain", "aardvark\r\naardwolf\r\naaron\r\naback\r\nabacus\r\nabaft\r\nabalone\r\nabandon\r\nabandoned\r\nabandonment\r\nabandons".getBytes());
   
      Set<String> anagrams = new HashSet<String>();
      anagrams.add("aaron");
   
      given(dictionaryService.getAnagrams(words)).willReturn(anagrams);
      
      mvc.perform(MockMvcRequestBuilders.multipart("/anagrams")
        .file(dictionaryFile)
        .param("words", words)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.anagrams", hasSize(1)))
        .andExpect(jsonPath("$.word", is(words)));
  }
}
