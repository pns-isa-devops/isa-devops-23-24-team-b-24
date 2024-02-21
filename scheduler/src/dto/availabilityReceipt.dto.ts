import { IsNotEmpty, IsPositive, IsString } from 'class-validator';

export class AvailabilityReceiptDto {
    constructor(payReceiptId: string, isAvailable: boolean) {
        this.schedulerReceiptId = payReceiptId;
        this.isAvailable = isAvailable;
    }

    @IsNotEmpty()
    @IsString()
    schedulerReceiptId: string;

    @IsNotEmpty()
    @IsPositive()
    isAvailable: boolean;
}
