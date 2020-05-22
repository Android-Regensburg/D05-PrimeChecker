package de.ur.mi.android.primechecker.primes;

import java.math.BigInteger;

/**
 * Diese Klasse stellt einen naiven Algorithmus zur Verfügung, der für eine beliebige Zahl (BigInteger)
 * prüft, ob es sich um eine Primzahl handelt.
 */
public class PrimeChecker {

    /**
     * Prüft ob der übergebene Primzahlkandidat "prim" ist. Das Ergebnis wird im übergebenen
     * Parameter-Objekt gespeichert, das im Anschluss als Ergebnis der Methode zurückgegeben wird.
     */
    public static PrimeCandidate checkCandidate(PrimeCandidate candidate) {
        // Prüft ob es sich bei der Zahl um den Wert 1 handelt => Keine Primzahl
        if (candidate.getValue().compareTo(BigInteger.valueOf(1)) != 1) {
            return markAsNoPrime(candidate, BigInteger.valueOf(1));
        }
        // Prüft ob es sich bei der Zahl um den Wert 2 handelt => Primzahl
        if (candidate.getValue().equals(BigInteger.valueOf(2))) {
            return markAsPrime(candidate);
        }
        // Prüft ob sich die Zahl ohne Rest durch ein Vielfaches von 2 teilen lässt => Keine Primzahl
        if (candidate.getValue().mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
            return markAsNoPrime(candidate, BigInteger.valueOf(2));
        }
        BigInteger i = BigInteger.valueOf(3);
        // Prüft, ob sich die Zahl ohne Rest durch eine der ungeraden Zahlen >= 3 teilen lässt => Keine Primzahl
        // Getestet wird der Zahlenraum bis zur Quadratwurzel des übergebenen Kanidaten (Faktorzerlegung)
        while ((i.multiply(i)).compareTo(candidate.getValue()) != 1) {
            if (candidate.getValue().mod(i).equals(BigInteger.valueOf(0))) {
                // Ist die Zahl durch den aktuellen Wert teilbar => Keine Primzahl
                return markAsNoPrime(candidate, i);
            }
            // Inkremtierung zur nächsten ungeraden Zahl
            i = i.add(BigInteger.valueOf(2));
        }
        // Ist die Zahl nur durch 1 und sich selbst teilbar => Primzahl
        return markAsPrime(candidate);
    }

    private static PrimeCandidate markAsPrime(PrimeCandidate candidate) {
        candidate.setResult(PrimeCandidate.Result.IS_PRIME);
        return candidate;
    }

    private static PrimeCandidate markAsNoPrime(PrimeCandidate candidate, BigInteger divisor) {
        candidate.setResult(PrimeCandidate.Result.NO_PRIME);
        candidate.setSmallestDivisor(divisor);
        return candidate;
    }


}
