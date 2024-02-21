import { Injectable } from '@nestjs/common';

import { AvailabilityRequestDto } from './dto/availabilityRequest.dto';
import {AvailabilityReceiptDto, AvailabilityReceiptDto} from './dto/availabilityReceipt.dto';
import { randomUUID } from 'crypto';
import {DateAlreadyScheduledException} from "./exceptions/date-already-scheduled-exception";

@Injectable()
export class AppService {

  private availabilities: Array<AvailabilityReceiptDTO>;

  constructor() {
    this.availabilities = [];
  }

  findAll(): AvailabilityReceiptDto[] {
    return this.availabilities;
  }

  checkAvailabilities(availabilityRequestDto: AvailabilityRequestDto): AvailabilityReceiptDto {
    let availabilityReceiptDto: AvailabilityReceiptDto;
    availabilityReceiptDto = new AvailabilityReceiptDto(
        'RECEIPT:' + randomUUID(),
        availabilityRequestDto.isAvailable);
    if (availabilityRequestDto.isAvailable) {
      this.availabilities.push(availabilityReceiptDto);
      console.log(
          'Schedulation number : ' +
          availabilityReceiptDto.schedulerReceiptId +
          '): ' +
          availabilityReceiptDto.isAvailable,
      );
      return availabilityReceiptDto;
    } else {
      console.log('Date ' + availabilityRequestDto.date + ' already scheduled.');
      throw new DateAlreadyScheduledException(availabilityRequestDto.date);
    }
  }

}
