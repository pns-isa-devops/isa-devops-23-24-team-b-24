import { Test, TestingModule } from '@nestjs/testing';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ReservationRequestDto } from './dto/reservationRequest.dto';
import { HttpException } from '@nestjs/common';

describe('AppController', () => {
  let appController: AppController;

  const goodSkiPassReservation: ReservationRequestDto = {
    name: 'ski',
    activity: 'day',
    duration: 3,
  };

  const badSkiPassReservationName: ReservationRequestDto = {
    name: 'surfing',
    activity: 'day',
    duration: 1,
  };

  const badSkiPassReservationDuration: ReservationRequestDto = {
    name: 'ski',
    activity: 'day',
    duration: -3,
  };

  const badSkiPassReservationActivity: ReservationRequestDto = {
    name: 'ski',
    activity: 'night',
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
    it('should return a ReservationReceiptDto with transaction success', () => {
      const reservation = appController.reserve(goodSkiPassReservation);
      expect(reservation.isReserved).toBe(true);
      expect(reservation.reservationReceiptId.substring(0, 8)).toBe('RECEIPT:');
      expect(appController.getAllTransactions().length).toBe(1);
    });
  });

  describe('reserveWithWrongName()', () => {
    it('should throw an exception transaction failure', () => {
      expect(() => appController.reserve(badSkiPassReservationName)).toThrow(
        HttpException,
      );
      expect(appController.getAllTransactions().length).toBe(0);
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
