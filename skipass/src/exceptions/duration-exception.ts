export class DurationException extends Error {
  constructor() {
    super(`Duration must be a positive number`);
  }
}
