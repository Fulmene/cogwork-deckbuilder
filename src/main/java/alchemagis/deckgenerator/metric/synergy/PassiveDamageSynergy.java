package alchemagis.deckgenerator.metric.synergy;

import java.util.List;
import java.util.stream.Stream;

import alchemagis.deckgenerator.metric.SynergyMetric;
import alchemagis.magic.Card;
import alchemagis.magic.MagicCardQuality;

public class PassiveDamageSynergy extends Synergy {

    private final MagicCardQuality quality;
    private final List<String> targets;

    public PassiveDamageSynergy(String[] qualities, String[] targets) throws MagicCardQuality.IllegalQualityException {
        this.quality = new MagicCardQuality(qualities);
        this.targets = List.of(targets);
    }

    @Override
    protected double getRawScore(SynergyMetric metric, Card card) {
        if (this.quality.isSatisfied(card)) {
            if (card.getTypes().contains("creature"))
                return 1.0;
            else {
                List<Synergy> otherSynergies = metric.getSynergyList(card);

                if (otherSynergies.stream().anyMatch(BecomeCreatureSynergy.class::isInstance))
                    return 1.0;
                else {
                    Stream<DamageSynergy> otherDamageSynergies = otherSynergies.stream().
                        filter(DamageSynergy.class::isInstance).
                        map(DamageSynergy.class::cast);

                    if (otherDamageSynergies.anyMatch(d -> d.getTargets().stream().anyMatch(this.targets::contains)))
                        return 1.0;
                    else
                        return 0.0;
                }
            }
        }
        else
            return 0.0;
    }

}
