import { IsNotEmpty, IsPositive, IsString } from 'class-validator';

export class BookedReceiptDto {
  constructor(schedulerReceiptId: string, isBooked: boolean, date: string) {
    this.schedulerReceiptId = schedulerReceiptId;
    this.isBooked = isBooked;
    this.date = date;
  }

  @IsNotEmpty()
  @IsString()
  schedulerReceiptId: string;

  @IsNotEmpty()
  @IsPositive()
  isBooked: boolean;

  @IsNotEmpty()
  @IsString()
  date: string;
}
