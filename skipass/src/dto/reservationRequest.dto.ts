import { IsNotEmpty, IsPositive, IsString } from 'class-validator';

export class ReservationRequestDto {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsNotEmpty()
  @IsString()
  activity: string;

  @IsNotEmpty()
  @IsPositive()
  duration: number;
}
