
  /*
    Random clicking (Normal distribution, which is the expectation) on a button with coordinates (x1, y1, x2, y2)
    @Author Indiana
  */

class RandomClick {

  /*
    Returns random number with normal distribution around the ZERO, given mean and std.
    Some variables are static, as fucntion saves some calcs by calculatings some stuff only once per two random numbers
    http://www.taygeta.com/random/gaussian.html
  */
  static boolean uselast = false;
  static double y2 = 0;
  public static double randomNormal(double m, double s) {
    /* normal random variate generator */
  	/* mean m, standard deviation s */
    double x1, x2, w, y1;

    /* use value from previous call */
    if (uselast) { y1 = y2; uselast = false; }
    else {
      do {
        x1 = 2.0 * Math.random() - 1.0;
        x2 = 2.0 * Math.random() - 1.0;
        w = x1 * x1 + x2 * x2;
      } while ( w >= 1.0 );

      w = Math.sqrt( (-2.0 * Math.log( w ) ) / w );
      y1 = x1 * w;
      y2 = x2 * w;
      uselast = true;
    }
    return( m + y1 * s );
  }

  // random number - 0 -> scale, with normal distribution
  // ignore results outside 3 stds from the mean
  public static double randomNormalScaled(double scale, double m, double s) {
    double res = -99;
    while (res < -3.5 || res > 3.5) res = randomNormal(m, s);
    return (res / 3.5*s + 1) * (scale / 2.0);
  }

  public static int[] _clickButton(int x, int y, int rx, int ry) {
    int rx2 = (int) (randomNormalScaled(2*rx, 0, 1) - (rx));
    int ry2 = (int) (randomNormalScaled(2*ry, 0, 1) - (ry));
    return new int[]{x + rx2, y + ry2};
  }

  public static int[] clickButton(int x1, int y1, int x2, int y2) {
    int mx = (x2-x1)/2;
    int my = (y2-y1)/2;
    return _clickButton(x1 + mx, y1 + my, mx, my);
  }

  public static void main(String args[]) {
//    for (int i = 0; i < 10000; i++) {
//      //int[] xy = clickButton(100, 100, 300, 200);
//      int[] xy = clickButton(100, 100, 200, 160);
//      System.out.println(xy[0] + " " + xy[1]);
//    }
     int[] xy = clickButton(100, 100, 300, 200);
     System.out.println("Clicking at position " + xy[0] + " " + xy[1]);
     clickMouseAt(xy[0], xy[1]);       
  }

}