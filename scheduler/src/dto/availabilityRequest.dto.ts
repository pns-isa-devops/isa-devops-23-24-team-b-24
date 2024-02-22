import { IsNotEmpty, IsPositive, IsNumber, IsString } from 'class-validator';

export class AvailabilityRequestDto {
  @IsNotEmpty()
  @IsNumber()
  @IsPositive()
  activityId: number;

  @IsNotEmpty()
  @IsString()
  date: string;
}
