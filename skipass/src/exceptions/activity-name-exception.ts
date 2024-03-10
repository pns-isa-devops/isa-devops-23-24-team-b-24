export class ActivityNameException extends Error {
  constructor(activity: string) {
    super(`Activity "${activity}" is not matching the name pattern.`);
  }
}
