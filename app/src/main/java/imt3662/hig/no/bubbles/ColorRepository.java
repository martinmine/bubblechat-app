package imt3662.hig.no.bubbles;

import android.graphics.Color;
import android.util.SparseIntArray;

/**
 * Generates and assigns colors to user-IDs.
 * Created by Martin on 14/09/27.
 */
public class ColorRepository {
    private float divider;
    private float lastValue;
    private SparseIntArray usedColors;

    private ColorRepository() {
        this.divider = 1;
        this.usedColors = new SparseIntArray();
    }

    private static ColorRepository instance;

    /**
     * Gets the ColorRepository-instance and creates it if it hasn't already been created.
     * @return The ColorRepository instance.
     */
    public static ColorRepository getReference() {
        if (instance == null)
            instance = new ColorRepository();
        return instance;
    }

    /**
     * Divide and conquer algorithm. The output should be (first run):
     * 0.5, 0.25, 0.75, 0.125, ....
     * @return
     */
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

    /**
     * Gets the color number assigned to a user-id, if no-one is assigned already,
     * a color will be generated using by using colors on each side of the color spectrum
     * in order to make it easier for the user to split each message from each other.
     * @param id The id which we will assign a color to.
     * @return A color value.
     */
    public int getColor(int id) {
        int colorId = usedColors.get(id, 0);
        if (colorId != 0) {
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
