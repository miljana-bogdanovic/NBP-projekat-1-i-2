import { User } from './user.model';

export class Tweet {
  constructor(
    public photo: string,
    public body: string,
    //public username : string,
    public firstName : string,
    public lastName : string,
    public image : string,
    public isRetweet: boolean = false,
    public originalOwnerUsername : string,
    public retweetedFrom : string, //retweet postoji samo ako je isRetweet true 
    public likes : number,
    public retweets : number,
    public createdAt : string,
    public isLikedByUser : boolean=false,
    public isRetweetedByUser : boolean=false
  ) {
    // slike itd?
  }
}


