import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AvailabilityRequestDto } from './dto/availabilityRequest.dto';
import { HttpException } from '@nestjs/common';

describe('AppController', () => {
  let appController: AppController;

  const goodDateFormat: AvailabilityRequestDto = {
    activityId: 1,
    date: '07/11/2023',
  };

  const wrongDateFormat: AvailabilityRequestDto = {
    activityId: 2,
    date: '29/01/2024',
  };

  beforeEach(async () => {
    const app: TestingModule = await Test.createTestingModule({
      controllers: [AppController],
      providers: [AppService],
    }).compile();

    appController = app.get<AppController>(AppController);
  });

  describe('root', () => {
    it('should return no availabilities at startup', () => {
      expect(appController.getAllAvailabilities().length).toBe(0);
    });
  });

  describe('isAvailable()', () => {
    it('should return a AvailabilityReceiptDto (generated UUID and input date) with transaction success', () => {
      const availabilityReceiptDto = appController.isAvailable(goodDateFormat);
      expect(availabilityReceiptDto.isAvailable).toBe(true);
      expect(availabilityReceiptDto.schedulerReceiptId.substring(0, 8)).toBe(
        'RECEIPT:',
      );
      expect(availabilityReceiptDto.schedulerReceiptId.length).toBe(44);
      expect(appController.getAllAvailabilities().length).toBe(1);
    });
  });

  describe('payByCredit()', () => {
    it('should throw exception transaction failure', () => {
      expect(() => appController.isAvailable(wrongDateFormat)).toThrow(
        HttpException,
      );
      expect(appController.getAllAvailabilities().length).toBe(0);
    });
  });
});
