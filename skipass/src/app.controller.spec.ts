import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { HttpException } from '@nestjs/common';

describe('AppController', () => {
  let appController: AppController;

  const goodSkiPassReservation: ReservationRequestDto = {
    name: 'Nikan the great',
    activity: 'ski',
    duration: 3,
  };

  const badSkiPassReservationDuration: ReservationRequestDto = {
    name: "Clement the not-so-great, but still good, but not that good, I mean, let's face it, he is not THAT great, yes he's kinda funny, but still, not that great, you know what I mean ?",
    activity: 'ski',
    duration: -3,
  };

  const badSkiPassReservationActivity: ReservationRequestDto = {
    name: 'Arnaud the great',
    activity: 'surfing',
    duration: 1,
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
      expect(reservation.isReserved).toBe(true);
      expect(reservation.reservationReceiptId.substring(0, 8)).toBe('RECEIPT:');
      expect(appController.getAllTransactions().length).toBe(1);
    });
  });

  describe('reserveWithWrongDuration()', () => {
    it('should throw an exception transaction failure', () => {
      expect(() =>
        appController.reserve(badSkiPassReservationDuration),
      ).toThrow(HttpException);
      expect(appController.getAllTransactions().length).toBe(0);
    });
  });

  describe('reserveWithWrongActivity()', () => {
    it('should throw an exception transaction failure', () => {
      expect(() =>
        appController.reserve(badSkiPassReservationActivity),
      ).toThrow(HttpException);
      expect(appController.getAllTransactions().length).toBe(0);
    });
  });
});
