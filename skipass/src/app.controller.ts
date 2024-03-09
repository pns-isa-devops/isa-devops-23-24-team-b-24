import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Post,
} from '@nestjs/common';

import { AppService } from './app.service';
import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { ReservationReceiptDto } from './dto/reservationReceipt.dto';

@Controller('cctransactions')
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getAllTransactions(): ReservationReceiptDto[] {
    return this.appService.findAll();
  }

  @Post()
  reserve(
    @Body() reservationRequestDto: ReservationRequestDto,
  ): ReservationReceiptDto {
    try {
      return this.appService.reserve(reservationRequestDto);
    } catch (e) {
      throw new HttpException(
        'business error: ' + e.message,
        HttpStatus.BAD_REQUEST,
      );
    }
  }
}
