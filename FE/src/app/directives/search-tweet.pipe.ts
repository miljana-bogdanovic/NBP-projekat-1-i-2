import { Pipe, PipeTransform } from '@angular/core';
import { Tweet } from '../models/tweet.model';

@Pipe({
  name: 'SearchTweet'
})
export class SearchTweetPipe implements PipeTransform {

 
    transform(items: Tweet[] | null , searchText: string): Tweet[] {
      if (!items || searchText=='') {
        return [];
      }
      if (!searchText) {
        return items;
      }
      searchText = searchText.toLocaleLowerCase();
  
      return items.filter(tweet => {
        return tweet.body.toLocaleLowerCase().includes(searchText) 
        || tweet.firstName.toLocaleLowerCase().includes(searchText) 
        || tweet.lastName.toLocaleLowerCase().includes(searchText)
        || tweet.originalOwnerUsername.toLocaleLowerCase().includes(searchText) 
        || tweet.retweetedFrom.toLocaleLowerCase().includes(searchText) ;
      });
    }
}
