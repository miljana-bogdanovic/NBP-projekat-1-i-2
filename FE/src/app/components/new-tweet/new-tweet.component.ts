import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { Tweet } from 'src/app/models/tweet.model';
import { TweetService } from 'src/app/services/tweet.service';

@Component({
  selector: 'app-new-tweet',
  templateUrl: './new-tweet.component.html',
  styleUrls: ['./new-tweet.component.css'],
})
export class NewTweetComponent implements OnInit {
  newTweet: FormGroup| null=null;
  body : string='';
  @Output() tweetCreated : Subject<null> = new Subject<null>();

  constructor(private tweetService : TweetService) {}

  ngOnInit(): void {
    this.newTweet = new FormGroup({
      body: new FormControl('',[Validators.required]),
    });

    //this.tweetService.fetchTweet("miljana", "2022-01-01 08:08:08");
  }

  onSubmit(){

    this.body = this.newTweet!.get('body')!.value;
    //novi tweet
    this.tweetService.createTweet(this.body);
    this.tweetCreated.next(null);
    this.newTweet!.patchValue({
      body : ' '
    });

  }
}
