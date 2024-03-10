import { Injectable } from '@nestjs/common';

import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { ReservationReceiptDto } from './dto/reservationReceipt.dto';
import { randomUUID } from 'crypto';
import { DurationException } from './exceptions/duration-exception';
import { ActivityTypeException } from './exceptions/activity-type-exception';
import { ActivityNameException } from './exceptions/activity-name-exception';

@Injectable()
export class AppService {
  private static readonly magicKeyName: string = 'ski'; // TODO : change if needed
  private magicKeyActivty: Array<string>;
  private skiReservations: Array<ReservationReceiptDto>;

  constructor() {
    this.skiReservations = [];
    this.magicKeyActivty = ['day', 'half_day', 'hourly'];
  }

  findAll(): ReservationReceiptDto[] {
    return this.skiReservations;
  }

  reserve(reservationRequestDto: ReservationRequestDto): ReservationReceiptDto {
    let reservationReceiptDto: ReservationReceiptDto;

    if (
      reservationRequestDto.name === AppService.magicKeyName &&
      reservationRequestDto.duration > 0 &&
      this.magicKeyActivty.includes(reservationRequestDto.activity)
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
    } else if (reservationRequestDto.name !== AppService.magicKeyName) {
      console.log(
        'Reservation rejected, due to the activity name ' +
          reservationRequestDto.name +
          '): ',
      );
      throw new ActivityNameException(reservationRequestDto.name);
    } else {
      console.log(
        'Reservation rejected, due to an activity error ' +
          reservationRequestDto.activity +
          '): ',
      );
      throw new ActivityTypeException(reservationRequestDto.activity);
    }
  }
}
