import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { HttpException } from '@nestjs/common';

describe('AppController', () => {
  let appController: AppController;

  const goodSkiPassReservation: ReservationRequestDto = {
    name : 'Nikan the great',
    activity : 'ski',
    duration : 3,
  };

  const badSkiPassReservation: ReservationRequestDto = {
    name : 'Clement the not-so-great, but still good, but not that good, I mean, let\'s face it, he is not THAT great, yes he\'s kinda funny, but still, not that great, you know what I mean ?',
    activity : 'ski',
    duration : -3,
  };

  beforeEach(async () => {
    const app: TestingModule = await Test.createTestingModule({
      controllers: [AppController],
      providers: [AppService],
    }).compile();

    appController = app.get<AppController>(AppController);
  });

  describe('root', () => {
    it('should return no transactions at startup', () => {
      expect(appController.getAllTransactions().length).toBe(0);
    });
  });

  describe('reserve()', () => {
    it('should return a ReservationReceiptDto (TO MODIFY) with transaction success', () => {
      const reservation = appController.reserve(goodSkiPassReservation);
      // expect(paymentReceiptDto.amount).toBe(goodPaymentDto.amount);
      // expect(paymentReceiptDto.payReceiptId.substring(0, 8)).toBe('RECEIPT:');
      // expect(paymentReceiptDto.payReceiptId.length).toBe(44);
      expect(appController.getAllTransactions().length).toBe(1);
    });
  });

  describe('reserve()', () => {
    it('should throw exception transaction failure', () => {
      expect(() => appController.reserve(badSkiPassReservation)).toThrow(
        HttpException,
      );
      expect(appController.getAllTransactions().length).toBe(0);
    });
  });
});
