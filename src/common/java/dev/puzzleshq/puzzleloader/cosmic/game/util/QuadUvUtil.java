package dev.puzzleshq.puzzleloader.cosmic.game.util;

public class QuadUvUtil {

    /*
        rotationStyle is CW

        corner:
            0 = bottomLeftCorner
            1 = topLeftCorner
            2 = bottomRightCorner
            3 = topRightCorner

        rotation:
            0 = normal
            1 = 90°
            2 = 180°
            3 = 270°
    */
    public static float[] createRotatedUv(float[] defaultUVs, int rotation) {
        if (rotation == 0) {
            return defaultUVs;
        /*
            | end uv result:
                -- -+ +- ++
        */
        }

        if (rotation == 1) {
            return new float[] {
                    defaultUVs[3], defaultUVs[0], defaultUVs[1], defaultUVs[2],
            };
        /*
            | end uv result:
            |         1-+ 3++ | 1\0-- 3\1-+
            |         0-- 2+- | 0\2+- 2\3++
        */
        }

        if (rotation == 2) {
            return new float[] {
                    defaultUVs[2], defaultUVs[3], defaultUVs[0], defaultUVs[1],
            };
        /*
            | end uv result:
            |         1-+ 3++ | 1\2+- 3\0--
            |         0-- 2+- | 0\3++ 2\1-+
        */
        }

        return new float[] {
                defaultUVs[1], defaultUVs[2], defaultUVs[3], defaultUVs[0],
        };
    /*
        | end uv result:
        |         1-+ 3++ | 1\3++ 3\2+-
        |         0-- 2+- | 0\1-+ 2\0--
    */
    }

}
