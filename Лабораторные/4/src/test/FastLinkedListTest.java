package test;

import collections.FastLinkedList;

/**
 * @author vladislav.trofimov@emc.com
 */
public class FastLinkedListTest {

    public static void main(final String[] args) {
        FastLinkedList<String> list = new FastLinkedList<>();
        FastLinkedList<Integer> list2 = new FastLinkedList<>();
        list.put("a").put("b").put("c").put("a").put("d").put("e").put("a").put("a").put("f").put("g").remove("d").remove("a");
        list2.put(1).put(2).put(3).put(1).put(4).put(5).put(1).put(1).put(6).put(7).remove(4).remove(1);
        for (final String s : list) {
            System.out.print(s + ' ');
        }
        System.out.println();
        for (final Integer s : list2) {
            System.out.print(s + " ");
        }
        System.out.println();
        list.remove("234");
        list2.remove(100);
        for (final String s : list) {
            System.out.print(s + ' ');
        }
        System.out.println();
        for (final Integer s : list2) {
            System.out.print(s + " ");
        }
        System.out.println();
        list.remove("e").remove("c").remove("e").remove("g").remove("b").remove("f").remove("").remove("xyz");
        list2.remove(5).remove(3).remove(5).remove(7).remove(2).remove(6).remove(0).remove(123);
        for (final String s : list) {
            System.out.print(s + ' ');
        }
        System.out.println();
        for (final Integer s : list2) {
            System.out.print(s + " ");
        }
    }

}
