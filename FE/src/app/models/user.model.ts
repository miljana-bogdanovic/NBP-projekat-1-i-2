export class User {
  constructor(
    public username: string,
    public image: string,
    public firstName: string,
    public lastName: string,
    public email : string,
    public followers : number,
    public following : number,
    public password : string
  ) {}
}
