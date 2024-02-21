export class DateAlreadyScheduledException extends Error {
  constructor(amount: number) {
    super(`Payment rejected as "${amount}" cannot be paid`);
  }
}
