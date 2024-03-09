export class ActivityMagicKeyException extends Error {
  constructor(activity: string) {
    super(`Activity "${activity}" is not matching the magic key pattern.`);
  }
}
