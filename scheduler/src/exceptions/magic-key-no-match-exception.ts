export class MagicKeyNoMatchException extends Error {
  constructor(date: string) {
    super(`Date "${date}" is not matching the magic key pattern.`);
  }
}
