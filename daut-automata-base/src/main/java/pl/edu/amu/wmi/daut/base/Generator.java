package pl.edu.amu.wmi.daut.base;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa zwracająca akceptowane słowa.
 */
public class Generator {

    private List<String> acceptedWords = new ArrayList<String>();
    private TransitionLabel currentLabel;
    /**
     * Metoda zwracająca listę wszystkich słów akceptowanych przez automat bez cykli.
     * Pobiera wszystkie przejscia z bieżącego stanu,
     * jeżeli takie istnieją to każdą po kolei etykietę porównuje z zadanym alfabetem
     * Jeżeli etykieta akceptuje znak z alfabetu,  obecny stan nie jest stanem akceptującym
     * dodaje do słowa symbol a poprzednie słowo usuwa, w przeciwnym wypadku tylko
     * dodaje symbol. Jeżeli z danego stanu nie wychodzą żadne etykiety a stan ten nie jest
     * akceptującu to napis jest usuwany
     */
    void acceptWords(AutomatonSpecification automaton, String alphabet, String word, State state) {
        int i = 0;
        StringBuffer buf = new StringBuffer(word);
        List<OutgoingTransition> allOutTransitions;
        allOutTransitions = automaton.allOutgoingTransitions(state);
            if (!allOutTransitions.isEmpty()) {
                for (OutgoingTransition transition : allOutTransitions) {
                    currentLabel = transition.getTransitionLabel();
                    for (i = 0; i < alphabet.length(); i++) {
                        if (currentLabel.canAcceptCharacter(alphabet.charAt(i))) {
                            if (!automaton.isFinal(state) && automaton.getInitialState() != state) {
                                 acceptedWords.remove(word);
                            }
                            buf.append(alphabet.charAt(i));
                            word = buf.toString();
                            acceptedWords.add(word);
                            acceptWords(automaton, alphabet, word, transition.getTargetState());
                        } else {
                                if (!automaton.isFinal(state)) {
                                    acceptedWords.remove(word);
                                }
                        }
                    }
                }
            } else {
                    if (!automaton.isFinal(state)) {
                        acceptedWords.remove(word);
                    }
            }
    }
    
    /**
     * Metoda zwracająca losowy napis akceptowany przez automat 
     * @param AutomatonSpecification automaton - automat
     * @param String alphabet - alfabet akceptowalny przez automat 
     * @param State state - stany automatu
     * @return String
     */
    String randomWord(AutomatonSpecification automaton, String alphabet, State state) {
        String word = new String();
        Random rand = new Random();
        List<OutgoingTransition> allOutTransitions;
        allOutTransitions = automaton.allOutgoingTransitions(state);
            if (!allOutTransitions.isEmpty()) {
                while (!automaton.isFinal(state)) {
                    int r = rand.nextInt(allOutTransitions.size()) + 1;
                    currentLabel = allOutTransitions.get(r).getTransitionLabel();
                    for (int i = 0; i < alphabet.length(); i++) {
                        if (currentLabel.canAcceptCharacter(alphabet.charAt(i))) {
                            state = allOutTransitions.get(r).getTargetState();
                            allOutTransitions = automaton.allOutgoingTransitions(state);
                            word.concat(alphabet.substring(i, i+1));
                            break;
                        }
                    }
                }
            }
        return word;
    }
}
