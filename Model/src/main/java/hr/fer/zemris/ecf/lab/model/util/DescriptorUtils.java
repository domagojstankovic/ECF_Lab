package hr.fer.zemris.ecf.lab.model.util;

import java.util.List;

/**
 * Created by dstankovic on 3/16/16.
 */
public class DescriptorUtils {
  public static String modifiedString(String filePath, String desc) {
    String newFilePath;
    int dotIndex = filePath.lastIndexOf(".");
    if (dotIndex < 0) {
      // no dot - no extension
      newFilePath = filePath + desc;
    } else {
      newFilePath = filePath.substring(0, dotIndex) + desc + filePath.substring(dotIndex);
    }
    return newFilePath;
  }

  public static String mergeDescriptor(List<Pair<String, String>> descriptors) {
    StringBuilder sb = new StringBuilder();
    for (Pair<String, String> pair : descriptors) {
      sb.append('_').append(pair.getFirst()).append('-').append(pair.getSecond());
    }
    return sb.toString();
  }
}
