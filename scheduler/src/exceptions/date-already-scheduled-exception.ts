export class DateAlreadyScheduledException extends Error {
  constructor(date: string) {
    super(`Date "${date}" already booked.`);
  }
}
