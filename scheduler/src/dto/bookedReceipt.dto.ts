import { IsNotEmpty, IsPositive, IsString } from 'class-validator';

export class BookedReceiptDto {
  constructor(payReceiptId: string, isBooked: boolean) {
    this.schedulerReceiptId = payReceiptId;
    this.isBooked = isBooked;
  }

  @IsNotEmpty()
  @IsString()
  schedulerReceiptId: string;

  @IsNotEmpty()
  @IsPositive()
  isBooked: boolean;
}
