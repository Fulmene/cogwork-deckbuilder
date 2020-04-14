package alchemagis.deckgenerator.metric.synergy;

import alchemagis.magic.quality.MagicCardQuality;

public final class BinaryOperatedSynergy extends Synergy {

    private final Synergy lhs;
    private final Synergy rhs;
    private final String binaryOperator;

    protected BinaryOperatedSynergy(Synergy lhs, String op, Synergy rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.binaryOperator = op;
    }

    @Override
    public boolean test(MagicCardQuality quality) {
        switch (this.binaryOperator) {
            case "&": return lhs.test(quality) && rhs.test(quality);
            case "|": return lhs.test(quality) || rhs.test(quality);
            default : throw new SynergyFormatException("operator " + this.binaryOperator);
        }
    }

}
