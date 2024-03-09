import { Injectable } from '@nestjs/common';

import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { ReservationReceiptDto } from './dto/reservationReceipt.dto';
import { randomUUID } from 'crypto';

@Injectable()
export class AppService {
  private static readonly magicKeyActivity: string = 'ski'; // TODO : change if needed
    private static readonly magicKeyDuration: number = 3; // TODO : change if needed
  private skiReservations: Array<ReservationReceiptDto>;

  constructor() {
    this.skiReservations = [];
  }

  findAll(): ReservationReceiptDto[] {
    return this.skiReservations;
  }

  reserve(reservationRequestDto: ReservationRequestDto): ReservationReceiptDto {
    let reservationReceiptDto: ReservationReceiptDto;
    if (paymentRequestDto.creditCard.includes(AppService.magicKey)) {
      paymentReceiptDto = new PaymentReceiptDto(
        'RECEIPT:' + randomUUID(),
        paymentRequestDto.amount,
      );
      this.transactions.push(paymentReceiptDto);
      console.log(
        'Payment accepted(' +
          paymentReceiptDto.payReceiptId +
          '): ' +
          paymentReceiptDto.amount,
      );
      return paymentReceiptDto;
    } else {
      console.log('Payment rejected: ' + paymentRequestDto.amount);
      throw new PaymentRejectedException(paymentRequestDto.amount);
    }
  }
}
