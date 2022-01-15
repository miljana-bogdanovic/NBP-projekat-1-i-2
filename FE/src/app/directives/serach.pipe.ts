import { Pipe, PipeTransform } from '@angular/core';
import { Tweet } from '../models/tweet.model';
import { User } from '../models/user.model';

@Pipe({ name: 'UserFilter' })
export class SearchPipe implements PipeTransform {
  transform(items: User[] | null , searchText: string): User[] {
    if (!items || searchText=='') {
      return [];
    }
    if (!searchText) {
      return items;
    }
    searchText = searchText.toLocaleLowerCase();

    return items.filter(user => {
      return user.username.toLocaleLowerCase().includes(searchText) 
      || user.firstName.toLocaleLowerCase().includes(searchText) || user.lastName.toLocaleLowerCase().includes(searchText);
    });
  }
}
