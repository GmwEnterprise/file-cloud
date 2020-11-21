package test;

import java.util.HashSet;

public class LeetCode {

    public static int lengthOfLongestSubstring(String s) {
        HashSet<String> set = new HashSet<>();
        int index = 0;
        StringBuilder temp = new StringBuilder();
        while (index < s.length()) {
            String sub = s.substring(index, index + 1);
            int dep = temp.toString().indexOf(sub);
            if (dep > -1) {
                set.add(temp.toString());
                temp = new StringBuilder(temp.substring(dep + 1)).append(sub);
            } else {
                temp.append(s.charAt(index));
            }
            index++;
        }
        set.add(temp.toString());
        String maxStr = "";
        for (String item : set) {
            if (item.length() >= maxStr.length()) {
                maxStr = item;
            }
        }
        return maxStr.length();
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
    }
}
