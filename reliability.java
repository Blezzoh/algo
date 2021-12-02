import java.lang.Math;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class reliability {
  /*
   * BY PRINCIPLE THIS SHOULD WORK, HOWEVER I M NOT GETTING WHERE THE MISTAKE IS
   * BECAUSE IT RUNS ON THE SAMPLE PROVIDED IN THE HOMEWORK WRITE UP BUT NOT IN
   * THE SAMPLE PROVIDED(inSample*.txt files).
   * THINK ABOUT THIS TOMORROW
   */
  static double recursiveReliability(int b, int c[], double[] r, int n, int[] machines) {
    int[][] track = new int[n][b];
    double[][] p = new double[n + 1][b + 1];
    for (int i = 0; i <= n; i++) {
      for (int j = 0; j <= b; j++) {
        // base cases
        if (j == 0) {
          p[i][j] = 0.0;
        } else if (i == 0) {
          p[i][j] = 1.0;
        } else {
          p[i][j] = -1.0;
        }
      }
    }
    // for (int i = 1; i <= n; i++) // at least one copy/machine
    // {
    //   p[i][c[i - 1]] = r[i - 1];
    //   b -= c[i - 1];
    // }
    double max = reliabilityRecursiveUtil(b, c, r, n, track, p);
    int[] costs = new int[n];
    int offset = b - 1;
    for (int i = n - 1; i >= 0; i--) {
      machines[i] = track[i][offset];
      costs[i] = machines[i] * c[i];
      offset = offset - costs[i];
    }
    for (int i = n - 1; i >= 0; i--) {
      System.out.println(machines[i] + " copies from machine " + (i + 1) + " cost " + c[i]);
    }
    double locUsed = 0, totalLoc = 0;
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= b; j++) {
        // base cases
        if (p[i][j] != -1) {
          locUsed++;
        }
        totalLoc++;
      }
    }
    double percentage = (locUsed / totalLoc) * 100.0;
    System.out.println("\n\nMemoization Statistics");
    System.out.println("Total locations: " + totalLoc);
    System.out.println("Number Used: " + locUsed);
    System.out.println("Percentage Used: " + percentage);
    return max;
  }

  static double reliabilityRecursiveUtil(int b, int c[], double[] r, int n, int[][] track, double[][] p) {
    if ((n == 0 && b >= 0) || b == 0 || p[n][b] != -1) {
      return p[n][b];
    }
    int nm = b / c[n - 1];
    double best = 0.0;
    for (int i = 0; i < nm; i++) {
      int leftover = b - (i * c[n - 1]);
      double probLeftover = reliabilityRecursiveUtil(leftover, c, r, n - 1, track, p) * 1.0
          - Math.pow((1.0 - r[n - 1]), i + 1);

      if (best < probLeftover) {
        best = probLeftover;
        // if this is the current best probability, then this is the current best number
        // of machines
        track[n - 1][b - 1] = i;
      }
      // best = Math.max(best, probLeftover);
    }
    p[n][b] = best;
    return p[n][b];
  }

  static double reliabilityBottomUp(int b, int[] c, double[] r, int n, int[] machines) {
    double[][] p = new double[n + 1][b + 1];
    int[][] track = new int[n][b];
    // base cases
    for (int i = 0; i < b; i++) {
      p[0][i] = 1.0;
    }
    for (int i = 1; i <= n; i++) {
      p[i][0] = 0;
    }
    for (int i = 1; i <= n; i++) // init the 0 spent colum
    {
      p[i][c[i - 1]] = r[i - 1];
      b -= c[i - 1];
    }
    for (int i = 1; i <= n; i++) {
      for (int currentB = 1; currentB <= b; currentB++) {
        int numberOfMachines = currentB / c[i - 1];
        double best = 0;
        track[i - 1][currentB - 1] = 0;
        for (int mi = 0; mi <= numberOfMachines; mi++) {
          double prob = 1.0 - Math.pow((1.0 - r[i - 1]), mi + 1);
          int leftover = currentB - (mi * c[i - 1]);
          double total_prob = p[i - 1][leftover] * prob;
          if (best < total_prob) {
            best = total_prob;
            track[i - 1][currentB - 1] = mi;
          }
        }

        p[i][currentB] = best;
      }
    }
    int[] costs = new int[n];
    int offset = b - 1;
    for (int i = n - 1; i >= 0; i--) {
      machines[i] = track[i][offset];
      costs[i] = machines[i] * c[i];
      offset = offset - costs[i];

    }
    for (int i = n - 1; i >= 0; i--) {
      System.out.println(machines[i] + " copies from machine " + (i + 1) + " cost " + c[i]);
    }
    System.out.println("Bottom up dynamic programming: ");
    System.out.println("Maximim Relatibility: " + p[n][b]);
    return p[n][b];
  }

  public static void main(String[] args) throws IOException {
    BufferedReader in = null;
    int b = 0, n = 0;
    ArrayList<Integer> costsList = new ArrayList<Integer>();
    ArrayList<Double> rList = new ArrayList<Double>();

    try {
      in = new BufferedReader(new InputStreamReader(System.in));
      String line;
      int i = 0;
      while ((line = in.readLine()) != null) {
        String[] str = line.split(" ");
        if (i == 0) {
          b = Integer.parseInt(str[0]);
        } else if (i == 1) {
          n = Integer.parseInt(str[0]);
        } else {
          costsList.add(Integer.parseInt(str[0]));
          rList.add(Double.parseDouble(str[1]));
        }
        i++;
      }
      int costs[] = new int[n];
      int used[] = new int[n];
      double r[] = new double[n];
      for (int j = 0; j < n; j++) {
        costs[j] = costsList.get(j);
        r[j] = rList.get(j);
      }
      reliabilityBottomUp(b, costs, r, n, used);
      System.out.println("\n\n");
      recursiveReliability(b, costs, r, n, used);
    } catch (IOException e) {
      System.err.println("IOException reading System.in");
      throw e;
    } finally {
      if (in != null) {
        in.close();
      }
    }

  }
}
