import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AvailabilityRequestDto } from './dto/availabilityRequest.dto';
import { HttpException } from '@nestjs/common';
import { BookedRequestDto } from './dto/bookedRequest.dto';

describe('AppController', () => {
  let appController: AppController;

  const goodDateFormat: AvailabilityRequestDto = {
    activityId: 1,
    date: '07-11 21:30',
  };

  const wrongDateFormat: AvailabilityRequestDto = {
    activityId: 2,
    date: '07-12 21:30',
  };

  const goodDateFormatBooked: BookedRequestDto = {
    activityId: 1,
    date: '07-11 21:30',
  };

  const dateAlreadyBooked: BookedRequestDto = {
    activityId: 2,
    date: '07-11 21:30',
  };

  beforeEach(async () => {
    const app: TestingModule = await Test.createTestingModule({
      controllers: [AppController],
      providers: [AppService],
    }).compile();

    appController = app.get<AppController>(AppController);
  });

  describe('root', () => {
    it('should return no booked date at startup', () => {
      expect(appController.getAllBooked().length).toBe(0);
    });
  });

  describe('isAvailable()', () => {
    it('should return a AvailabilityReceiptDto (generated UUID and input date) with transaction success', () => {
      const availabilityReceiptDto = appController.isAvailable(goodDateFormat);
      expect(availabilityReceiptDto.isAvailable).toBe(true);
      expect(availabilityReceiptDto.schedulerReceiptId.substring(0, 8)).toBe(
        'RECEIPT:',
      );
      //As we only test the availability, we shouldn't expect a new instance in the tab
      expect(appController.getAllBooked().length).toBe(0);
    });
  });

  describe('isNotAvailable()', () => {
    it('should throw exception transaction failure', () => {
      expect(() => appController.isAvailable(wrongDateFormat)).toThrow(
        HttpException,
      );
      expect(appController.getAllBooked().length).toBe(0);
    });
  });

  describe('CanBook()', () => {
    it('should return a BookedReceiptDto (generated UUID, boolean and input date) with transaction success', () => {
      const bookedReceiptDto = appController.book(goodDateFormat);
      expect(bookedReceiptDto.isBooked).toBe(true);
      expect(bookedReceiptDto.schedulerReceiptId.substring(0, 8)).toBe(
        'RECEIPT:',
      );
      expect(bookedReceiptDto.date).toBe(goodDateFormat.date);
      expect(appController.getAllBooked().length).toBe(1);
    });
  });

  describe('CantBook()', () => {
    it('should throw exception transaction failure', () => {
      const bookedReceiptDto = appController.book(goodDateFormatBooked);
      expect(bookedReceiptDto.isBooked).toBe(true);
      expect(bookedReceiptDto.schedulerReceiptId.substring(0, 8)).toBe(
        'RECEIPT:',
      );
      expect(bookedReceiptDto.date).toBe(goodDateFormat.date);
      expect(appController.getAllBooked().length).toBe(1);

      expect(() => appController.book(dateAlreadyBooked)).toThrow(
        HttpException,
      );
      expect(appController.getAllBooked().length).toBe(1);
    });
  });
});
