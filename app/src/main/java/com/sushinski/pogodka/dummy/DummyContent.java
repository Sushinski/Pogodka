package com.sushinski.pogodka.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces
 */
public class DummyContent {
    private static final String[] dummy_data = {"Sankt-Peterburg", "Moscow", "Ekaterinburg",
    "Novosibirsk", "Kazan", "Samara"};

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 0; i < dummy_data.length; i++) {
            addItem(createDummyItem(i, dummy_data[i]));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position, String name) {
        return new DummyItem(String.valueOf(position), name, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
