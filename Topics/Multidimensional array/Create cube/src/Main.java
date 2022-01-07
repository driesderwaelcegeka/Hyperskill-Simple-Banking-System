class ArrayOperations {

  public static final int ARRAY_SIZE = 3;

  public static int[][][] createCube() {
    int[][][] cube = new int[ARRAY_SIZE][ARRAY_SIZE][ARRAY_SIZE];

    for (int i = 0; i < cube.length; i++) {
      for (int j = 0; j < cube[i].length; j++) {
        for (int k = 0; k < cube[i][j].length; k++) {
          cube[i][j][k] = k + j * ARRAY_SIZE;
        }
      }
    }

    return cube;
  }

}