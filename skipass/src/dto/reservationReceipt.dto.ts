import { IsNotEmpty, IsString } from 'class-validator';

export class ReservationReceiptDto {
  constructor(reservationReceiptId: string, isReserved: boolean, date: string) {
    this.reservationReceiptId = reservationReceiptId;
    this.isReserved = isReserved;
    this.date = date;
  }

  @IsNotEmpty()
  @IsString()
  reservationReceiptId: string;

  @IsNotEmpty()
  isReserved: boolean;

  @IsNotEmpty()
  @IsString()
  date: string;
}
