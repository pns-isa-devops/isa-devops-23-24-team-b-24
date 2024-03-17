package teamb.w4e.cli.model;

public record PointTrade(
        CliCustomer sender,
        CliCustomer receiver,
        int points
) {
}
