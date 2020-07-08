/*
 * Configs.java - part of the GATOR project
 *
 * Copyright (c) 2018 The Ohio State University
 *
 * This file is distributed under the terms described in LICENSE
 * in the root directory.
 */

package presto;

import com.google.common.collect.Sets;

import java.util.Set;

public class Configs {
  public static String sdkPlatformsPath;
  public static String apkName;
  public static String apkPath;
  public static String dynCGDir;
  public static Set<String> excludedPackages = Sets.newHashSet();
}
