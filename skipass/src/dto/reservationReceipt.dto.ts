import { IsNotEmpty, IsString } from 'class-validator';

export class ReservationReceiptDto {
  constructor(reservationReceiptId: string, isReserved: boolean, date: string) {
    this.payReceiptId = reservationReceiptId;
    this.isReserved = isReserved;
    this.date = date;
  }

  @IsNotEmpty()
  @IsString()
  payReceiptId: string;

  @IsNotEmpty()
  isReserved: boolean;

  @IsNotEmpty()
  @IsString()
  date: string;
}
