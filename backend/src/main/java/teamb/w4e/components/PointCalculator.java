package teamb.w4e.components;

import org.springframework.stereotype.Service;
import teamb.w4e.interfaces.PointAdder;

@Service
public class PointCalculator implements PointAdder {
    @Override
    public int convertPriceToPoints(double amount) {
        if (amount < 25)
            return (int) (amount * 0.25);
        else if (amount < 50)
            return (int) (amount * 0.5);
        else if (amount < 75)
            return (int) (amount * 0.75);
        else
            return (int) (amount * 0.9);
    }

    @Override
    public double convertPointsToPrice(int points) {
        if (points < 25)
            return points * 0.25;
        else if (points < 50)
            return points * 0.5;
        else if (points < 75)
            return points * 0.75;
        else
            return points * 0.9;
    }
}
