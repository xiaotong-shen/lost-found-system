package use_case.search.util;

public class FuzzyMatcher {

    // Return true if the Levenshtein distance between a and b is less than or equal to the threshold
    public static boolean isFuzzyMatch(String a, String b, int threshold) {
        return levenshteinDistance(a.toLowerCase(), b.toLowerCase()) <= threshold;
    }

    // Levenshtein distance implementation (dynamic programming)
    public static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(
                                dp[i - 1][j] + 1,      // delete
                                dp[i][j - 1] + 1),     // insert
                        dp[i - 1][j - 1] + cost); // replace
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
