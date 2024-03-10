export class ActivityTypeException extends Error {
  constructor(activity: string) {
    super(`Activity "${activity}" is not matching the activity type pattern.`);
  }
}
