package mtgcogwork.magic;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class CardSet {

    private String name;
    private Set<Card> cards;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CardSet(@JsonProperty("name") String name, @JsonProperty("cards") Card[] cards) {
        this.name = name;
        this.cards = Collections.unmodifiableSet(new HashSet<Card>(List.of(cards)));
    }

    public String getName() {
        return this.name;
    }

    public Set<Card> getCards() {
        return this.cards;
    }

}
