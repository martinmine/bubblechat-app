package imt3662.hig.no.bubbles;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marti_000 on 14/09/27.
 */
public class ColorRepository {
    private float divider;
    private float lastValue;
    private Map<Integer, Integer> usedColors;

    private ColorRepository()
    {
        this.divider = 1;
        this.usedColors = new HashMap<Integer, Integer>();
    }

    private static ColorRepository instance;
    public static ColorRepository getReference() {
        if (instance == null)
            instance = new ColorRepository();
        return instance;
    }

    private float getNextColor() {
        if (lastValue + divider >= 1) {
            divider = divider / 2;
            lastValue = divider;
        }
        else {
            lastValue += 2 * divider;
        }
        return lastValue;
    }

    public int getColor(int id) {
        if (usedColors.containsKey(id)) {
            return usedColors.get(id);
        }
        else {
            float hue = 360f * getNextColor();
            int color = Color.HSVToColor(255, new float[]{hue, 100f ,74.5f});
            this.usedColors.put(id, color);

            return color;
        }
    }
}
