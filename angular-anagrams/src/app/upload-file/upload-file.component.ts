import { Component, OnInit } from '@angular/core';
import { UploadFileService } from '../upload-file.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']

})
export class UploadFileComponent implements OnInit {

  dictionaryFile: File;
  words: String;
  anagramsResult: any;
  loading: boolean;

  constructor(private uploadService: UploadFileService) { }

  ngOnInit() {
  }

  selectFile(event) {
    this.dictionaryFile = event.target.files.item(0);
  }

  searchAnagrams() {
    this.loading = true;
    this.anagramsResult = null;
    this.uploadService.getAnagrams(this.dictionaryFile, this.words).subscribe(event => {
      if (event instanceof HttpResponse) {
        this.anagramsResult = event.body;
        this.loading = false;
      }
    });
  }
}
