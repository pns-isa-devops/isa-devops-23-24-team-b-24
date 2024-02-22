import { Injectable } from '@nestjs/common';

import { AvailabilityRequestDto } from './dto/availabilityRequest.dto';
import { AvailabilityReceiptDto } from './dto/availabilityReceipt.dto';
import { randomUUID } from 'crypto';
import { DateAlreadyScheduledException } from './exceptions/date-already-scheduled-exception';
import { MagicKeyNoMatchException } from './exceptions/magic-key-no-match-exception';

@Injectable()
export class AppService {
  private static readonly magicKey: string = '07/11/2023'; // ASCII code for 'YES'
  private availabilities: Array<AvailabilityReceiptDto>;

  constructor() {
    this.availabilities = [];
  }

  findAll(): AvailabilityReceiptDto[] {
    return this.availabilities;
  }

  checkAvailabilities(
    availabilityRequestDto: AvailabilityRequestDto,
  ): AvailabilityReceiptDto {
    let availabilityReceiptDto: AvailabilityReceiptDto;
    if (availabilityRequestDto.date.includes(AppService.magicKey)) {
      // eslint-disable-next-line prefer-const
      availabilityReceiptDto = new AvailabilityReceiptDto(
        'RECEIPT:' + randomUUID(),
        true,
      );
      if (availabilityReceiptDto.isAvailable) {
        this.availabilities.push(availabilityReceiptDto);
        console.log(
          'Schedulation number : ' +
            availabilityReceiptDto.schedulerReceiptId +
            '): ' +
            availabilityReceiptDto.isAvailable,
        );
        return availabilityReceiptDto;
      } else {
        console.log(
          'Date ' + availabilityRequestDto.date + ' already scheduled.',
        );
        throw new DateAlreadyScheduledException(availabilityRequestDto.date);
      }
    } else {
      console.log(
        'Date ' +
          availabilityRequestDto.date +
          ' is not matching the magic key.',
      );
      throw new MagicKeyNoMatchException(availabilityRequestDto.date);
    }
  }
}
