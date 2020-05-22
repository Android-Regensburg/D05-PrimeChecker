package de.ur.mi.android.primechecker.primes;

import java.math.BigInteger;

public class PrimeChecker {

    public static PrimeCandidate checkCandidate(PrimeCandidate candidate) {
        if (candidate.getValue().compareTo(BigInteger.valueOf(1)) != 1) {
            return markAsNoPrime(candidate, BigInteger.valueOf(1));
        }
        if (candidate.getValue().equals(BigInteger.valueOf(2))) {
            return markAsPrime(candidate);
        }
        if (candidate.getValue().mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))) {
            return markAsNoPrime(candidate, BigInteger.valueOf(2));
        }
        BigInteger i = BigInteger.valueOf(3);
        while ((i.multiply(i)).compareTo(candidate.getValue()) != 1) {
            if (candidate.getValue().mod(i).equals(BigInteger.valueOf(0))) {
                return markAsNoPrime(candidate, i);
            }
            i = i.add(BigInteger.valueOf(2));
        }
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
