import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject } from 'rxjs';
import { Tweet } from 'src/app/models/tweet.model';
import { TweetService } from 'src/app/services/tweet.service';

@Component({
  selector: 'app-edit-tweet',
  templateUrl: './edit-tweet.component.html',
  styleUrls: ['./edit-tweet.component.css']
})
export class EditTweetComponent implements OnInit {
 tweet: FormGroup| null=null;
 @Input() tweetObject : Tweet | null=null;
  body : string='';
  @Output() close : EventEmitter<string> = new EventEmitter<string>();
  @Output() tweetUpdated : Subject<Tweet> = new Subject<Tweet>();

  constructor(private tweetService : TweetService) {}

  ngOnInit(): void {
    this.tweet = new FormGroup({
      body: new FormControl('',[Validators.required]),
    });
    this.tweet.patchValue({
      body : this.tweetObject?.body
    })
  
  }

  onSubmit(){

    this.body = this.tweet!.get('body')!.value;
    this.tweetObject!.body=this.body;
    //novi tweet
    this.tweetService.updateTweet(this.body, this.tweetObject!.originalOwnerUsername, this.tweetObject!.createdAt,  this.tweetObject!.likes, this.tweetObject!.retweets);
    this.close.emit(this.body);
    this.tweetUpdated.next(this.tweetObject!);
  }

  cancel(){
    this.close.emit(this.body);
  }
}
