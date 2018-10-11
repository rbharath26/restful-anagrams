import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UploadFileService {
  public apiBaseURL : String = 'http://localhost:8080/api/'

  constructor(private http: HttpClient) { }

  getAnagrams(dictionaryFile:File, words:any): Observable<HttpEvent<{}>>{
    let apiCreateEndpoint = `${this.apiBaseURL}/anagrams`
    const formData: FormData = new FormData();

    // Add the file to formData
    formData.append('dictionaryFile', dictionaryFile, dictionaryFile.name);

    // Add the word/s to search diagrams
    formData.append('words', words);

    // Call to endpoint
    const req = new HttpRequest('POST', apiCreateEndpoint, formData, {
      reportProgress: true // for progress data
    });
    return this.http.request(req)
  }
}
