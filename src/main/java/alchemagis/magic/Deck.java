package alchemagis.magic;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import com.google.common.collect.Multiset;
import com.google.common.collect.HashMultiset;

public class Deck {

    private Multiset<Card> cards;

    public Deck() {
        this.cards = HashMultiset.create();
    }

    public Deck(Iterable<? extends Card> cards) {
        this.cards = HashMultiset.create(cards);
    }

    public int size() {
        return this.cards.size();
    }

    public void add(final Card card) {
        this.cards.add(card);
    }

    public int count(final Card card) {
        return this.cards.count(card);
    }

    public Stream<Card> stream() {
        return this.cards.stream();
    }

    public Collection<Card> getCards() {
        return Collections.unmodifiableCollection(this.cards);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Multiset.Entry<Card> cardEntry : this.cards.entrySet()) {
            str.append(cardEntry.getCount());
            str.append(' ');
            str.append(cardEntry.getElement().getName());
            str.append('\n');
        }
        return str.toString();
    }

}
