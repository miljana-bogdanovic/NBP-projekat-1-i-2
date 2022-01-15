export class UserInfo {
    constructor(
      public followerUsername: string,
      public friendUsername : string,
      public photo: string,
      public firstName: string,
      public lastName: string,
      public isFollowedByMe : boolean
    ) {}
  }
  