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

@Controller('ccavailabilities')
export class AppController {
  constructor(private readonly appService: AppService) {}

  @Get()
  getAllAvailabilities(): AvailabilityReceiptDto[] {
    return this.appService.findAllAvailabilities();
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
}
