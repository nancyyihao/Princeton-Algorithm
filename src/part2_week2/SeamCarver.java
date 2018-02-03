package part2_week2;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class SeamCarver {

    private final static double MAX_ENERGY = 1000.0d;
    private List<List<Color>> mColors = new ArrayList<>();
    private int mHeight;
    private int mWidth;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        checkCondition(picture == null);

        for (int i=0; i<picture.width(); i++) {
            List<Color> colorList = new ArrayList<>(picture.height());
            for (int j=0; j<picture.height(); j++) {
                colorList.add(picture.get(i, j));
            }
            mColors.add(colorList);
        }
        mHeight = picture.height();
        mWidth = picture.width();
    }


    // current picture
    public Picture picture() {

        Picture picture = new Picture(width(), height());
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++)
                picture.set(i, j, mColors.get(i).get(j));

        return picture;
    }

    // width of current picture
    public  int width() {
        return mWidth;
    }

    // height of current picture
    public  int height() {
        return mHeight;
    }

    // energy of pixel at column x and row y
    public  double energy(int x, int y) {

        int column = mColors.get(0).size();
        int row = mColors.size();

        checkCondition(x < 0 || x > row - 1 || y < 0 || y > column - 1);
        if (x == 0 || x == row - 1 || y == 0 || y == column - 1) {
            return MAX_ENERGY;
        }

        Color colorXPlusOne = mColors.get(x+1).get(y);
        Color colorXMinusOne = mColors.get(x-1).get(y);
        int deltaXGreen = colorXPlusOne.getGreen() - colorXMinusOne.getGreen();
        int deltaXBlue = colorXPlusOne.getBlue() - colorXMinusOne.getBlue();
        int deltaXRed = colorXPlusOne.getRed() - colorXMinusOne.getRed();

        double xGradient = deltaXGreen * deltaXGreen
                + deltaXBlue * deltaXBlue
                + deltaXRed * deltaXRed;

        Color colorYPlusOne = mColors.get(x).get(y+1);
        Color colorYMinusOne = mColors.get(x).get(y-1);
        int deltaYGreen = colorYPlusOne.getGreen() - colorYMinusOne.getGreen();
        int deltaYBlue = colorYPlusOne.getBlue() - colorYMinusOne.getBlue();
        int deltaYRed = colorYPlusOne.getRed() - colorYMinusOne.getRed();

        double yGradient = deltaYGreen * deltaYGreen
                + deltaYBlue * deltaYBlue
                + deltaYRed * deltaYRed;

        return Math.sqrt(xGradient + yGradient);
    }

    // sequence of indices for horizontal seam
    public  int[] findHorizontalSeam() {

        int column = mColors.get(0).size();
        int row = mColors.size();

        checkCondition(row <= 0);
        int[] seam = new int[row] ;
        double[][] d = new double[row][column];
        for (int i=0 ; i<row; i++)
            for (int j=0; j<column; j++)
                d[i][j] = MAX_ENERGY * row;
        for (int i=0 ; i<column; i++) d[0][i] = energy(0, i);

        for (int i=1; i<row; i++)
            for (int j=0; j<column; j++) {
                double e = energy(i, j);
                if ( j + 1 < column) d[i][j] = Math.min(d[i][j], d[i-1][j+1]);
                if ( j - 1 > 0) d[i][j] = Math.min(d[i][j], d[i-1][j-1]);
                d[i][j] = Math.min(d[i][j], d[i-1][j]);
                d[i][j] += e;
            }

        double min = MAX_ENERGY * row;
        int index = 0;
        for (int i=0 ; i<column; i++) {
            if (d[row-1][i] < min) {
                min = d[row-1][i];
                index = i;
            }
        }

        seam[row-1] = index;
        int k = row - 1;
        while (k > 0) {
            double e = energy(k, index);
            if (index > 0 && Double.compare(d[k][index],d[k-1][index-1] + e) == 0) {
                seam[k-1] = --index;
            } else if (Double.compare(d[k][index],d[k-1][index] + e) == 0) {
                seam[k-1] = index;
            } else if (index+1 < column && Double.compare(d[k][index],d[k-1][index+1] + e) == 0) {
                seam[k-1] = ++index;
            }
            k--;
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public  int[] findVerticalSeam() {

        int column = mColors.get(0).size();
        int row = mColors.size();

        checkCondition(column <= 0);
        int[] seam = new int[column] ;
        double[][] d = new double[row][column];
        for (int i=0 ; i<row; i++)
            for (int j=0; j<column; j++)
                d[i][j] = MAX_ENERGY * column;
        for (int i=0 ; i<row; i++) d[i][0] = energy(i, 0);

        for (int j=1; j<column; j++)
            for (int i=0 ; i<row; i++) {
                double e = energy(i, j);
                if ( i + 1 < row) d[i][j] = Math.min(d[i][j], d[i+1][j-1]);
                if ( i - 1 > 0) d[i][j] = Math.min(d[i][j], d[i-1][j-1]);
                d[i][j] = Math.min(d[i][j], d[i][j-1]);
                d[i][j] += e;
            }

        double min = MAX_ENERGY * column;
        int index = 0;
        for (int i=0 ; i<row; i++) {
            if (d[i][column-1] < min) {
                min = d[i][column-1];
                index = i;
            }
        }

        seam[column-1] = index;
        int k = column - 1;
        while ( k > 0 ) {
            double e = energy(index, k);
            if (index > 0 && Double.compare(d[index][k],d[index-1][k-1] + e) == 0) {
                seam[k-1] = --index;
            } else if (Double.compare(d[index][k],d[index][k-1] + e) == 0) {
                seam[k-1] = index;
            } else if (index+1 < row && Double.compare(d[index][k],d[index+1][k-1] + e) == 0) {
                seam[k-1] = ++index;
            }
            k--;
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        int column = height();
        int row = width();
        checkCondition(seam == null || seam.length != row);
        for (int s : seam) {
            checkCondition(s < 0 || s>= column);
        }
        for (int i=0; i<seam.length; i++) {
            int s = seam[i];
            checkCondition(s < 0 || s>= column);
            if ( i > 0) {
                checkCondition(Math.abs(seam[i-1] - seam[i]) > 1);
            }
        }

        for (int i=0; i<row; i++)
            mColors.get(i).remove(seam[i]);
        mHeight--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }

    private void transpose() {
        List<List<Color>> transpose = new ArrayList<>();
        for (int i = 0; i < height(); i++) {
            List<Color> c = new ArrayList<>(width());
            for (int j = 0; j < width(); j++) {
                c.add(mColors.get(j).get(i));
            }
            transpose.add(c);
        }

        int tmp = mWidth;
        mWidth = mHeight;
        mHeight = tmp;
        mColors = transpose;
    }

    private void checkCondition(boolean invalid) {
        if (invalid)
            throw new IllegalArgumentException("Data invalid! Please check your input!");
    }
}
