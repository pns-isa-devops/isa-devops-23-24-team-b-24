package teamb.w4e.cli.model;

public class AppliedAdvantage {
    private CliAdvantage advantage;
    private CliLeisure leisure;

    public AppliedAdvantage() {
    }

    public AppliedAdvantage(CliAdvantage advantage, CliLeisure leisure) {
        this.advantage = advantage;
        this.leisure = leisure;
    }

    public CliAdvantage getAdvantage() {
        return advantage;
    }

    public void setAdvantage(CliAdvantage advantage) {
        this.advantage = advantage;
    }

    public CliLeisure getLeisure() {
        return leisure;
    }

    public void setLeisure(CliLeisure leisure) {
        this.leisure = leisure;
    }

    @Override
    public String toString() {
        return "AppliedAdvantage{" +
                ", advantage=" + advantage +
                ", leisure=" + leisure +
                '}';
    }
}
