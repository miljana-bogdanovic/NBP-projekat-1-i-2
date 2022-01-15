import { Component, Input, OnInit, Output } from '@angular/core';
import { Tweet } from 'src/app/models/tweet.model';
import { User } from 'src/app/models/user.model';
import { TweetService } from 'src/app/services/tweet.service';
import { EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-tweet-list',
  templateUrl: './tweet-list.component.html',
  styleUrls: ['./tweet-list.component.css'],
})
export class TweetListComponent implements OnInit {
  @Input() tweets: Tweet[] = [];
  @Output() refreshTweets : Subject<null>=new Subject<null>();
  constructor(private tweetService : TweetService, ) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(): void {
    console.log(this.tweets); // logs correct data, yay!
  }
  onTweetChanged(event : Tweet)
  {
    const i=this.tweets.findIndex((tweet)=> { tweet.originalOwnerUsername==event.originalOwnerUsername && tweet.createdAt==event.createdAt});
    this.tweets[i].body=event.body;
  }
  onTweetDeleted(event: Tweet)
  {
    const index = this.tweets.indexOf(event, 0);
    if (index > -1) {
      this.tweets.splice(index, 1);
}
  }
  onTweetCreated()
  {
 // fire event ka rodtelju
  }



}
