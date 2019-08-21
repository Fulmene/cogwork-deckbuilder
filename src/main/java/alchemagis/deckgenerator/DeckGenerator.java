package alchemagis.deckgenerator;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alchemagis.deckgenerator.metric.Metric;
import alchemagis.deckgenerator.metric.CostEffectivenessMetric;
import alchemagis.deckgenerator.metric.SynergyMetric;
import alchemagis.magic.Card;
import alchemagis.magic.CardPool;
import alchemagis.magic.Deck;
import alchemagis.magic.MagicConstants;

public final class DeckGenerator {

    private CardPool cardPool;
    private List<Metric> metrics;
    private Map<String, Double> utilityScores;

    public static DeckGenerator createDeckGenerator(String ...sets) {
        URL[] setURLs = Arrays.stream(sets).
            map(name -> { try { return new URL("file:///home/adelaide/Downloads/AllSetFiles/" + name + ".json"); } catch (MalformedURLException e) { throw new RuntimeException(e); } }).
            toArray(URL[]::new);
        URL[] metricTableURLs = Arrays.stream(sets).
            map(name -> Metric.class.getResource(name + ".csv")).
            toArray(URL[]::new);

        CardPool cardPool = CardPool.loadCardPool(setURLs);
        Metric costEffectivenessMetric = new CostEffectivenessMetric(metricTableURLs);
        Metric synergyMetric = new SynergyMetric(metricTableURLs);

        return new DeckGenerator(
            cardPool,
            List.of(
                costEffectivenessMetric,
                synergyMetric));
    }

    public DeckGenerator(CardPool cardPool, List<Metric> metrics) {
        this.cardPool = cardPool;
        this.metrics = metrics;
        this.utilityScores = new HashMap<>();
    }

    public Deck generateDeck() {
        return this.generateDeck(List.of(this.cardPool.getRandomCard()));
    }

    public Deck generateDeck(List<Card> startingCards) {
        final Deck generatedDeck = new Deck(startingCards);

        while (generatedDeck.size() < MagicConstants.MIN_DECK_SIZE) {
            this.utilityScores.clear();
            Card maxUtilityCard = cardPool.stream().
                filter(c ->
                    generatedDeck.count(c) < MagicConstants.MAX_COPIES ||
                    MagicConstants.canHaveAnyNumberOf(c)).
                max((c1, c2) ->
                    Double.compare(
                        this.getUtilityScore(generatedDeck, c1),
                        this.getUtilityScore(generatedDeck, c2))).
                get();
            generatedDeck.add(maxUtilityCard);
        }

        return generatedDeck;
    }

    private double getUtilityScore(Deck deck, Card card) {
        if (!this.utilityScores.containsKey(card.getName())) {
            this.utilityScores.put(
                card.getName(),
                this.metrics.stream().
                    mapToDouble(m -> m.getMetricScore(deck, card)).
                    sum());
        }
        return this.utilityScores.get(card.getName());
    }

}
