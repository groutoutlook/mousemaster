package jmouseable.jmouseable;

import java.util.Set;
import java.util.stream.Collectors;

public record ComboPrecondition(Set<Set<Key>> mustNotBePressedKeySets,
                                Set<Set<Key>> mustBePressedKeySets) {

    public boolean isEmpty() {
        return mustNotBePressedKeySets.isEmpty() && mustBePressedKeySets.isEmpty();
    }

    public boolean satisfied(Set<Key> currentlyPressedComboKeys) {
        for (Set<Key> mustNotBePressedKeySet : mustNotBePressedKeySets) {
            if (currentlyPressedComboKeys.containsAll(mustNotBePressedKeySet))
                return false;
        }
        for (Set<Key> mustBePressedKeySet : mustBePressedKeySets) {
            if (!currentlyPressedComboKeys.containsAll(mustBePressedKeySet))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.join(" ", "^{" + keySetsToString(mustNotBePressedKeySets) + "}",
                "_{" + keySetsToString(mustBePressedKeySets) + "}");
    }

    private static String keySetsToString(Set<Set<Key>> keySets) {
        return keySets.stream()
                      .map(keySet -> keySet.stream()
                                           .map(Key::keyName)
                                           .collect(Collectors.joining(" ")))
                      .collect(Collectors.joining("|"));
    }
}