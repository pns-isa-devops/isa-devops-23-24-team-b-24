import { Injectable } from '@nestjs/common';

import { AvailabilityRequestDto } from './dto/availabilityRequest.dto';
import { AvailabilityReceiptDto } from './dto/availabilityReceipt.dto';
import { randomUUID } from 'crypto';
import { DateAlreadyScheduledException } from './exceptions/date-already-scheduled-exception';
import { MagicKeyNoMatchException } from './exceptions/magic-key-no-match-exception';

@Injectable()
export class AppService {
  private magicKey: Array<string>; // ASCII code for 'YES'
  private availabilities: Array<AvailabilityReceiptDto>;

  constructor() {
    this.magicKey = [
      '07-11 21:30',
      '04-07 13:00',
      '29-01 17:00',
      '15-09 19:00',
      '12-12 20:00',
      '31-12 23:59',
    ];
  }

  findAllAvailabilities(): AvailabilityReceiptDto[] {
    return this.availabilities;
  }

  findAllKeys(): string[] {
    return this.magicKey;
  }

  isScheduled(date: string): boolean {
    const availabities = this.findAllKeys();
    for (let i = 0; i < availabities.length; i++) {
      if (availabities[i] == date) {
        return true;
      }
    }
    return false;
  }

  checkAvailabilities(
    availabilityRequestDto: AvailabilityRequestDto,
  ): AvailabilityReceiptDto {
    let availabilityReceiptDto: AvailabilityReceiptDto;
    if (this.magicKey.includes(availabilityRequestDto.date)) {
      // eslint-disable-next-line prefer-const
      availabilityReceiptDto = new AvailabilityReceiptDto(
        'RECEIPT:' + randomUUID(),
        this.isScheduled(availabilityRequestDto.date),
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
