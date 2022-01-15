
export class LoggedUser {
    public username: string;
    private token: string;
   // private tokenExpiration: Date;
  
    constructor(username: string, token: string, ) {
      this.username = username;
      this.token = token;
     // this.tokenExpiration = tokenExpiration;
 
    }
  
    get Token() {
    //  if (!this.tokenExpiration || new Date() > this.tokenExpiration) {
    //    return null;
    //  }
      return this.token;
  
      //return this._token;
    }

  }
  