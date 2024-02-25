import { IsNotEmpty, IsPositive, IsNumber, IsString } from 'class-validator';

export class BookedRequestDto {
  @IsNotEmpty()
  @IsNumber()
  @IsPositive()
  activityId: number;

  @IsNotEmpty()
  @IsString()
  date: string;
}
