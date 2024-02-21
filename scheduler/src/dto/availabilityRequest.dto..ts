import { IsNotEmpty, IsPositive, IsLong, IsDate } from 'class-validator';

export class AvailabilityRequestDto {
    @IsNotEmpty()
    @IsLong()
    @IsPositive()
    activityId: number;

    @IsNotEmpty()
    @IsDate()
    date: Date;
}
