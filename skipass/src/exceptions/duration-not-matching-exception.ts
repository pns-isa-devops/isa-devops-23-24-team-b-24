export class DurationNotMatchingException extends Error {
  constructor(duration: number) {
    super(`Duration "${duration}" is not matching the pattern.`);
  }
}
