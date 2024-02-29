import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Post,
} from '@nestjs/common';

import { AppService } from './app.service';
import { AvailabilityRequestDto } from './dto/availabilityRequest.dto';
import { AvailabilityReceiptDto } from './dto/availabilityReceipt.dto';
import { BookedReceiptDto } from './dto/bookedReceipt.dto';
import { BookedRequestDto } from './dto/bookedRequest.dto';

@Controller('ccavailabilities')
export class AppController {
  constructor(private readonly appService: AppService) {

  }

  @Post()
  isAvailable(
    @Body() availabilityRequestDto: AvailabilityRequestDto,
  ): AvailabilityReceiptDto {
    try {
      return this.appService.checkAvailabilities(availabilityRequestDto);
    } catch (e) {
      throw new HttpException(
        'business error: ' + e.message,
        HttpStatus.BAD_REQUEST,
      );
    }
  }
  @Get()
  getAllBooked(): BookedReceiptDto[] {
    return this.appService.findAllBooked();
  }

  @Post()
  book(@Body() bookedRequestDto: BookedRequestDto): BookedReceiptDto {
    try {
      return this.appService.bookDate(bookedRequestDto);
    } catch (e) {
      throw new HttpException(
        'business error: ' + e.message,
        HttpStatus.BAD_REQUEST,
      );
    }
  }
}
