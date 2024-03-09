import { Injectable } from '@nestjs/common';

import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { ReservationReceiptDto } from './dto/reservationReceipt.dto';
import { randomUUID } from 'crypto';
import { DurationException } from './exceptions/duration-exception';
import { ActivityMagicKeyException } from './exceptions/activity-magic-key-exception';

@Injectable()
export class AppService {
  private static readonly magicKeyActivity: string = 'ski'; // TODO : change if needed
  private skiReservations: Array<ReservationReceiptDto>;

  constructor() {
    this.skiReservations = [];
  }

  findAll(): ReservationReceiptDto[] {
    return this.skiReservations;
  }

  reserve(reservationRequestDto: ReservationRequestDto): ReservationReceiptDto {
    let reservationReceiptDto: ReservationReceiptDto;
    if (
      reservationRequestDto.activity === AppService.magicKeyActivity &&
      reservationRequestDto.duration > 0
    ) {
      reservationReceiptDto = new ReservationReceiptDto(
        'RECEIPT:' + randomUUID(),
        true,
        new Date().toISOString(),
      );
      this.skiReservations.push(reservationReceiptDto);
      console.log(
        'reservation accepted(' +
          reservationReceiptDto.reservationReceiptId +
          reservationRequestDto.activity +
          reservationRequestDto.duration +
          '): ',
      );
      return reservationReceiptDto;
    } else if (reservationRequestDto.duration <= 0) {
      console.log(
        'Reservation rejected, due to a duration error ' +
          reservationRequestDto.duration +
          '): ',
      );
      throw new DurationException();
    } else {
      console.log(
        'Reservation rejected, due to an activity error ' +
          reservationRequestDto.activity +
          '): ',
      );
      throw new ActivityMagicKeyException(reservationRequestDto.activity);
    }
  }
}
